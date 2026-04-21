import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ResearcherService } from '../../services/researcher.service';
import { ChangeDetectorRef } from '@angular/core';
@Component({
  selector: 'app-researchers',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './researchers.html'
})
export class Researchers implements OnInit {

  researchers: any[] = [];
  searchText: string = '';

  selectedId: number | null = null;

  form: any = {
    nom: '',
    prenom: '',
    email: '',
    biographie: ''
  };


constructor(
  private service: ResearcherService,
  private cdr: ChangeDetectorRef
) {}

ngOnInit(): void {
  this.load();
}

load(): void {
  this.service.getAll().subscribe((data: any) => {
    this.researchers = data;
    this.cdr.detectChanges();
  });
}

  // ➕ AJOUT
  add(): void {
    this.service.create(this.form).subscribe(() => {
      this.load();
      this.reset();
    });
  }

  // ✏️ EDIT
  edit(r: any): void {
    this.selectedId = r.id;
    this.form = {
      nom: r.nom,
      prenom: r.prenom,
      email: r.email,
      biographie: r.biographie
    };
  }

  // 🔄 UPDATE
  update(): void {
    if (!this.selectedId) return;

    this.service.update(this.selectedId, this.form).subscribe(() => {
      this.load();
      this.reset();
    });
  }

  // ❌ DELETE
  delete(id: number): void {
    this.service.delete(id).subscribe(() => {
      this.load();
    });
  }

  // 🔍 SEARCH
  search(): void {
    if (!this.searchText.trim()) {
      this.load();
      return;
    }

    this.service.search(this.searchText).subscribe((data: any) => {
      this.researchers = data;
    });
  }

  //  RESET FORM
  reset(): void {
    this.selectedId = null;
    this.form = {
      nom: '',
      prenom: '',
      email: '',
      biographie: ''
    };
  }
}