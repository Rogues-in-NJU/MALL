import http from '../../request';

var app = getApp();

Page({
  data: {
    name: '',
    phone: '',
    identityCard:'',

    imageAddresses: [],
    name: "",
    price: 0,
    submitBarText: "结算",
    // checkedGoods: ['1'],
    // goods: [
    //   {
    //     id: '1',
    //     title: '苏州园林',
    //     price: 239900,
    //     num: 1,
    //     thumb:
    //       '/images/2.png',
    //   },
    // ],
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
        console.log(thisgood);
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
    console.log('Name=' + this.data.name)
    console.log('Phone=' + this.data.phone)
    console.log('identityCard=' + this.data.identityCard)
    //TODO 商品信息（是否勾选）、库存、用户信息的检查

    //TODO 调用支付接口获取支付信息
    wx.request({
      url: "/api/product/generateOrder",
      method: "POST",
      data: {
        userId: 'py',
        productId: 18,
        num: 2,
        consignee: 'xx',
        consigneePhone: 'xx',
        consigneeAddress: 'xx'
      },
      header: {
        "Content-Type": "application/x-www-form-urlencoded"
      },
      success: function (res) {
        console.log(res.data);
        wx.showToast({
          title: '成功！',
          icon: 'success',
        })
      },
    })

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