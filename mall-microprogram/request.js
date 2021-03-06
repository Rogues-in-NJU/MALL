// const baseUrl = 'https://175.24.41.132:8080';
// const baseUrl = 'https://localhost:8080';
const baseUrl = 'https://aishangwujiang.com';
const tokenHead = '';

function request(url, method, data, header) {
  data = data || {};
  header = header || {
    'Content-Type': 'application/json'
  };
  let token = wx.getStorageSync("UserToken");
  if (token) {
    header['Authorization'] = tokenHead + token;
  }
  let promise = new Promise(function(resolve, reject) {
    wx.request({
      url: baseUrl + url,
      header: header,
      data: data,
      method: method,
      success: resolve,
      fail: reject
    });
  });
  return promise.then(res => res.data);
}

module.exports = {
  baseUrl: baseUrl,
  'get': function(url, header) {
    return request(url, 'GET', {}, header);
  },
  'post': function(url, data, header) {
    return request(url, 'POST', data, header);
  }
}