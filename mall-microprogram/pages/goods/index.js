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

    if (options && options.sharedUserId) {
      var sharedUserId = options.sharedUserId;
      var tx = sharedUserId !== undefined && sharedUserId !== null ? sharedUserId : '';
      if (sharedUserId) {
        wx.setStorageSync('sharedUserId', sharedUserId);
      }
    }
    
    // 根据options.id获取对应的商品
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
    let token = wx.getStorageSync("UserToken");
    if (token) {
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
    } else {
      wx.navigateTo({
        url: '/pages/user/index',
        success: () => { 
          wx.showToast({
            icon: 'none',
            title: '请登录后进行购买操作!'
          });
        },
        error: () => {
          wx.showToast({
            icon: 'none',
            title: '用户页面加载失败',
          });
        },
      });
    }
  },

  onShareAppMessage: function (res) {
    if (!this.data.userId) {
      return null;
    }
    console.log(this.data.userId);

    var that = this;
    var goods_id=that.data.productid;//获取产品id
    var goods_title=that.data.name;//获取产品标题

    var sharedUserId = app.globalData.userId;
    if (res.from === 'button') {
      // 来自页面内转发按钮
      return {
        title: goods_title,
        path: '/pages/goods/index?id=' +goods_id+'&sharedUserId='+sharedUserId,
      }
    }
  }
});