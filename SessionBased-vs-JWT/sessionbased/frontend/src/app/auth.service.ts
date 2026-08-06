import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs';
import { environment } from '../environments/environment';

export interface UserInfo {
  username: string;
  role: string;
}

export interface AdminUserView {
  id: number;
  username: string;
  role: string;
  enabled: boolean;
  activeSessions: number;
}

const API = environment.apiUrl;

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  readonly user = signal<UserInfo | null>(null);

  login(username: string, password: string) {
    return this.http
      .post<UserInfo>(`${API}/auth/login`, { username, password }, { withCredentials: true })
      .pipe(tap((user) => this.user.set(user)));
  }

  logout() {
    return this.http
      .post<{ message: string }>(`${API}/auth/logout`, {}, { withCredentials: true })
      .pipe(tap(() => this.user.set(null)));
  }

  me() {
    return this.http
      .get<UserInfo>(`${API}/auth/me`, { withCredentials: true })
      .pipe(tap((user) => this.user.set(user)));
  }

  changePassword(currentPassword: string, newPassword: string) {
    return this.http.post<{ message: string }>(
      `${API}/auth/change-password`,
      { currentPassword, newPassword },
      { withCredentials: true }
    );
  }

  publicInfo() {
    return this.http.get<{ message: string }>(`${API}/public/info`);
  }

  protectedData() {
    return this.http.get<{ message: string }>(`${API}/protected/data`, { withCredentials: true });
  }

  listUsers() {
    return this.http.get<AdminUserView[]>(`${API}/admin/users`, { withCredentials: true });
  }

  blockUser(username: string) {
    return this.http.post<{ message: string }>(
      `${API}/admin/users/${username}/block`,
      {},
      { withCredentials: true }
    );
  }

  unblockUser(username: string) {
    return this.http.post<{ message: string }>(
      `${API}/admin/users/${username}/unblock`,
      {},
      { withCredentials: true }
    );
  }
}
