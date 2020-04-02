// pages/order/index.js
import http from '../../request';

Page({

  /**
   * 页面的初始数据
   */
  data: {
    id: null,
    status: 0,
    orderCode: 12,
    consigneePhone: 'xx',
    transactionNumber: null,
    price: 0,
    num: 0,
    createdAt: '',
    payTime: '',
    refundTime: '',
    productId: 0
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
          orderCode: res.data.orderCode,
          consigneePhone: res.data.consigneePhone,
          transactionNumber: res.data.transactionNumber,
          price: res.data.price,
          num: res.data.num,
          createdAt: res.data.createdAt,
          payTime: res.data.payTime,
          refundTime: res.data.refundTime,
          productId: res.data.productId
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