package com.jt.anno;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)	//运行期有效
@Target(ElementType.METHOD)			//该注解对方法有效
public @interface CacheFind {
	
	public String key();				//定义用户的key
	public int seconds()  default 0;	//定义超时时间
}