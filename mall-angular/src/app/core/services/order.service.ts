import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {ResultVO, TableQueryParams, TableResultVO} from "../model/result-vm";
import {Observable} from "rxjs";
import {AppConfig} from "../../../environments/environment";
import {Order} from "../model/order";


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

  public refund(id: number): Observable<ResultVO<any>> {
    return this.http.get<ResultVO<any>>(`${AppConfig.BASE_URL}/api/order/finishRefund/${id}`, {
      withCredentials: true
    });
  }

}

