import http from '../../request';

var app = getApp();

Page({
  data: {
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

  onLoad(options) {
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
          price: thisgood.price,
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

  onChange(event) {
    const { goods } = this.data;
    const checkedGoods = event.detail;
    const totalPrice = goods.reduce(
      (total, item) =>
        total + (checkedGoods.indexOf(item.id) !== -1 ? item.price : 0),
      0,
    );
    const submitBarText = checkedGoods.length ? `结算`: '结算';
    this.setData({
      checkedGoods,
      totalPrice,
      submitBarText,
    });
  },

  onSubmit() {
    //TODO 商品信息（是否勾选）、库存、用户信息的检查
    console.log(this.data.productid);
    var data = {
      // userId: 111,
      productId: this.data.productid,
      num: 1,
      consignee: this.data.name,
      consigneePhone: this.data.phone,
      consigneeAddress: this.data.identityCard
    };
    //TODO 调用支付接口获取支付信息
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
        console.log(this.data.orderid);
      })
      .catch(err => {
        wx.hideLoading();
        wx.showToast({
          title: '网络连接失败!',
          icon: 'none',
          duration: 1500
        });
      });

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
      //调用pay接口
    // console.log(this.data.orderid);
    // http.get('/wechat/api/order/pay/' +this.data. orderid)
    //   .then(res => {
    //     if (res === undefined || res === null) {
    //       wx.showToast({
    //         title: '网络连接失败!',
    //         icon: 'none',
    //         duration: 1500
    //       });
    //       return;
    //     }
    //     if (res.code !== 10000) {
    //       wx.showToast({
    //         title: res.message,
    //         icon: 'none',
    //         duration: 1500
    //       });
    //       return;
    //     }
    //     console.log(res.data);
    //   })
    //   .catch(err => {
    //     wx.hideLoading();
    //     wx.showToast({
    //       title: '网络连接失败!',
    //       icon: 'none',
    //       duration: 1500
    //     });
    //   });

  //   wx.requestPayment(
  //     {
  //       'timeStamp': '',
  //       'nonceStr': '',
  //       'package': '',
  //       'signType': 'MD5',
  //       'paySign': '',
  //       'success': function (res) {
  //         //TODO 跳转至订单
  //         console.log('支付成功');
  //       },
  //       'fail': function (res) {
  //         //TODO 跳转至订单
  //         console.log('支付失败');
  //         return;
  //       },
  //       'complete': function (res) {
  //         //TODO 完成支付调用接口
  //       }
  //     })



  },
});