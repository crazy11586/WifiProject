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

		System.out.println("OK    ");
//		if (!username.equals("admin")) {
//			super.addFieldError("username", "�û�������");
//			return ERROR;
//		}
//
//		if (!password.equals("123456")) {
//			super.addFieldError("password", "�������");
//			return ERROR;
//
//		}
//	
//		Session session = HibernateSessionFactory.getSession();
//
//		Transaction tx = null;
//        try
//        {
//             // Begin transaction
//             tx = session.beginTransaction();
//             
//             // Create a Product object and set its property
//             Users product = new Users();
//             product.setUser_name("yes");
//            
//             // Save the object
//             session.save(product);
//             // Commin
//             tx.commit();
//        }
//        catch (Exception e)
//        {
//             if (tx != null)
//             {
//                  tx.rollback();
//             }
//             try
//             {
//                  // Spread the exception
//                  throw e;
//             }
//             catch (Exception e2)
//             {
//                  e2.printStackTrace();
//             }
//        }
//        finally
//        {
//             // Close the session
//             session.close();
//        }
//		
		
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		      //������ �ӹ�Bean
		Users user = (Users) ac.getBean("TUser");
		      //�����ӭ��Ϣ
		
		System.out.println( "Hello:" + user.getUser_name() + ";u is in " + user.getUser_id() + " ;");
		
//		try {
//			testName();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		return SUCCESS;
	}

	public void validate() {
		
		if (username == null || username.length() == 0) {
			super.addActionError("�û�������Ϊ��");

		}
		
		if (password == null || password.length() == 0) {
			super.addActionError("���벻��Ϊ��");

		}

	}
	
	@Test
	public void testName() throws Exception {


//		ApplicationContext context= new ClassPathXmlApplicationContext("/applicationContext.xml");  
//	    UserService service =(UserService) context.getBean("userService");  
//		 
//		Users user = new Users();  
//		   
//		user.setUser_name("me"); 
//		 
//		service.saveUser(user);
//		
		//Calendar calendar = new Calendar(new Date().getTimezoneOffset());
	
		String weeks[] = {"������","����һ","���ڶ�","������","������","������","������"};
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		System.out.println(weeks[calendar.get(Calendar.DAY_OF_WEEK)-1]);
		System.out.println(calendar.get(Calendar.HOUR_OF_DAY));
		
		
	}

}