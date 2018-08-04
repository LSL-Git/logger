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
