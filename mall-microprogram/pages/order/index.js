// pages/order/index.js
import http from '../../request';

Page({

  /**
   * 页面的初始数据
   */
  data: {
    id: null,
    status: 0,
    statusName: '',
    orderCode: 12,
    consigneePhone: 'xx',
    transactionNumber: null,
    price: 0,
    num: 0,
    createdAt: '',
    payTime: '',
    refundTime: '',
    productId: 0,
    product: {},

    nonce_str:'',
    sign:'',
    prepay_id:'',
    appid:'',
    timeStamp:'',

    isRefund: false,
    isCancel: false,
    isPay: false
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      id: options.id
    });
    this.refresh();
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
    this.refresh();
  },

  refresh: function() {
    http.get('/wechat/api/order/' + this.data.id)
      .then(res => {
        console.log(res);
        if (res === undefined || res === null) {
          wx.showToast({
            icon: 'none',
            title: '网络连接失败!',
            duration: 1500
          });
          return;
        }
        if (res.code !== 10000) {
          wx.showToast({
            icon: 'none',
            title: res.message,
            duration: 1500
          });
          return;
        }
        this.setData({
          status: res.data.status,
          statusName: getStatus(res.data.status),
          orderCode: res.data.orderCode,
          consigneePhone: res.data.consigneePhone,
          transactionNumber: res.data.transactionNumber,
          price: (res.data.price / 100.0).toFixed(2),
          // price: res.data.price,
          num: res.data.num,
          createdAt: res.data.createdAt,
          payTime: res.data.payTime,
          refundTime: res.data.refundTime,
          productId: res.data.productId,
          isRefund: res.data.status === 1 || res.data.status === 2,
          isCancel: res.data.status === 0,
          isPay: res.data.status === 0
        });
        http.get('/wechat/api/product/get?id=' + this.data.productId)
          .then(res => {
            if (res === undefined || res === null) {
              wx.showToast({
                icon: 'none',
                title: '网络连接失败!',
                duration: 1500
              });
              return;
            }
            if (res.code !== 10000) {
              wx.showToast({
                icon: 'none',
                title: res.message,
                duration: 1500
              });
              return;
            }
            this.setData({
              product: res.data
            });
          })
          .catch(err => {
            wx.showToast({
              icon: 'none',
              title: '网络连接失败!',
              duration: 1500
            });
          });
      })
      .catch(err => {
        wx.showToast({
          icon: 'none',
          title: '网络连接失败!',
          duration: 1500
        });
      });
  },

  pay: function () {
    http.get('/wechat/api/order/pay/' + this.data.id)
      .then(res => {
        if (res === undefined || res === null) {
          wx.showToast({
            icon: 'none',
            title: '网络连接失败!',
            duration: 1500
          });
          return;
        }
        if (res.code !== 10000) {
          wx.showToast({
            icon: 'none',
            title: res.message,
            duration: 1500
          });
          return;
        }
        console.log(res);
        this.setData({
          nonce_str: res.data.nonce_str,
          sign: res.data.sign,
          prepay_id: res.data.prepay_id,
          appid: res.data.appid,
          timeStamp: res.data.timeStamp,
        });
        this.generatePayCode();
      })
      .catch(err => {
        wx.showToast({
          icon: 'none',
          title: '网络连接失败!',
          duration: 1500
        });
      });
  },

  refund: function() {
    http.get('/wechat/api/order/refund/' + this.data.id)
      .then(res => {
        if (res === undefined || res === null) {
          wx.showToast({
            icon: 'none',
            title: '网络连接失败!',
            duration: 1500
          });
          return;
        }
        if (res.code !== 10000) {
          wx.showToast({
            icon: 'none',
            title: res.message,
            duration: 1500
          });
          return;
        }
        wx.showToast({
          icon: 'none',
          title: '退款申请中!',
          duration: 1500
        });
        this.refresh();
      })
      .catch(err => {
        wx.showToast({
          icon: 'none',
          title: '网络连接失败!',
          duration: 1500
        });
      });
  },

  generatePayCode : function() {
    wx.requestPayment({
      'appId': this.data.appid,
      'timeStamp': this.data.timeStamp,
      'nonceStr': this.data.nonce_str,
      'package': 'prepay_id='+this.data.prepay_id,
      'signType': 'MD5',
      'paySign': this.data.sign,
      'success': (res) => {
        //TODO 支付成功调用接口
        http.get('/wechat/api/order/finishPay/' + this.data.id)
          .then(res => {
            console.log(res);
            if (res === undefined || res === null) {
              wx.showToast({
                icon: 'none',
                title: '网络连接失败!',
                duration: 1500
              });
              return;
            }
            if (res.code !== 10000) {
              wx.showToast({
                icon: 'none',
                title: res.message,
                duration: 1500
              });
              return;
            }
            wx.showToast({
              icon: 'none',
              title: '支付成功!',
              duration: 1500
            });
            this.refresh();
          })
          .catch(err => {
            wx.showToast({
              icon: 'none',
              title: '支付失败!',
              duration: 1500
            });
          });
      },
      'fail': (res) => {
        console.log('支付失败');
        wx.showToast({
          icon: 'none',
          title: '支付失败!',
          duration: 1500
        });
        // TODO: 删除这段
        http.get('/wechat/api/order/finishPay/' + this.data.id)
          .then(res => {
            console.log(res);
            if (res === undefined || res === null) {
              wx.showToast({
                icon: 'none',
                title: '网络连接失败!',
                duration: 1500
              });
              return;
            }
            if (res.code !== 10000) {
              wx.showToast({
                icon: 'none',
                title: res.message,
                duration: 1500
              });
              return;
            }
            wx.showToast({
              icon: 'none',
              title: '支付成功!',
              duration: 1500
            });
            this.refresh();
          })
          .catch(err => {
            wx.showToast({
              icon: 'none',
              title: '支付失败!',
              duration: 1500
            });
          });
      },
      'complete': function (res) {}
    });
  },

  cancel: function() {
    http.get('/wechat/api/order/cancel/' + this.data.id)
      .then(res => {
        if (res === undefined || res === null) {
          wx.showToast({
            icon: 'none',
            title: '网络连接失败!',
            duration: 1500
          });
          return;
        }
        if (res.code !== 10000) {
          wx.showToast({
            icon: 'none',
            title: res.message,
            duration: 1500
          });
          return;
        }
        wx.showToast({
          icon: 'none',
          title: '订单已取消!',
          duration: 1500
        });
        this.refresh();
      })
      .catch(err => {
        wx.showToast({
          icon: 'none',
          title: '网络连接失败!',
          duration: 1500
        });
      });
  },

  goToProductDetail: function(event) {
    console.log(event);
    var dataset = event.currentTarget.dataset || event.target.dataset;
    if (dataset.product) {
      var id = dataset.product;
      wx.navigateTo({
        url: '/pages/goods/index?id=' + id,
        success: () => { },
        error: () => {
          wx.showToast({
            icon: 'none',
            title: '打开商品失败!',
          });
        },
      });
    } else {
      wx.showToast({
        title: '商品不存在!',
        icon: 'none'
      });
    }
  }

})

function getStatus(i) {
  if (i === 0) {
    return '待付款';
  } else if (i === 1) {
    return '待完成';
  } else if (i === 2) {
    return '已完成';
  } else if (i === 3) {
    return '退款中';
  } else if (i === 4) {
    return '退款成功';
  } else {
    return '已废弃';
  }
}