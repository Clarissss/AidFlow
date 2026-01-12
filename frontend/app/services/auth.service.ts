import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';

export interface User {
  id: number;
  username: string;
  role: 'ADMIN' | 'FIELD_WORKER';
  name: string;
  token?: string;
}

export interface LoginRequest {
  username: string;
  password: string;
  role: string;
}

export interface LoginResponse {
  id: number;
  username: string;
  name: string;
  role: string;
  token: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(
    private router: Router,
    private http: HttpClient
  ) {
    const savedUser = localStorage.getItem('currentUser');
    if (savedUser) {
      this.currentUserSubject.next(JSON.parse(savedUser));
    }
  }

  login(username: string, password: string, role: 'admin' | 'field-worker'): Observable<LoginResponse> {
    const loginRequest: LoginRequest = {
      username,
      password,
      role
    };

    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, loginRequest).pipe(
      tap(response => {
        const normalizedRole = response.role.toUpperCase();

        const user: User = {
          id: response.id,
          username: response.username,
          role: normalizedRole as 'ADMIN' | 'FIELD_WORKER',
          name: response.name,
          token: response.token
        };

        localStorage.setItem('currentUser', JSON.stringify(user));
        localStorage.setItem('token', response.token);
        this.currentUserSubject.next(user);

        if (normalizedRole === 'ADMIN') {
          this.router.navigate(['/admin']);
        } else if (normalizedRole === 'FIELD_WORKER' || normalizedRole === 'FIELDWORKER') {
          this.router.navigate(['/field-worker']);
        } else {
          console.warn('Unknown role from backend:', response.role, 'using input role:', role);
          if (role === 'admin') {
            this.router.navigate(['/admin']);
          } else {
            this.router.navigate(['/field-worker']);
          }
        }
      })
    );
  }

  logout(): void {
    localStorage.removeItem('currentUser');
    localStorage.removeItem('token');
    this.currentUserSubject.next(null);
    this.router.navigate(['/']);
  }

  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  isAuthenticated(): boolean {
    return this.currentUserSubject.value !== null;
  }
}
