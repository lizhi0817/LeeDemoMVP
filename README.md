# 部分插件使用方法 #
[lottie动画库](http://airbnb.io/lottie/#/android?id=sample-app "lottie动画库")	

## 未封装 ##

数据库未封装，可根据自己的需求封装自己所需要的数据库。
需要详细看一下，因为GreenDaoApi还没有更新，可能要换一种数据库的存储方式。

## 已封装 ##
- 崩溃日志记录
- Fresco
- 屏幕适配 
- 异常类
- 布局建议父布局是约束布局，尽量较少嵌套布局嵌套，如果业务没有需求，不要设置背景颜色(减少UI叠加，如果有需求，请自定义View，例如：扑克牌)
- log工具
- 内存泄露检查工具
- 各种所需要的工具类....  https://github.com/Blankj/AndroidUtilCode/blob/master/lib/utilcode/README-CN.md
- 组件化
- 加载动画选择Lottie的可能性比较高(备选gif)
- 网络请求已封装好，如果需要多接口，后续叠加



有些地方考虑的可能不是很全面,如果有不同的见解,可以反馈一下。



