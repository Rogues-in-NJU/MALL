import http from '../../request';

var app = getApp();

Page({
  data: {
    mainImage:'',
    searchValue: '',
    tabActive: 0,
    orders: [],
    canRefresh: true,
    pageIndex: 0,
    pageSize: 10,
    category: [
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
    ],
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var url = '/api/cover/info';
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
        let data = 
        this.setData({
          mainImage: res.data.imageAddresses,
          canRefresh: true
        });
        console.log(res.data);
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
    if (options && options.i) {
      this.setData({
        tabActive: parseInt(options.i)
      });
    }
    this.loadGoods();
  },

  /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
  onPullDownRefresh: function () {
    // console.log('refresh');
  },

  /**
     * 页面上拉触底事件的处理函数
     */
  onReachBottom: function () {
    console.log('reach bottom');
    if (this.data.searchValue) {

    } else {
      if (this.data.canRefresh) {
        this.setData({
          canRefresh: false
        });
        this.loadGoods();
      }
    }
  },

  onTabChange: function (event) {
    this.setData({
      tabActive: event.detail.index
    });
    if (this.data.searchValue) {
      this.searchGoods();
    } else {
      this.setData({
        pageIndex: 0,
        pageSize: 10,
        canRefresh: true,
        goods: []
      });
      this.loadGoods();
    }
  },

  onSearchInputChange: function (e) {
    this.setData({
      searchValue: e.detail
    });
  },
  onSearch: function () {
    this.setData({
      goods: []
    });
    this.searchGoods();
  },
  onSearchCancel: function () {
    this.setData({
      searchValue: '',
      pageIndex: 0,
      pageSize: 10,
      canRefresh: true
    });
    this.goods = [];
    this.loadGoods();
  },
  loadClassification:function(){
    var url = '/wechat/api/classification/list';
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
        console.log(res.data);
        this.setData({
          category:res.data,
        });
        console.log(category);
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
  },


  loadGoods: function () {
    var url = '/wechat/api/product/list?pageIndex='
      + (this.data.pageIndex + 1) + '&pageSize=' + this.data.pageSize;
    if (this.data.tabActive !== 0) {
      url += '&status=' + (this.data.tabActive - 1)
    }
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
        let listData = res.data;
        let goodShowList = listData.result.map(o => {
          let ov = {
            id: o.id,
            name: o.name,
            classificationId: o.classificationId,
            classificationName: o.classificationName,
            buyingPrice: o.buyingPrice,
            percent: o.percent,
            quantity: o.quantity,
            price: (o.price / 100.0).toFixed(2),
            status: o.status,
            imageAddresses: o.imageAddresses,
            imageInfoAddresses: o.imageInfoAddresses,
          }
          return ov;
        });
        console.log(goodShowList);
        this.setData({
          pageIndex: listData.pageIndex,
          pageSize: listData.pageSize,
          goods: goodShowList,
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
  },

  searchOrders: function () {
    var url = '/wechat/api/product/search?productName='
      + this.data.searchValue;
    if (this.data.tabActive !== 0) {
      url += '&status=' + (this.data.tabActive - 1)
    }
    http.get(url)
      .then(res => {
        if (res === undefined || res === null) {
          wx.showToast({
            icon: 'none',
            title: '网络连接失败!',
            duration: 1500
          });
          return;
        }
        if (res.code !== 10000) {
          wx.showToast({
            icon: 'none',
            title: res.message,
            duration: 1500
          });
          return;
        }
        console.log(res);
      })
      .catch(err => {
        wx.showToast({
          icon: 'none',
          title: '网络连接失败!',
          duration: 1500
        });
      });
  },

  onGoodsItemButtonClick: function (event) {
    console.log(event);
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