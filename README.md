# 2018 ThoughtWoks招聘作业

2018 ThoughtWoks招聘作业

created by 孙益超

**Email：**     [sunyichao_believe@yahoo.com](mailto:sunyichao_believe@yahoo.com)

## 作业说明

本程序采用Java实现，`maven`构建：

* `maven`导入即可自动构建
* `test/java/com/yichao/thoughtworks/homework/badminton/TestCase1.java`为题设测试用例，Maven导入可直接运行测试
* `src/main/java/com/yichao/thoughtworks/homework/badminton/Main.java `入口函数，Maven导入后可直接进入运行程序
* `git log`可见程序实现思路

测试用例全部通过

## 程序说明

* 问题域模型可复用：程序采用MVC组合设计模式，问题域模型接口不变，可配合不同的view-controller，或者web应用程序
* 不同时间计价，违约金策略可配置调整：问题域核心`BadmintonField`羽毛球场地类完成订单计价，下订单，撤销订单等核心功能；采用`BadmintonFieldFactory`封装依赖配置文件创建，配置文件可自定义修改，无需修改类文件
* 代码依据配置计价，剔除`if else`大量判断

###	目录结构

工程目录：

* `badminton/common/Constants.java`常量设定
* `badminton/model`问题域模型，包含
  * `BadmintonField`羽毛球场地类
  * `Order`订单类
  * `BadmintonFieldFactory`封装从配置文件组装`BadmintonField`对象
* `badminton/controller`控制器类与识图一一对应，用于解析视图输入，调用相应问题域模型处理
* `Main`程序入口，控制台交互界面

配置文件：

* `src/main/resources/properties/badmintonProp.json`计价策略配置文件

测试用例：

* `test/java/com/yichao/thoughtworks/homework/badminton/model/BadmintonFieldTest.java`为问题域模型`BadmintonField`羽毛球场地测试类
* `test/java/com/yichao/thoughtworks/homework/badminton/TestCase1.java`为题设测试用例1
* `test/java/com/yichao/thoughtworks/homework/badminton/TestCase2.java`为题设测试用例2

###	配置文件说明

`src/main/resources/properties/badmintonProp.json`计价策略配置文件，文件说明如下注释

```json
  {
  "name": "A"//场地名称
  "dayMatrix": [1, 6],//定价策略分：周一～周五，周六～周日
  "dayCancelCostMatrix": [0.5, 0.25],//周一～周五 违约金比例0.5；周六～周日 违约金比例0.25
  "dayTimeMatrix": [[9, 12, 18, 20, 22], [9, 12, 18, 22]],//周一～周五，不同计价时间段9点-12点，12点-18点，18点-20点
  "dayUnitCostMatrix": [
    [30, 50, 80, 60],//对应周一～周五，不同时间段定价；例如9点-12点价格30/时
    [40, 50, 60]],//对应周六～周日，不同时间段定价；
}
```

