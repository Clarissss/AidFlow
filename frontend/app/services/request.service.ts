import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface RequestItem {
  inventoryItemId: number;
  inventoryItemName: string;
  quantity: number;
}

export interface Request {
  id: number;
  fieldWorkerId: number;
  fieldWorkerName: string;
  items: RequestItem[];
  status: 'PENDING' | 'APPROVED' | 'REJECTED';
  createdAt: string;
  updatedAt?: string;
  notes?: string;
}

@Injectable({
  providedIn: 'root'
})
export class RequestService {
  private apiUrl = `${environment.apiUrl}/requests`;

  constructor(private http: HttpClient) {}

  createRequest(fieldWorkerId: number, items: RequestItem[]): Observable<Request> {
    const params = new HttpParams().set('fieldWorkerId', fieldWorkerId.toString());
    return this.http.post<Request>(this.apiUrl, items, { params });
  }

  getRequests(fieldWorkerId?: number): Observable<Request[]> {
    let params = new HttpParams();
    if (fieldWorkerId) {
      params = params.set('fieldWorkerId', fieldWorkerId.toString());
    }
    return this.http.get<Request[]>(this.apiUrl, { params });
  }

  getRequestsByFieldWorker(fieldWorkerId: number): Observable<Request[]> {
    return this.getRequests(fieldWorkerId);
  }

  getRequestById(id: number): Observable<Request> {
    return this.http.get<Request>(`${this.apiUrl}/${id}`);
  }

  approveRequest(requestId: number, notes?: string): Observable<Request> {
    const body = notes ? { notes } : {};
    return this.http.put<Request>(`${this.apiUrl}/${requestId}/approve`, body);
  }

  rejectRequest(requestId: number, notes?: string): Observable<Request> {
    const body = notes ? { notes } : {};
    return this.http.put<Request>(`${this.apiUrl}/${requestId}/reject`, body);
  }

  getPendingRequests(): Observable<Request[]> {
    return this.http.get<Request[]>(`${this.apiUrl}/pending`);
  }
}
