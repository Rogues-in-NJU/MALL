import { Component, OnInit } from "@angular/core";
import { UserService } from "../../../core/services/user.service";
import { NzMessageService } from "ng-zorro-antd";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { ResultCode, ResultVO } from "../../../core/model/result-vm";
import { HttpErrorResponse } from "@angular/common/http";
import { Objects } from "../../../core/services/util.service";
import { LocalStorageService } from "../../../core/services/local-storage.service";
import { TabService } from "../../../core/services/tab.service";

@Component({
  selector: 'passport-login',
  templateUrl: './login.component.html',
  styleUrls: [ './login.component.less' ]
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  isLoading: boolean = false;

  constructor(
    private user: UserService,
    private message: NzMessageService,
    private fb: FormBuilder,
    private router: Router,
    private storage: LocalStorageService,
    private tab: TabService
  ) {
  }

  forgetPassword(): void {
    this.message.info('请咨询管理员重置密码！');
  }

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      phoneNumber: [ null, [ Validators.required, Validators.pattern(/^\d{11}$/) ] ],
      password: [ null, Validators.required ],
      city: [ 1, Validators.required ]
    });
  }

  login(): void {
    if (!this.loginForm.valid) {
      Object.values(this.loginForm.controls).forEach(item => {
        item.markAsDirty();
        item.updateValueAndValidity();
      });
      return;
    }
    this.isLoading = true;
    this.user.login(this.loginForm.getRawValue())
      .subscribe((res: ResultVO<Boolean>) => {
        if (!Objects.valid(res)) {
          this.message.error('登录失败!');
          return;
        }
        if (res.code !== ResultCode.SUCCESS.code) {
          this.message.error(res.message);
          return;
        }
        const ans: Boolean = res.data;
        if (ans === true) {
          this.storage.setObject('user', {
          });
          this.router.navigate(['/workspace']);
        } else {
          this.message.error("手机号或密码错误!");
        }
      }, (error: HttpErrorResponse) => {
        this.message.error('网络异常，请检查网络或者尝试重新登录!');
        this.isLoading = false;
      }, () => {
        this.isLoading = false;
      });
  }

}
