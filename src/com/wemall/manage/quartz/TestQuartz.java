package com.wemall.manage.quartz;

import org.junit.Test;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import com.wemall.apis.jd.test.base.TestBase;

public class TestQuartz extends TestBase {

	 @Test
	    public void quartz() {
	        try {
	            SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();
	            Scheduler sche = gSchedulerFactory.getScheduler();
	            String job_name = "动态任务调度";
	            System.out.println("【系统启动】开始(每1秒输出一次)...");

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
}
