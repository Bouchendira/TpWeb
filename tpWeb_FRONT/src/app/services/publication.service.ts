import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PublicationService {

  private baseUrl = 'http://localhost:8080/api/publications';

  constructor(private http: HttpClient) {}

 getAll() {
  return this.http.get<any[]>('http://localhost:8080/api/publications');
}

  uploadPublication(formData: FormData): Observable<any> {
    return this.http.post(this.baseUrl, formData);
  }
}