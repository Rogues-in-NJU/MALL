import {Component, OnInit} from "@angular/core";
import {RefreshableTab} from "../../tab/tab.component";
import {NzMessageService} from "ng-zorro-antd";
import {WithdrawalRecord} from "../../../../core/model/withdrawalRecord";

import {WithdrawalService} from "../../../../core/services/withdrawal.service";

@Component({
  selector: 'app-withdrawl-list',
  templateUrl: './withdrawal-condition.component.html',
  styleUrls: ['./withdrawal-condition.component.less']
})
export class WithdrawalConditionComponent implements RefreshableTab, OnInit {

  totalPages: number = 1;
  pageIndex: number = 1;
  pageSize: number = 10;
  todoRecordList: WithdrawalRecord[] = [];

  needResetPageIndex: boolean = false;
  constructor(
    private withdrawlService: WithdrawalService,
    private message: NzMessageService,
    // private fb: FormBuilder,
  ) {

  }

  refresh(): void {

  }

  ngOnInit(): void {

  }



}
