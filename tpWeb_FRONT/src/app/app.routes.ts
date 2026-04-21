import { Routes } from '@angular/router';
import { RoleGuard } from './guards/role-guard';

export const routes: Routes = [

  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },

  // LOGIN
  {
    path: 'login',
    loadComponent: () =>
      import('./login/login').then(m => m.LoginComponent)
  },

  // SIGNUP
  {
    path: 'signup',
    loadComponent: () =>
      import('./signup/signup').then(m => m.SignupComponent)
  },

  // =========================
  // ADMIN
  // =========================
  {
    path: 'admin',
    loadComponent: () =>
      import('./admin/dashboard/dashboard').then(m => m.Dashboard),

    canActivate: [RoleGuard],
    data: { role: 'ROLE_ADMIN' },

    children: [
      {
        path: 'researchers',
        loadComponent: () =>
          import('./admin/researchers/researchers').then(m => m.Researchers)
      },
      {
        path: 'domains',
        loadComponent: () =>
          import('./admin/domains/domains').then(m => m.Domains)
      },
      {
        path: 'publications',
        loadComponent: () =>
          import('./admin/publications/publications').then(m => m.PublicationComponent)
      },
      {
        path: 'users',
        loadComponent: () =>
          import('./admin/users/users').then(m => m.Users)
      }
    ]
  },

  // =========================
  // USER
  // =========================
  {
    path: 'user',
    loadComponent: () =>
      import('./user/user').then(m => m.User),

    canActivate: [RoleGuard],
    data: { role: 'ROLE_USER' }
  },

  // =========================
  // MODERATOR
  // =========================
  {
    path: 'moderator',
    loadComponent: () =>
      import('./moderator/moderator').then(m => m.Moderator),

    canActivate: [RoleGuard],
    data: { role: 'ROLE_MODERATOR' }
  }
];