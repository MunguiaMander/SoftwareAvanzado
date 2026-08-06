import { Component, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-public',
  imports: [RouterLink, ButtonModule, CardModule],
  templateUrl: './public.component.html',
  styleUrl: './public.component.scss',
})
export class PublicComponent {
  private auth = inject(AuthService);
  message = signal('Cargando...');

  constructor() {
    this.auth.publicInfo().subscribe({
      next: (res) => this.message.set(res.message),
      error: () => this.message.set('No se pudo contactar al backend'),
    });
  }
}
