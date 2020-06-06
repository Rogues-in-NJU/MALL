import http from '../../request';
import common from '../../common';
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
    showGetAuth: false,
    isLogin:false
  },
  onLoad: function(options) {
    if (options && options.sharedUserId) {
      var sharedUserId = options.sharedUserId;
      var tx = sharedUserId !== undefined && sharedUserId !== null ? sharedUserId : '';
      if (sharedUserId) {
        wx.setStorageSync('sharedUserId', sharedUserId);
      }
    }
    // if (app.userInfo) {
    //   this.getUserInfo();
    // }
  },
  onShow: function(options) {
    console.log('onShow');
    let token = wx.getStorageSync("UserToken");
    if (token) {
      if (app.userInfo) {
        this.getUserInfo();
      } else {
        this.refresh();
      }
    }
  },
  refresh: function() {
    var that = this;
    this.setData({
      showGetAuth: false
    });
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
        var sharedUserId = wx.getStorageSync('sharedUserId');
        if (sharedUserId) {
          console.log('user index send sharedUserId');
          http.get('/wechat/api/subordinate/add/' + sharedUserId)
            .then(res => {

            })
            .catch(err => {

            });
        }
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
        if (!res) {
          return;
        }
        if (!res.code === 10000) {
          return;
        }
        that.setData({
          userId: res.data.userId,
          subordinateNum: res.data.subordinateNum,
          withdrawal: common.number_format(res.data.withdrawal / 100.0, 2),
          nickName: app.userInfo.nickName,
          avatarUrl: app.userInfo.avatarUrl,
          isLogin:true
        });
      })
      .catch(err => {
        console.log(err);
        wx.showToast({
          title: '网络连接失败!',
          icon: 'none',
          duration: 1500
        });
        wx.removeStorageSync('UserToken');
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
      this.refresh();
    } else {
      //用户按了拒绝按钮
    }
  }
});
