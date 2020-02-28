import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { ResultVO, TableQueryParams, TableResultVO } from "../model/result-vm";
import { Observable } from "rxjs";
import { ProductVO } from "../model/product";
import { AppConfig } from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor(
    private http: HttpClient
  ) {

  }

  public findAll(queryParams: TableQueryParams): Observable<ResultVO<TableResultVO<ProductVO>>>{
    return this.http.get<ResultVO<TableResultVO<ProductVO>>>(`${AppConfig.BASE_URL}/api/product/list`, {
      params: queryParams,
      withCredentials: true
    });
  };

  public findOne(productId: number): Observable<ResultVO<ProductVO>>{
    return this.http.get<ResultVO<ProductVO>>(`${AppConfig.BASE_URL}/api/product/info/${productId}`, {
      withCredentials: true
    });
  };

  public updateOrAddProduct(queryParams: ProductVO): Observable<ResultVO<any>>{
    console.log(queryParams);
    return this.http.post<ResultVO<any>>(`${AppConfig.BASE_URL}/api/product/save`, queryParams, {
      withCredentials: true
    });
  }

  public deleteProduct(productId: number): Observable<ResultVO<any>>{
    return this.http.get<ResultVO<any>>(`${AppConfig.BASE_URL}/api/product/delete/${productId}`);
  }

}
