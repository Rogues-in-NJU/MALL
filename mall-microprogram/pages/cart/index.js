import http from '../../request';

var app = getApp();

Page({
  data: {
    productName: '',
    name: '',
    phone: '',
    identityCard:'',
    orderid:0,
    productid:0,
    imageAddresses: [],
    price: 0,
    submitBarText: "结算",
    totalPrice: 0,

    chooseDate: '',
    chooseDateToken: new Date().getTime(),

    showDatePicker: false,
    minDate: new Date().getTime(),
    formatter: (type, value) => {
      if (type === 'year') {
        return `${value}年`;
      } else if (type === 'month') {
        return `${value}月`;
      } else if (type === 'day') {
        return `${value}日`;
      }
      return value;
    }
  },

  listenerNameInput: function (e) {
    this.data.name = e.detail;
  },

  listenerPhoneInput: function (e) {
    this.data.phone = e.detail;
  },

  listeneridentityCardInput: function (e) {
    this.data.identityCard = e.detail;
  },
  listenerDateInput: function() {
    this.setData({
      showDatePicker: true
    });
  },

  changeDate: function(e){
    //获取当前选择日期
    console.log(e);
    var date = new Date(e.detail);
    this.setData({
      chooseDate: formatDate(date),
      showDatePicker: false
    });
  },

  cancelChangeDate: function(e) {
    this.setData({
      showDatePicker: false
    });
  },

  onLoad(options) {
    const formatNumber = n => {
      n = n.toString()
      return n[1] ? n : '0' + n
    }
    const year = new Date().getFullYear();
    const month = new Date().getMonth() + 1;
    const day = new Date().getDate();
    this.setData({
      today:[year, month, day].map(formatNumber).join('-'),
    });
    // console.log(this.data.today);

    var url = '/wechat/api/product/get?id=' + options.id;
    http.get(url)
      .then(res => {
        if (res === undefined || res === null) {
          wx.showToast({
            icon: 'none',
            title: '网络连接失败!',
            duration: 1500
          });
          this.setData({
            canRefresh: true
          });
          return;
        }
        if (res.code !== 10000) {
          wx.showToast({
            icon: 'none',
            title: res.message,
            duration: 1500
          });
          this.setData({
            canRefresh: true
          });
          return;
        }
        let thisgood = res.data;
        this.setData({
          productid: options.id,
          imageAddresses: thisgood.imageAddresses,
          productName: thisgood.name,
          price: (thisgood.price / 100).toFixed(2),
          totalPrice: thisgood.price,
        });
      })
      .catch(err => {
        wx.showToast({
          icon: 'none',
          title: '网络连接失败!',
          duration: 1500
        });
        this.setData({
          canRefresh: true
        });
      });
  },

  onSubmit() {
    if (this.data.name === undefined || this.data.name === null || this.data.name === '') {
      wx.showToast({
        title: "姓名不能为空",
        icon: 'none',
        duration: 1500
      });
      return;
    }
    //验证手机号码
    var pattern = /^[1][3-9]\d{9}$/
    if (!pattern.test(this.data.phone)) {
      wx.showToast({
        title: "手机号码格式有误",
        icon: 'none',
        duration: 1500
      });
      return;
    }
    //验证身份证号码
    var idpattern = /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$|^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X|x)$/
    if (!idpattern.test(this.data.identityCard)){
      wx.showToast({
        title: "身份证号码格式有误",
        icon: 'none',
        duration: 1500
      });
      return;
    }
    //验证出游时间
    var datepattern = /^(\d{4})(-|\/)(\d{2})\2(\d{2})$/
    if (!datepattern.test(this.data.chooseDate)){
      wx.showToast({
        title: "请选择正确时间",
        icon: 'none',
        duration: 1500
      });
      return;
    }
    //包装post数据
    var data = {
      // userId: 111,
      productId: this.data.productid,
      num: 1,
      consignee: this.data.name,
      consigneePhone: this.data.phone,
      consigneeAddress: this.data.identityCard,
      startTime: this.data.chooseDate,
    };
    //生成订单
    http.post('/wechat/api/order/generate', data)
      .then(res => {
        if (res === undefined || res === null) {
          wx.showToast({
            title: '网络连接失败!',
            icon: 'none',
            duration: 1500
          });
          return;
        }
        if (res.code !== 10000) {
          wx.showToast({
            title: res.message,
            icon: 'none',
            duration: 1500
          });
          return;
        }
        console.log(res.data);
        this.setData({
          orderid: res.data,
        });
        // console.log(this.data.orderid);
        //跳转至生成的未支付状态的订单
        wx.navigateTo({
          url: '/pages/order/index?id=' + this.data.orderid,
          success: () => { },
          error: () => {
            wx.showToast({
              icon: 'none',
              title: '生成订单失败!',
            });
          },
        });
      })
      .catch(err => {
        wx.hideLoading();
        wx.showToast({
          title: '网络连接失败!',
          icon: 'none',
          duration: 1500
        });
      });
    
  },
});

function formatDate(date, fmt) {
  if (!fmt) {
    fmt = 'yyyy-MM-dd';
  }
  let o = {
    "M+": date.getMonth() + 1,                 //月份
    "d+": date.getDate(),                    //日
    "H+": date.getHours(),                   //小时
    "m+": date.getMinutes(),                 //分
    "s+": date.getSeconds(),                 //秒
    "q+": Math.floor((date.getMonth() + 3) / 3), //季度
    "S": date.getMilliseconds()             //毫秒
  };
  if (/(y+)/.test(fmt)) {
    fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
  }
  for (const k in o) {
    if (new RegExp("(" + k + ")").test(fmt)) {
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    }
  }
  return fmt;
}