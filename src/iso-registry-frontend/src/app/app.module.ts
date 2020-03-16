import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";
import { HttpClientModule } from "@angular/common/http";
import { AppRoutingModule } from "./app-routing.module";
import { AppComponent } from "./app.component";
import { FooterComponent } from "./footer/footer.component";
import { HomeComponent } from "./home/home.component";
import { HeaderComponent } from "./header/header.component";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import {
  NgbDropdownModule,
  NgbCollapseModule
} from "@ng-bootstrap/ng-bootstrap";
import { TreeModule } from "angular-tree-component";
import { TreeComponent } from './tree/tree.component';
@NgModule({
  declarations: [AppComponent, FooterComponent, HomeComponent, HeaderComponent, TreeComponent],
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
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}
