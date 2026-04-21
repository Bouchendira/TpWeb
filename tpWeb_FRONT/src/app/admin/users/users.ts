import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './users.html'
})
export class Users {

  users = [
    { id: 1, username: 'admin', role: 'ADMIN' }
  ];

  deleteUser(id: number) {
    this.users = this.users.filter(u => u.id !== id);
  }
}