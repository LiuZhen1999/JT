package com.jt.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jt.anno.CacheFind;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.JedisCluster;

@Component	//将该类交给Spring容器管理
@Aspect		//标识我是一个切面
public class CacheAOP {
	
	@Autowired(required = false)
	private JedisCluster jedis;
	/**
	 * 实现思路:拦截被@CacheFind标识的方法之后利用aop进行缓存的控制
	 * 通知方法: 环绕通知
	 * 实现过程:
	 * 			1:准备查询redis的key  ITEM_CAT_LIST::第一个参数
	 * 			2:@annotation(cacheFind) 动态获取注解的语法
	 * 			  拦截指定注解类型的注解并且将注解的参数对象当做参数进行传递
	 */
	@Around("@annotation(cacheFind)")
	public Object around(ProceedingJoinPoint joinPoint,CacheFind cacheFind) {
		//获取注解里的key
		String key = cacheFind.key();
		//动态获取第一个参数
		//joinPoint.getArgs()获取目标方法的参数值(传入的参数)
		Object firstArg = joinPoint.getArgs()[0].toString();
		key +="::"+firstArg;
		Object result = null;
		//根据key查询redis;
		if(jedis.exists(key)) {
			//根据redis获取数据信息
			String json = jedis.get(key);
			//如何获取返回值类型
			MethodSignature methodSignature =(MethodSignature)joinPoint.getSignature();
			result = ObjectMapperUtil.toObject(json, methodSignature.getReturnType());
			System.out.println("aop查询redis");
		}else {
			//如果可以不存在,查询数据库
			try {
				System.out.println("aop查询sql");
				result=joinPoint.proceed();
				//将数据保存到redis中
				String json = ObjectMapperUtil.toJSON(result);
				int seconds=cacheFind.seconds();
				if(seconds>0) {
					jedis.setex(key, seconds, json);
				}else{
					jedis.set(key, json);
				
				}
				
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//throw new RuntimeException(e);
			}
		}
		
		return result;
	}
	
	
	/*
	//切面  = 切入点表达式 + 通知方法
	//可以理解为 就是一个if判断
	@Pointcut("bean(itemCatServiceImpl)")   //只对特定的某个类有效
	//@Pointcut("within(com.jt.*.ItemCatServiceImpl)")
	//拦截com.jt.service下边的所有类的所有方法并且所有的参数
	//@Pointcut("execution(* com.jt.service..*.*(..))")
	public void joinPoint() {
		
	}
	
	//记录程序的执行状态  获取哪个类,哪个方法执行的
	@Before("joinPoint()")
	public void before(JoinPoint joinPoint) {
		Date date = new Date();
		String strDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		String typeName = joinPoint.getSignature().getDeclaringTypeName();
		String methodName = joinPoint.getSignature().getName();
		System.out.println("我是前置通知");
		System.out.println(strDate+":"+typeName+":"+methodName);
	}
	
	//定义环绕通知  控制程序的执行过程
	@Around("joinPoint()")
	public Object around(ProceedingJoinPoint joinPoint) {
		System.out.println("我是环绕通知开始");
		Object result = null;
		try {
			result = joinPoint.proceed();	//执行真实的目标方法
		} catch (Throwable e) {
			
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		System.out.println("我是环绕通知结束");
		return result;
	}*/
	/**
	 * 通知类型:
	 * 		1.前置通知:  在目标方法执行之前执行
	 * 		2.后置通知:	在目标方法执行之后执行	
	 * 		3.异常通知:  在目标方法执行之后发生异常时执行
	 * 		4.最终通知:  在程序执行最后执行的通知方法
	 * 		5.环绕通知:  在目标方法执行前后都要执行的通知方法.
	 * 
	 * 记忆:
	 * 		1.如果要对程序的执行的流程进行控制,则首选环绕通知.  最重要的通知方法
	 * 		2.如果需要对程序的执行的状态进行记录,则使用其他四大通知类型.
	 * 
	 * 
	 * 切入点表达式:
	 * 		bean= Spring容器管理的对象称之为bean
	 * 		1. bean(bean的ID)      只能拦截某个bean的操作.执行通知方法  1个
	 * 		2. within(包名.类名)    按类匹配,类可以有多个
	 * 		//上述的切入点表达式控制的粒度较粗   只能控制到类级别.
	 * 
	 * 		//可以控制到方法参数级别
	 * 		3. execution(返回值类型  包名.类名.方法名(参数列表))  控制粒度较细
	 * 		4. annotation(包名.注解名)  按照指定的注解进行匹配.
	 * 
	 */
}
