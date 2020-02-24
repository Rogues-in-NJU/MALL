import {Component, OnInit} from "@angular/core";
import {RefreshableTab} from "../../tab/tab.component";
import {ClassificationService} from "../../../../core/services/classification.service";
import {NzMessageService} from "ng-zorro-antd";
import {Objects} from "../../../../core/services/util.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ClassificationVO} from "../../../../core/model/classification";
import {ResultCode, ResultVO} from "../../../../core/model/result-vm";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-classification-list',
  templateUrl: './classification-list.component.html',
  styleUrls: ['./classification-list.component.less']
})
export class ClassificationListComponent implements RefreshableTab, OnInit {
  classificationEditVisible: boolean = false;
  classificationEditForm: FormGroup;
  classificationList: ClassificationVO[] = [];

  constructor(
    private classificationService: ClassificationService,
    private fb: FormBuilder,
    private message: NzMessageService,
    // private fb: FormBuilder,
  ) {

  }

  refresh(): void {
    this.classificationEditForm.reset({
      id: null,
      name: null
    });
    this.classificationEditVisible = false;
    this.loadData();
  }

  ngOnInit(): void {
    this.classificationEditForm = this.fb.group({
      id: [null, Validators.required],
      name: [null, Validators.required]
    });
    this.loadData();
  }

  loadData():void{
    this.classificationService.findAll()
      .subscribe((res: ResultVO<Array<ClassificationVO>>) => {
        if (!Objects.valid(res)) {
          this.message.error("请求失败！");
          return;
        }
        if (res.code !== ResultCode.SUCCESS.code) {
          this.message.error(res.message);
          return;
        }
        this.classificationList = res.data;
      }, (error: HttpErrorResponse) => {
        this.message.error('网络异常，请检查网络或者尝试重新登录!');
      });
  }
  showEditModal(id: number): void {
    const index: number = this.classificationList.findIndex(item => item.id === id);
    if (index === -1) {
      return;
    }
    this.classificationEditVisible = true;
    this.classificationEditForm.reset(this.classificationList[index]);
  }

  confirmEdit(): void {
    const classificationEditData: ClassificationVO = this.classificationEditForm.getRawValue();
    this.classificationService.save(classificationEditData)
      .subscribe((res: ResultVO<any>) => {
        if (!Objects.valid(res)) {
          this.message.error('修改失败!');
          return;
        }
        if (res.code !== ResultCode.SUCCESS.code) {
          this.message.error(res.message);
          return;
        }
        this.message.success('修改成功!');
      }, (error: HttpErrorResponse) => {
        this.message.error('网络异常，请检查网络或者尝试重新登录!');
        this.refresh();
      }, () => {
        this.refresh();
      });
  }


  cancelEdit(): void {
    this.classificationEditForm.reset({
      id: null,
      name: null
    });
    this.classificationEditVisible = false;
  }
}
