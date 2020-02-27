import {BaseInfo} from "./base-info";

export interface Order extends BaseInfo {

  id?: number;
  userId?: string;
  transactionNumber?: string;
  createdAt?: string;
  payTime?: string;
  refundTime?: string;
  consignee?: string;
  consigneePhone?: string;
  consigneeAddress?: string;
  price?: number;
  status?: string;
}
