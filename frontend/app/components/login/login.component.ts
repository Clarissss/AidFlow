import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username: string = '';
  password: string = '';
  role: 'admin' | 'field-worker' = 'field-worker';
  error: string = '';
  loading: boolean = false;

  constructor(private authService: AuthService) {}

  onSubmit(): void {
    this.error = '';
    this.loading = true;
    
    if (!this.username || !this.password) {
      this.error = 'Username dan password harus diisi';
      this.loading = false;
      return;
    }

    this.authService.login(this.username, this.password, this.role).subscribe({
      next: () => {
        // Login successful, navigation handled by AuthService
        this.loading = false;
      },
      error: (err) => {
        this.loading = false;
        console.error('Login error:', err);
        console.error('Error details:', JSON.stringify(err, null, 2));
        
        // Show more specific error message
        if (err.error && err.error.message) {
          // Show backend error message
          this.error = err.error.message;
        } else if (err.status === 0) {
          this.error = 'Tidak dapat terhubung ke server. Pastikan backend sudah running di http://localhost:8080';
        } else if (err.status === 400) {
          // Bad request - show backend message or default
          this.error = err.error?.message || 'Login gagal. Periksa username, password, dan role.';
        } else if (err.status === 401) {
          this.error = 'Login gagal. Username atau password salah.';
        } else if (err.status === 404) {
          this.error = 'Endpoint tidak ditemukan. Pastikan backend sudah running.';
        } else {
          this.error = `Login gagal. Error: ${err.status || 'Unknown'}. Silakan coba lagi.`;
        }
      }
    });
  }
}
