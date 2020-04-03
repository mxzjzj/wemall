package com.wemall.apis.jd.dao.inventory;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TestJDInventory {

	@Test
	public void testJDOrderInventory() {
		JDInventory jdInventory = new JDInventory();
		String x=jdInventory.JDOrderInventory("[{skuId:4516602,num:101}]", "12_984_3381");
		System.out.println(x);
	}

	@Test
	public void testJDArticleInventory() {
		JDInventory jdInventory = new JDInventory();
		String x = jdInventory.JDOrderInventory("[{skuId:1028463148,num:1}]", "12_984_3381");
		System.out.println(x);
	}

	@Test
	public void testJDArticleInventoryByList() {
		JDInventory jdInventory = new JDInventory();
		List lists = new ArrayList();
		lists.add("214595");
		lists.add("859646");
		lists.add("859632");
		lists.add("736342");
		lists.add("394306");
		lists.add("322370");
		List l = jdInventory.JDArticleInventoryByList(lists, "12_984_3381");
		System.out.println(l);
	}
}
