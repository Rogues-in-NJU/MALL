import { RouterModule, Routes } from "@angular/router";
import { NgModule } from "@angular/core";
import {AuthorizationGuard} from "../../../guards/authorization.guard";
import {SharedModule} from "../../../shared/shared.module";
import {OrderListComponent} from "./order-list/order-list.component";
import {OrderSummaryComponent} from "./order-summary/order-summary.component";

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'list' },
  { path: 'list',
    pathMatch: 'full',
    component: OrderListComponent,
    canActivate: [ AuthorizationGuard ],
    data: {
      title: '订单列表',
      removable: true
    }
  },
  { path: 'summary',
    pathMatch: 'full',
    component: OrderSummaryComponent,
    canActivate: [ AuthorizationGuard ],
    data: {
      title: '订单统计信息',
      removable: true
    }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes), SharedModule],
  declarations: [OrderListComponent,OrderSummaryComponent
    ],
  exports: [ RouterModule ]
})
export class OrderModule {

}
