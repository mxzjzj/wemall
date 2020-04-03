package com.wemall.apis.jd.dao.goods;

import org.junit.Test;

public class TestJDGoodsSearch {

	@Test
	public void testSearchByCatId() {
		JDGoodsSearch jdGS = new JDGoodsSearch();
		jdGS.searchByCatId("2676");
	}
}
