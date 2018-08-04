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
