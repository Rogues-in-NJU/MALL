import {Component, OnInit} from "@angular/core";
import {RefreshableTab} from "../../tab/tab.component";
import {NzMessageService} from "ng-zorro-antd";
import {WithdrawalRecord} from "../../../../core/model/withdrawalRecord";

import {WithdrawalService} from "../../../../core/services/withdrawal.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ResultCode, ResultVO} from "../../../../core/model/result-vm";
import {Objects} from "../../../../core/services/util.service";
import {HttpErrorResponse} from "@angular/common/http";
import {WithdrawalCondition} from "../../../../core/model/withdrawalCondition";

@Component({
  selector: 'app-withdrawl-list',
  templateUrl: './withdrawal-condition.component.html',
  styleUrls: ['./withdrawal-condition.component.less']
})
export class WithdrawalConditionComponent implements RefreshableTab, OnInit {

  conditionEditForm: FormGroup;
  withdrawalCondition: WithdrawalCondition;

  constructor(
    private withdrawalService: WithdrawalService,
    private fb: FormBuilder,
    private message: NzMessageService,
  ) {

  }

  refresh(): void {
    this.loadData();
  }

  ngOnInit(): void {
    this.conditionEditForm = this.fb.group({
      member: [null, Validators.required],
      cash: [null, Validators.required]
    });
    this.loadData();
  }

  loadData(): void {
    this.withdrawalService.getCondition()
      .subscribe((res: ResultVO<WithdrawalCondition>) => {
        if (!Objects.valid(res)) {
          this.message.error("请求失败！");
          return;
        }
        if (res.code !== ResultCode.SUCCESS.code) {
          this.message.error(res.message);
          return;
        }
        this.withdrawalCondition = res.data;
        this.conditionEditForm = this.fb.group({
          id: [this.withdrawalCondition.id],
          member: [this.withdrawalCondition.member, Validators.required],
          cash: [this.withdrawalCondition.cash / 100, Validators.required]
        });
      }, (error: HttpErrorResponse) => {
        this.message.error('网络异常，请检查网络或者尝试重新登录!');
      }, () => {
      });
  }

  saveCondition(): void {
    const conditionEditData: WithdrawalCondition = this.conditionEditForm.getRawValue();
    this.withdrawalService.saveCondition(conditionEditData)
      .subscribe((res: ResultVO<any>) => {
        if (!Objects.valid(res)) {
          this.message.error('修改失败!');
          return;
        }
        if (res.code !== ResultCode.SUCCESS.code) {
          this.message.error(res.message);
          return;
        }
        this.message.success('修改成功!');
      }, (error: HttpErrorResponse) => {
        this.message.error('网络异常，请检查网络或者尝试重新登录!');
        this.refresh();
      }, () => {
        this.refresh();
      });
  }


}
