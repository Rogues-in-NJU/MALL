import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { ResultVO, TableQueryParams, TableResultVO } from "../model/result-vm";
import { Observable } from "rxjs";
import { ProductVO } from "../model/product";
import { AppConfig } from "../../../environments/environment";
import {Cover} from "../model/Cover";

@Injectable({
  providedIn: 'root'
})
export class CoverService {

  constructor(
    private http: HttpClient
  ) {
  }

  public findCover(): Observable<ResultVO<Cover>>{
    return this.http.get<ResultVO<Cover>>(`${AppConfig.BASE_URL}/api/cover/info`, {
      withCredentials: false
    });
  }

  public deleteCoverImage(coverImageId: number): Observable<ResultVO<any>>{
    return this.http.get<ResultVO<any>>(`${AppConfig.BASE_URL}/deleteCoverImage/${coverImageId}`);
  }

  public deleteCoverImageByImageLink(imageLink: string): Observable<ResultVO<any>>{
    return this.http.get<ResultVO<any>>(`${AppConfig.BASE_URL}/deleteCoverImageByImageLink/${imageLink}`);
  }

}
