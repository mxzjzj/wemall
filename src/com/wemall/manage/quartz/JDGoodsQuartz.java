package com.wemall.manage.quartz;

import javax.annotation.Resource;

import com.wemall.apis.jd.helper.JdGoodsHelper;

public class JDGoodsQuartz {

	@Resource(name = "jdGoodsHelper")
	private JdGoodsHelper jdGoodsHelper;

	public void execute() throws Exception {
		this.jdGoodsHelper.updateGoods();
	}
	
	
}
