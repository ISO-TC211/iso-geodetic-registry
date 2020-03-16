import { Component, OnInit } from "@angular/core";
import { RegisterRepositoryService } from "../services/register-repository.service";

@Component({
  selector: "app-tree",
  templateUrl: "./tree.component.html",
  styleUrls: ["./tree.component.scss"]
})
export class TreeComponent implements OnInit {
  constructor(private registerRepositoryService: RegisterRepositoryService) {}
  restAPIResults = "";

  private options = {};

  ngOnInit() {
    this.getData();
  }

  private getData = () => {
    this.registerRepositoryService.getData().subscribe((response: any) => {
      this.restAPIResults = response;

      this.restAPIResults[0]["children"] = response[0]["containedItemClasses"];
    });
  };
}
