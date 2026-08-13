import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap, catchError, of } from 'rxjs';

const SP = 'http://localhost:8083';

export interface SamlUser {
  nameId: string;
  relyingParty: string;
  attributes: Record<string, string[]>;
}

export interface AssertionInfo {
  available: boolean;
  message?: string;
  xml?: string;
  sizeBytes?: number;
  lineCount?: number;
  hasSignature?: boolean;
  hasAudienceRestriction?: boolean;
  hasConditions?: boolean;
  hasAttributeStatement?: boolean;
}

@Injectable({ providedIn: 'root' })
export class ApiService {
  private http = inject(HttpClient);

  readonly user = signal<SamlUser | null>(null);

  login(): void {
    window.location.href = `${SP}/saml2/authenticate/samltest`;
  }

  logout(): void {
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = `${SP}/logout`;
    document.body.appendChild(form);
    form.submit();
  }

  me(): Observable<SamlUser> {
    return this.http
      .get<SamlUser>(`${SP}/api/me`, { withCredentials: true })
      .pipe(tap((u) => this.user.set(u)));
  }

  checkSession(): Observable<SamlUser | null> {
    return this.http.get<SamlUser>(`${SP}/api/me`, { withCredentials: true }).pipe(
      tap((u) => this.user.set(u)),
      catchError(() => {
        this.user.set(null);
        return of(null);
      })
    );
  }

  publicInfo(): Observable<{ message: string }> {
    return this.http.get<{ message: string }>(`${SP}/api/public/info`);
  }

  assertion(): Observable<AssertionInfo> {
    return this.http.get<AssertionInfo>(`${SP}/api/debug/assertion`, { withCredentials: true });
  }
}
