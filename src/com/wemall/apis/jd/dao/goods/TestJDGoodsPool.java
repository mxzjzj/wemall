package com.wemall.apis.jd.dao.goods;

import java.util.List;

import org.junit.Test;

public class TestJDGoodsPool {

	@Test
	public void testGetPoolNum() {
		JDGoodsPool jdGP = new JDGoodsPool();
		System.out.println(jdGP.getPoolNum("手机"));
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testGetPoolGoodsSkuId() {
		JDGoodsPool jdGP = new JDGoodsPool();
		
		List lists = jdGP.getPoolGoodsSkuId("5003");
		System.out.println(lists.size());
		for (int i = 0; i < 10; i++) {
			String str = (String) lists.get(i);
			System.out.println(str);
		}
	}
	
	@Test
	public void testGetSkuid(){
		JDGoodsPool jdGP = new JDGoodsPool();
		System.out.println(jdGP.getPoolGoodsSkuIdByCategory("655"));
	}
}
