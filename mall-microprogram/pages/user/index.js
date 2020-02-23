import http from '../../request';

var app = getApp();

Page({
  data: {
    nickName: '加载中...',
    avatarUrl: null,
    withdrawal: 0,
    subordinateNum: 0,
  },
  onLoad: function() {
    var that = this;
    console.log('here');
    console.log(app.userInfo);
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
  onChange: app.onRouteChange
});
