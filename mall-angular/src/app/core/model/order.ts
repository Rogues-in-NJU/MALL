import {BaseInfo} from "./base-info";

export interface Order extends BaseInfo {

  id?: number;
  userId?: string;
  orderTime?: string;
  refundTime?: string;
  consignee?: string;
  consigneePhone?: string;
  consigneeAddress?: string;
  price?: number;
  status?: string;
}
