import http from '../../request';

var app = getApp();

Page({
  data: {
    nickName: '用户加载中...',
    avatarUrl: null,
    withdrawal: 0,
    subordinateNum: 0,
    payingNum: null,
    todoNum: null,
    finishedNum: null
  },
  onLoad: function(options) {
    var that = this;
    if (app.userInfo) {
      this.getUserInfo();
    } else {
      // 需要登录
      app.userInfoReadyCallback = function() {
        that.getUserInfo();
      }
      app.login();
    }
  },
  getUserInfo: function() {
    var that = this;
    http.get('/wechat/api/userInfo')
      .then(res => {
        console.log(res);
        console.log(app.userInfo);
        if (!res) {
          return;
        }
        if (!res.code === 10000) {
          return;
        }
        that.setData({
          subordinateNum: res.data.subordinateNum,
          withdrawal: res.data.withdrawal,
          nickName: app.userInfo.nickName,
          avatarUrl: app.userInfo.avatarUrl
        });
      })
      .catch(err => {
        console.log(err);
      });
  },
  onChange: app.onRouteChange,
  goToOrders: function (event) {
    console.log(event);
    var dataset = event.currentTarget.dataset || event.target.dataset;
    console.log(dataset);
    var i;
    if (dataset.i) {
      i = dataset.i;
    } else {
      i = 0;
    }
    wx.navigateTo({
      url: '/pages/orderslist/index?i=' + i,
      success: () => { },
      error: () => {
        wx.showToast({
          icon: 'none',
          title: '打开订单列表失败!',
        });
      },
    })
  }
});
