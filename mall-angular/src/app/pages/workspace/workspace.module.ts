import { NgModule } from "@angular/core";
import { CoreModule } from "../../core/core.module";
import { SharedModule } from "../../shared/shared.module";
import { WorkspaceComponent } from "./workspace.component";
import { RouteReuseStrategy, RouterModule, Routes } from "@angular/router";
import { SimpleReuseStrategy } from "../../core/strategy/simple-reuse.strategy";
import { TabComponent } from "./tab/tab.component";
// import { CookieModule } from "ngx-cookie";

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'product' },
  {
    path: 'product',
    loadChildren: './product/product.module#ProductModule'
  },
  {
    path: 'classification',
    loadChildren: './classification/classification.module#ClassificationModule'
  },
  {
    path: 'withdrawal',
    loadChildren: './withdrawal/withdrawal.module#WithdrawalModule'
  },
  {
    path: 'order',
    loadChildren: './order/order.module#OrderModule'
  },
  {
    path: 'cover',
    loadChildren: './cover/cover.module#CoverModule'
  }
];

@NgModule({
  providers: [
    { provide: RouteReuseStrategy, useClass: SimpleReuseStrategy }
  ],
  // imports: [ CoreModule, SharedModule, RouterModule.forChild(routes), CookieModule.forRoot() ],
  imports: [ CoreModule, SharedModule, RouterModule.forChild(routes)],
  declarations: [ WorkspaceComponent, TabComponent],
  exports: [ WorkspaceComponent, RouterModule ]
})
export class WorkspaceModule {
}
