import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { environment } from "../../environments/environment";

@Injectable({
  providedIn: "root"
})
export class RegisterRepositoryService {
  constructor(private http: HttpClient) {}

  getData() {
    return this.http.get(`${environment.apiRoot}/findAll`);
  }
}
