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
import {DateUtils, Objects} from "../../../../core/services/util.service";
import { FormGroup } from "@angular/forms";
import {ResultCode, ResultVO, TableQueryParams, TableResultVO} from "../../../../core/model/result-vm";
import {ActivatedRoute, Router} from "@angular/router";
import {TabService} from "../../../../core/services/tab.service";
import {AppConfig} from "../../../../../environments/environment";

@Component({
  selector: 'app-product-list',
  templateUrl: './product-info.component.html',
  styleUrls: ['./product-info.component.less']
})
export class ProductInfoComponent implements RefreshableTab, OnInit {

  productVO: ProductVO ={};
  id:string = "";
  productId: number;
  isLoading: boolean = true;


  //image
  showUploadList = {
    showPreviewIcon: true,
    showRemoveIcon: true,
    hidePreviewIconInNonImage: true
  };
  fileList = [
  ];
  previewImage: string | undefined = '';
  previewVisible = false;

  //info
  showUploadList_info = {
    showPreviewIcon: true,
    showRemoveIcon: true,
    hidePreviewIconInNonImage: true
  };
  fileList_info = [
  ];
  previewImage_info: string | undefined = '';
  previewVisible_info = false;


  ngOnInit(): void {
    this.productId = this.route.snapshot.params['productId'];

    this.refresh();
  }

  constructor(
    private product : ProductService,
    private message: NzMessageService,
    // private fb: FormBuilder,
    private http : HttpClient,
    private route: ActivatedRoute,
    private router: Router,
    private tab: TabService,
  ){

  }

  refresh(): void {
    this.search();
  }

  search(): void{
    this.isLoading = true;
    console.log(this.productVO);
    this.product.findOne(this.productId)
      .subscribe((res: ResultVO<ProductVO>) => {
        console.log(res);
        if (!Objects.valid(res)) {
          this.message.error("请求失败！");
          return;
        }
        if (res.code !== ResultCode.SUCCESS.code) {
          this.message.error(res.message);
          return;
        }
        this.productVO = res.data;
        this.id = String(this.productVO.id);
        for(let link of this.productVO.imageAddresses){
          this.fileList.push({'url': AppConfig.BASE_URL + link,
            "response" : {
              "url" : link
            }});
        }
        for(let link of this.productVO.imageInfoAddresses){
          this.fileList_info.push({'url': AppConfig.BASE_URL + link,
            "response" : {
              "url" : link
            }});
        }

        console.log(this.fileList);
      }, (error: HttpErrorResponse) => {
        this.message.error('网络异常，请检查网络或者尝试重新登录!');
      }, () => {
        this.isLoading = false;
      });
  }

  update():void {
    this.product.updateProduct(this.productVO).subscribe((res: ResultVO<any>) => {
      console.log(res);
      if (!Objects.valid(res)) {
        this.message.error("请求失败！");
        return;
      }
      if (res.code !== ResultCode.SUCCESS.code) {
        this.message.error(res.message);
        return;
      }
      this.refresh();
    }, (error: HttpErrorResponse) => {
      this.message.error('网络异常，请检查网络或者尝试重新登录!');
    }, () => {
    });
  }

  handlePreview_info = (file: UploadFile) => {
    this.previewImage_info = file.url || file.thumbUrl;
    this.previewVisible_info = true;
  };

  handlePreview = (file: UploadFile) => {
    this.previewImage = file.url || file.thumbUrl;
    this.previewVisible = true;
  };

  imageDelete = (file: UploadFile) => {
    this.product.deleteProductImageByImageLink(file['response']['url']).subscribe(
      (res: ResultVO<any>) => {
        console.log(res);
        if (!Objects.valid(res)) {
          this.message.error("请求失败！");
          return;
        }
        if (res.code !== ResultCode.SUCCESS.code) {
          this.message.error(res.message);
          return;
        }
        this.fileList = this.fileList.filter(f => (f['url'] != file['url'])
                          || f['response']['url']!= file['response']['url']);
        console.log(this.fileList);
      }, (error: HttpErrorResponse) => {
        this.message.error('网络异常，请检查网络或者尝试重新登录!');
      }, () => {

      });
  };

  imageDelete_info = (file: UploadFile) => {
    this.product.deleteProductInfoImageByImageLink(file['response']['url']).subscribe(
      (res: ResultVO<any>) => {
        console.log(res);
        if (!Objects.valid(res)) {
          this.message.error("请求失败！");
          return;
        }
        if (res.code !== ResultCode.SUCCESS.code) {
          this.message.error(res.message);
          return;
        }
        this.fileList_info = this.fileList_info.filter(f => (f['url'] != file['url'])
          || f['response']['url']!= file['response']['url']);
      }, (error: HttpErrorResponse) => {
        this.message.error('网络异常，请检查网络或者尝试重新登录!');
      }, () => {

      });
  };

  imageUpload = (item: UploadXHRArgs) => {
    if(Objects.isNaN(this.id)){
      this.message.warning("请先上传产品信息!");
      return ;
    }
    const url = `${AppConfig.BASE_URL}/upload-product-image`;
    const formData = new FormData();
    formData.append('upload_file', item.file as any);
    formData.append('product_id', this.id);
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
          // item['url'] = event['link'];
          console.log(this.fileList)
        },
        err => {
          item.onError!(err, item.file!);
          this.message.error("图片上传失败！")
        }
      );
  };

  infoImageUpload = (item: UploadXHRArgs) => {
    if(Objects.isNaN(this.id)){
      this.message.warning("请先上传产品信息!");
      return ;
    }
    const url = `${AppConfig.BASE_URL}/upload-product-info`;
    const formData = new FormData();
    formData.append('upload_file', item.file as any);
    formData.append('product_id', this.id);
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
          this.message.error("图片上传失败！")
        }
      );
  };


}
