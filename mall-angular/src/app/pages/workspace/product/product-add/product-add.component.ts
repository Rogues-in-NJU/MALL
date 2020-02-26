/**
 * Created by Administrator on 2019/12/18.
 */
import {Component, OnInit} from "@angular/core";
import {RefreshableTab} from "../../tab/tab.component";
import {ProductService} from "../../../../core/services/product.service";
import {ProductVO} from "../../../../core/model/product";
import {HttpClient, HttpErrorResponse, HttpEvent, HttpEventType, HttpResponse} from "@angular/common/http";
import {NzMessageService, UploadFile, UploadXHRArgs} from "ng-zorro-antd";
import {BehaviorSubject, Observable} from "rxjs";
import {Objects} from "../../../../core/services/util.service";
import { FormGroup } from "@angular/forms";
import {ResultCode, ResultVO, TableQueryParams, TableResultVO} from "../../../../core/model/result-vm";
import {environment} from "ng-zorro-antd/core/environments/environment";
import {AppConfig} from "../../../../../environments/environment";

@Component({
  selector: 'app-product-list',
  templateUrl: './product-add.component.html',
  styleUrls: ['./product-add.component.less']
})
export class ProductAddComponent implements RefreshableTab, OnInit {

  constructor(
    private product : ProductService,
    private message: NzMessageService,
    // private fb: FormBuilder,
    private http : HttpClient,
  ){

  }

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

  handlePreview = (file: UploadFile) => {
    this.previewImage = file.url || file.thumbUrl;
    this.previewVisible = true;
  };

  imageUpload = (item: UploadXHRArgs) => {
    console.log("!!!!!!!!!!!!!!!!!!!!!!!")
    const url = `${AppConfig.BASE_URL}/upload-product-info`;
    const formData = new FormData();
    formData.append('image', item.file as any);
    return this.http.post(url, formData, {
      reportProgress: true,
      withCredentials: true
    })
      .subscribe(
          // tslint:disable-next-line no-any
        (event: HttpEvent<any>) => {
          if (event.type === HttpEventType.UploadProgress) {
            if (event.total! > 0) {
              // tslint:disable-next-line:no-any
              (event as any).percent = (event.loaded / event.total!) * 100;
            }
            item.onProgress!(event, item.file!);
          } else if (event instanceof HttpResponse) {
            item.onSuccess!(event.body, item.file!, event);
          }
        },
        err => {
          item.onError!(err, item.file!);
        }
        );
    console.log("~~~~~~~~~~~~~~~~~~~~~")
  };
}
