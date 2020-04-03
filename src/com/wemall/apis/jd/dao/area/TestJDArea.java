package com.wemall.apis.jd.dao.area;

import org.junit.Test;

public class TestJDArea {
	
	@Test
	public void testGetTwoAreaId(){
		JDArea area=new JDArea();
		System.out.println("所有一级地址:"+area.getAllOneArea());
		System.out.println("二级地址："+area.getAllTwoAreaByParentId("12"));
		System.out.println("三级地址"+area.getAllThreeAreaByParentId("13989"));
		System.out.println("town:"+area.getAllFourAreaByParentId("13989"));
	}


}
