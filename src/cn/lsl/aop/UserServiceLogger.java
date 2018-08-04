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
