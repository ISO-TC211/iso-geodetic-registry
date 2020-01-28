import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  // version: string;

  constructor(private httpClient: HttpClient) { }

  ngOnInit() {
    // this.httpClient.get(`${environment.apiRoot}/version`).subscribe((version: string) => this.version = version);//.then(version => this.version = version);
  }
}
