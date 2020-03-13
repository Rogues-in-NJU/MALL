import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {ResultVO, TableQueryParams, TableResultVO} from "../model/result-vm";
import {Observable} from "rxjs";
import {AppConfig} from "../../../environments/environment";
import {Order} from "../model/order";
import {OrderSummary} from "../model/orderSummary";
import {ProductVO} from "../model/product";


@Injectable({
  providedIn: 'root'
})
export class OrderService {

  constructor(
    private http: HttpClient
  ) {

  }

  public findRefundOrderList(queryParams: TableQueryParams): Observable<ResultVO<TableResultVO<Order>>> {
    return this.http.get<ResultVO<TableResultVO<Order>>>(`${AppConfig.BASE_URL}/api/order/refundOrderList`, {
      params: queryParams,
      withCredentials: true
    });
  };

  public findOrderList(queryParams: TableQueryParams): Observable<ResultVO<TableResultVO<Order>>>{
    return this.http.get<ResultVO<TableResultVO<Order>>>(`${AppConfig.BASE_URL}/api/order/orderList`, {
      params: queryParams,
      withCredentials: false
    });
  };

  public refund(id: number): Observable<ResultVO<any>> {
    return this.http.get<ResultVO<any>>(`${AppConfig.BASE_URL}/api/order/finishRefund/${id}`, {
      withCredentials: true
    });
  }

  public summary(): Observable<ResultVO<OrderSummary>> {
    return this.http.get<ResultVO<any>>(`${AppConfig.BASE_URL}/api/order/summaryInfo`, {
      withCredentials: true
    });
  }

}

