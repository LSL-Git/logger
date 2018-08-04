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
