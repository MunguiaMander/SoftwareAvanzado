import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ApiService } from '../api.service';

@Component({
  selector: 'app-public',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './public.component.html',
  styleUrl: '../dashboard/dashboard.component.scss',
})
export class PublicComponent implements OnInit {
  private api = inject(ApiService);
  private router = inject(Router);

  readonly message = signal('');
  readonly protectedResult = signal('');

  ngOnInit(): void {
    this.api.publicInfo().subscribe((r) => this.message.set(r.message));
    this.api.checkSession().subscribe((u) => {
      if (u) this.router.navigate(['/dashboard']);
    });
  }

  login(): void {
    this.api.login();
  }

  tryProtected(): void {
    this.api.me().subscribe({
      next: (u) => this.protectedResult.set(`200 OK — sesión activa: ${u.nameId}`),
      error: (e) =>
        this.protectedResult.set(
          `${e.status} — sin assertion válida no hay acceso. El SP exige autenticación.`
        ),
    });
  }
}
