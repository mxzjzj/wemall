package com.wemall.manage.seller.tools;

import com.wemall.core.domain.virtual.CglibBean;
import com.wemall.core.domain.virtual.SysMap;
import com.wemall.core.tools.CommUtil;
import com.wemall.foundation.domain.*;
import com.wemall.foundation.service.IAreaService;
import com.wemall.foundation.service.IGoodsService;
import com.wemall.foundation.service.ITransportService;

import org.apache.commons.lang3.StringUtils;
import org.nutz.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 运送工具组件
 */
@Component
public class TransportTools {

	@Autowired
	private ITransportService transportService;

	@Autowired
	private IAreaService areaService;

	@Autowired
	private IGoodsService goodsService;

	public String query_transprot(String json, String mark) {
		String ret = "";
		List<Map> list = (List) Json.fromJson(ArrayList.class, json);
		if ((list != null) && (list.size() > 0)) {
			for (Map map : list) {
				if (CommUtil.null2String(map.get("city_id")).equals("-1")) {
					ret = CommUtil.null2String(map.get(mark));
				}
			}
		}

		return ret;
	}

	public List<CglibBean> query_all_transprot(String json, int type) throws ClassNotFoundException {
		List cbs = new ArrayList();
		List<Map> list = (List) Json.fromJson(ArrayList.class, json);
		if ((list != null) && (list.size() > 0)) {
			if (type == 0) {
				for (Map map : list) {
					HashMap propertyMap = new HashMap();
					propertyMap.put("city_id", Class.forName("java.lang.String"));
					propertyMap.put("city_name", Class.forName("java.lang.String"));
					propertyMap.put("trans_weight", Class.forName("java.lang.String"));
					propertyMap.put("trans_fee", Class.forName("java.lang.String"));
					propertyMap.put("trans_add_weight", Class.forName("java.lang.String"));
					propertyMap.put("trans_add_fee", Class.forName("java.lang.String"));
					CglibBean cb = new CglibBean(propertyMap);
					cb.setValue("city_id", CommUtil.null2String(map.get("city_id")));
					cb.setValue("city_name", CommUtil.null2String(map.get("city_name")));
					cb.setValue("trans_weight", CommUtil.null2String(map.get("trans_weight")));
					cb.setValue("trans_fee", CommUtil.null2String(map.get("trans_fee")));
					cb.setValue("trans_add_weight", CommUtil.null2String(map.get("trans_add_weight")));
					cb.setValue("trans_add_fee", CommUtil.null2String(map.get("trans_add_fee")));
					cbs.add(cb);
				}
			}
			if (type == 1) {
				for (Map map : list) {
					if (!CommUtil.null2String(map.get("city_id")).equals("-1")) {
						HashMap propertyMap = new HashMap();
						propertyMap.put("city_id", Class.forName("java.lang.String"));
						propertyMap.put("city_name", Class.forName("java.lang.String"));
						propertyMap.put("trans_weight", Class.forName("java.lang.String"));
						propertyMap.put("trans_fee", Class.forName("java.lang.String"));
						propertyMap.put("trans_add_weight", Class.forName("java.lang.String"));
						propertyMap.put("trans_add_fee", Class.forName("java.lang.String"));
						CglibBean cb = new CglibBean(propertyMap);
						cb.setValue("city_id", CommUtil.null2String(map.get("city_id")));
						cb.setValue("city_name", CommUtil.null2String(map.get("city_name")));
						cb.setValue("trans_weight", CommUtil.null2String(map.get("trans_weight")));
						cb.setValue("trans_fee", CommUtil.null2String(map.get("trans_fee")));
						cb.setValue("trans_add_weight", CommUtil.null2String(map.get("trans_add_weight")));
						cb.setValue("trans_add_fee", CommUtil.null2String(map.get("trans_add_fee")));
						cbs.add(cb);
					}
				}
			}
		}

		return cbs;
	}

	public float cal_goods_trans_fee(String trans_id, String type, String weight, String volume, String city_name) {
		Transport trans = this.transportService.getObjById(CommUtil.null2Long(trans_id));
		String json = "";
		if (type.equals("mail")) {
			json = trans.getTrans_mail_info();
		}
		if (type.equals("express")) {
			json = trans.getTrans_express_info();
		}
		if (type.equals("ems")) {
			json = trans.getTrans_ems_info();
		}
		float fee = 0.0F;
		boolean cal_flag = false;
		List<Map> list = (List) Json.fromJson(ArrayList.class, json);
		if ((list != null) && (list.size() > 0)) {
			for (Map map : list) {
				String[] city_list = CommUtil.null2String(map.get("city_name")).split("、");
				for (String city : city_list) {
					if (city.equals(city_name)) {
						cal_flag = true;
						float trans_weight = CommUtil.null2Float(map.get("trans_weight"));
						float trans_fee = CommUtil.null2Float(map.get("trans_fee"));
						float trans_add_weight = CommUtil.null2Float(map.get("trans_add_weight"));
						float trans_add_fee = CommUtil.null2Float(map.get("trans_add_fee"));
						if (trans.getTrans_type() == 0) {
							fee = trans_fee;
						}
						if (trans.getTrans_type() == 1) {
							float goods_weight = CommUtil.null2Float(weight);
							if (goods_weight > 0.0F) {
								fee = trans_fee;
								float other_price = 0.0F;
								if (trans_add_weight > 0.0F) {
									other_price = trans_add_fee * (float) Math.round(Math.ceil(CommUtil
											.subtract(Float.valueOf(goods_weight), Float.valueOf(trans_weight))))
											/ trans_add_fee;
								}
								fee += other_price;
							}
						}
						if (trans.getTrans_type() != 2)
							break;
						float goods_volume = CommUtil.null2Float(volume);
						if (goods_volume <= 0.0F)
							break;
						fee = trans_fee;
						float other_price = 0.0F;
						if (trans_add_weight > 0.0F) {
							other_price = trans_add_fee * (float) Math.round(Math
									.ceil(CommUtil.subtract(Float.valueOf(goods_volume), Float.valueOf(trans_weight))))
									/ trans_add_fee;
						}
						fee += other_price;

						break;
					}
				}
			}
			if (!cal_flag) {
				for (Map map : list) {
					String[] city_list = CommUtil.null2String(map.get("city_name")).split("、");
					for (String city : city_list) {
						if (city.equals("全国")) {
							float trans_weight = CommUtil.null2Float(map.get("trans_weight"));
							float trans_fee = CommUtil.null2Float(map.get("trans_fee"));
							float trans_add_weight = CommUtil.null2Float(map.get("trans_add_weight"));
							float trans_add_fee = CommUtil.null2Float(map.get("trans_add_fee"));
							if (trans.getTrans_type() == 0) {
								fee = trans_fee;
							}
							if (trans.getTrans_type() == 1) {
								float goods_weight = CommUtil.null2Float(weight);
								if (goods_weight > 0.0F) {
									fee = trans_fee;
									float other_price = 0.0F;
									if (trans_add_weight > 0.0F) {
										other_price = trans_add_fee * (float) Math.round(Math.ceil(CommUtil
												.subtract(Float.valueOf(goods_weight), Float.valueOf(trans_weight))))
												/ trans_add_fee;
									}
									fee += other_price;
								}
							}
							if (trans.getTrans_type() != 2)
								break;
							float goods_volume = CommUtil.null2Float(volume);
							if (goods_volume <= 0.0F)
								break;
							fee = trans_fee;
							float other_price = 0.0F;
							if (trans_add_weight > 0.0F) {
								other_price = trans_add_fee * (float) Math.round(Math.ceil(
										CommUtil.subtract(Float.valueOf(goods_volume), Float.valueOf(trans_weight))))
										/ trans_add_fee;
							}
							fee += other_price;

							break;
						}
					}
				}
			}
		}

		return fee;
	}

	public List<SysMap> query_cart_trans(StoreCart sc, String area_id) {
		List sms = new ArrayList();
		Store store = sc.getStore();
		Float free_money = store.getFree_money();
		if (StringUtils.isBlank(area_id)) {
			return sms;
		}
		Area area = this.areaService.getObjById(CommUtil.null2Long(area_id)).getParent();
		String city_name = area.getAreaName();

		float mail_fee = 0.0F;
		float express_fee = 0.0F;
		float ems_fee = 0.0F;
		float total = 0.0f;
		Transport transport = null;
		Goods firstGoods = null;
		for (GoodsCart gc : sc.getGcs()) {
			firstGoods = this.goodsService.getObjById(gc.getGoods().getId());
			if (firstGoods.getGoods_transfee() != 0) {
				continue;
			}
			if (null != transport) {
				break;
			}
			transport = firstGoods.getTransport();
		}
		BigDecimal weightTtotal = new BigDecimal(0);
		BigDecimal volumeTtotal = new BigDecimal(0);
		for (GoodsCart gc : sc.getGcs()) {
			Goods goods = this.goodsService.getObjById(gc.getGoods().getId());
			if (goods.getGoods_transfee() != 0) {
				continue;
			}
			// 总价
			total += multiply(goods.getGoods_current_price(), gc.getCount()).floatValue();
			// 总重量
			weightTtotal = weightTtotal.add(multiply(goods.getGoods_weight(), gc.getCount()));
			// 总体积
			volumeTtotal = volumeTtotal.add(multiply(goods.getGoods_volume(), gc.getCount()));
		}

		if (null != free_money && free_money > 0 && free_money <= total) {
			sms.add(new SysMap("满单免运费", Integer.valueOf(0)));
			return sms;
		}

		if (transport != null) {
			mail_fee = mail_fee + cal_order_trans(transport.getTrans_mail_info(), transport.getTrans_type(),
					weightTtotal, volumeTtotal, city_name);

			express_fee = express_fee + cal_order_trans(transport.getTrans_express_info(), transport.getTrans_type(),
					weightTtotal, volumeTtotal, city_name);

			ems_fee = ems_fee + cal_order_trans(transport.getTrans_ems_info(), transport.getTrans_type(), weightTtotal,
					volumeTtotal, city_name);
		} else {
			mail_fee = mail_fee + CommUtil.null2Float(firstGoods.getMail_trans_fee());

			express_fee = express_fee + CommUtil.null2Float(firstGoods.getExpress_trans_fee());

			ems_fee = ems_fee + CommUtil.null2Float(firstGoods.getEms_trans_fee());
		}

		// for (GoodsCart gc : sc.getGcs()) {
		// Goods goods = this.goodsService.getObjById(gc.getGoods().getId());
		// if (goods.getGoods_transfee() != 0) {
		// continue;
		// }
		// price = null == goods.getGoods_current_price() ? 0f :
		// goods.getGoods_current_price().floatValue();
		// total += price * gc.getCount();
		// if (transport != null) {
		// mail_fee = mail_fee +
		// cal_order_trans(goods.getTransport().getTrans_mail_info(),
		// goods.getTransport().getTrans_type(),
		// multiply(goods.getGoods_weight(), gc.getCount()),
		// multiply(goods.getGoods_volume(), gc.getCount()), city_name);
		//
		// express_fee = express_fee +
		// cal_order_trans(goods.getTransport().getTrans_express_info(),
		// goods.getTransport().getTrans_type(),
		// multiply(goods.getGoods_weight(), gc.getCount()),
		// multiply(goods.getGoods_volume(), gc.getCount()), city_name);
		//
		// ems_fee = ems_fee +
		// cal_order_trans(goods.getTransport().getTrans_ems_info(),
		// goods.getTransport().getTrans_type(),
		// multiply(goods.getGoods_weight(), gc.getCount()),
		// multiply(goods.getGoods_volume(), gc.getCount()), city_name);
		// } else {
		// mail_fee = mail_fee + CommUtil.null2Float(goods.getMail_trans_fee());
		//
		// express_fee = express_fee +
		// CommUtil.null2Float(goods.getExpress_trans_fee());
		//
		// ems_fee = ems_fee + CommUtil.null2Float(goods.getEms_trans_fee());
		// }
		// }
		if ((mail_fee == 0.0F) && (express_fee == 0.0F) && (ems_fee == 0.0F)) {
			sms.add(new SysMap("卖家承担", Integer.valueOf(0)));
		} else {
			sms.add(new SysMap("平邮[" + mail_fee + "元]", Float.valueOf(mail_fee)));
			sms.add(new SysMap("快递[" + express_fee + "元]", Float.valueOf(express_fee)));
			sms.add(new SysMap("EMS[" + ems_fee + "元]", Float.valueOf(ems_fee)));
		}
		return sms;
	}

	private float cal_order_trans(String trans_json, int trans_type, Object goods_weight, Object goods_volume,
			String city_name) {
		float fee = 0.0F;
		if (StringUtils.isBlank(trans_json)) {
			return fee;
		}
		boolean cal_flag = false;
		List<Map> list = (List) Json.fromJson(ArrayList.class, trans_json);
		if ((list != null) && (list.size() > 0)) {
			for (Map map : list) {
				String[] city_list = CommUtil.null2String(map.get("city_name")).split("、");
				for (String city : city_list) {
					if ((city.equals(city_name)) || (city_name.indexOf(city) == 0)) {
						cal_flag = true;
						float trans_weight = CommUtil.null2Float(map.get("trans_weight"));
						float trans_fee = CommUtil.null2Float(map.get("trans_fee"));
						float trans_add_weight = CommUtil.null2Float(map.get("trans_add_weight"));
						float trans_add_fee = CommUtil.null2Float(map.get("trans_add_fee"));

						switch (trans_type) {
						case 0:
							// 按照件数来收运费
							fee = trans_fee;
							return fee;
						case 1:
							// 按照重量来计算运费
							fee = trans_fee;
							fee += feeByWeight(goods_weight, trans_add_weight, trans_weight, trans_add_fee);
							return fee;

						case 2:
							// 按照体积来计算运费
							fee = trans_fee;
							fee += feeByVolume(goods_volume, trans_add_weight, trans_weight, trans_add_fee);
							return fee;

						default:
							return fee;
						}
					}
				}
			}
			if (cal_flag) {
				return fee;
			}
			for (Map map : list) {
				String[] city_list = CommUtil.null2String(map.get("city_name")).split("、");
				for (String city : city_list) {
					if (!city.equals("全国")) {
						return fee;
					}
					float trans_weight = CommUtil.null2Float(map.get("trans_weight"));
					float trans_fee = CommUtil.null2Float(map.get("trans_fee"));
					float trans_add_weight = CommUtil.null2Float(map.get("trans_add_weight"));
					float trans_add_fee = CommUtil.null2Float(map.get("trans_add_fee"));

					switch (trans_type) {
					case 0:
						// 按照件数来收运费
						fee = trans_fee;
						return fee;
					case 1:
						// 按照重量来计算运费
						fee = trans_fee;
						fee += feeByWeight(goods_weight, trans_add_weight, trans_weight, trans_add_fee);
						return fee;
					case 2:
						// 按照体积来计算运费
						fee = trans_fee;
						fee += feeByVolume(goods_volume, trans_add_weight, trans_weight, trans_add_fee);
						return fee;

					default:
						break;
					}
				}
			}
		}
		return fee;
	}

	private BigDecimal multiply(BigDecimal big, int num) {
		if (null == big) {
			return new BigDecimal(0);
		}
		return big.multiply(new BigDecimal(num));
	}

	private float feeByWeight(Object goods_weight, float trans_add_weight, float trans_weight, float trans_add_fee) {
		float weight = CommUtil.null2Float(goods_weight);
		if (weight <= 0.0F) {
			return 0l;
		}
		if (weight <= trans_weight) {
			return 0l;
		}
		if (trans_add_weight <= 0.0f) {
			return 0l;
		}
		weight = weight - trans_weight;
		int ceil = (int) Math.ceil(weight);
		return trans_add_fee * ceil / trans_add_weight;
	}

	private float feeByVolume(Object goods_volume, float trans_add_volume, float trans_volume, float trans_add_fee) {
		float volume = CommUtil.null2Float(goods_volume);
		if (volume <= 0.0F) {
			return 0l;
		}
		if (volume <= trans_volume) {
			return 0l;
		}
		if (trans_add_volume <= 0.0f) {
			return 0l;
		}
		volume = volume - trans_volume;
		return trans_add_fee * volume / trans_add_volume;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		String s = "[{\"trans_weight\":1,\"trans_fee\":13.5,\"trans_add_weight\":1,\"trans_add_fee\":2},{\"city_id\":1,\"city_name\":\"沈阳\",\"trans_weight\":1,\"trans_fee\":13.5,\"trans_add_weight\":1,\"trans_add_fee\":2}]";
		List<Map> list = (List) Json.fromJson(ArrayList.class, s);
		for (Map map : list)
			System.out.println(map.get("city_id"));
	}
}
