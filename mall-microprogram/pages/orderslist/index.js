// pages/orderslist.js
import http from '../../request';

Page({

  /**
   * 页面的初始数据
   */
  data: {
    searchValue: '',
    tabActive: 0,
    orders: [],
    canRefresh: true,
    pageIndex: 0,
    pageSize: 10,
    tabDisabled: false
  },
  // [{ name: 1 }, { name: 2 }, { name: 3 }, { name: 4 }, { name: 5 }]
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    if (options && options.i && options.i !== "0") {
      this.setData({
        tabActive: parseInt(options.i)
      });
    } else {
      this.loadOrders();
    }
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
    console.log('refresh');
    this.setData({
      pageIndex: 0,
      pageSize: 10,
      canRefresh: true,
      orders: []
    });
    this.loadOrders();
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    console.log('reach bottom');
    if (this.data.searchValue) {
      this.searchOrders();
    } else {
      if (this.data.canRefresh) {
        this.setData({
          canRefresh: false
        });
        this.loadOrders();
      }
    }
  },

  onTabChange: function(event) {
    console.log('tab change');
    this.setData({
      tabActive: event.detail.index
    });
    this.setData({
      pageIndex: 0,
      pageSize: 10,
      canRefresh: true,
      orders: []
    });
    if (this.data.searchValue) {
      this.searchOrders();
    } else {
      this.loadOrders();
    }
  },
  onSearchInputChange: function(e) {
    this.setData({
      searchValue: e.detail
    });
  },
  onSearch: function() {
    this.setData({
      pageIndex: 0,
      pageSize: 10,
      canRefresh: true,
      orders: []
    });
    this.searchOrders();
  },
  onSearchCancel: function() {
    this.setData({
      searchValue: '',
      pageIndex: 0,
      pageSize: 10,
      canRefresh: true,
      orders: []
    });
    this.loadOrders();
  },

  loadOrders: function() {
    var url = '/wechat/api/order/list?pageIndex=' 
      + (this.data.pageIndex + 1) + '&pageSize=' + this.data.pageSize;
    if (this.data.tabActive !== 0) {
      url += '&status=' + (this.data.tabActive - 1)
    }
    this.setData({
      tabDisabled: true
    });
    wx.showLoading({
      title: '加载中',
      mask: true
    });
    http.get(url)
      .then(res => {
        wx.hideLoading();
        if (res === undefined || res === null) {
          wx.showToast({
            icon: 'none',
            title: '网络连接失败!',
            duration: 1500
          });
          this.setData({
            canRefresh: true,
            tabDisabled: false
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
            canRefresh: true,
            tabDisabled: false
          });
          return;
        }
        let listData = res.data;
        let orderShowList = listData.result.map(o => {
          let ov = {
            id: o.id,
            num: o.num,
            price: (o.price / 100.0).toFixed(2),
            status: o.status,
            statusName: getStatus(o.status),
            productImage: o.productImage,
            productName: o.productName,
          }
          return ov;
        });
        this.setData({
          pageIndex: listData.pageIndex,
          pageSize: listData.pageSize, 
          orders: [
            ...this.data.orders,
            ...orderShowList
          ],
          canRefresh: true,
          tabDisabled: false
        });
      })
      .catch(err => {
        wx.hideLoading();
        wx.showToast({
          icon: 'none',
          title: '网络连接失败!',
          duration: 1500
        });
        this.setData({
          canRefresh: true,
          tabDisabled: false
        });
      });
  },

  searchOrders: function() {
    var url = '/wechat/api/order/search?productName='
      + this.data.searchValue;
    if (this.data.tabActive !== 0) {
      url += '&status=' + (this.data.tabActive - 1)
    }
    this.setData({
      tabDisabled: true
    });
    wx.showLoading({
      title: '加载中',
      mask: true
    });
    http.get(url)
      .then(res => {
        wx.hideLoading();
        if (res === undefined || res === null) {
          wx.showToast({
            icon: 'none',
            title: '网络连接失败!',
            duration: 1500
          });
          this.setData({
            canRefresh: true,
            tabDisabled: false
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
            canRefresh: true,
            tabDisabled: false
          });
          return;
        }
        let listData = res.data;
        let orderShowList = listData.result.map(o => {
          let ov = {
            id: o.id,
            num: o.num,
            price: (o.price / 100.0).toFixed(2),
            status: o.status,
            statusName: getStatus(o.status),
            productImage: o.productImage,
            productName: o.productName,
          }
          return ov;
        });
        this.setData({
          pageIndex: listData.pageIndex,
          pageSize: listData.pageSize,
          orders: [
            ...this.data.orders,
            ...orderShowList
          ],
          canRefresh: true,
          tabDisabled: false
        });
      })
      .catch(err => {
        wx.hideLoading();
        wx.showToast({
          icon: 'none',
          title: '网络连接失败!',
          duration: 1500
        });
        this.setData({
          canRefresh: true,
          tabDisabled: false
        });
      });
  },

  onOrderItemButtonClick: function(event) {
    console.log(event);
  },

  goToOrderDetail: function(event) {
    console.log(event);
    var dataset = event.currentTarget.dataset || event.target.dataset;
    var id;
    if (dataset.id) {
      id = dataset.id;
      wx.navigateTo({
        url: '/pages/order/index?id=' + id,
        success: () => { },
        error: () => {
          wx.showToast({
            icon: 'none',
            title: '打开订单失败!',
          });
        },
      });
    } else {
      wx.showToast({
        title: '订单不存在!',
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