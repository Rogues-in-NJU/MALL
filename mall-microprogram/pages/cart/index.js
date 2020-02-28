Page({
  data: {
    checkedGoods: ['1'],
    goods: [
      {
        id: '1',
        title: '苏州园林',
        price: 239900,
        num: 1,
        thumb:
          '/images/2.png',
      },
    ],
    totalPrice: 0,

  },
  onLoad: function() {
    const { checkedGoods, goods } = this.data;
    const submitBarText = `结算`;
    const totalPrice = goods.reduce(
      (total, item) =>
        total + (checkedGoods.indexOf(item.id) !== -1 ? item.price : 0),
      0,
    );
    goods.forEach(item => {
      item.formatPrice = (item.price / 100).toFixed(2);
    });
    this.setData({
      totalPrice,
      submitBarText,
      goods,
    });
  },

  onChange(event) {
    const { goods } = this.data;
    const checkedGoods = event.detail;
    const totalPrice = goods.reduce(
      (total, item) =>
        total + (checkedGoods.indexOf(item.id) !== -1 ? item.price : 0),
      0,
    );
    const submitBarText = checkedGoods.length ? `结算`: '结算';
    this.setData({
      checkedGoods,
      totalPrice,
      submitBarText,
    });
  },

  onSubmit() {
    wx.showToast({
      title: '点击结算',
      icon: 'none'
    });
  },
});