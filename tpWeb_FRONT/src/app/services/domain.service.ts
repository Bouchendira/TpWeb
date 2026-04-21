import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class DomaineService {

  private api = 'http://localhost:8080/api/domaines';

  constructor(private http: HttpClient) {}

  getAll(): Observable<any> {
    return this.http.get(this.api);
  }

  create(data: any): Observable<any> {
    return this.http.post(this.api, data);
  }

  update(id: number, data: any): Observable<any> {
    return this.http.put(`${this.api}/${id}`, data);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.api}/${id}`);
  }

  // 🔍 recherche par nom
  search(name: string): Observable<any> {
    return this.http.get(`${this.api}/search?name=${name}`);
  }

  // 📚 filtre par classification
  filterByClassification(classification: string): Observable<any> {
    return this.http.get(`${this.api}/filter?classification=${classification}`);
  }


  getClassifications() {
  return this.http.get<string[]>('http://localhost:8080/api/domaines/classifications');
}

searchWithFilter(name: string, classification: string) {
  return this.http.get<any[]>(
    `http://localhost:8080/api/domaines/search?name=${name}&classification=${classification}`
  );
}
}