# mavis-boot-common
common use case for springboot
1. 统一数据返回格式   
2. 全局异常处理：业务异常、参数校验异常...   
3. validation+aop实现校验失败统一处理
4. aop记录controller，特定注解标注的日志（入参，返回值，执行时间，执行人，方法详情、类详情）
5. spring StopWatch记录方法执行时间
6. RestTemplate整合HttpComponent，okhttp3，WebClient
7. Object Mapper:mapstruct(compile),orika(runtime)
