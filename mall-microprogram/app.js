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
    ]
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
    wx.login({
      success: function(res) {
        if (res.code) {
          http.get('/wechat/api/login')
            .then(res => {
              console.log(res);
            })
            .catch(err => {
              console.log(err);
            });
        }
      }
    });
  }
})

var app = getApp();
app.login();