import {Component, OnInit} from "@angular/core";
import {RefreshableTab} from "../../tab/tab.component";
import {NzMessageService} from "ng-zorro-antd";
import {Objects} from "../../../../core/services/util.service";
import {FormGroup} from "@angular/forms";
import {WithdrawalRecord} from "../../../../core/model/withdrawalRecord";
import {ResultCode, ResultVO, TableQueryParams, TableResultVO} from "../../../../core/model/result-vm";
import {HttpErrorResponse} from "@angular/common/http";
import {WithdrawalService} from "../../../../core/services/withdrawal.service";

@Component({
  selector: 'app-withdrawl-list',
  templateUrl: './withdrawal-record.component.html',
  styleUrls: ['./withdrawal-record.component.less']
})
export class WithdrawalRecordComponent implements RefreshableTab, OnInit {

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
    if (this.needResetPageIndex) {
      this.needResetPageIndex = false;
      this.pageIndex = 1;
    }
    const queryParams: TableQueryParams = {
      pageIndex: this.pageIndex,
      pageSize: this.pageSize
    };
    this.withdrawlService.findTodoRecordList(queryParams)
      .subscribe((res: ResultVO<TableResultVO<WithdrawalRecord>>) => {
        if (!Objects.valid(res)) {
          this.message.error("请求失败！");
          return;
        }
        if (res.code !== ResultCode.SUCCESS.code) {
          this.message.error(res.message);
          return;
        }
        const tableResult: TableResultVO<WithdrawalRecord> = res.data;
        this.totalPages = tableResult.totalPages;
        this.pageIndex = tableResult.pageIndex;
        this.pageSize = tableResult.pageSize;
        this.todoRecordList = tableResult.content;
        console.log(tableResult);
      }, (error: HttpErrorResponse) => {
        this.message.error('网络异常，请检查网络或者尝试重新登录!');
      });
  }



}
