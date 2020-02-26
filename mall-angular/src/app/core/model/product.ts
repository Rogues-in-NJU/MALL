import {BaseInfo} from "./base-info";

export interface ProductVO extends BaseInfo {

  id?: number;
  name?: string;
  classificationId?: number;
  classificationName?: string;
  buyingPrice?: number;
  price?: number;
  percent?: number;
  quantity?: number;
  sellStartTime?: string;
  sellEndTime?: string;
  status?: number;
  updatedAt?: string;
  deletedAt?: string;
  createdAt?: string;
}
