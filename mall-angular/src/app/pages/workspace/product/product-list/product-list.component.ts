/**
 * Created by Administrator on 2019/12/18.
 */
import {Component, OnInit} from "@angular/core";
import {RefreshableTab} from "../../tab/tab.component";
import {ProductService} from "../../../../core/services/product.service";
import {ProductVO} from "../../../../core/model/product";
import {HttpErrorResponse} from "@angular/common/http";
import {NzMessageService} from "ng-zorro-antd";
import {BehaviorSubject, Observable} from "rxjs";
import {Objects} from "../../../../core/services/util.service";
import { FormGroup } from "@angular/forms";
import {ResultCode, ResultVO, TableQueryParams, TableResultVO} from "../../../../core/model/result-vm";

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.less']
})
export class ProductListComponent implements RefreshableTab, OnInit{

  totalPages : number = 1;
  pageIndex : number = 1;
  pageSize : number = 10;
  isLoading: boolean = false;

  products: ProductVO[] = [];

  constructor(
    private product : ProductService,
    private message: NzMessageService,
    // private fb: FormBuilder,
  ){

  }

  refresh(): void {
    this.search();
  }

  ngOnInit(): void {
    this.search();
  }

  search() : void{
    this.isLoading = true;

    const queryParams: TableQueryParams = {
      pageIndex : this.pageIndex,
      pageSize : this.pageSize,
    };

    this.product.findAll(queryParams)
      .subscribe((res: ResultVO<TableResultVO<ProductVO>>) =>{
        console.log(res);
        if (!Objects.valid(res)) {
          this.message.error("请求失败！");
          return;
        }
        if (res.code !== ResultCode.SUCCESS.code) {
          this.message.error(res.message);
          return;
        }

        const tableResult: TableResultVO<ProductVO> = res.data;
        this.totalPages = tableResult.totalPages;
        this.pageIndex = tableResult.pageIndex;
        this.pageSize = tableResult.pageSize;
        this.products = tableResult.result;

      }, (error: HttpErrorResponse) => {
        this.message.error('网络异常，请检查网络或者尝试重新登录!');
      });

    this.isLoading = false;
  }

  confirmAbandon(id: number): void {
    console.log('confirm abandon: ' + id);
    this.product.deleteProduct(id)
      .subscribe((res: ResultVO<any>) => {
        if (!Objects.valid(res)) {
          this.message.error('删除失败!');
          return;
        }
        if (res.code !== ResultCode.SUCCESS.code) {
          this.message.error(res.message);
          return;
        }
        this.message.success('删除成功!');
      }, (error: HttpErrorResponse) => {
        this.message.error('网络异常，请检查网络或者尝试重新登录!');
        this.refresh();
      }, () => {
        this.refresh();
      });
  }


}
