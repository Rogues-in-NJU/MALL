/**
 * Created by Administrator on 2019/12/18.
 */
import {Component, OnInit} from "@angular/core";
import {RefreshableTab} from "../../tab/tab.component";
import {ProductService} from "../../../../core/services/product.service";
import {ProductVO} from "../../../../core/model/product";
import {HttpErrorResponse} from "@angular/common/http";
import {NzMessageService, UploadFile} from "ng-zorro-antd";
import {BehaviorSubject, Observable} from "rxjs";
import {Objects} from "../../../../core/services/util.service";
import { FormGroup } from "@angular/forms";
import {ResultCode, ResultVO, TableQueryParams, TableResultVO} from "../../../../core/model/result-vm";

@Component({
  selector: 'app-product-list',
  templateUrl: './product-add.component.html',
  styleUrls: ['./product-add.component.less']
})
export class ProductAddComponent implements RefreshableTab, OnInit {

  ngOnInit(): void {
  }

  refresh(): void {
  }

  showUploadList = {
    showPreviewIcon: true,
    showRemoveIcon: true,
    hidePreviewIconInNonImage: true
  };
  fileList = [
    {
      uid: -1,
      name: 'xxx.png',
      status: 'done',
      url: 'https://zos.alipayobjects.com/rmsportal/jkjgkEfvpUPVyRjUImniVslZfWPnJuuZ.png'
    }
  ];
  previewImage: string | undefined = '';
  previewVisible = false;

  constructor() {}

  handlePreview = (file: UploadFile) => {
    this.previewImage = file.url || file.thumbUrl;
    this.previewVisible = true;
  };
}
