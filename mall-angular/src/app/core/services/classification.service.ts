import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {ResultVO, TableQueryParams, TableResultVO} from "../model/result-vm";
import {Observable} from "rxjs";
import {ClassificationVO} from "../model/classification";
import {AppConfig} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class ClassificationService {

  constructor(
    private http: HttpClient
  ) {

  }

  public findAll(): Observable<ResultVO<Array<ClassificationVO>>> {
    return this.http.get<ResultVO<Array<ClassificationVO>>>(`${AppConfig.BASE_URL}/api/classification/list`, {
      withCredentials: true
    });
  };

  public save(queryParams: ClassificationVO): Observable<ResultVO<any>> {
    console.log(queryParams);
    return this.http.post<ResultVO<any>>(`${AppConfig.BASE_URL}/api/classification`, queryParams, {
      withCredentials: true
    });
  }


}


