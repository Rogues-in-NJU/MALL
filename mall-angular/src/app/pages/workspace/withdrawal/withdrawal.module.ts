import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {CoreModule} from "../../../core/core.module";
import {SharedModule} from "../../../shared/shared.module";
import {AuthorizationGuard} from "../../../guards/authorization.guard";
import {ReactiveFormsModule} from "@angular/forms";
import {WithdrawalRecordComponent} from "./withdrawal-record/withdrawal-record.component";
import {WithdrawalConditionComponent} from "./withdrawal-condition/withdrawal-condition.component";

const routes: Routes = [
  // { path: '', pathMatch: 'full', redirectTo: 'list' },
  {
    path: 'todoRecord',
    pathMatch: 'full',
    component: WithdrawalRecordComponent,
    canActivate: [AuthorizationGuard],
    data: {
      title: '提现列表',
      removable: true
    }
  },
  {
    path: 'condition',
    pathMatch: 'full',
    component: WithdrawalConditionComponent,
    canActivate: [AuthorizationGuard],
    data: {
      title: '提现条件设置',
      removable: true
    }
  }
];

@NgModule({
  imports: [CoreModule, SharedModule, RouterModule.forChild(routes), ReactiveFormsModule],
  declarations: [WithdrawalRecordComponent, WithdrawalConditionComponent],
  exports: [RouterModule]
})
export class WithdrawalModule {

}
