package com.wemall.apis.jd.dao.goods;

import org.junit.Test;

public class TestJdAreaRestrict {

	@Test
	public void testJdAreaRestrict() {
		JdAreaRestrict jd = new JdAreaRestrict();
		boolean b = jd.jdAreaRestrict("959718", "12", "984", "40035", "52206");
	}
}
