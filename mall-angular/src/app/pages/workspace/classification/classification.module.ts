import { RouterModule, Routes } from "@angular/router";
import { NgModule } from "@angular/core";
import { CoreModule } from "../../../core/core.module";
import { SharedModule } from "../../../shared/shared.module";
import { AuthorizationGuard } from "../../../guards/authorization.guard";
import { ClassificationListComponent } from "./classification-list/classification-list.component";
import { ReactiveFormsModule } from "@angular/forms";

const routes: Routes = [
  // { path: '', pathMatch: 'full', redirectTo: 'list' },
  {
    path: 'list',
    pathMatch: 'full',
    component: ClassificationListComponent,
    canActivate: [ AuthorizationGuard ],
    data: {
      title: '分类列表',
      removable: true
    }
  }
];

@NgModule({
  imports: [ CoreModule, SharedModule, RouterModule.forChild(routes), ReactiveFormsModule ],
  declarations: [ ClassificationListComponent ],
  exports: [ RouterModule ]
})
export class ClassificationModule {

}
