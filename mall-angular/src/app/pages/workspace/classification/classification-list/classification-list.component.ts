import {Component, OnInit} from "@angular/core";
import {RefreshableTab} from "../../tab/tab.component";
import {ClassificationService} from "../../../../core/services/classification.service";
import {NzMessageService} from "ng-zorro-antd";
import {BehaviorSubject, Observable} from "rxjs";
import {Objects} from "../../../../core/services/util.service";
import {FormGroup} from "@angular/forms";
import {ClassificationVO} from "../../../../core/model/classification";
import {ResultCode, ResultVO} from "../../../../core/model/result-vm";
import {HttpErrorResponse} from "@angular/common/http";
import {RefreshTabEvent} from "../../../../core/services/tab.service";

@Component({
  selector: 'app-classification-list',
  templateUrl: './classification-list.component.html',
  styleUrls: ['./classification-list.component.less']
})
export class ClassificationListComponent implements RefreshableTab, OnInit {
  classificationAddVisible: boolean = false;
  classificationAddForm: FormGroup;

  constructor(
    private classification: ClassificationService,
    private message: NzMessageService,
    // private fb: FormBuilder,
  ) {

  }

  refresh(): void {
  }

  ngOnInit(): void {
    this.tab.refreshEvent.subscribe((event: RefreshTabEvent) => {
      if (Objects.valid(event) && event.url === '/workspace/user-management/list') {
        this.refresh();
      }
    });
    this.refresh();
  }


  confirmAdd(): void {
    if (!this.classificationAddForm.valid) {
      // 主动触发验证
      Object.values(this.classificationAddForm.controls).forEach(item => {
        item.markAsDirty();
        item.updateValueAndValidity();
      });
      return;
    }
    const classificaitonAddData: ClassificationVO = this.classificationAddForm.getRawValue();

    this.classification.save(classificaitonAddData)
      .subscribe((res: ResultVO<any>) => {
        if (!Objects.valid(res)) {
          return;
        }
        if (res.code !== ResultCode.SUCCESS.code) {
          this.message.error(res.message);
          return;
        }
        this.message.success('添加成功!');
      }, (error: HttpErrorResponse) => {
        this.message.error('网络异常，请检查网络或者尝试重新登录!');
        this.refresh();
      }, () => {
        this.refresh();
      });
    this.classificationAddVisible = false;
    this.refresh();
  }

  cancelAdd():void{
    this.classificationAddVisible = false;
    this.refresh();
  }

  showAddModal(): void {
    this.classificationAddVisible = true;
    console.log(this.classificationAddVisible);
    // this.classificationAddForm.reset({
    //   name: null
    // });
  }



}
