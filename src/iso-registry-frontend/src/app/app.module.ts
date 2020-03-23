import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";
import { HttpClientModule } from "@angular/common/http";
import { AppRoutingModule } from "./app-routing.module";
import { AppComponent } from "./app.component";
import { FooterComponent } from "./footer/footer.component";
import { HomeComponent } from "./home/home.component";
import { HeaderComponent } from "./header/header.component";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { HTTP_INTERCEPTORS } from "@angular/common/http";
import { RequestInterceptorService } from "../app/services/request-interceptor.service";
import { AuthService } from "../app/services/auth.service";
import {
  NgbDropdownModule,
  NgbCollapseModule
} from "@ng-bootstrap/ng-bootstrap";
import { TreeModule } from "angular-tree-component";
import { TreeComponent } from "./tree/tree.component";
import { ErrorInterceptorService } from "./services/error-interceptor.service";
@NgModule({
  declarations: [
    AppComponent,
    FooterComponent,
    HomeComponent,
    HeaderComponent,
    TreeComponent
  ],
  imports: [
    NgbDropdownModule,
    NgbCollapseModule,
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    TreeModule.forRoot()
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: RequestInterceptorService,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ErrorInterceptorService,
      multi: true
    },
    AuthService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
