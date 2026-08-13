import { Component, inject, signal, computed, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ApiService, AssertionInfo } from '../api.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent implements OnInit {
  private api = inject(ApiService);
  private router = inject(Router);

  readonly user = this.api.user;
  readonly assertion = signal<AssertionInfo | null>(null);
  readonly showXml = signal(false);

  readonly attributeRows = computed(() => {
    const u = this.user();
    if (!u) return [];
    return Object.entries(u.attributes).map(([name, values]) => ({
      name,
      value: values.join(', '),
    }));
  });

  readonly sizeKb = computed(() => {
    const a = this.assertion();
    return a?.sizeBytes ? (a.sizeBytes / 1024).toFixed(1) : '—';
  });

  ngOnInit(): void {
    this.api.checkSession().subscribe((u) => {
      if (!u) {
        this.router.navigate(['/public']);
        return;
      }
      this.api.assertion().subscribe({
        next: (a) => this.assertion.set(a),
        error: () => this.assertion.set(null),
      });
    });
  }

  formatXml(xml: string): string {
    let formatted = '';
    let indent = 0;
    xml.replace(/>\s*</g, '><').split(/(?=<)/).forEach((node) => {
      if (node.match(/^<\/\w/)) indent--;
      formatted += '  '.repeat(Math.max(0, indent)) + node.trim() + '\n';
      if (node.match(/^<\w[^>]*[^\/]>$/) && !node.startsWith('<?')) indent++;
    });
    return formatted.trim();
  }

  logout(): void {
    this.api.logout();
  }
}
