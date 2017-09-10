//index.js
//获取应用实例
const app = getApp()

Page({
  data: {
    motto: 'Hello World',
    userInfo: {},
    hasUserInfo: false,
    canIUse: wx.canIUse('button.open-type.getUserInfo')
  },
  //事件处理函数
  bindViewTap: function() {
    wx.navigateTo({
      url: '../logs/logs'
    })
  },
  onLoad: function () {
    wx.request({
      url: 'http://localhost:8080/wxdemo/websocket', //仅为示例，并非真实的接口地址
      data: {
        x: '',
        y: ''
      },
      header: {
        'content-type': 'application/json' // 默认值
      },
      success: function (res) {
        console.log(res.data)
      }
    })
    wx.connectSocket({
      url: "ws://localhost:8080/wxdemo/websocket",
    })
    wx.onSocketOpen(function () {
      console.log('WebSocket连接已经打开!')
      wx.sendSocketMessage({
        data: 'HELLO,WORLD' + Math.random() * 0XFFFFFF.toString()
      })
    });
    wx.onSocketMessage(function (data) {
      console.log(data);
    });     // 监听是否关闭
    wx.onSocketClose(function () {
      console.log('WebSocket连接已经关闭!')
    });
  }
  })
