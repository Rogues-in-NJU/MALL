import { RouterModule, Routes } from "@angular/router";
import { NgModule } from "@angular/core";
import { CoreModule } from "../../../core/core.module";
import { SharedModule } from "../../../shared/shared.module";
import { AuthorizationGuard } from "../../../guards/authorization.guard";
import { ReactiveFormsModule } from "@angular/forms";
import {CoverInfoComponent} from "./cover-info/cover-info.component";

const routes: Routes = [
  // { path: '', pathMatch: 'full', redirectTo: 'list' },
  {
    path: 'info',
    pathMatch: 'full',
    component: CoverInfoComponent,
    canActivate: [ AuthorizationGuard ],
    data: {
      title: '封面图片',
      removable: true
    }
  }
];

@NgModule({
  imports: [ CoreModule, SharedModule, RouterModule.forChild(routes), ReactiveFormsModule ],
  declarations: [ CoverInfoComponent ],
  exports: [ RouterModule ]
})
export class CoverModule {

}
