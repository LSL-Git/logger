# logger

**SSM 框架学习 (Spring篇)**

一、我对AOP的理解   

什么是切面编程：我们把一段程序运行流程比喻成一条水果大小分拣流水线，而AOP的思想就是可以在这个流水线的某个点设置一个检查点，或者说大小过滤器，不同的过滤器能过滤不同大小的水果（过滤器表示一个单独的功能或切入的代码），到这里可能就与现实有些不同了，当有能穿过这个过滤器的水果出现时，穿过过滤器的是这个水果的一个分身，过滤器这边会产生一个结果，而对主流水线不会产生影响（简单的说就是这部分切入的代码不会影响源代码也不用修改源代码），而这个过滤器还能够用于分拣其他相同或者不同的水果分拣线并统一管理。（以上就是个人对切面编程的理解，已经想不到更通俗的解释了。其他参考：[https://www.cnblogs.com/Wolfmanlq/p/6036019.html]  [https://blog.csdn.net/kouryoushine/article/details/77504222])

二、AOP基本用法


1.用户实体类User.java
```
package cn.lsl.entity;

/**
 * 用户实体类
 * 
 * @author LSL
 * 
 */
public class User{

	private Integer id; 		// 用户id
	private String username;	// 用户名
	private String password;	// 用户密码
	private String email;		// 用户邮箱

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
```

2.定义持久层接口UserDao.java
```
package cn.lsl.dao;

import cn.lsl.entity.User;

/**
 * 增加dao接口,定义了所需的持久化方法
 * 
 * @author LSL
 *
 */
public interface UserDao {
	
	public void save(User user);
}
```

3.实现持久层接口UserDaoImp.java
```
package cn.lsl.dao.impl;

import cn.lsl.dao.UserDao;
import cn.lsl.entity.User;

/**
 * 用户DAO,类,实现了IDAO接口,负责User类的持久化操作
 * 
 * @author LSL
 *
 */
public class UserDaoImpl implements UserDao {

	public void save(User user) {
		System.out.println("保存用户信息到数据库");
	}
}
```

4.定义业务接口UserService.java
```
package cn.lsl.service;

import cn.lsl.entity.User;

/**
 * 用户业务接口,定义了所需的业务方法
 * 
 * @author LSL
 *
 */
public interface UserService {

	public void addNewUser(User user);
}
```

5.实现业务接口UserServiceImp.java
```
package cn.lsl.service.impl;

import cn.lsl.dao.UserDao;
import cn.lsl.entity.User;
import cn.lsl.service.UserService;

/**
 * 用户业务类,实现了对User功能的业务管理
 * 
 * @author LSL
 *
 */
public class UserServiceImpl implements UserService {

	/**
	 * 声明接口类型的引用.和具体实现类解耦合
	 */
	private UserDao dao;
	
	/**
	 * dao 属性的setter访问器,会被Spring调用,实现设值注入
	 * 
	 * @param dao
	 */
	public void setDao(UserDao dao) {
		this.dao = dao;
	}
	
	/**
	 * 调用用户的dao方法保存用户信息
	 */
	public void addNewUser(User user) {
		dao.save(user);
	}

}
```

6.定义切入方法UserServiceLogger.java
```
package cn.lsl.aop;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;

public class UserServiceLogger {

	private static final Logger log = Logger.getLogger(UserServiceLogger.class);
	
	/**
	 * 前置增强
	 * @param jp
	 */
	public void before(JoinPoint jp) { // JoinPoint 连接点对象
		log.info("调用" + jp.getTarget() + " 的 " + jp.getSignature().getName()
				+ " 方法,方法入参:" + Arrays.toString(jp.getArgs())); // jp.getArgs()连接点方法参数数组
	}
	
	/**
	 * 后置增强
	 * @param jp
	 * @param result
	 */
	public void afterReturning(JoinPoint jp, Object result) {
		// 连接点所在目标类
		log.info("调用" + jp.getTarget() + " 的 " + jp.getSignature().getName() // 连接点方法信息
				+ " 方法,方法返回值: " + result);
	}
}
```

7.配置Spring以及切入点
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:aop="http://www.springframework.org/schema/aop"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
	http://www.springframework.org/schema/beans/spring-beans-3.2.xsd
	http://www.springframework.org/schema/aop
	http://www.springframework.org/schema/aop/spring-aop-3.2.xsd">
	
	<bean id="dao" class="cn.lsl.dao.impl.UserDaoImpl"/>
	
	<bean id="service" class="cn.lsl.service.impl.UserServiceImpl">
		<property name="dao" ref="dao"></property>
	</bean>
	<!-- 声明增强方法所在的Bean -->
	<bean id="theLogger" class="cn.lsl.aop.UserServiceLogger"></bean>
	<!-- 切面配置 -->
	<aop:config>
		<!-- 
		public * addNewUser(entity.User)： “*”表示匹配所有类型的返回值。
		public void *(entity.User)： “*”表示匹配所有方法名。
		public void addNewUser(..)： “..”表示匹配所有参数个数和类型。
		* com.service.*.*(..)：匹配com.service包下所有类的所有方法。
		* com.service..*.*(..)：匹配com.service包及其子包下所有类的所有方法
		 -->
		<!-- 定义切入点 -->
		<aop:pointcut expression="execution(public void addNewUser(cn.lsl.entity.User))"
			id="pointcut"/>
		<!-- 引用包含增强方法的Bean -->
		<aop:aspect ref="theLogger">
			<!-- 将before()方法定义为前置增强,并引用pointcut切入点 -->
			<aop:before method="before" pointcut-ref="pointcut"/>
			<!-- 将afterReturning()方法定义为后置增强,并引用pointcut切入点 -->
			<!-- 通过returning属性定义名为result的参数注入返回值 -->
			<aop:after-returning method="afterReturning"
				pointcut-ref="pointcut" returning="result"/>
		</aop:aspect>
	</aop:config>
</beans>
```

8.编写测试类(我用的是Junit 4测试，不一定要用相同的测试工具)
```
package cn.lsl.test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.lsl.entity.User;
import cn.lsl.service.UserService;

public class AopTest {

	@Test
	public void aopTest() {
		ApplicationContext context =  new ClassPathXmlApplicationContext("applicationContext.xml");
		UserService service = (UserService) context.getBean("service");
		
		User user = new User();
		user.setId(1);
		user.setUsername("test2");
		user.setPassword("test123");
		user.setEmail("110@China.com");
		
		service.addNewUser(user);
	}
}
```

以上就是使用AOP打印log的完整代码。



@Author 瞌睡虫   
@mybatis-3.2.2   
@Database: mysql 5.7.15   
@Tool: MyEclipse
