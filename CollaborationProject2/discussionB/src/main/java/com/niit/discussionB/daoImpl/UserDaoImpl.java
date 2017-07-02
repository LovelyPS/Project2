package com.niit.discussionB.daoImpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.niit.discussionB.dao.UserDao;
import com.niit.discussionB.model.User;

@EnableTransactionManagement
@Repository("userDao")
public class UserDaoImpl implements UserDao
{
	@Autowired
	private SessionFactory sessionFactory;
	
	public UserDaoImpl(SessionFactory sessionFactory)
	{
		
		this.sessionFactory = sessionFactory;
	}
	

	public UserDaoImpl() {
		super();
	}

	@Transactional
	public User get(String userName) 
	{
		User user =  (User)sessionFactory.getCurrentSession().get(User.class, userName);
		return user;
	}

	@Transactional
	public boolean save(User u)
	{
		Session s=sessionFactory.openSession();
		
		s.save(u);
		
		s.close();
		return true;
		

	}

	@Transactional
	public boolean update(User u) 
	{
		try
		{
			sessionFactory.getCurrentSession().saveOrUpdate(u);
			
			return true;
		}
		catch(Exception ex)
		{
			
			ex.printStackTrace();
			return false;
		}

	}
    
	@Transactional
	public List<User> list() {
		
		
		Query query = sessionFactory.getCurrentSession().createQuery("FROM User");
		
		return (List<User>)query.list();
	}

	@Transactional
	public boolean validate(String userName, String password) {
		
		try
		{
			User user =  (User)sessionFactory.getCurrentSession().get(User.class, userName);
            
			if(user.getPassword().equals(password))
			{
				user.setErrorCode("200");
				user.setErrorMessage("User Not Found");
				/*sessionFactory.getCurrentSession().setAttribute("username", user.getUsername());
				sessionFactory.getCurrentSession().setAttribute("role", user.getRole());
				sessionFactory.getCurrentSession().setAttribute("isLoggedIn", "true");*/
				return true;
			}
			else
			{
				user.setErrorCode("100");
				user.setErrorMessage("Password is incorrect");
				return false;
			}
		} catch(Exception ex)
		{
			User user = new User();
			user.setErrorCode("100");
			user.setErrorMessage("Username not found");
			return false;
		}
	}

}
