package com.wemall.apis.jd.dao.goods;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class TestJDGoodsInfo {
	@Test
	  public void testCheck()
	  {
	    JDGoodsInfo jdGI = new JDGoodsInfo();
	    List lists = new ArrayList();
	    lists.add("1743571");
	    lists.add("1794933");
	    lists.add("1778536");
	    lists.add("1624885");
	    lists.add("1564163");
	    lists.add("1411049");
	    List skus = jdGI.getCheckSkuId(lists);
	    System.out.println(skus.size());
	  }

	  @Test
	  public void testSkuStates() {
	    JDGoodsInfo jdGI = new JDGoodsInfo();
	    Map maps = jdGI.getSkuState("10700821297");
	    System.out.println((String)maps.get("10700821297"));
	  }

	  @Test
	  public void testGetGoodsParticularInfo() {
	    JDGoodsInfo jdGI = new JDGoodsInfo();
	    Map maps = jdGI.getGoodsParticularInfo("10217121730");

	    System.out.println(maps);
	  }

	  @Test
	  public void testGetSkuState()
	  {
	    JDGoodsInfo jdGI = new JDGoodsInfo();
	    List lists = new LinkedList();
	    lists.add("2808083");
	    lists.add("3034770");
	    lists.add("2870957");
	    lists.add("2794592");
	    lists.add("2935614");
	    lists.add("2839509");
	    lists.add("2758735");
	    Map maps = jdGI.getSkuState("698236");
	    System.out.println(maps.toString());
	  }

	  @Test
	  public void testGetFreight() {
	    JDGoodsInfo jdGI = new JDGoodsInfo();

	    String money = jdGI.getFreight("4337912", "1", "江苏", "无锡市", "滨湖区", "城区");
	    System.out.println(money);
	  }

}
