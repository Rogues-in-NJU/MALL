import http from '../../request';

var app = getApp();

Page({
  data: {

  },
  onLoad: function(options) {
  },

  goToUser: function (event) {
    wx.navigateTo({
      url: '/pages/user/index',
      success: () => { },
      error: () => {
        wx.showToast({
          icon: 'none',
          title: '登录失败!',
        });
      },
    })
  }
});
