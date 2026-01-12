import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface NewItemRequest {
  id: number;
  fieldWorkerId: number;
  fieldWorkerName: string;
  itemName: string;
  category: string;
  requestedQuantity: number;
  unit: string;
  description?: string;
  status: 'PENDING' | 'APPROVED' | 'REJECTED';
  createdAt: string;
  updatedAt?: string;
  notes?: string;
}

export interface NewItemRequestForm {
  itemName: string;
  category: string;
  requestedQuantity: number;
  unit: string;
  description?: string;
}

@Injectable({
  providedIn: 'root'
})
export class NewItemRequestService {
  private apiUrl = `${environment.apiUrl}/new-item-requests`;

  constructor(private http: HttpClient) {}

  createRequest(fieldWorkerId: number, request: NewItemRequestForm): Observable<NewItemRequest> {
    const params = new HttpParams().set('fieldWorkerId', fieldWorkerId.toString());
    return this.http.post<NewItemRequest>(this.apiUrl, request, { params });
  }

  getRequests(fieldWorkerId?: number): Observable<NewItemRequest[]> {
    let params = new HttpParams();
    if (fieldWorkerId) {
      params = params.set('fieldWorkerId', fieldWorkerId.toString());
    }
    return this.http.get<NewItemRequest[]>(this.apiUrl, { params });
  }

  getRequestsByFieldWorker(fieldWorkerId: number): Observable<NewItemRequest[]> {
    return this.getRequests(fieldWorkerId);
  }

  getPendingRequests(): Observable<NewItemRequest[]> {
    return this.http.get<NewItemRequest[]>(`${this.apiUrl}/pending`);
  }

  approveRequest(requestId: number, notes?: string): Observable<NewItemRequest> {
    const body = notes ? { notes } : {};
    return this.http.put<NewItemRequest>(`${this.apiUrl}/${requestId}/approve`, body);
  }

  rejectRequest(requestId: number, notes?: string): Observable<NewItemRequest> {
    const body = notes ? { notes } : {};
    return this.http.put<NewItemRequest>(`${this.apiUrl}/${requestId}/reject`, body);
  }
}
