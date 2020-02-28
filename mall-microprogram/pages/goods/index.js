Page({
  data: {
    goods: {
      id:'1',
      title: '苏州园林',
      price: 239900,
      formatPrice: '2399',
      express: '免运费',
      remain: 19,
      thumb:
        '/images/2.png',
    },
  },
  onLoad(options) {
    // console.log(options);
    console.log(options.id);
    //TODO 根据options.id获取对应的商品
    const { goods } = this.data;
    const formatPrice = `¥${(goods.price / 100).toFixed(2)}`;
    this.setData({
      goods: {
        ...goods,
        formatPrice,
      },
    });
  },

  onClickCart() {
    wx.navigateTo({
      url: '/pages/cart/index',
      success: () => {},
      error: () => {
        wx.showToast({
          icon: 'none',
          title: '打开购物车失败',
        });
      },
    });
  },

  onClickUser() {
    wx.navigateTo({
      url: '/pages/user/index',
      success: () => {},
      error: () => {
        wx.showToast({
          icon: 'none',
          title: '打开个人中心失败',
        });
      },
    });
  },

  onClickButton:function(event) {
    wx.navigateTo({
      url: '/pages/cart/index',
      success: () => { },
      error: () => {
        wx.showToast({
          icon: 'none',
          title: '查看商品失败!',
        });
      },
    });
  }

});