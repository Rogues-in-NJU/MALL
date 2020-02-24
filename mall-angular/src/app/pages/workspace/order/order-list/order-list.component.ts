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

@Component({
  selector: 'app-product-list',
  templateUrl: './order-list.component.html',
  styleUrls: ['./order-list.component.less']
})
export class OrderListComponent implements RefreshableTab, OnInit {

  totalPages: number = 1;
  pageIndex: number = 1;
  pageSize: number = 10;
  refundOrderList: Order[] = [];
  needResetPageIndex: boolean = false;

  constructor(
    private orderService: OrderService,
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
    this.orderService.findRefundOrderList(queryParams)
      .subscribe((res: ResultVO<TableResultVO<Order>>) => {
        if (!Objects.valid(res)) {
          this.message.error("请求失败！");
          return;
        }
        if (res.code !== ResultCode.SUCCESS.code) {
          this.message.error(res.message);
          return;
        }
        const tableResult: TableResultVO<Order> = res.data;
        this.totalPages = tableResult.totalPages;
        this.pageIndex = tableResult.pageIndex;
        this.pageSize = tableResult.pageSize;
        this.refundOrderList = tableResult.content;
        console.log(tableResult);
      }, (error: HttpErrorResponse) => {
        this.message.error('网络异常，请检查网络或者尝试重新登录!');
      });
  }

}
