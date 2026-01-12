import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
    providedIn: 'root'
})
export class AuthGuard implements CanActivate {
    constructor(
        private authService: AuthService,
        private router: Router
    ) { }

    canActivate(route: ActivatedRouteSnapshot): boolean {
        const requiredRole = route.data['role'];
        const currentUser = this.authService.getCurrentUser();

        if (!currentUser) {
            this.router.navigate(['/login']);
            return false;
        }

        if (currentUser.role.toUpperCase() !== requiredRole.toUpperCase()) {
            this.router.navigate(['/']);
            return false;
        }

        return true;
    }
}
