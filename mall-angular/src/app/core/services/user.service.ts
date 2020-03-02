import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {ResultVO} from "../model/result-vm";
import {Observable} from "rxjs";
import {AppConfig} from "../../../environments/environment";
import {OrderSummary} from "../model/orderSummary";
import {PassportVO} from "../model/user";


@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(
    private http: HttpClient
  ) {

  }

  public login(loginVO: PassportVO): Observable<ResultVO<Boolean>> {
    return this.http.post<ResultVO<Boolean>>(`${AppConfig.BASE_URL}/api/admin/login`, loginVO, {
      withCredentials: true
    });
  }

}

