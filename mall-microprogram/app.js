import http from 'request';

App({
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