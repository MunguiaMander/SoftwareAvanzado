import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService, TokenInfo, Note } from '../api.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent implements OnInit {
  private api = inject(ApiService);
  private router = inject(Router);

  readonly user = this.api.user;
  readonly tokens = signal<TokenInfo | null>(null);
  readonly notes = signal<Note[]>([]);
  readonly newNoteText = signal('');

  readonly lastResult = signal<{ ok: boolean; status: number; message: string } | null>(null);

  readonly showRaw = signal(false);

  ngOnInit(): void {
    this.api.checkSession().subscribe((u) => {
      if (!u) {
        this.router.navigate(['/public']);
        return;
      }
      this.loadTokens();
    });
  }

  loadTokens(): void {
    this.api.tokens().subscribe({
      next: (t) => this.tokens.set(t),
      error: () => this.tokens.set(null),
    });
  }

  readNotes(): void {
    this.api.listNotes().subscribe({
      next: (n) => {
        this.notes.set(n);
        this.lastResult.set({
          ok: true,
          status: 200,
          message: `200 OK — el token incluye notes.read. Se leyeron ${n.length} notas.`,
        });
      },
      error: (err) => {
        this.lastResult.set({
          ok: false,
          status: err.status,
          message: this.explainError(err, 'notes.read'),
        });
      },
    });
  }

  writeNote(): void {
    const text = this.newNoteText().trim() || 'Nota creada desde la POC';
    this.api.createNote(text).subscribe({
      next: (n) => {
        this.notes.update((list) => [...list, n]);
        this.newNoteText.set('');
        this.lastResult.set({
          ok: true,
          status: 200,
          message: '200 OK — el token incluye notes.write. Nota creada.',
        });
      },
      error: (err) => {
        this.lastResult.set({
          ok: false,
          status: err.status,
          message: this.explainError(err, 'notes.write'),
        });
      },
    });
  }

  private explainError(err: any, scope: string): string {
    if (err.status === 403) {
      const granted = err.error?.grantedScopes?.join(', ') ?? '(desconocidos)';
      return `403 FORBIDDEN — el access token NO incluye el scope "${scope}". ` +
             `Scopes concedidos: [${granted}]. ` +
             `Mismo usuario, misma sesion: lo unico que cambia es lo que se consintio.`;
    }
    if (err.status === 401) {
      return '401 UNAUTHORIZED — el token expiro o no es valido.';
    }
    return `Error ${err.status}`;
  }

  pretty(json: string): string {
    try {
      return JSON.stringify(JSON.parse(json), null, 2);
    } catch {
      return json;
    }
  }

  logout(): void {
    this.api.logout();
  }
}
