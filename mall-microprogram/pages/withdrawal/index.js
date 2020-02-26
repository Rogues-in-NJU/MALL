// pages/withdrawal/index.js
import common from '../../common';

Page({

  /**
   * 页面的初始数据
   */
  data: {
    tabActive: 0,
    withdrawalNum: "",
    
    tryWithdrawalNum: 0
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      withdrawalNum: common.number_format(10000, 2)
    })
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  onReachBottom: function() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },

  onValueChange: function(event) {
    try {
      this.setData({
        tryWithdrawalNum: parseFloat(event.detail)
      });
    } catch (e) {

    } 
  }
})