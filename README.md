# androidMonitor

## 简介

androidMonitor是一个安卓监控软件，可监控你运行应用的实时性能（cpu/net/mem）,实现方式借助安卓sdk中的adb命令，所以需要本地有adb环境。

## 环境

jdk 1.8

android sdk

android系统4.0以上

手机需要连接电脑

## 使用方法

1. 手机连接电脑
2. cmd运行adb devices，确认电脑是否有adb环境且手机是否连接电脑成功
3. 运行com.ly.gui.MonitorFrame该类即可（也可直接用maven打包成jar运行，可以用exe4j把jar包打包成exe文件）

## 主要功能

### 首页：

![image]()

### CPU监控：

cpu监控主要监控数据：总使用率， app使用率， app cpu平均占用率，监听次数

目前实时图上面只加入了总使用率， app使用率，其他数据保存在csv文件中，需要可自取

![image]()


### 内存监控：

mem监控主要监控两个:占用内存，占用内存比

![image]()



### 网络监控：

net监控主要监控：接收数据，传输数据, 接收数据平均值，传输数据平均值， 总监控次数

图中未有的数据可去csv文件中查找

![image]()
