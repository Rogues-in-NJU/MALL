
import {Component, OnInit} from "@angular/core";
import {RefreshableTab} from "../../tab/tab.component";
import {ClassificationService} from "../../../../core/services/classification.service";
import {NzMessageService} from "ng-zorro-antd";
import {BehaviorSubject, Observable} from "rxjs";
import {Objects} from "../../../../core/services/util.service";
import { FormGroup } from "@angular/forms";

@Component({
  selector: 'app-classification-list',
  templateUrl: './classification-list.component.html',
  styleUrls: ['./classification-list.component.less']
})
export class ClassificationListComponent implements RefreshableTab, OnInit{

  constructor(
    private classification : ClassificationService,
    private message: NzMessageService,
    // private fb: FormBuilder,
  ){

  }
  refresh(): void {
  }

  ngOnInit(): void {
  }

}
