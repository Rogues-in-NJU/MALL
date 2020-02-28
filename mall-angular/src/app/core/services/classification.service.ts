import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {ResultVO, TableQueryParams, TableResultVO} from "../model/result-vm";
import {Observable} from "rxjs";
import {Classification} from "../model/classification";
import {AppConfig} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class ClassificationService {

  constructor(
    private http: HttpClient
  ) {

  }

  public findAll(): Observable<ResultVO<Array<Classification>>> {
    return this.http.get<ResultVO<Array<Classification>>>(`${AppConfig.BASE_URL}/api/classification/list`, {
      withCredentials: true
    });
  };

  public save(queryParams: Classification): Observable<ResultVO<any>> {
    return this.http.post<ResultVO<any>>(`${AppConfig.BASE_URL}/api/classification`, queryParams, {
      withCredentials: false
    });
  }


}


