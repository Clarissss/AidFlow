import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../services/auth.service';
import { InventoryService, InventoryItem } from '../../../services/inventory.service';
import { RequestService, Request } from '../../../services/request.service';
import { NewItemRequestService, NewItemRequest } from '../../../services/new-item-request.service';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent implements OnInit {
  activeTab: 'inventory' | 'requests' | 'new-item-requests' = 'inventory';
  inventory: InventoryItem[] = [];
  requests: Request[] = [];
  newItemRequests: NewItemRequest[] = [];
  currentUser: any;
  loading = false;

  // Inventory form
  showAddItemForm = false;
  newItem: Partial<InventoryItem> = {
    name: '',
    category: '',
    quantity: 0,
    unit: '',
    description: ''
  };

  // Request details
  selectedRequest: Request | null = null;
  approvalNotes: string = '';

  // New Item Request details
  selectedNewItemRequest: NewItemRequest | null = null;
  newItemApprovalNotes: string = '';

  constructor(
    private authService: AuthService,
    private inventoryService: InventoryService,
    private requestService: RequestService,
    private newItemRequestService: NewItemRequestService
  ) {}

  ngOnInit(): void {
    this.currentUser = this.authService.getCurrentUser();
    this.loadInventory();
    this.loadRequests();
    this.loadNewItemRequests();
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

  loadRequests(): void {
    this.requestService.getRequests().subscribe({
      next: (requests) => {
        this.requests = requests.sort((a, b) =>
          new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
        );
      },
      error: (err) => {
        console.error('Error loading requests:', err);
      }
    });
  }

  loadNewItemRequests(): void {
    this.newItemRequestService.getRequests().subscribe({
      next: (requests) => {
        this.newItemRequests = requests.sort((a, b) =>
          new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
        );
      },
      error: (err) => {
        console.error('Error loading new item requests:', err);
      }
    });
  }

  logout(): void {
    this.authService.logout();
  }

  // Inventory methods
  toggleAddItemForm(): void {
    this.showAddItemForm = !this.showAddItemForm;
    if (!this.showAddItemForm) {
      this.newItem = {
        name: '',
        category: '',
        quantity: 0,
        unit: '',
        description: ''
      };
    }
  }

  addItem(): void {
    if (!this.newItem.name || !this.newItem.category || !this.newItem.unit) {
      alert('Nama, kategori, dan satuan harus diisi');
      return;
    }

    this.inventoryService.addItem({
      name: this.newItem.name!,
      category: this.newItem.category!,
      quantity: this.newItem.quantity || 0,
      unit: this.newItem.unit!,
      description: this.newItem.description
    }).subscribe({
      next: () => {
        this.toggleAddItemForm();
        this.loadInventory();
      },
      error: (err) => {
        console.error('Error adding item:', err);
        alert('Gagal menambahkan item');
      }
    });
  }

  updateQuantity(item: InventoryItem, newQuantity: number): void {
    if (newQuantity < 0) {
      alert('Jumlah tidak boleh negatif');
      return;
    }
    this.inventoryService.updateQuantity(item.id, newQuantity).subscribe({
      next: () => {
        this.loadInventory();
      },
      error: (err) => {
        console.error('Error updating quantity:', err);
        alert('Gagal mengupdate jumlah');
      }
    });
  }

  deleteItem(item: InventoryItem): void {
    if (confirm(`Apakah Anda yakin ingin menghapus ${item.name}?`)) {
      this.inventoryService.deleteItem(item.id).subscribe({
        next: () => {
          this.loadInventory();
        },
        error: (err) => {
          console.error('Error deleting item:', err);
          alert('Gagal menghapus item');
        }
      });
    }
  }

  // Request methods
  getPendingRequests(): Request[] {
    return this.requests.filter(r => r.status === 'PENDING');
  }

  getPendingNewItemRequests(): NewItemRequest[] {
    return this.newItemRequests.filter(r => r.status === 'PENDING');
  }

  viewRequest(request: Request): void {
    this.selectedRequest = request;
    this.approvalNotes = '';
  }

  closeRequestView(): void {
    this.selectedRequest = null;
    this.approvalNotes = '';
  }

  viewNewItemRequest(request: NewItemRequest): void {
    this.selectedNewItemRequest = request;
    this.newItemApprovalNotes = '';
  }

  closeNewItemRequestView(): void {
    this.selectedNewItemRequest = null;
    this.newItemApprovalNotes = '';
  }

  approveRequest(): void {
    if (this.selectedRequest) {
      this.requestService.approveRequest(this.selectedRequest.id, this.approvalNotes).subscribe({
        next: () => {
          this.closeRequestView();
          this.loadRequests();
          this.loadInventory(); // Reload inventory karena quantity berubah
        },
        error: (err) => {
          console.error('Error approving request:', err);
          alert('Gagal menyetujui permintaan. ' + (err.error?.message || ''));
        }
      });
    }
  }

  rejectRequest(): void {
    if (this.selectedRequest) {
      this.requestService.rejectRequest(this.selectedRequest.id, this.approvalNotes).subscribe({
        next: () => {
          this.closeRequestView();
          this.loadRequests();
        },
        error: (err) => {
          console.error('Error rejecting request:', err);
          alert('Gagal menolak permintaan');
        }
      });
    }
  }

  approveNewItemRequest(): void {
    if (this.selectedNewItemRequest) {
      this.newItemRequestService.approveRequest(this.selectedNewItemRequest.id, this.newItemApprovalNotes).subscribe({
        next: () => {
          this.closeNewItemRequestView();
          this.loadNewItemRequests();
          this.loadInventory(); // Reload inventory karena ada item baru
        },
        error: (err) => {
          console.error('Error approving new item request:', err);
          alert('Gagal menyetujui permintaan. ' + (err.error?.message || ''));
        }
      });
    }
  }

  rejectNewItemRequest(): void {
    if (this.selectedNewItemRequest) {
      this.newItemRequestService.rejectRequest(this.selectedNewItemRequest.id, this.newItemApprovalNotes).subscribe({
        next: () => {
          this.closeNewItemRequestView();
          this.loadNewItemRequests();
        },
        error: (err) => {
          console.error('Error rejecting new item request:', err);
          alert('Gagal menolak permintaan');
        }
      });
    }
  }

  getStatusBadgeClass(status: string): string {
    switch (status) {
      case 'APPROVED': return 'badge-success';
      case 'REJECTED': return 'badge-danger';
      default: return 'badge-warning';
    }
  }

  getItemUnit(itemId: number): string {
    const item = this.inventory.find(i => i.id === itemId);
    return item ? item.unit : '';
  }

  getItemQuantity(itemId: number): number {
    const item = this.inventory.find(i => i.id === itemId);
    return item ? item.quantity : 0;
  }
}
