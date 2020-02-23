import http from 'request';

App({
  data: {
    routes: [
      {
        url: '/pages/goodslist/index',
        name: '商品列表'
      }, {
        url: '/pages/user/index',
        name: '用户中心'
      }
    ],
    userInfo: null
  },
  onLaunch: function() {
    this.login();
  },
  onRouteChange: function(event) {
    wx.navigateTo({
      url: this.data.routes[event.detail].url,
      success: () => { },
      error: () => {
        wx.showToast({
          icon: 'none',
          title: '打开' + this.data.routes[event.detail].name + '失败!',
        });
      },
    });
  },
  login: function() {
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
                    if (!res) {
                      that.loginFailed();
                      return;
                    }
                    if (!res.code || res.code !== 10000) {
                      that.loginFailed(res.message);
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
                    that.loginFailed('网络连接失败!');
                  });
              }
            },
            fail: function() {
              that.loginFailed('没有给予权限!');
            }
          });
        }
      },
      fail: function() {
        that.loginFailed('网络连接失败!');
      }
    });
  },
  userInfoReadyCallback: null,
  loginFailed: function(msg) {
    wx.hideLoading();
    wx.showToast({
      title: msg | '登录失败!',
      icon: 'cancel',
      duration: 1500
    });
  }
});