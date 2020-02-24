import { RouterModule, Routes } from "@angular/router";
import { NgModule } from "@angular/core";
import {AuthorizationGuard} from "../../../guards/authorization.guard";
import {SharedModule} from "../../../shared/shared.module";
import {OrderListComponent} from "./order-list/order-list.component";

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
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes), SharedModule],
  declarations: [OrderListComponent,
    ],
  exports: [ RouterModule ]
})
export class OrderModule {

}
