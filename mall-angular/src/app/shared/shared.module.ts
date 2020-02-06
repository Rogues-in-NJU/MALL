import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PageNotFoundComponent } from './components/';
import { WebviewDirective } from './directives/';
import { FormsModule } from '@angular/forms';
import { NgZorroAntdModule, NzFormModule, NzMessageModule } from "ng-zorro-antd";
import { HttpClientModule } from "@angular/common/http";


const MODULES = [
  CommonModule,
  FormsModule,
  HttpClientModule
];

const THIRD_MODULES = [
  NgZorroAntdModule,
  NzMessageModule,
  NzFormModule,
];

@NgModule({
  declarations: [ PageNotFoundComponent, WebviewDirective ],
  imports: [
    ...MODULES,
    ...THIRD_MODULES
  ],
  exports: [
    ...MODULES,
    ...THIRD_MODULES,
    PageNotFoundComponent,
    WebviewDirective
  ]
})
export class SharedModule {
}
