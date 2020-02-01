# 数据库表设计（讨论补充）

### product（商品）
| 名称        | 字段名   |  数据类型  |
| --------   | -----:  | :----:  |
| 商品Id     | id |   int     |
| 名称        |   name   |   String   |
| 所属分类        |    classificationId    |  int  |
| 所属分类名称        |    classificationName    |  String  |
| 进货价        |   buyingPrice   |   double   |
| 售价        |    price    |  double  |
| 分销抽成比率        |    percent    |  double  |
| 销售时间        |    sellTime    |  String  |
| 库存数量        |    quantity    |  int  |
| 开始销售时间（可选，用于秒杀等）        |    sellStartTime    |  String  |
| 结束销售时间（可选，用于秒杀等）        |    sellEndTime    |  String  |
| 状态(0：使用中，1：废弃)      |    status    |  int  |
| 更新时间        |    updateTime    |  String  |
| 添加时间        |    addTime    |  String  |
| 废弃时间        |    dropTime    |  String  |

销售时间有疑问，这个字段是指什么？

### productImage（商品图片关联）
| 名称        | 字段名   |  数据类型  |
| --------   | -----:  | :----:  |
| Id     | id |   int     |
| 商品Id     | productId |   int     |
| 图片链接        |   pictureLink   |   String   |

### productInfo（商品与详情图片关联）
| 名称        | 字段名   |  数据类型  |
| --------   | -----:  | :----:  |
| Id     | id |   int     |
| 商品Id     | productId |   int     |
| 图片链接        |   detailLink   |   String   |

### classification（分类）
| 名称        | 字段名   |  数据类型  |
| --------   | -----:  | :----:  |
| 分类Id     | id |   int     |
| 分类名称        |   name   |   String   |
| 添加时间     | addTime |   String     |
| 废弃时间     | dropTime |   String     |
| 状态(0：使用中，1：废弃)        |   status   |   int   |


### withdrawalCondition（提现条件）
| 名称        | 字段名   |  数据类型  |
| --------   | -----:  | :----:  |
| 下级人数要求     | member |   int     |
| 额度要求        |   cash   |   double   |



### withdrawalRecord（提现条件）
| 名称        | 字段名   |  数据类型  |
| --------   | -----:  | :----:  |
| 用户Id（微信号）     | userId |   String     |
| 提现时间        |   withdrawalTime   |   String   |
| 提现金额     | cash |   double     |
| 状态（0：待处理，1：已完成）        |   status   |   int   |

### user（用户）
| 名称        | 字段名   |  数据类型  |
| --------   | -----:  | :----:  |
| 用户Id（微信号）     | userId |   String     |
| 手机号        |   phoneNumber   |   String   |
| 上级用户Id（微信号）     | parentId |   String     |
| 待提现额度        |   cash   |   double   |
| 权限（0：普通用户，1：管理员）        |   auth   |   int   |

### order（订单）
| 名称        | 字段名   |  数据类型  |
| --------   | -----:  | :----:  |
| 订单Id     | id |   int     |
| 购买人Id        |   userId   |   String   |
| 下单时间     | orderTime |   String     |
| 退款时间     | refundTime |   String     |
| 收货人(姓名)       |   consignee   |   String   |
| 收货人手机       |   consigneePhone   |   String   |
| 收货人地址       |   consigneeAddress   |   String   |
| 总金额        |   price   |   double   |
| 状态（待支付，待发货，已发货，已签收，申请退款中，退款成功）        |   status   |   int   |

### orderProduct（订单商品关联）
| 名称        | 字段名   |  数据类型  |
| --------   | -----:  | :----:  |
| id     | id |   int     |
| 商品id        |   productId   |   int   |
| 商品名称        |   productName   |   String   |
| 购买数量     | productNum |   int     |
| 订单Id       |   orderId   |   int   |

关于商品和订单商品关联的两张表，有一定的数据冗余，是否需要？ 




