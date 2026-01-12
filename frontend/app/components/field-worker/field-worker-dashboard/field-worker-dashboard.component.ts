import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../services/auth.service';
import { InventoryService, InventoryItem } from '../../../services/inventory.service';
import { RequestService, RequestItem, Request } from '../../../services/request.service';
import { NewItemRequestService, NewItemRequest, NewItemRequestForm } from '../../../services/new-item-request.service';

@Component({
  selector: 'app-field-worker-dashboard',
  templateUrl: './field-worker-dashboard.component.html',
  styleUrls: ['./field-worker-dashboard.component.css']
})
export class FieldWorkerDashboardComponent implements OnInit {
  inventory: InventoryItem[] = [];
  myRequests: Request[] = [];
  myNewItemRequests: NewItemRequest[] = [];
  currentUser: any;
  loading = false;

  // Request form
  showRequestForm = false;
  selectedItems: Map<number, number> = new Map();
  requestNotes: string = '';

  // New Item Request form
  showNewItemForm = false;
  newItemRequest: NewItemRequestForm = {
    itemName: '',
    category: '',
    requestedQuantity: 0,
    unit: '',
    description: ''
  };

  constructor(
    private authService: AuthService,
    private inventoryService: InventoryService,
    private requestService: RequestService,
    private newItemRequestService: NewItemRequestService
  ) {}

  ngOnInit(): void {
    this.currentUser = this.authService.getCurrentUser();
    this.loadInventory();
    this.loadMyRequests();
    this.loadMyNewItemRequests();
  }

  loadInventory(): void {
    this.loading = true;
    this.inventoryService.getInventory().subscribe({
      next: (items) => {
        this.inventory = items;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading inventory:', err);
        this.loading = false;
      }
    });
  }

  loadMyRequests(): void {
    if (this.currentUser) {
      this.requestService.getRequestsByFieldWorker(this.currentUser.id).subscribe({
        next: (requests) => {
          this.myRequests = requests.sort((a, b) => 
            new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
          );
        },
        error: (err) => {
          console.error('Error loading requests:', err);
        }
      });
    }
  }

  loadMyNewItemRequests(): void {
    if (this.currentUser) {
      this.newItemRequestService.getRequestsByFieldWorker(this.currentUser.id).subscribe({
        next: (requests) => {
          this.myNewItemRequests = requests.sort((a, b) => 
            new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
          );
        },
        error: (err) => {
          console.error('Error loading new item requests:', err);
        }
      });
    }
  }

  logout(): void {
    this.authService.logout();
  }

  toggleRequestForm(): void {
    this.showRequestForm = !this.showRequestForm;
    if (!this.showRequestForm) {
      this.selectedItems.clear();
      this.requestNotes = '';
    }
  }

  toggleNewItemForm(): void {
    this.showNewItemForm = !this.showNewItemForm;
    if (!this.showNewItemForm) {
      this.newItemRequest = {
        itemName: '',
        category: '',
        requestedQuantity: 0,
        unit: '',
        description: ''
      };
    }
  }

  addItemToRequest(item: InventoryItem, quantity: number): void {
    if (quantity <= 0) {
      this.selectedItems.delete(item.id);
      return;
    }

    if (quantity > item.quantity) {
      alert(`Jumlah melebihi stok tersedia (${item.quantity} ${item.unit})`);
      return;
    }

    this.selectedItems.set(item.id, quantity);
  }

  removeItemFromRequest(itemId: number): void {
    this.selectedItems.delete(itemId);
  }

  getSelectedItemsCount(): number {
    return this.selectedItems.size;
  }

  submitRequest(): void {
    if (this.selectedItems.size === 0) {
      alert('Pilih minimal satu barang untuk diminta');
      return;
    }

    if (!this.currentUser) {
      alert('User tidak ditemukan');
      return;
    }

    const requestItems: RequestItem[] = Array.from(this.selectedItems.entries()).map(([itemId, quantity]) => {
      const item = this.inventory.find(i => i.id === itemId);
      return {
        inventoryItemId: itemId,
        inventoryItemName: item ? item.name : '',
        quantity: quantity
      };
    });

    this.requestService.createRequest(this.currentUser.id, requestItems).subscribe({
      next: () => {
        alert('Permintaan bantuan berhasil dikirim!');
        this.toggleRequestForm();
        this.loadMyRequests();
      },
      error: (err) => {
        console.error('Error creating request:', err);
        alert('Gagal mengirim permintaan. ' + (err.error?.message || ''));
      }
    });
  }

  submitNewItemRequest(): void {
    if (!this.newItemRequest.itemName || !this.newItemRequest.category || !this.newItemRequest.unit) {
      alert('Nama barang, kategori, dan satuan harus diisi');
      return;
    }

    if (this.newItemRequest.requestedQuantity <= 0) {
      alert('Jumlah yang diminta harus lebih dari 0');
      return;
    }

    if (!this.currentUser) {
      alert('User tidak ditemukan');
      return;
    }

    this.newItemRequestService.createRequest(this.currentUser.id, this.newItemRequest).subscribe({
      next: () => {
        alert('Permintaan barang baru berhasil dikirim!');
        this.toggleNewItemForm();
        this.loadMyNewItemRequests();
      },
      error: (err) => {
        console.error('Error creating new item request:', err);
        alert('Gagal mengirim permintaan. ' + (err.error?.message || ''));
      }
    });
  }

  getStatusBadgeClass(status: string): string {
    switch (status) {
      case 'APPROVED': return 'badge-success';
      case 'REJECTED': return 'badge-danger';
      default: return 'badge-warning';
    }
  }

  getStatusText(status: string): string {
    switch (status) {
      case 'APPROVED': return 'Disetujui';
      case 'REJECTED': return 'Ditolak';
      default: return 'Menunggu Persetujuan';
    }
  }

  getItemName(itemId: number): string {
    const item = this.inventory.find(i => i.id === itemId);
    return item ? item.name : '';
  }

  getItemUnit(itemId: number): string {
    const item = this.inventory.find(i => i.id === itemId);
    return item ? item.unit : '';
  }

  getItemUnitFromRequest(itemId: number): string {
    return this.getItemUnit(itemId);
  }
}
