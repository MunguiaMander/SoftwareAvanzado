import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { MessageModule } from 'primeng/message';
import { PasswordModule } from 'primeng/password';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { AdminUserView, AuthService } from '../auth.service';

@Component({
  selector: 'app-dashboard',
  imports: [FormsModule, ButtonModule, CardModule, MessageModule, PasswordModule, TableModule, TagModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent {
  auth = inject(AuthService);
  private router = inject(Router);

  currentPassword = '';
  newPassword = '';
  protectedMessage = signal<string | null>(null);
  protectedError = signal<string | null>(null);
  passwordMessage = signal<string | null>(null);
  users = signal<AdminUserView[]>([]);

  constructor() {
    this.auth.me().subscribe();
  }

  loadProtected() {
    this.protectedMessage.set(null);
    this.protectedError.set(null);
    this.auth.protectedData().subscribe({
      next: (res) => this.protectedMessage.set(res.message),
      error: () => this.protectedError.set('401: la sesion ya no es valida'),
    });
  }

  changePassword() {
    this.passwordMessage.set(null);
    this.auth.changePassword(this.currentPassword, this.newPassword).subscribe({
      next: (res) => {
        this.passwordMessage.set(res.message);
        this.currentPassword = '';
        this.newPassword = '';
      },
      error: (err) => this.passwordMessage.set(err.error?.message ?? 'Error'),
    });
  }

  loadUsers() {
    this.auth.listUsers().subscribe((res) => this.users.set(res));
  }

  block(username: string) {
    this.auth.blockUser(username).subscribe(() => this.loadUsers());
  }

  unblock(username: string) {
    this.auth.unblockUser(username).subscribe(() => this.loadUsers());
  }

  logout() {
    this.auth.logout().subscribe(() => this.router.navigate(['/login']));
  }
}
