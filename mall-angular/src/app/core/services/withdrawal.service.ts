import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { ResultVO, TableQueryParams, TableResultVO } from "../model/result-vm";
import { Observable } from "rxjs";
import { WithdrawalRecord } from "../model/withdrawalRecord";
import { AppConfig } from "../../../environments/environment";
import {ClassificationVO} from "../model/classification";

@Injectable({
  providedIn: 'root'
})
export class WithdrawalService {

  constructor(
    private http: HttpClient
  ) {

  }

  public findTodoRecordList(queryParams: TableQueryParams): Observable<ResultVO<TableResultVO<WithdrawalRecord>>> {
    return this.http.get<ResultVO<TableResultVO<ClassificationVO>>>(`${AppConfig.BASE_URL}/api/withDrawl/todoRecordList`, {
      params: queryParams,
      withCredentials: true
    });
  };

  public findDoneRecordList(queryParams: TableQueryParams): Observable<ResultVO<TableResultVO<WithdrawalRecord>>> {
    return this.http.get<ResultVO<TableResultVO<ClassificationVO>>>(`${AppConfig.BASE_URL}/api/withDrawl/doneRecordList`, {
      params: queryParams,
      withCredentials: true
    });
  };


}
