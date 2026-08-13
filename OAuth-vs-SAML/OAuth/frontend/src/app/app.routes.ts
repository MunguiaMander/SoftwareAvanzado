import { Routes } from '@angular/router';
import { PublicComponent } from './public/public.component';
import { DashboardComponent } from './dashboard/dashboard.component';

export const routes: Routes = [
  { path: '', redirectTo: 'public', pathMatch: 'full' },
  { path: 'public', component: PublicComponent },
  { path: 'dashboard', component: DashboardComponent },
  { path: '**', redirectTo: 'public' },
];
