import {BaseInfo} from "./base-info";

export interface ClassificationVO extends BaseInfo {

  id?: number;
  name?: string;
  addTime?: string;
  dropTime?: number;
  status?: number;
}
