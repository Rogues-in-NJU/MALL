import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {WorkspaceComponent} from "./pages/workspace/workspace.component";


const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'workspace' },
  // {
  //   path: 'passport',
  //   component: PassportComponent,
  //   children: [
  //     { path: '', pathMatch: 'full', redirectTo: 'login' },
  //     { path: 'login', component: LoginComponent }
  //   ]
  // },
  {
    path: 'workspace',
    component: WorkspaceComponent,
    loadChildren: './pages/workspace/workspace.module#WorkspaceModule'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
