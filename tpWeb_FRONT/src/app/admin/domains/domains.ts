import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DomaineService } from '../../services/domain.service';

@Component({
  selector: 'app-domains',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './domains.html'
})
export class Domains implements OnInit {

  domains: any[] = [];
  classifications: string[] = [];

  searchText: string = '';
  classification: string = '';

  selectedId: number | null = null;

  form = {
    nom: '',
    description: '',
    classification: ''
  };

  constructor(private service: DomaineService) {}

  // 🚀 INIT
  ngOnInit(): void {
    this.load();
    this.loadClassifications();
  }

  // 📌 LISTE
  load(): void {
    this.service.getAll().subscribe(data => {
      this.domains = data;
    });
  }

  // 📌 CLASSIFICATIONS (DYNAMIQUE)
  loadClassifications(): void {
    this.service.getClassifications().subscribe(data => {
      console.log("classifications:", data); // debug
      this.classifications = data;
    });
  }

  // ➕ ADD
  add(): void {
    console.log("FORM:", this.form);

    this.service.create(this.form).subscribe(() => {
      this.load();
      this.loadClassifications(); // refresh catégories
      this.reset();
    });
  }

  // ✏️ EDIT
  edit(d: any): void {
    this.selectedId = d.id;

    this.form = {
      nom: d.nom,
      description: d.description,
      classification: d.classification || ''
    };
  }

  // 🔄 UPDATE
  update(): void {
    if (!this.selectedId) return;

    this.service.update(this.selectedId, this.form).subscribe(() => {
      this.load();
      this.loadClassifications();
      this.reset();
    });
  }

  // ❌ DELETE
  delete(id: number): void {
    this.service.delete(id).subscribe(() => {
      this.load();
      this.loadClassifications();
    });
  }

  // 🔍 SEARCH + FILTER
 search(): void {
 console.log("🔥 SEARCH CLICKED");
  const hasText = this.searchText && this.searchText.trim() !== '';
  const hasClass = this.classification && this.classification.trim() !== '';

  // 🔄 rien → tout afficher
  if (!hasText && !hasClass) {
    this.load();
    return;
  }

  // 🔍 recherche + filtre combinés (si backend supporte)
  if (hasText && hasClass) {
    this.service.searchWithFilter(this.searchText, this.classification)
      .subscribe(data => {
        this.domains = data;
      });
    return;
  }

  // 🔍 search seul
  if (hasText) {
    this.service.search(this.searchText).subscribe(data => {
      this.domains = data;
    });
    return;
  }

  // 🔍 classification seule
  if (hasClass) {
    this.service.filterByClassification(this.classification).subscribe(data => {
      this.domains = data;
    });
  }
}
  // 🔁 RESET FORM
  reset(): void {
    this.selectedId = null;
    this.form = {
      nom: '',
      description: '',
      classification: ''
    };
  }
}