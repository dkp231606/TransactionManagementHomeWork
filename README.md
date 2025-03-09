## 银行交易管理项目Demo
   仅适用于HSBC Home Work

## 项目介绍
该项目是一个银行交易系统demo，实现一个简单的银行交易功能，提供增删改查功能，支持银行交易信息的创建，查询，分页查询，交易信息修改，删除功能。

## 项目方案

* 采用前后端分离的模式
* 后端采用SpringBoot
* 数据存储采用内存H2数据库（暂不支持持久化）
* 缓存采用Spring Cache（提升查询性能）
* 遵循RESTFUL API 设计规则
* 采用JMeter进行性能压力测试
* 采用Maven进行项目管理
* 采用Docker进行容器化部署

## 核心技术栈

| 技术栈          | 版本     |
|--------------|--------|
| Java         | 21     |
| Spring       | 6.1.15 |
| Spring Boot  | 3.2.8  |
| JPA          | 3.2.8  |
| Spring Cache |        |
| Swagger      | 3      |


## 项目Demo地址
项目部署在阿里云服务器（2vCPU/4GiB-ESSD云盘/50GiB）上，目前暂时只支持swagger访问验证相关api接口，前台界面功能暂未支持。

swagger地址：http://47.122.0.113/swagger-ui/index.html


## 工程结构
``` 
HSBC
├─src
│  ├─main
│  │  ├─docker
│  │  ├─java
│  │  │  └─com
│  │  │      └─hometask
│  │  │          └─dkp
│  │  │              └─hsbctransactionmanagement
│  │  │                  ├─config
│  │  │                  ├─controller
│  │  │                  ├─entity
│  │  │                  ├─exception
│  │  │                  ├─repository
│  │  │                  └─service
│  │  └─resources
│  │      ├─static
│  │      └─templates
│  └─test
│      └─java
│          └─com
│              └─hometask
│                  └─dkp
│                      └─hsbctransactionmanagement
│                          ├─controller
│                          ├─serviceImpl
│                          └─StressTest
```

## 数据库设计
简易Demo系统采用H2数据库，使用内存模式运行。

交易信息采用如下字段，详见src/main/java/com/hometask/dkp/hsbctransactionmanagement/entity/Transaction.java
* 字段：id（Long）              备注：系统主键id，交易记录唯一标识
* 字段：transactionId（String） 备注：交易编号，校验交易是否重复(不能为空，字符长度在1~100之间)
* 字段：sourceAccountId（Long） 备注：转账账户ID（不能为空，数值必须为正数）
* 字段：targetAccountId（Long） 备注：到账账户ID（不能为空，数值必须为正数）
* 字段：amount（BigDecimal）    备注： 转账金额（不能为空，金额必须为两位小数）
* 字段：description（String）   备注：银行交易备注信息(不能为空，字符长度在1~100之间)
* 字段：createTime（Long） 备注：交易创建时间（ms） 
* 字段：updateTime（Long） 备注：交易更新时间（ms）

## API设计
项目API遵循RESTful API设计原则，完成交易系统的增删改查等功能

基础路径：/api/transaction

1）创建交易信息
* 请求方式：POST
* 请求地址：/api/transaction
* 请求参数：
- **请求参数**：
    - `transactionId`（必填）：交易编号，校验交易是否重复
    - `sourceAccountId`（必填）：转账账户ID
    - `targetAccountId`（必填）：到账账户ID。
    - `amount`（必填）：转账金额
    - `description`（必填）：银行交易备注信息
- **响应内容**：返回创建成功的交易记录的对象，或发生异常返回异常错误信息。

2）删除交易信息
* 请求方式：DELETE
* 请求地址：/api/transaction/{id}
* 请求参数：
- **请求参数**：
    - `id`（必填）：需要删除交易信息的id
- **响应内容**：在删除成功后，返回成功对象，或发生异常返回异常错误信息。

3）修改交易信息
* 请求方式：PUT
* 请求地址：/api/transaction/{id}
* 接口参数：
    - `id`（必填）：需要修改交易信息的id
* 请求参数：
- **请求参数**：
    - `sourceAccountId`（必填）：转账账户ID
    - `targetAccountId`（必填）：到账账户ID。
    - `amount`（必填）：转账金额
    - `description`（必填）：银行交易备注信息
- **响应内容**：返回修改成功的交易记录的对象，或发生异常返回异常错误信息。

4）获取交易信息
* 请求方式：GET
* 请求地址：/api/transaction/{id}
* 接口参数：
    - `id`（必填）：需要获取交易信息的id
- **响应内容**：返回获取成功的交易记录对象，或发生异常返回异常错误信息。

5）获取交易分页查询结果
* 请求方式：GET
* 请求地址：/api/transaction
* 请求参数：
    - `nums`：当前页码
    - `size`：每页显示记录
- **响应内容**：返回获取成功的交易分页记录对象。

## 可靠性设计
异常处理
* 使用Spring Validation对输入参数进行验证，确保数据的合法性。
* 在 Controller层统一处理异常，使用 @ControllerAdvice 和 @ExceptionHandler 注解捕获不同类型的异常，并返回相应的错误信息给客户端。

## 性能设计
* 采用Spring Data JPA Pageable接口实现分页查询，提高数据查询效率
* 采用Spring Cache缓存，提高系统查询效率

## 测试设计
* 通过UT单元测试覆盖所有后端服务接口业务场景
* 通过swagger api进行api接口功能验证及覆盖率验证
* 使用JMeter进行性能压力测试验证

测试用例

1）创建交易信息
* 参数校验失败，则创建失败
* 参数校验成功，则创建成功
* 创建重复的transactionId交易，创建失败，报错提示已存在

2）删除交易信息
* 删除交易id存在，删除成功
* 删除交易id不存在，删除失败，提示id不存在
* 删除交易成功后，缓存也同步删除

3）修改交易信息
* 修改交易id存在，修改成功
* 修改交易id不存在，修改失败，返回提示信息
* 修改交易成功后，缓存也同步删除

4）获取交易信息
* 查询交易id存在，查询成功
* 查询交易id不存在，查询失败，返回提示信息
* 重复查询相同id，除第一次，后面都是查询缓存（通过观察db日志信息）

5）获取交易分页查询结果
* 查询正常，返回结果
* 重复查询相同入参，除第一次，后面都是查询缓存（通过观察db日志信息）

## 安装部署
采用Docker容器部署

Step1：构建Docker镜像
```sh
docker build -t hsbc-transaction-management:v1.3 .
```
![img.png](image\dockerBuild.png)
Step2：启动Docker容器
 ```sh
docker run -d -p 80:80 hsbc-transaction-management:v1.3
 ```

![img.png](image\runDocker.png)

## 测试验证
1) UT单元测试结果

![img.png](image\utTestResult.png)
2) 代码覆盖率验证结果

![img.png](image\coverageTestResult.png)


3) 性能测试验证结果

* 使用压测代码，模拟高并发请求，模拟用户数200，每人执行操作250次，验证结果成功率100%，QPS为2564（50000/19.5）。

压测代码路径：com/hometask/dkp/hsbctransactionmanagement/StressTest/StressTest.java

![img.png](image\stressTestbyCode.png)

* 使用JMeter进行压测结果
模拟用户并发数100，每人交易操作100次，测试创建，更新，删除，查询和分页查询交易信息接口。

JMeter压测脚本存在位置： Result/Jmeter测试脚本/Test Plan.jmx

JMeter压测结果报告存放位置：
Result/JMeter结果报告/report_result

压测结果：

![img.png](image\JMeterResult.png)


## 下一步工作
* 实现前端页面显示功能
* 使用布隆过滤器提高查询接口性能，避免缓存穿透
* 细化分页查询接口，实现根据不同字段进行筛选过滤，提高查询效率
* 增加更多场景的测试
