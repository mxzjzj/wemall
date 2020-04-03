package com.wemall.manage.quartz;

import org.springframework.beans.factory.annotation.Autowired;

import com.wemall.apis.jd.helper.JdConfigureHelper;

public class GetJdTokenQuartz {

	@Autowired
	private JdConfigureHelper jdConfigureHelper;

	public void execute() throws Exception {
		try {
			this.jdConfigureHelper.update();
		} catch (Exception e) {
			this.jdConfigureHelper.update();
		}
	}
}
