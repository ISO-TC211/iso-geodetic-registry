import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";

@Injectable({
  providedIn: "root"
})
export class LoginService {
  constructor(private http: HttpClient) {}

  login(values) {
    console.log("called login. values: " + JSON.stringify(values));
  }

  forgotPass() {
    console.log("called forgotPass");
  }

  register() {
    console.log("called register");
  }
}
