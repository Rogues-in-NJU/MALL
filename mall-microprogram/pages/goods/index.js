import http from '../../request';

var app = getApp();

Page({
  data: {
    productid:0,
    // buyingPrice: 0,
    classificationName:"",
    imageAddresses:[],
    imageInfoAddresses:[],
    name:"",
    price:0,
    quantity:0,
    saleVolume:0,
    sellEndTime:"",
    sellStartTime:"",
    status:0
  },
  onLoad(options) {
    console.log(options.id);
    //TODO 根据options.id获取对应的商品
    var url = '/wechat/api/product/get?id=' + options.id;
    http.get(url)
      .then(res => {
        if (res === undefined || res === null) {
          wx.showToast({
            icon: 'none',
            title: '网络连接失败!',
            duration: 1500
          });
          this.setData({
            canRefresh: true
          });
          return;
        }
        if (res.code !== 10000) {
          wx.showToast({
            icon: 'none',
            title: res.message,
            duration: 1500
          });
          this.setData({
            canRefresh: true
          });
          return;
        }
        let thisgood = res.data;
        console.log(thisgood);
        this.setData({
          // buyingPrice: thisgood.buyingPrice,
          productid: options.id,
          classificationName: thisgood.classificationName,
          imageAddresses: thisgood.imageAddresses,
          imageInfoAddresses: thisgood.imageInfoAddresses,
          name: thisgood.name,
          price: (thisgood.price / 100).toFixed(2),
          quantity: thisgood.quantity,
          saleVolume:thisgood.saleVolume,
          sellEndTime: thisgood.sellEndTime,
          sellStartTime: thisgood.sellStartTime,
          status: thisgood.status,
          canRefresh: true
        });
      })
      .catch(err => {
        wx.showToast({
          icon: 'none',
          title: '网络连接失败!',
          duration: 1500
        });
        this.setData({
          canRefresh: true
        });
      });
    // const formatPrice = `¥${(goods.price / 100).toFixed(2)}`;
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
    wx.showToast({
      title: '点击结算',
      icon: 'none'
    });
    //TODO 跳转到结算页面，
    wx.navigateTo({
      url: '/pages/cart/index?id=' + this.data.productid,
      success: () => { },
      error: () => {
        wx.showToast({
          icon: 'none',
          title: '结算页面加载失败',
        });
      },
    });


    
  }

});