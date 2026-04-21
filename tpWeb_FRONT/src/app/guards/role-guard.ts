import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class RoleGuard implements CanActivate {

  constructor(private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {

    const roles = JSON.parse(localStorage.getItem('roles') || '[]');

    const expectedRole = route.data['role'];

    if (roles.includes(expectedRole)) {
      return true;
    }

    this.router.navigate(['/login']);
    return false;
  }
}