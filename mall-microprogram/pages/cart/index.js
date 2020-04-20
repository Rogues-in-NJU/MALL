import http from '../../request';

var app = getApp();

Page({
  data: {
    br:'\n',
    name: '',
    phone: '',
    identityCard:'',
    orderid:0,
    productid:0,
    imageAddresses: [],
    name: "",
    price: 0,
    submitBarText: "结算",
    totalPrice: 0,

    chooseDate:'点击此处选择出游时间',
    today:'',
  },

  listenerNameInput: function (e) {
    this.data.name = e.detail.value;
  },

  listenerPhoneInput: function (e) {
    this.data.phone = e.detail.value;
  },

  listeneridentityCardInput: function (e) {
    this.data.identityCard = e.detail.value;
  },

  changeDate:function(e){
    //获取当前选择日期
    var date = e.detail.value;
    this.setData({
      chooseDate:date
    })
    console.log(this.data.chooseDate);
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
          name: thisgood.name,
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

  // onChange(event) {
  //   const { goods } = this.data;
  //   const checkedGoods = event.detail;
  //   const totalPrice = goods.reduce(
  //     (total, item) =>
  //       total + (checkedGoods.indexOf(item.id) !== -1 ? item.price : 0),
  //     0,
  //   );
  //   const submitBarText = checkedGoods.length ? `结算`: '结算';
  //   this.setData({
  //     checkedGoods,
  //     totalPrice,
  //     submitBarText,
  //   });
  // },

  onSubmit() {
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
    console.log(this.data.productid);
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