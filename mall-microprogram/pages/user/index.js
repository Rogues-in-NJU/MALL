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
        wx.showToast({
          title: '网络连接失败!',
          icon: 'none',
          duration: 1500
        });
      });
  },
  onChange: app.onRouteChange,
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
  
  login: function(query) {
    var that = this;
    wx.showLoading({
      title: '登录中',
      mask: true
    });
    wx.login({
      success: function(res) {
        if (res.code) {
          var code = res.code;
          wx.getUserInfo({
            success: function(res) {
              if (res) {
                var data = {
                  code: code,
                  rawData: res.rawData,
                  signature: res.signature
                };
                that.userInfo = null;
                wx.clearStorageSync('UserToken');
                http.post('/wechat/api/login', data)
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
                    wx.hideLoading();
                    wx.setStorageSync('UserToken', res.data);
                    wx.showToast({
                      title: '登录成功!',
                      icon: 'success',
                      duration: 1500
                    });
                    that.userInfo = JSON.parse(data.rawData);
                    // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
                    // 所以此处加入 callback 以防止这种情况
                    if (that.userInfoReadyCallback) {
                      that.userInfoReadyCallback();
                      that.userInfoReadyCallback = null;
                    }
                  })
                  .catch(err => {
                    wx.hideLoading();
                    wx.showToast({
                      title: '网络连接失败!',
                      icon: 'none',
                      duration: 1500
                    });
                  });
              }
            },
            fail: function() {
              wx.hideLoading();
              wx.showToast({
                title: '没有给予权限!',
                icon: 'none',
                duration: 1500
              });
            }
          });
        }
      },
      fail: function() {
        wx.hideLoading();
        wx.showToast({
          title: '网络连接失败!',
          icon: 'none',
          duration: 1500
        });
      }
    });
  },
});
