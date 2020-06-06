/**
 * Created by Administrator on 2019/12/18.
 */
import {Component, OnInit} from "@angular/core";
import {ClosableTab, RefreshableTab} from "../../tab/tab.component";
import {ProductService} from "../../../../core/services/product.service";
import {HttpClient, HttpErrorResponse, HttpEvent, HttpEventType, HttpResponse} from "@angular/common/http";
import {NzMessageService, UploadFile, UploadXHRArgs} from "ng-zorro-antd";
import {BehaviorSubject, Observable} from "rxjs";
import {DateUtils, NumberUtils, Objects} from "../../../../core/services/util.service";
import {ResultCode, ResultVO, TableQueryParams, TableResultVO} from "../../../../core/model/result-vm";
import {environment} from "ng-zorro-antd/core/environments/environment";
import {AppConfig} from "../../../../../environments/environment";
import { NzFormModule } from 'ng-zorro-antd/form';
import {ProductVO} from "../../../../core/model/product";
import {TabService} from "../../../../core/services/tab.service";
import {ActivatedRoute, Router} from "@angular/router";
import {debounceTime, map, switchMap} from "rxjs/operators";
import {Classification} from "../../../../core/model/classification";
import {ClassificationService} from "../../../../core/services/classification.service";
@Component({
  selector: 'app-product-list',
  templateUrl: './product-add.component.html',
  styleUrls: ['./product-add.component.less']
})
export class ProductAddComponent implements RefreshableTab, OnInit, ClosableTab {

  productVO: ProductVO = {
    id: null,
    name: "",
    classificationId: null,
    classificationName: null,
    buyingPrice: null,
    price: null,
    percent: null,
    saleVolume: null,
    quantity: null,
    sellStartTime: null,
    sellEndTime: null,
  };

  id:string = "";
  name: string = "";
  classification: Classification = {};
  classificationId: number = -1;
  classificationName: string = "";
  buyingPrice: number = 0;
  price: number = 0;
  percent: number = 0;
  saleVolume: number = 0;
  quantity: number = 0;
  timeRange: Date[];
  status: number = 0;


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

  //select
  searchProducts: Classification[];
  searchChanges$: BehaviorSubject<string> = new BehaviorSubject('');
  isProductsLoading: boolean = false;


  constructor(
    private classificationService: ClassificationService,
    private product : ProductService,
    private message: NzMessageService,
    // private fb: FormBuilder,
    private http : HttpClient,
    private route: ActivatedRoute,
    private router: Router,
    private tab: TabService,
  ){

  }

  ngOnInit(): void {
    const getProducts: any = (name: string) => {
      const t: Observable<ResultVO<Array<Classification>>>
        = <Observable<ResultVO<Array<Classification>>>>this.classificationService
        .findAll();

      return t.pipe(map(res => {
        if (!Objects.valid(res)) {
          return [];
        }
        if (res.code !== ResultCode.SUCCESS.code) {
          return [];
        }
        return res.data;
      }));
    };

    const productOptionList$: Observable<Classification[]> = this.searchChanges$
      .asObservable()
      .pipe(debounceTime(500))
      .pipe(switchMap(getProducts));
    productOptionList$.subscribe((data: Classification[]) => {
      this.searchProducts = data;
      this.isProductsLoading = false;
    });
  }


  refresh(): void {
  }

  submitForm(): void {
    //todo
    this.productVO.name = this.name;
    this.productVO.classificationId = this.classificationId;
    this.productVO.buyingPrice = NumberUtils.toInteger(this.buyingPrice * 100);
    this.productVO.price = NumberUtils.toInteger(this.price * 100);
    this.productVO.percent = this.percent;
    this.productVO.saleVolume = this.saleVolume;
    this.productVO.quantity = this.quantity;
    if (Objects.valid(this.timeRange) && this.timeRange.length === 2) {
      this.productVO.sellStartTime = DateUtils.format(this.timeRange[0]);
      this.productVO.sellEndTime = DateUtils.format(this.timeRange[1]);
    }

    console.log(this.productVO);
    this.product.addProduct(this.productVO)
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
        // this.tabClose();
      }, (error: HttpErrorResponse) => {
        this.message.error('网络异常，请检查网络或者尝试重新登录!');
      }, () => {

      });

  }

  onProductSearch(value: string): void {
    this.isProductsLoading = true;
    this.searchChanges$.next(value);
  }

  onChangeSelectedProduct(event: Classification): void {
    this.classification = event;
    this.classificationId = event.id;
    this.classificationName = event.name;
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
    this.product.deleteProductImage(file.response['uid']).subscribe(
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
        this.fileList = this.fileList.filter(f => f.uid != file.uid);
        console.log(this.fileList);
      }, (error: HttpErrorResponse) => {
        this.message.error('网络异常，请检查网络或者尝试重新登录!');
      }, () => {

      });
  };

  imageDelete_info = (file: UploadFile) => {
    this.product.deleteProductInfoImage(file.response['uid']).subscribe(
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
        this.fileList_info = this.fileList_info.filter(f => f.uid != file.uid);
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
          item.file.uid = event['uid'];
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

  tabClose(): void {
    this.tab.closeEvent.emit({
      url: this.router.url,
      goToUrl: '/workspace/product/list',
      refreshUrl: '/workspace/product/list',
      routeConfig: this.route.snapshot.routeConfig
    });
  }
}
