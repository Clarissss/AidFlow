import { Component, OnInit } from '@angular/core';
import { DonationService } from '../../services/donation.service';
import { InventoryService, InventoryItem } from '../../services/inventory.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  donationForm = {
    donorName: '',
    email: '',
    phone: '',
    amount: null as number | null,
    message: ''
  };
  inventory: InventoryItem[] = [];
  selectedDonationItems: Map<number, number> = new Map();
  showDonationForm = false;
  donationSubmitted = false;
  isAuthenticated = false;
  loading = false;

  constructor(
    private donationService: DonationService,
    private inventoryService: InventoryService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.authService.currentUser$.subscribe(user => {
      this.isAuthenticated = user !== null;
    });
    this.loadInventory();
  }

  loadInventory(): void {
    this.inventoryService.getInventory().subscribe({
      next: (items) => {
        this.inventory = items;
      },
      error: (err) => {
        console.error('Error loading inventory:', err);
      }
    });
  }

  toggleDonationForm(): void {
    this.showDonationForm = !this.showDonationForm;
    this.donationSubmitted = false;
    if (!this.showDonationForm) {
      this.selectedDonationItems.clear();
    }
  }

  addDonationItem(item: InventoryItem, quantity: number): void {
    if (quantity <= 0) {
      this.selectedDonationItems.delete(item.id);
      return;
    }
    this.selectedDonationItems.set(item.id, quantity);
  }

  removeDonationItem(itemId: number): void {
    this.selectedDonationItems.delete(itemId);
  }

  getSelectedDonationItemsCount(): number {
    return this.selectedDonationItems.size;
  }

  getSelectedDonationItemsList(): Array<{name: string, quantity: number, unit: string}> {
    return Array.from(this.selectedDonationItems.entries()).map(([itemId, quantity]) => {
      const item = this.inventory.find(i => i.id === itemId);
      return {
        name: item ? item.name : '',
        quantity: quantity,
        unit: item ? item.unit : ''
      };
    });
  }

  submitDonation(): void {
    if (!this.donationForm.donorName || !this.donationForm.email) {
      alert('Nama dan email harus diisi');
      return;
    }

    // Build items string from selected items
    const itemsList = this.getSelectedDonationItemsList();
    const itemsString = itemsList.map(item => `${item.name} (${item.quantity} ${item.unit})`).join(', ');
    const totalQuantity = Array.from(this.selectedDonationItems.values()).reduce((sum, qty) => sum + qty, 0);

    this.loading = true;
    this.donationService.submitDonation({
      donorName: this.donationForm.donorName,
      email: this.donationForm.email,
      phone: this.donationForm.phone,
      amount: this.donationForm.amount || 0,
      item: itemsString || undefined,
      itemQuantity: totalQuantity || undefined,
      message: this.donationForm.message
    }).subscribe({
      next: () => {
        this.donationSubmitted = true;
        this.donationForm = {
          donorName: '',
          email: '',
          phone: '',
          amount: null,
          message: ''
        };
        this.selectedDonationItems.clear();
        this.loading = false;

        setTimeout(() => {
          this.showDonationForm = false;
          this.donationSubmitted = false;
        }, 3000);
      },
      error: (err) => {
        console.error('Error submitting donation:', err);
        alert('Gagal mengirim donasi. Silakan coba lagi.');
        this.loading = false;
      }
    });
  }
}
