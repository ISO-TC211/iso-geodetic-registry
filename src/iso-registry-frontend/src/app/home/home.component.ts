import { Component, OnInit } from "@angular/core";
import { RegisterRepositoryService } from "../services/register-repository.service";
@Component({
  selector: "app-home",
  templateUrl: "./home.component.html",
  styleUrls: ["./home.component.scss"]
})
export class HomeComponent implements OnInit {
  constructor(private registerRepositoryService: RegisterRepositoryService) {}
  restAPIResults = "";
  ngOnInit() {
    this.getData();
  }

  getData = () => {
    this.registerRepositoryService.getData().subscribe((response: any) => {
      this.restAPIResults = response;
    });
  };
}

export interface Result {
  goldenCrosses: [];
  totalCount: 0;
}
