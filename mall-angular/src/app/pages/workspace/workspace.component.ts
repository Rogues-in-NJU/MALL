import { Component, OnInit, Pipe, PipeTransform, ViewChild } from "@angular/core";
import { LocalStorageService } from "../../core/services/local-storage.service";
import { Router } from "@angular/router";
import { HttpErrorResponse } from "@angular/common/http";
import { SimpleReuseStrategy } from "../../core/strategy/simple-reuse.strategy";
import { TabComponent } from "./tab/tab.component";
import { Objects } from "../../core/services/util.service";
import { NzNotificationService } from "ng-zorro-antd";

@Component({
  selector: 'app-workspace',
  templateUrl: './workspace.component.html',
  styleUrls: [ './workspace.component.less' ]
})
export class WorkspaceComponent implements OnInit {

  @ViewChild("tabComponent", null) tabComponent: TabComponent;

  isCollapsed: boolean = false;
  isOpen: boolean = false;


  constructor(
    private storage: LocalStorageService,
    private route: Router,
    private notification: NzNotificationService
  ) {
  }

  ngOnInit(): void {
  }

  logout(): void {
  }

}
