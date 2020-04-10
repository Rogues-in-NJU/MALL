import http from '../../request';

var app = getApp();

Page({
  data: {
    nickName: '用户加载中...',
    userId: null,
    avatarUrl: null,
    withdrawal: 0,
    subordinateNum: 0,
    payingNum: null,
    todoNum: null,
    finishedNum: null,
    showGetAuth: false
  },
  onLoad: function(options) {
    var that = this;
    // 查看是否授权
    wx.getSetting({
      success: (res) => {
        console.log(res);
        if (res.authSetting['scope.userInfo']) {
          return;
        } else {
          this.setData({
            showGetAuth: true
          });
        }
      }
    });
    if (app.userInfo) {
      this.getUserInfo();
    } else {
      // 需要登录
      app.userInfoReadyCallback = function () {
        that.getUserInfo();
      }
      app.login();
    }
  },
  getUserInfo: function() {
    var that = this;
    http.get('/wechat/api/userInfo')
      .then(res => {
        if (!res) {
          return;
        }
        if (!res.code === 10000) {
          return;
        }
        that.setData({
          userId: res.data.userId,
          subordinateNum: res.data.subordinateNum,
          withdrawal: res.data.withdrawal,
          nickName: app.userInfo.nickName,
          avatarUrl: app.userInfo.avatarUrl
        });
      })
      .catch(err => {
        wx.showToast({
          title: '网络连接失败!',
          icon: 'none',
          duration: 1500
        });
      });
  },
  onChange: app.onRouteChange,
  onShareAppMessage: function(res) {
    if (!this.data.userId) {
      return null;
    }
    console.log(this.data.userId);
    return {
      imageUrl: '',
      path: '/pages/user/index?sharedUserId=' + this.data.userId
    }
  },
  goToOrders: function (event) {
    var dataset = event.currentTarget.dataset || event.target.dataset;
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
  },
  bindGetUserInfo: function(event) {
    if (event.detail.userInfo) {
      //用户按了允许授权按钮
      app.userInfoReadyCallback = function () {
        that.getUserInfo();
      }
      app.login();
    } else {
      //用户按了拒绝按钮
    }
  }
});
