package com.jsu.action;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jsu.Iservice.UserService;
import com.jsu.po.Users;
import com.opensymphony.xwork2.ActionSupport;

public class LoginAction extends ActionSupport {

	public String username;
	public String password;

	public String execute(){

		
		if (!username.equals("admin")) {
			super.addFieldError("username", "用户名错误！");
			return ERROR;
		}

		if (!password.equals("123456")) {
			super.addFieldError("password", "密码错误！");
			return ERROR;

		}

//		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
//		      //从容器 接管Bean
//		Users user = (Users) ac.getBean("TUser");
//		      //输出欢迎信息
//		
//		System.out.println( "Hello:" + user.getUser_name() + ";u is in " + user.getUser_id() + " ;");
		
		return SUCCESS;
	}

	public void validate() {
		
		if (username == null || username.length() == 0) {
			super.addActionError("用户名不能为空");

		}
		
		if (password == null || password.length() == 0) {
			super.addActionError("密码不能为空");

		}
		//sadas

	}
	
	@Test
	public void testName() throws Exception {


		ApplicationContext context= new ClassPathXmlApplicationContext("/applicationContext.xml");  
	    UserService service =(UserService) context.getBean("userService");
	    
		Users user = new Users();  	   
		user.setUser_name("me"); 
		// Save the object
		service.saveUser(user);	
		
	}

}
