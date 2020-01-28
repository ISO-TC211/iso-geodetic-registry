import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {

  version: string;

  constructor(private httpClient: HttpClient) { }

  ngOnInit() {
    this.httpClient.get(`${environment.apiRoot}/version`, {responseType: "text"}).subscribe((version: string) => this.version = version);//.then(version => this.version = version);
  }

}
