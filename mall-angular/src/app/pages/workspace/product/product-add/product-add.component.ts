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
import {ResultCode, ResultVO, TableQueryParams, TableResultVO} from "../../../../core/model/result-vm";
import {environment} from "ng-zorro-antd/core/environments/environment";
import {AppConfig} from "../../../../../environments/environment";
import { NzFormModule } from 'ng-zorro-antd/form';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-add.component.html',
  styleUrls: ['./product-add.component.less']
})
export class ProductAddComponent implements RefreshableTab, OnInit {

  validateForm: FormGroup;

  name: string = "";
  classificationId: number = -1;
  buyingPrice: number = 0;
  price: number = 0;
  percent: number = 0;
  quantity: number = 0;
  sellStartTime: string = "";
  sellEndTime: string = "";
  status: number = 0;

  submitForm(): void {
    for (const i in this.validateForm.controls) {
      this.validateForm.controls[i].markAsDirty();
      this.validateForm.controls[i].updateValueAndValidity();
    }
  }

  updateConfirmValidator(): void {
    /** wait for refresh value */
    Promise.resolve().then(() => this.validateForm.controls.checkPassword.updateValueAndValidity());
  }

  confirmationValidator = (control: FormControl): { [s: string]: boolean } => {
    if (!control.value) {
      return { required: true };
    } else if (control.value !== this.validateForm.controls.password.value) {
      return { confirm: true, error: true };
    }
    return {};
  };

  getCaptcha(e: MouseEvent): void {
    e.preventDefault();
  }


  ngOnInit(): void {
    this.validateForm = this.fb.group({
      email: [null, [Validators.email, Validators.required]],
      password: [null, [Validators.required]],
      checkPassword: [null, [Validators.required, this.confirmationValidator]],
      nickname: [null, [Validators.required]],
      phoneNumberPrefix: ['+86'],
      phoneNumber: [null, [Validators.required]],
      website: [null, [Validators.required]],
      captcha: [null, [Validators.required]],
      agree: [false]
    });
  }

  constructor(
    private product : ProductService,
    private message: NzMessageService,
    private fb: FormBuilder,
    private http : HttpClient,
  ){

  }

  refresh(): void {
  }

  showUploadList = {
    showPreviewIcon: true,
    showRemoveIcon: true,
    hidePreviewIconInNonImage: true
  };
  fileList = [
  ];
  previewImage: string | undefined = '';
  previewVisible = false;

  handlePreview = (file: UploadFile) => {
    this.previewImage = file.url || file.thumbUrl;
    this.previewVisible = true;
  };

  imageUpload = (item: UploadXHRArgs) => {
    const url = `${AppConfig.BASE_URL}/upload-product-info`;
    const formData = new FormData();
    formData.append('upload_file', item.file as any);
    return this.http.post(url, formData, {
      reportProgress: true,
      withCredentials: false
    })
      .subscribe(
          // tslint:disable-next-line no-any
        (event: HttpEvent<any>) => {
          console.log(event);
          if (event.type === HttpEventType.UploadProgress) {
            if (event.total! > 0) {
              // tslint:disable-next-line:no-any
              (event as any).percent = (event.loaded / event.total!) * 100;
            }
            item.onProgress!(event, item.file!);
          } else if (event instanceof HttpResponse) {
            item.onSuccess!(event.body, item.file!, event);
          }
          item.onSuccess!(event, item.file!, event);
        },
        err => {
          item.onError!(err, item.file!);
        }
        );
  };
}
