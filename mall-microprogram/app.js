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
  onLaunch: function(query) {
    //this.login(query.query);
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
                    if (query && query.sharedUserId) {
                      http.get('/wechat/api/subordinate/add/' + query.sharedUserId)
                        .then(res => {

                        })
                        .catch(err => {

                        });
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
  userInfoReadyCallback: null
});