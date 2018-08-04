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
