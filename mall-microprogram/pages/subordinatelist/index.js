// pages/subordinatelist/index.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    subordinateList: [
      {nickname: '测试', createdAt: 'test'},

      { nickname: '测试', createdAt: 'test' },
      { nickname: 'test', createdAt: 'test' },
      { nickname: 'test', createdAt: 'test' },
      { nickname: 'test', createdAt: 'test' },
      { nickname: 'test', createdAt: 'test' },
      { nickname: 'test', createdAt: 'test' },
      { nickname: 'test', createdAt: 'test' },
      { nickname: 'test', createdAt: 'test' },
      { nickname: 'test', createdAt: 'test' },
      { nickname: 'test', createdAt: 'test' },
      { nickname: 'test', createdAt: 'test' },
      { nickname: 'test', createdAt: 'test' },
      { nickname: 'test', createdAt: 'test' },
      { nickname: 'test', createdAt: 'test' },
      { nickname: 'test', createdAt: 'test' }
    ]
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
    this.refresh();
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },

  refresh: function() {
    console.log('refresh');
  }
})