import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Donation {
    id: number;
    donorName: string;
    email: string;
    phone: string;
    amount: number;
    item?: string;
    itemQuantity?: number;
    message?: string;
    createdAt: string;
    status: 'PENDING' | 'VERIFIED';
}

export interface DonationRequest {
    donorName: string;
    email: string;
    phone: string;
    amount: number;
    item?: string;
    itemQuantity?: number;
    message?: string;
}

@Injectable({
    providedIn: 'root'
})
export class DonationService {
    private apiUrl = `${environment.apiUrl}/donations`;

    constructor(private http: HttpClient) { }

    submitDonation(donation: DonationRequest): Observable<Donation> {
        return this.http.post<Donation>(this.apiUrl, donation);
    }

    getDonations(): Observable<Donation[]> {
        return this.http.get<Donation[]>(this.apiUrl);
    }

    getPendingDonations(): Observable<Donation[]> {
        return this.http.get<Donation[]>(`${this.apiUrl}/pending`);
    }

    getDonationById(id: number): Observable<Donation> {
        return this.http.get<Donation>(`${this.apiUrl}/${id}`);
    }

    verifyDonation(donationId: number): Observable<Donation> {
        return this.http.put<Donation>(`${this.apiUrl}/${donationId}/verify`, {});
    }
}
