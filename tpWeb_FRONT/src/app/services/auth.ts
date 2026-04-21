import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) {}

  login(data: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/signin`, data);
  }

  register(data: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/signup`, data);
  }

  saveUserData(response: any): void {
    localStorage.setItem('token', response.token);
    localStorage.setItem('user', JSON.stringify(response));
    localStorage.setItem('roles', JSON.stringify(response.roles));
  }

  getRoles(): string[] {
    return JSON.parse(localStorage.getItem('roles') || '[]');
  }

  isAdmin(): boolean {
    return this.getRoles().includes('ROLE_ADMIN');
  }

  isModerator(): boolean {
    return this.getRoles().includes('ROLE_MODERATOR');
  }

  isUser(): boolean {
    return this.getRoles().includes('ROLE_USER');
  }

  logout(): void {
    localStorage.clear();
  }
}