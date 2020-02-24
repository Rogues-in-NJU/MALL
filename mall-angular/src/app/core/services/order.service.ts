import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {ResultVO, TableQueryParams, TableResultVO} from "../model/result-vm";
import {Observable} from "rxjs";
import {WithdrawalRecord} from "../model/withdrawalRecord";
import {ClassificationVO} from "../model/classification";
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

}

