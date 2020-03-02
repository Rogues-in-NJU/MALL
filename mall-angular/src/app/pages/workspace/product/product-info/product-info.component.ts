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

  productVO: ProductVO = {
    id: null,
    name: "",
    classificationId: null,
    classificationName: null,
    buyingPrice: null,
    price: null,
    percent: null,
    quantity: null,
    sellStartTime: null,
    sellEndTime: null,
  };

  id:string = "";
  name: string = "";
  classificationId: number = -1;
  buyingPrice: number = 0;
  price: number = 0;
  percent: number = 0;
  quantity: number = 0;
  timeRange: Date[];
  status: number = 0;

  ngOnInit(): void {
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
  }

  submitForm(): void {
    //todo
    this.productVO.name = this.name;
    this.productVO.classificationId = this.classificationId;
    this.productVO.buyingPrice = this.buyingPrice;
    this.productVO.price = this.price;
    this.productVO.percent = this.percent;
    this.productVO.quantity = this.quantity;
    if (Objects.valid(this.timeRange) && this.timeRange.length === 2) {
      this.productVO.sellStartTime = DateUtils.format(this.timeRange[0]);
      this.productVO.sellEndTime = DateUtils.format(this.timeRange[1]);
    }

    console.log(this.productVO);
    this.product.updateOrAddProduct(this.productVO)
      .subscribe((res: ResultVO<any>) => {
        console.log(res);
        if (!Objects.valid(res)) {
          this.message.error("请求失败！");
          return;
        }
        if (res.code !== ResultCode.SUCCESS.code) {
          this.message.error(res.message);
          return;
        }
        const id: number = res.data;
        this.productVO.id = id;
        this.id = String(id);
        this.message.success('新增成功,请上传相关图片！');
        this.tabClose();
      }, (error: HttpErrorResponse) => {
        this.message.error('网络异常，请检查网络或者尝试重新登录!');
      }, () => {

      });

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
    if(!Objects.isNaN(this.id)){
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
        }
      );
  };

  tabClose(): void {
    this.tab.closeEvent.emit({
      url: this.router.url,
      goToUrl: '/workspace/product/list',
      refreshUrl: '/workspace/product/list',
      routeConfig: this.route.snapshot.routeConfig
    });
  }
}
