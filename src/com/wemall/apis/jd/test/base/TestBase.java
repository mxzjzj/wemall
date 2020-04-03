package com.wemall.apis.jd.test.base;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class TestBase {

	public static ClassPathXmlApplicationContext context;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// new
		// String[]{"classpath:applicationContext-configuration.xml"","classpath:applicationContext-security.xml"}
		String[] conf = { "applicationContext-configuration.xml", "applicationContext-security.xml"};
		context = new ClassPathXmlApplicationContext(conf);
		context.start();
		System.out.println("服务启动成功!");
	}

	
}
