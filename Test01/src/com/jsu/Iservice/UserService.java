package com.jsu.Iservice;

import java.sql.SQLException;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.jsu.dao.IUserDaoImpl;
import com.jsu.po.Users;
import com.jsu.service.IUserService;

public class UserService implements IUserService{
	
	private IUserDaoImpl user_imple;
	
	
	public void setIUserDaoImpl(IUserDaoImpl iUserDaoImpl) {
		this.user_imple = iUserDaoImpl;
	}
	
	@Override
	public void saveUser(Users user) throws SQLException {
		
		user_imple.saveUser(user);

	}
	
	@Override
	public void delUser(Users user) throws SQLException {
		
		user_imple.delUser(user);

	}
	
	@Override
	public void editUsre(Users user) throws SQLException {
		
		user_imple.editUsre(user);
		
	}
	
	@Override
	public Users getUserById(int id) throws SQLException {

		return user_imple.getUserById(id);
	}

}
