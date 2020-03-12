import { Component } from "@angular/core";
import { environment } from "../../environments/environment";
import { FormGroup, FormBuilder } from "@angular/forms";
import { LoginService } from "./login.service";

@Component({
  selector: "app-header",
  templateUrl: "./header.component.html",
  styleUrls: ["./header.component.scss"]
})
export class HeaderComponent {
  loginForm: FormGroup;

  feedbackUrl = environment.feedbackUrl;

  constructor(
    private formBuilder: FormBuilder,
    private loginService: LoginService
  ) {
    this.createForm();
  }

  private createForm() {
    this.loginForm = this.formBuilder.group({
      email: "",
      password: ""
    });
  }

  public forgotPass() {
    this.loginService.forgotPass();
  }

  public register() {
    this.loginService.register();
  }

  public login() {
    this.loginService.login(this.loginForm.value);
  }
}
