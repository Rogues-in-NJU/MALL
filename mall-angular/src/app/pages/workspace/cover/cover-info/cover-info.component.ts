import {Component, OnInit} from "@angular/core";
import {RefreshableTab} from "../../tab/tab.component";
import {ClassificationService} from "../../../../core/services/classification.service";
import {NzMessageService, UploadFile, UploadXHRArgs} from "ng-zorro-antd";
import {Objects} from "../../../../core/services/util.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Classification} from "../../../../core/model/classification";
import {ResultCode, ResultVO} from "../../../../core/model/result-vm";
import {HttpClient, HttpErrorResponse, HttpEvent, HttpEventType, HttpResponse} from "@angular/common/http";
import {AppConfig} from "../../../../../environments/environment";
import {CoverService} from "../../../../core/services/cover.service";
import {ActivatedRoute} from "@angular/router";
import {ProductVO} from "../../../../core/model/product";
import {Cover} from "../../../../core/model/Cover";

@Component({
  selector: 'app-cover-info',
  templateUrl: './cover-info.component.html',
  styleUrls: ['./cover-info.component.less']
})
export class CoverInfoComponent implements RefreshableTab, OnInit {
  isLoading: boolean = true;
  coverVO : Cover = {};

  showUploadList = {
    showPreviewIcon: true,
    showRemoveIcon: true,
    hidePreviewIconInNonImage: true
  };
  fileList = [
  ];
  previewImage: string | undefined = '';
  previewVisible = false;

  constructor(
    private classificationService: ClassificationService,
    private fb: FormBuilder,
    private message: NzMessageService,
    private cover: CoverService,
    private http : HttpClient,
    private route: ActivatedRoute,
  ) {

  }

  refresh(): void {
    this.search();
  }

  ngOnInit(): void {
    this.refresh();
  }

  search(): void{
    this.isLoading = true;
    this.cover.findCover()
      .subscribe((res: ResultVO<Cover>) => {
        console.log(res);
        if (!Objects.valid(res)) {
          this.message.error("请求失败！");
          return;
        }
        if (res.code !== ResultCode.SUCCESS.code) {
          this.message.error(res.message);
          return;
        }
        this.coverVO = res.data;
        var tempFileList = [];
        for(let link of this.coverVO.imageAddresses){
          tempFileList.push({'url': AppConfig.BASE_URL + link,
            "response" : {
              "url" : link
            }});
        }
        this.fileList = tempFileList;
        console.log(this.fileList);
      }, (error: HttpErrorResponse) => {
        this.message.error('网络异常，请检查网络或者尝试重新登录!');
      }, () => {
        this.isLoading = false;
      });
  }

  handlePreview = (file: UploadFile) => {
    this.previewImage = file.url || file.thumbUrl;
    this.previewVisible = true;
  };

  imageDelete = (file: UploadFile) => {
    this.cover.deleteCoverImageByImageLink(file['response']['url']).subscribe(
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

  imageUpload = (item: UploadXHRArgs) => {

    const url = `${AppConfig.BASE_URL}/upload-cover-image`;
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
          // item['url'] = event['link'];
          console.log(this.fileList)
        },
        err => {
          item.onError!(err, item.file!);
          this.message.error("图片上传失败！")
        }
      );
  };

}
