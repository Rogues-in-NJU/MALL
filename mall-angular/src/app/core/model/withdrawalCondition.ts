import {BaseInfo} from "./base-info";

export interface WithdrawalCondition extends BaseInfo {

  id?: number;
  member?: number;
  cash?: number;


}
