/**
 * Created by Administrator on 2019/12/18.
 */
import {Component, OnInit} from "@angular/core";
import {RefreshableTab} from "../../tab/tab.component";
import {OrderService} from "../../../../core/services/order.service";
import {NzMessageService} from "ng-zorro-antd";
import {Order} from "../../../../core/model/order";
import {ResultCode, ResultVO, TableQueryParams, TableResultVO} from "../../../../core/model/result-vm";
import {WithdrawalRecord} from "../../../../core/model/withdrawalRecord";
import {Objects} from "../../../../core/services/util.service";
import {HttpErrorResponse} from "@angular/common/http";
import {OrderSummary} from "../../../../core/model/orderSummary";

@Component({
  selector: 'app-product-list',
  templateUrl: './order-summary.component.html',
  styleUrls: ['./order-summary.component.less']
})
export class OrderSummaryComponent implements RefreshableTab, OnInit {
  summary: OrderSummary;
  constructor(
    private orderService: OrderService,
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
    this.orderService.summary().subscribe((res: ResultVO<OrderSummary>) => {
      if (!Objects.valid(res)) {
        this.message.error("请求失败！");
        return;
      }
      if (res.code !== ResultCode.SUCCESS.code) {
        this.message.error(res.message);
        return;
      }
      this.summary = res.data;
      console.log(this.summary);
    }, (error: HttpErrorResponse) => {
      this.message.error('网络异常，请检查网络或者尝试重新登录!');
    });
  }


}
