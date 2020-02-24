// pages/orderslist.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    searchValue: '',
    tabActive: 0,
    orders: [{ name: 1 }, { name: 2 }, { name: 3 }, { name: 4 }, { name: 5 }]
  },
  // [{ name: 1 }, { name: 2 }, { name: 3 }, { name: 4 }, { name: 5 }]
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    if (options && options.i) {
      this.setData({
        tabActive: parseInt(options.i)
      });
    }
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
    console.log('refresh');
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    console.log('search more');
  },

  onTabChange: function(event) {
    this.setData({
      tabActive: event.detail.index
    });
  },
  onSearchInputChange: function(e) {
    this.setData({
      searchValue: e.detail
    });
  },
  onSearch: function() {
    console.log(this.data.searchValue);
  },
  onSearchCancel: function() {
    this.setData({
      searchValue: ''
    });
  },

  searchOrders: function() {

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