import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { ResultVO, TableQueryParams, TableResultVO } from "../model/result-vm";
import { Observable } from "rxjs";
import { WithdrawlCondition } from "../model/withdrawlCondition";
import { WithdrawlRecord } from "../model/withdrawlRecord";
import { AppConfig } from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class WithdrawlService {

  constructor(
    private http: HttpClient
  ) {

  }



}
