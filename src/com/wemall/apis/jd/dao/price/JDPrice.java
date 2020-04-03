package com.wemall.apis.jd.dao.price;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Repository;
import com.wemall.apis.jd.dao.util.JDBase;
import com.wemall.apis.jdutil.BigDecimalUtil;

/**
 * 
 * @author zhangjun
 * 价格API接口
 *  *
 */

@Repository("jDPriceDao")
public class JDPrice extends JDBase
{
  //5.1 批量查询京东价价格
  private String queryJDPrice = "https://bizapi.jd.com/api/price/getJdPrice";

  //5.2 批量查询协议价价格
  private String queryProtocolPrice = "https://bizapi.jd.com/api/price/getPrice";

  //5.3 批量查询商品售卖价
  private String querySellPrice = "https://bizapi.jd.com/api/price/getSellPrice";

  public String getProtocolPrice(String skuId)
  {
    Map createMap = new HashMap();
    String id = skuId.split(",")[0];
    createMap.put("token", accessToken);
    createMap.put("sku", id);

    JSONArray jsonResult = returnJDArray(this.querySellPrice, createMap);
    if (jsonResult != null) {
      JSONObject json = jsonResult.getJSONObject(0);
      return json.getString("price");
    }
    return null;
  }

  public String getJDPrice(String skuId)
  {
    Map createMap = new HashMap();
    String id = skuId.split(",")[0];
    createMap.put("token", accessToken);
    createMap.put("sku", id);

    JSONArray jsonResult = returnJDArray(this.querySellPrice, createMap);
    if (jsonResult != null) {
      JSONObject json = jsonResult.getJSONObject(0);
      String price = json.getString("jdPrice");
      String originalPrice = json.getString("price");

      BigDecimal newprice = new BigDecimal(price);// 京东价格
      BigDecimal jdprice = new BigDecimal(price);
      BigDecimal bei = new BigDecimal("1.15");
      BigDecimal neworiginalPrice = new BigDecimal(originalPrice);// 京东协议价
      BigDecimal signPrice = new BigDecimal(100);
      BigDecimal grossmargin = BigDecimalUtil.mul(signPrice, BigDecimalUtil.div2(BigDecimalUtil.sub(newprice, neworiginalPrice), newprice, 2), 0);
      BigDecimal isPrice = new BigDecimal(5);
      BigDecimal truePrice = new BigDecimal(0.05D);

     /* if (grossmargin.compareTo(isPrice) == -1) {
        newprice = BigDecimalUtil.add(neworiginalPrice, BigDecimalUtil.mul(truePrice, newprice), 2);
        grossmargin = new BigDecimal(5);
      }*/

      newprice = BigDecimalUtil.add(newprice, BigDecimalUtil.mul(truePrice, newprice), 2);
      return newprice.toString()+"; price: "+price;
    }
    return null;
  }

  public Map<String, String> getAllPrice(String skuId)
  {
    Map createMap = new HashMap();
    String id = skuId.split(",")[0];
    createMap.put("token", accessToken);
    createMap.put("sku", id);

    JSONArray jsonResult = returnJDArray(this.querySellPrice, createMap);
    if (jsonResult != null) {
      JSONObject json = jsonResult.getJSONObject(0);
      Map map = new HashMap();
      map.put("jdPrice", json.getString("jdPrice"));
      map.put("price", json.getString("price"));

      //zhangjun
      String price = json.getString("jdPrice");
      String originalPrice = json.getString("price");

      BigDecimal newprice = new BigDecimal(price);// 京东价格
      BigDecimal jdprice = new BigDecimal(price);
      BigDecimal bei = new BigDecimal("1.15");
      BigDecimal neworiginalPrice = new BigDecimal(originalPrice);// 京东协议价
      BigDecimal signPrice = new BigDecimal(100);
      BigDecimal grossmargin = BigDecimalUtil.mul(signPrice, BigDecimalUtil.div2(BigDecimalUtil.sub(newprice, neworiginalPrice), newprice, 2), 0);
      BigDecimal isPrice = new BigDecimal(5);
      BigDecimal truePrice = new BigDecimal(0.05D);

      if (grossmargin.compareTo(isPrice) == -1) {
        newprice = BigDecimalUtil.add(neworiginalPrice, BigDecimalUtil.mul(truePrice, newprice), 2);
        grossmargin = new BigDecimal(5);
      }
      map.put("zhangjun", newprice);
      //---------------------


      return map;
    }
    return null;
  }
}