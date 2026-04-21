import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../services/auth';
import { Router } from '@angular/router'; // Optionnel pour la redirection
import { RouterModule } from '@angular/router';
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  loading = false;
  errorMessage = '';
 

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router 
  ) {}

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

 

  onSubmit(): void {
  if (this.loginForm.invalid) return;

  this.loading = true;
  this.errorMessage = '';

  this.authService.login(this.loginForm.value).subscribe({
    next: (res) => {

       console.log("LOGIN RESPONSE =", res);
       
      //  STORE TOKEN + USER
      localStorage.setItem('token', res.token);
      localStorage.setItem('user', JSON.stringify(res));

      //  IMPORTANT : STOCKER LES ROLES
      localStorage.setItem('roles', JSON.stringify(res.roles));

      this.loading = false;

      //  REDIRECTION SELON ROLE
      const roles = res.roles;

      if (roles.includes('ROLE_ADMIN')) {
        this.router.navigate(['/admin']);
      }
      else if (roles.includes('ROLE_MODERATOR')) {
        this.router.navigate(['/moderator']);
      }
      else {
        this.router.navigate(['/user']);
      }
    },

    error: (err) => {
      this.errorMessage = err.error?.message || 'Identifiants invalides';
      this.loading = false;
    }
  });
}

  onCreateAccount(event: Event): void {
    event.preventDefault();
    // Logique de navigation vers l'inscription
  }
}