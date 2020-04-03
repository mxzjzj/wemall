package com.wemall.apis.jd.dao.goods;

import java.util.Map;

import org.junit.Test;

public class TestJDGoodsClassify {
	@Test
	public void testGetCategory() {
		JDGoodsClassify jdGC = new JDGoodsClassify();

		String level = jdGC.getCategoryCatClass("9987");
		System.out.println(level);
	}

	@Test
	public void testGetCategorys() {
		JDGoodsClassify jdGC = new JDGoodsClassify();
		Map maps = jdGC.getCategorys("1", "5000", null, "0");

		System.out.println(maps);
	}

	@Test
	public void testGetSimilarSku() {
		JDGoodsClassify jdGC = new JDGoodsClassify();
		jdGC.getSimilarSku("332040");
	}

}
