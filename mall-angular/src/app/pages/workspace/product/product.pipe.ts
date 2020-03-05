import {Pipe, PipeTransform} from "@angular/core";

@Pipe({
  name: 'product_status'
})
export class ProductStatusPipe implements PipeTransform {
  transform(value: number, ...args: any[]): any {
    switch (value) {
      case 1: return '正常';
      case 2: return '已删除';
      default: return '已删除';
    }
  }

}

@Pipe({
  name: 'product_status_color'
})
export class ProductStatusColorPipe implements PipeTransform {

  transform(value: number, ...args: any[]): any {
    switch (value) {
      case 1: return 'blue';
      case 2: return 'red';
      default: return 'red';
    }
  }

}
