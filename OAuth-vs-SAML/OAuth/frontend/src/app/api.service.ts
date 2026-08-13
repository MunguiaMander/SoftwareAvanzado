import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap, catchError, of } from 'rxjs';

const BFF = 'http://localhost:8081';

export interface UserInfo {
  sub: string;
  name: string;
  email: string;
  picture: string;
}

export interface TokenInfo {
  idTokenRaw: string;
  idTokenSizeBytes: number;
  idTokenHeader: string;
  idTokenPayload: string;
  accessTokenRaw: string;
  accessTokenPayload: string;
  grantedScopes: string[];
  expiresAt: string;
  hasRefreshToken: boolean;
}

export interface Note {
  id: number;
  owner: string;
  text: string;
}

@Injectable({ providedIn: 'root' })
export class ApiService {
  private http = inject(HttpClient);

  readonly user = signal<UserInfo | null>(null);

  login(): void {
    window.location.href = `${BFF}/oauth2/authorization/bff-client`;
  }

  logout(): void {
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = `${BFF}/logout`;
    document.body.appendChild(form);
    form.submit();
  }

  me(): Observable<UserInfo> {
    return this.http
      .get<UserInfo>(`${BFF}/api/me`, { withCredentials: true })
      .pipe(tap((u) => this.user.set(u)));
  }

  checkSession(): Observable<UserInfo | null> {
    return this.http.get<UserInfo>(`${BFF}/api/me`, { withCredentials: true }).pipe(
      tap((u) => this.user.set(u)),
      catchError(() => {
        this.user.set(null);
        return of(null);
      })
    );
  }

  publicInfo(): Observable<{ message: string }> {
    return this.http.get<{ message: string }>(`${BFF}/api/public/info`);
  }

  tokens(): Observable<TokenInfo> {
    return this.http.get<TokenInfo>(`${BFF}/api/debug/tokens`, { withCredentials: true });
  }

  listNotes(): Observable<Note[]> {
    return this.http.get<Note[]>(`${BFF}/api/notes`, { withCredentials: true });
  }

  createNote(text: string): Observable<Note> {
    return this.http.post<Note>(`${BFF}/api/notes`, { text }, { withCredentials: true });
  }
}
