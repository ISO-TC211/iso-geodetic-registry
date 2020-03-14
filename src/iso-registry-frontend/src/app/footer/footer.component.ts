import { Component, OnInit } from "@angular/core";
import { RegisterRepositoryService } from "../services/register-repository.service";
@Component({
  selector: "app-footer",
  templateUrl: "./footer.component.html",
  styleUrls: ["./footer.component.scss"]
})
export class FooterComponent implements OnInit {
  constructor(private registeryService: RegisterRepositoryService) {}
  public version = "";
  ngOnInit() {
    this.getVersion();
  }

  private getVersion() {
    this.registeryService.getVersion().subscribe((response: any) => {
      this.version = response;
    });
  }
}
