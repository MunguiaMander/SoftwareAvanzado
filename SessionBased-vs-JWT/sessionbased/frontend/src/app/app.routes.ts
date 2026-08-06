import { Routes } from '@angular/router';
import { authGuard } from './auth.guard';
import { DashboardComponent } from './dashboard/dashboard.component';
import { LoginComponent } from './login/login.component';
import { PublicComponent } from './public/public.component';

export const routes: Routes = [
  { path: '', redirectTo: 'public', pathMatch: 'full' },
  { path: 'public', component: PublicComponent },
  { path: 'login', component: LoginComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [authGuard] },
  { path: '**', redirectTo: 'public' },
];
