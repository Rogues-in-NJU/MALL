var app = getApp();

Page({
  data: {
    goods: [
      {
        id: '1',
        title: '仙境',
        desc: '仙境，指中国传统的远古神话中仙人生活的地方；亦指风景绝美的地方，如“蓬莱仙界”、“人间仙界”。',
        price: 2000,
        num: 1,
        thumb:
          '/images/1.png',
      },
      {
        id: '2',
        title: '苏州园林',
        desc: '苏州古典园林，亦称“苏州园林”，是位于江苏省苏州市境内的中国古典园林的总称。',
        price: 2399,
        num: 1,
        thumb:
          '/images/2.png',
      },
      {
        id: '3',
        title: '北京天坛',
        desc: '天坛，世界文化遗产，全国重点文物保护单位，国家AAAAA级旅游景区，全国文明风景旅游区示范点。',
        price: 6900,
        num: 1,
        thumb:
          '/images/3.png',
      },
    ],

    category:[
      {
        name: '自驾游',
      },
      {
        name: '老年游',
      },
      {
        name: '周末游',
      },
      {
        name: '出国游',
      }
    ]
  },

  onChange: app.onRouteChange,
  goToGoods: function (event) {
    // console.log(event);
    var dataset = event.currentTarget.dataset || event.target.dataset;
    var id;
    if (dataset.i) {
      id = dataset.i;
      wx.navigateTo({
        url: '/pages/goods/index?id=' + id,
        success: () => { },
        error: () => {
          wx.showToast({
            icon: 'none',
            title: '查看商品失败!',
          });
        },
      });
    } else {
      wx.showToast({
        title: '商品下架或不存在!',
        icon: 'none'
      });
    }
  }
});