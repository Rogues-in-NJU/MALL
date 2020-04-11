// pages/withdrawal/index.js
import common from '../../common';
import http from '../../request';

Page({

  /**
   * 页面的初始数据
   */
  data: {
    tabActive: 0,
    withdrawalNum: "",
    
    tryWithdrawalNum: 0,

    records: []
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.refresh();
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  onReachBottom: function() {

  },

  onValueChange: function(event) {
    try {
      this.setData({
        tryWithdrawalNum: parseFloat(event.detail)
      });
    } catch (e) {

    } 
  },

  refresh: function() {
    var that = this;
    this.setData({
      tryWithdrawalNum: 0
    })
    http.get('/wechat/api/userInfo')
      .then(res => {
        if (!res) {
          return;
        }
        if (!res.code === 10000) {
          return;
        }
        that.setData({
          withdrawalNum: common.number_format(res.data.withdrawal / 100.0, 2)
        });
      })
      .catch(err => {
        wx.showToast({
          title: '网络连接失败!',
          icon: 'none',
          duration: 1500
        });
      });
    http.get('/wechat/api/withdrawal/history/list')
      .then(res => {
        if (!res) {
          return;
        }
        if (!res.code === 10000) {
          return;
        }
        var list = res.data;
        var records = list.map(r => {
          return {
            withdrawalTime: r.withdrawalTime,
            cash: common.number_format(r.cash / 100.0, 2),
            status: r.status === 0 ? '待处理' : '已完成'
          }
        });
        that.setData({
          records: records
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

  apply: function() {
    if (this.data.tryWithdrawalNum === undefined
      || this.data.tryWithdrawalNum === null
      || this.data.tryWithdrawalNum <= 0) {
      wx.showToast({
        title: '请输入合理的金额!',
        icon: 'none',
        duration: 1500
      });
      return;
    }
    console.log(this.data.tryWithdrawalNum);
    http.post('/wechat/api/withdrawal/apply', {
      applyValue: this.data.tryWithdrawalNum * 100
    }).then(res => {
        if (!res) {
          wx.showToast({
            title: '网络连接失败!',
            icon: 'none',
            duration: 1500
          });
          return;
        }
        console.log(res);
        if (res.code !== 10000) {
          wx.showToast({
            title: res.message,
            icon: 'none',
            duration: 1500
          });
          return;
        }
        wx.showToast({
          title: '申请提交成功!',
          icon: 'none',
          duration: 1500
        });
        this.refresh();
      })
      .catch(err => {
        wx.showToast({
          title: '网络连接失败!',
          icon: 'none',
          duration: 1500
        });
      });
  }
})