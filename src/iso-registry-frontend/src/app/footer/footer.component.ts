import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {

  public response: Observable<{version: string}>;

  constructor(private httpClient: HttpClient) { }

  ngOnInit() {
    this.response = this.httpClient.get<{version: string}>(`${environment.apiRoot}/version`, );
  }

}
