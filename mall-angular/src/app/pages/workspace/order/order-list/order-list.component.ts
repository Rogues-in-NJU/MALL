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

  isLoading: boolean = false;
  totalPages: number = 1;
  pageIndex: number = 1;
  pageSize: number = 10;
  orderList: Order[] = [];
  needResetPageIndex: boolean = false;

  selectedStatus: number;
  shouldResetIndex: boolean = false;

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

  resetIndex(): void {
    this.shouldResetIndex = true;
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
    this.orderService.findOrderList(queryParams)
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
        this.orderList = tableResult.result;
        console.log(tableResult);
      }, (error: HttpErrorResponse) => {
        this.message.error('网络异常，请检查网络或者尝试重新登录!');
      });
  }

  confirmRefund(id: number): void {
    this.orderService.refund(id)
      .subscribe((res: ResultVO<any>) => {
          if (!Objects.valid(res)) {
            return;
          }
          if (res.code !== ResultCode.SUCCESS.code) {
            this.message.error(res.message);
          }
        }, (error: HttpErrorResponse) => {
          this.message.error('网络异常，请检查网络或者尝试重新登录!');
        }, () => {
          this.refresh();
        }
      );

  }

  search(): void {
    this.isLoading = true;
    if (this.shouldResetIndex) {
      this.pageIndex = 1;
      this.shouldResetIndex = false;
    }
    const queryParams: TableQueryParams = {
      pageIndex: this.pageIndex,
      pageSize: this.pageSize
    };
    if (Objects.valid(this.selectedStatus)) {
      Object.assign(queryParams, {
        status: this.selectedStatus
      });
    }

    this.orderService.findOrderList(queryParams)
      .subscribe((res: ResultVO<TableResultVO<Order>>) => {
        // console.log(res)
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
        this.orderList = tableResult.result;
      }, (error: HttpErrorResponse) => {
        this.message.error('网络异常，请检查网络或者尝试重新登录!');
      });
    this.isLoading = false;
  }

  resetQueryParams(): void {
    this.selectedStatus = null;
    this.refresh();
  }

}
