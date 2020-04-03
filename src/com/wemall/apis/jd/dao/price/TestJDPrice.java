package com.wemall.apis.jd.dao.price;

import java.util.Map;
import org.junit.Test;

public class TestJDPrice {
	@Test
	  public void testGetJDPrice()
	  {
	    JDPrice jdP = new JDPrice();
	    String price = jdP.getJDPrice("231858");
	    System.out.println(price);
	  }

	  @Test
	  public void testGetProtocolPrice() {
	    JDPrice jdP = new JDPrice();
	    String price = jdP.getProtocolPrice("2245533");
	    System.out.println(price);
	  }

	  @Test
	  public void testGetProtocolPricem() {
	    JDPrice jdP = new JDPrice();
	    Map price = jdP.getAllPrice("1071292");
	    System.out.println(price);
	  }

	  @Test
	  public void testGetSellPrice()
	  {
		  JDPrice jdP = new JDPrice();
		  String price = jdP.getAllPrice("831433").toString();
		  System.out.println(price);
	  }
}
