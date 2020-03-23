import { Component, OnInit, Output, EventEmitter } from "@angular/core";
import { RegisterRepositoryService } from "../services/register-repository.service";

@Component({
  selector: "app-tree",
  templateUrl: "./tree.component.html",
  styleUrls: ["./tree.component.scss"]
})
export class TreeComponent implements OnInit {
  @Output() loading = new EventEmitter<boolean>();

  constructor(private registerRepositoryService: RegisterRepositoryService) {
    this.loading.emit(true);
  }
  restAPIResults = "";

  private options = {};

  ngOnInit() {
    this.getData();
  }

  private getData = () => {
    this.registerRepositoryService.getData().subscribe(
      (response: any) => {
        this.loading.emit(false);
        this.restAPIResults = response;

        this.restAPIResults[0]["children"] =
          response[0]["containedItemClasses"];
      },
      error => {
        this.loading.emit(false);
      }
    );
  };
}
