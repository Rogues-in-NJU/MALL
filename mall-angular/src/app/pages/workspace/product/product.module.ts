import { RouterModule, Routes } from "@angular/router";
import { NgModule } from "@angular/core";
import {ProductListComponent} from "./product-list/product-list.component";
import {AuthorizationGuard} from "../../../guards/authorization.guard";
import {SharedModule} from "../../../shared/shared.module";
import {ProductInfoComponent} from "./product-info/product-info.component";
import {ProductAddComponent} from "./product-add/product-add.component";
import {ProductStatusColorPipe, ProductStatusPipe} from "./product.pipe";

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'list' },
  { path: 'list',
    pathMatch: 'full',
    component: ProductListComponent,
    canActivate: [ AuthorizationGuard ],
    data: {
      title: '商品管理',
      removable: true
    }
  },
  { path: 'add',
    pathMatch: 'full',
    component: ProductAddComponent,
    canActivate: [ AuthorizationGuard ],
    data: {
      title: '商品添加',
      removable: true
    }
  },
  { path: 'info/:productId',
    component: ProductInfoComponent,
    canActivate: [ AuthorizationGuard ],
    data: {
      title: '商品详情{}',
      removable: true,
      replaceParams: ['productId'],
    }
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes), SharedModule],
  declarations: [ProductListComponent,
    ProductInfoComponent,
    ProductAddComponent,
    ProductStatusPipe,
    ProductStatusColorPipe,
    ],
  exports: [ RouterModule ]
})
export class ProductModule {

}
