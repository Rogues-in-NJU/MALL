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
  templateUrl: './withdrawal-history.component.html',
  styleUrls: ['./withdrawal-history.component.less']
})
export class WithdrawalHistoryComponent implements RefreshableTab, OnInit {

  totalPages: number = 1;
  pageIndex: number = 1;
  pageSize: number = 10;
  todoRecordList: WithdrawalRecord[] = [];
  isLoading: boolean = false;

  needResetPageIndex: boolean = false;

  constructor(
    private withdrawalService: WithdrawalService,
    private message: NzMessageService,
    // private fb: FormBuilder,
  ) {

  }

  refresh(): void {
    this.loadData();
  }

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    if (this.needResetPageIndex) {
      this.needResetPageIndex = false;
      this.pageIndex = 1;
    }
    const queryParams: TableQueryParams = {
      pageIndex: this.pageIndex,
      pageSize: this.pageSize
    };
    this.withdrawalService.findDoneRecordList(queryParams)
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
        this.todoRecordList = tableResult.result;
      }, (error: HttpErrorResponse) => {
        this.message.error('网络异常，请检查网络或者尝试重新登录!');
      });
  }

}
