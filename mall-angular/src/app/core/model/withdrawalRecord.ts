import {BaseInfo} from "./base-info";

export interface WithdrawalRecord extends BaseInfo {


  id?: number;
  userId?: string;
  withdrawalTime?: string;
  cash?: number;
  status?: number;


}
