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
  templateUrl: './product-info.component.html',
  styleUrls: ['./product-info.component.less']
})
export class ProductInfoComponent implements RefreshableTab, OnInit {

  ngOnInit(): void {
  }

  refresh(): void {
  }

}
