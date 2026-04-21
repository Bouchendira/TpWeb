import { Component, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { PublicationService } from '../../services/publication.service';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
@Component({
  selector: 'app-publication',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './publications.html'
})
export class PublicationComponent implements OnInit {


  publications: any[] = [];

  // form fields
  titre = '';
  doi = '';
  file: File | null = null;

  selectedPdf: any = null;

  constructor(
    private service: PublicationService,
    private sanitizer: DomSanitizer
  ) {}

  ngOnInit(): void {
    this.load();
  }

 load() {
  this.service.getAll().subscribe(data => {
    console.log("LISTE REÇUE =", data);
    this.publications = data;
  });
}

  onFileSelected(event: any) {
    this.file = event.target.files[0];
  }

 upload() {
  const formData = new FormData();

  formData.append('titre', this.titre);
  formData.append('doi', this.doi);

  if (this.file) {
    formData.append('fichierPdf', this.file);
  }

  this.service.uploadPublication(formData).subscribe({
    next: (res) => {
      console.log("UPLOAD OK", res);

      // RESET FORM
      this.titre = '';
      this.doi = '';
      this.file = null;

      // FORCE reload
      this.load();
    }
  });
}

  openPdf(pub: any) {
    window.open(pub.fichierPdf, '_blank');
  }

  viewPdf(pub: any) {
    this.selectedPdf =
      this.sanitizer.bypassSecurityTrustResourceUrl(pub.fichierPdf);
  }
}