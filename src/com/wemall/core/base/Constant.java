package com.wemall.core.base;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Constant {

  /* ======================================================= Identifier ======================================================= */
  public static final short ARTICLE_BLOCK_USER_GRADE_VIEW = 1;
  public static final short ARTICLE_BLOCK_USER_GRADE_POST = 2;
  public static final short ARTICLE_BLOCK_USER_GRADE_REPLY = 3;

  public static final short EVALUATIION_CREDIT_GOOD = 1;
  public static final short EVALUATIION_CREDIT_NORMAL = 0;
  public static final short EVALUATIION_CREDIT_BAD = -1;

  public static final short JOINT_BUY_FLAG_NEW = 1;
  public static final short JOINT_BUY_FLAG_JOIN = 2;

  public static final String MAP_BAIDU = "baidu";
  public static final String MAP_GOOGLE = "google";

  public static final short NAVIGATION_FLAG_NEW = 1;
  public static final short NAVIGATION_FLAG_HOT = 2;

  public static final String OPERATOR_ADD = "+";
  public static final String OPERATOR_SUBSTRACT = "-";
  public static final String OPERATOR_LARGE_THAN = ">";
  public static final String OPERATOR_LARGE_EQUAL = ">=";
  public static final String OPERATOR_SMALL_THAN = "<";
  public static final String OPERATOR_SMALL_EQUAL = "<=";

  public static final String PREFIX_ACCOUNT_RECHARGE = "G";
  public static final String PREFIX_ACCOUNT_CASH = "H";
  public static final String PREFIX_CARD_RECHARGE = "C";
  public static final String PREFIX_JOINT = "J";
  public static final String PREFIX_PURCHASE_ORDER = "P";
  public static final String PREFIX_RETURN_ORDER = "R";
  public static final String PREFIX_REFUND = "F";

  public static final String SYSTEM_MODULE_ID = "systemModuleId";
  public static final String SYSTEM_MODULE_CODE = "systemModuleCode";

  public static final String SYSTEM_NAME = "xihao";
  public static final String COOKIE_SESSION_ID = SYSTEM_NAME + "_session_id";
  public static final String COOKIE_CART_SESSION = SYSTEM_NAME + "_cart_session";

  public static final String UPPER_ALPHA = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z";

  public static final short MAX_RECORD_1 = 1;
  public static final short MAX_RECORD_4 = 4;
  public static final short MAX_RECORD_5 = 5;
  public static final short MAX_RECORD_6 = 6;
  public static final short MAX_RECORD_8 = 8;
  public static final short MAX_RECORD_10 = 10;
  public static final short MAX_RECORD_12 = 12;
  public static final short MAX_RECORD_18 = 18;
  public static final short MAX_RECORD_20 = 20;
  public static final short MAX_RECORD_24 = 24;
  public static final short MAX_RECORD_100 = 100;
  public static final short MAX_RECORD_1000 = 1000;
  public static final short MAX_RECORD_10000 = 10000;

  public static final short PAGE_SIZE_1 = 1;
  public static final short PAGE_SIZE_3 = 3;
  public static final short PAGE_SIZE_6 = 6;
  public static final short PAGE_SIZE_9 = 9;
  public static final short PAGE_SIZE_10 = 10;
  public static final short PAGE_SIZE_12 = 12;
  public static final short PAGE_SIZE_15 = 15;
  public static final short PAGE_SIZE_16 = 16;
  public static final short PAGE_SIZE_18 = 18;
  public static final short PAGE_SIZE_20 = 20;

  public static final short PAY_FOR_PURCHASE_ORDER = 1;
  public static final short PAY_FOR_ACCOUNT_RECHARGE = 2;
  public static final short PAY_FOR_CARD_RECHARGE = 3;

  public static final short PAYMENT_RECOMMEND_LEVEL_0 = 0;
  public static final short PAYMENT_RECOMMEND_LEVEL_1 = 1;
  public static final short PAYMENT_RECOMMEND_LEVEL_2 = 2;
  public static final short PAYMENT_RECOMMEND_LEVEL_3 = 3;

  public static final short RESULT_CODE_SUCCESS = 1;
  public static final short RESULT_CODE_SITE_CLOSE = 2;
  public static final short RESULT_CODE_NO_USER = 3;
  public static final short RESULT_CODE_NO_STORE = 4;
  public static final short RESULT_CODE_USER_INVALID = 10;
  public static final short RESULT_CODE_USER_LOCKED = 11;
  public static final short RESULT_CODE_USER_BLACK_IP = 12;
  public static final short RESULT_CODE_USER_BLACK_MOBILE = 13;
  public static final short RESULT_CODE_USER_PASSWORD_CHANGED = 14;
  public static final short RESULT_CODE_USER_NO_SINGLE = 16;
  public static final short RESULT_CODE_USER_NO_AUTHORITY = 19;
  public static final short RESULT_CODE_STORE_STATUS_NONE = 40;
  public static final short RESULT_CODE_STORE_STATUS_CLOSED = 41;
  public static final short RESULT_CODE_STORE_STATUS_AUDITING = 42;
  public static final short RESULT_CODE_STORE_STATUS_REJECTED = 43;
  public static final short RESULT_CODE_WECHAT_OPEN_ONLY = 91;
  public static final short RESULT_CODE_NOT_APP_REQUEST = 99;

  public static final short VISIT_TYPE_CLIENT = 1;
  public static final short VISIT_TYPE_ADMIN = 2;

  /* ======================================================= System ID ======================================================= */
  public static final long ADVERT_TYPE_SCROLL = 1;
  public static final long ADVERT_TYPE_SLIDE = 2;
  public static final long ADVERT_TYPE_IMAGE = 3;
  public static final long ADVERT_TYPE_TEXT = 4;

  public static final long ACCOUNT_LOG_ACTION_RECHARGE = 1;
  public static final long ACCOUNT_LOG_ACTION_CASH = 2;
  public static final long ACCOUNT_LOG_ACTION_PURCHASE_ORDER_PAY = 5;
  public static final long ACCOUNT_LOG_ACTION_PURCHASE_ORDER_CANCEL = 6;
  public static final long ACCOUNT_LOG_ACTION_PURCHASE_ORDER_REMOVE = 7;
  public static final long ACCOUNT_LOG_ACTION_PURCHASE_ORDER_RECEIVE = 8;
  public static final long ACCOUNT_LOG_ACTION_REFUND = 9;
  public static final long ACCOUNT_LOG_ACTION_MANUAL = 10;

  public static final long ACCOUNT_STATUS_UNPAID = 1;
  public static final long ACCOUNT_STATUS_AUDITING = 2;
  public static final long ACCOUNT_STATUS_SUCCESSFUL = 3;
  public static final long ACCOUNT_STATUS_REJECTED = 4;

  public static final long ARTICLE_SYSTEM_1 = 1;
  public static final long ARTICLE_SYSTEM_11 = 11;
  public static final long ARTICLE_SYSTEM_12 = 12;
  public static final long ARTICLE_SYSTEM_13 = 13;
  public static final long ARTICLE_SYSTEM_14 = 14;
  public static final long ARTICLE_SYSTEM_21 = 21;
  public static final long ARTICLE_SYSTEM_22 = 22;
  public static final long ARTICLE_SYSTEM_23 = 23;
  public static final long ARTICLE_SYSTEM_24 = 24;
  public static final long ARTICLE_SYSTEM_31 = 31;
  public static final long ARTICLE_SYSTEM_32 = 32;
  public static final long ARTICLE_SYSTEM_33 = 33;
  public static final long ARTICLE_SYSTEM_34 = 34;
  public static final long ARTICLE_SYSTEM_61 = 61;
  public static final long ARTICLE_SYSTEM_62 = 62;

  public static final long ADVERT_FLOOR_TEMPLATE_UMALL_DEFAULT = 21;
  public static final long ADVERT_FLOOR_TEMPLATE_UGROUP_DEFAULT = 31;

  public static final long BRAND_STATUS_ONSELL = 1;
  public static final long BRAND_STATUS_AUDITING = 2;
  public static final long BRAND_STATUS_REJECTED = 3;

  public static final long CARD_USER = 1;

  public static final long CARD_LOG_ACTION_RECHARGE_CASH = 1;
  public static final long CARD_LOG_ACTION_RECHARGE_CARD = 2;
  public static final long CARD_LOG_ACTION_RECHARGE_GIFT = 3;
  public static final long CARD_LOG_ACTION_PURCHASE_ORDER_PAY = 5;
  public static final long CARD_LOG_ACTION_PURCHASE_ORDER_CANCEL = 6;
  public static final long CARD_LOG_ACTION_PURCHASE_ORDER_REMOVE = 7;
  public static final long CARD_LOG_ACTION_REFUND = 9;
  public static final long CARD_LOG_ACTION_MANUAL = 10;

  public static final long CARD_RECHARGE_WAY_CASH = 1;
  public static final long CARD_RECHARGE_WAY_CARD = 2;

  public static final long CARD_PUBLISH_WAY_ON2ON = 1;
  public static final long CARD_PUBLISH_WAY_ON2OFF = 2;
  public static final long CARD_PUBLISH_WAY_OFF2ON = 3;

  public static final long CARD_STATUS_UNPAID = 1;
  public static final long CARD_STATUS_AUDITING = 2;
  public static final long CARD_STATUS_SUCCESSFUL = 3;
  public static final long CARD_STATUS_REJECTED = 4;

  public static final long CARD_TYPE_RECHARGE = 6;

  public static final long COUPON_PUBLISH_WAY_ON2ON = 1;
  public static final long COUPON_PUBLISH_WAY_ON2OFF = 2;
  public static final long COUPON_PUBLISH_WAY_OFF2ON = 3;

  public static final long COUPON_STATUS_UNUSED = 1;
  public static final long COUPON_STATUS_USED = 2;
  public static final long COUPON_STATUS_RETURNING = 3;
  public static final long COUPON_STATUS_RETURNED = 4;

  public static final long COUPON_TYPE_DEDUCT = 1;
  public static final long COUPON_TYPE_TAKE = 2;

  public static final long EVALUATIION_TYPE_GOODS = 1;

  public static final long FAVORITE_TYPE_GOODS = 1;
  public static final long FAVORITE_TYPE_STORE = 8;
  public static final long FAVORITE_TYPE_CLASSIFICATION = 9;
  public static final long FAVORITE_TYPE_ARTICLE = 10;

  public static final long FORETASTE_STATUS_AUDITING = 1;
  public static final long FORETASTE_STATUS_SUCCESSFUL = 2;
  public static final long FORETASTE_STATUS_REJECTED = 3;

  public static final long GOODS_STATUS_ONSELL = 1;
  public static final long GOODS_STATUS_UNSELL = 2;
  public static final long GOODS_STATUS_VIOLATE = 3;
  public static final long GOODS_STATUS_AUDITING = 4;
  public static final long GOODS_STATUS_REJECTED = 5;

  public static final long INTEGRAL_LOG_ACTION_REGISTER = 1;
  public static final long INTEGRAL_LOG_ACTION_LOGIN = 2;
  public static final long INTEGRAL_LOG_ACTION_WECHAT_SUBSCRIBE = 3;
  public static final long INTEGRAL_LOG_ACTION_PURCHASE_ORDER_BUY = 5;
  public static final long INTEGRAL_LOG_ACTION_PURCHASE_ORDER_EVALUATE = 6;
  public static final long INTEGRAL_LOG_ACTION_PURCHASE_ORDER_DEDUCT = 7;
  public static final long INTEGRAL_LOG_ACTION_PURCHASE_ORDER_CANCEL = 8;
  public static final long INTEGRAL_LOG_ACTION_PURCHASE_ORDER_REMOVE = 9;
  public static final long INTEGRAL_LOG_ACTION_MANUAL = 10;

  public static final long JOINT_ROLE_LEADER = 1;
  public static final long JOINT_ROLE_MEMBER = 2;
  public static final long JOINT_ROLE_ALL = 3;

  public static final long JOINT_STATUS_JOINING = 1;
  public static final long JOINT_STATUS_SUCCESSFUL = 2;
  public static final long JOINT_STATUS_FAILED = 3;
  public static final long JOINT_STATUS_LACKING = 4;

  public static final long PRESTIGE_LOG_ACTION_REGISTER = 1;
  public static final long PRESTIGE_LOG_ACTION_LOGIN = 2;
  public static final long PRESTIGE_LOG_ACTION_ARTICLE_POST = 6;
  public static final long PRESTIGE_LOG_ACTION_ARTICLE_REPLY = 9;
  public static final long PRESTIGE_LOG_ACTION_ARTICLE_TOP_1 = 11;
  public static final long PRESTIGE_LOG_ACTION_ARTICLE_TOP_2 = 12;
  public static final long PRESTIGE_LOG_ACTION_ARTICLE_TOP_3 = 13;
  public static final long PRESTIGE_LOG_ACTION_ARTICLE_PRIME_1 = 16;
  public static final long PRESTIGE_LOG_ACTION_ARTICLE_PRIME_2 = 17;
  public static final long PRESTIGE_LOG_ACTION_ARTICLE_PRIME_3 = 18;
  public static final long PRESTIGE_LOG_ACTION_MANUAL = 20;

  public static final long PAYMENT_COMBINE = 1;

  public static final long PURCHASE_ORDER_STATUS_CANCELLED = 1; //已取消
  public static final long PURCHASE_ORDER_STATUS_UNPAID = 2; //未支付
  public static final long PURCHASE_ORDER_STATUS_REJECTED = 3; //被驳回
  public static final long PURCHASE_ORDER_STATUS_JOINING = 4; //拼团中
  public static final long PURCHASE_ORDER_STATUS_AUDITING = 5; //待审核
  public static final long PURCHASE_ORDER_STATUS_PREPARING = 6; //待发货
  public static final long PURCHASE_ORDER_STATUS_SHIPPED = 8; //已发货
  public static final long PURCHASE_ORDER_STATUS_UNRECEIVED = 10; //送货失败
  public static final long PURCHASE_ORDER_STATUS_RECEIVED = 12; //已收货
  public static final long PURCHASE_ORDER_STATUS_RETURNING = 14; //退货中
  public static final long PURCHASE_ORDER_STATUS_FINISHED = 16; //已完成, 买家已评价
  //public static final long PURCHASE_ORDER_STATUS_ENDED = 18; //已完成, 商家已评价

  public static final long PURCHASE_ORDER_STATUS_RETURN_AUDITING = 20; //退货审核
  public static final long PURCHASE_ORDER_STATUS_RETURN_APPROVED = 22; //同意退货
  public static final long PURCHASE_ORDER_STATUS_RETURN_REJECTED = 24; //拒绝退货
  public static final long PURCHASE_ORDER_STATUS_RETURN_CONFIRMING = 26; //退货确认
  public static final long PURCHASE_ORDER_STATUS_RETURN_FINISHED = 28; //退货完成
  //public static final long PURCHASE_ORDER_RETURN_STATUS_FAILED = 30; //退货失败

  public static final long PURCHASE_ORDER_STATUS_REFUND_AUDITING = 32; //退款审核
  public static final long PURCHASE_ORDER_STATUS_REFUND_SUCCESSFUL = 34; //退款成功
  public static final long PURCHASE_ORDER_STATUS_REFUND_REJECTED = 36; //退款驳回

  public static final long PURCHASE_ORDER_TYPE_GOODS = 1;
  public static final long PURCHASE_ORDER_TYPE_COUPON = 2;

  public static final long RESOURCE_TYPE_ADMIN = 1;

  public static final long SHIPPING_PRICING_COUNT = 1;
  public static final long SHIPPING_PRICING_WEIGHT = 2;
  public static final long SHIPPING_PRICING_VOLUME = 3;

  /* ================================= Resource ================================= */
  public static final long RESOURCE_USHOP_GOODS_MENU = 211;
  public static final long RESOURCE_USHOP_BUSINESS_MENU = 212;
  public static final long RESOURCE_USHOP_CUSTOM_MENU = 213;
  public static final long RESOURCE_USHOP_USER_MENU = 215;

  public static final long RESOURCE_USHOP_GOODS = 221;
  public static final long RESOURCE_USHOP_BRAND = 224;
  public static final long RESOURCE_USHOP_PURCHASE_ORDER = 241;
  public static final long RESOURCE_USHOP_CONSULTATION = 261;
  public static final long RESOURCE_USHOP_REPORT = 263;
  public static final long RESOURCE_USHOP_COMPLAINT = 266;
  public static final long RESOURCE_USHOP_USER = 296;
  public static final long RESOURCE_USHOP_ACCOUNT_RECHARGE = 298;
  public static final long RESOURCE_USHOP_ACCOUNT_CASH = 299;
  public static final long RESOURCE_USHOP_USER_CARD_RECHARGE = 304;

  public static final long RESOURCE_UMALL_GOODS_MENU = 421;
  public static final long RESOURCE_UMALL_STORE_MENU = 422;
  public static final long RESOURCE_UMALL_BUSINESS_MENU = 423;
  public static final long RESOURCE_UMALL_CUSTOM_MENU = 424;
  public static final long RESOURCE_UMALL_USER_MENU = 426;

  public static final long RESOURCE_UMALL_GOODS = 431;
  public static final long RESOURCE_UMALL_BRAND = 434;
  public static final long RESOURCE_UMALL_GOODS_UNIT = 438;
  public static final long RESOURCE_UMALL_STORE = 451;
  public static final long RESOURCE_UMALL_STORE_UPGRADE = 455;
  public static final long RESOURCE_UMALL_PURCHASE_ORDER = 471;
  public static final long RESOURCE_UMALL_CONSULTATION = 491;
  public static final long RESOURCE_UMALL_REPORT = 493;
  public static final long RESOURCE_UMALL_COMPLAINT = 496;
  public static final long RESOURCE_UMALL_USER = 526;
  public static final long RESOURCE_UMALL_ACCOUNT_RECHARGE = 528;
  public static final long RESOURCE_UMALL_ACCOUNT_CASH = 529;
  public static final long RESOURCE_UMALL_USER_CARD_RECHARGE = 534;

  public static final long RESOURCE_UGROUP_GOODS_MENU = 631;
  public static final long RESOURCE_UGROUP_STORE_MENU = 632;
  public static final long RESOURCE_UGROUP_BUSINESS_MENU = 633;
  public static final long RESOURCE_UGROUP_CUSTOM_MENU = 634;
  public static final long RESOURCE_UGROUP_USER_MENU = 636;

  public static final long RESOURCE_UGROUP_GOODS = 641;
  public static final long RESOURCE_UGROUP_BRAND = 645;
  public static final long RESOURCE_UGROUP_GOODS_UNIT = 649;
  public static final long RESOURCE_UGROUP_STORE = 661;
  public static final long RESOURCE_UGROUP_STORE_UPGRADE = 665;
  public static final long RESOURCE_UGROUP_PURCHASE_ORDER = 681;
  public static final long RESOURCE_UGROUP_CONSULTATION = 701;
  public static final long RESOURCE_UGROUP_REPORT = 703;
  public static final long RESOURCE_UGROUP_COMPLAINT = 706;
  public static final long RESOURCE_UGROUP_USER = 736;
  public static final long RESOURCE_UGROUP_ACCOUNT_RECHARGE = 738;
  public static final long RESOURCE_UGROUP_ACCOUNT_CASH = 739;
  public static final long RESOURCE_UGROUP_USER_CARD_RECHARGE = 744;

  public static final long RESOURCE_UJOIN_GOODS_MENU = 1261;
  public static final long RESOURCE_UJOIN_BUSINESS_MENU = 1262;
  public static final long RESOURCE_UJOIN_CUSTOM_MENU = 1263;
  public static final long RESOURCE_UJOIN_USER_MENU = 1266;

  public static final long RESOURCE_UJOIN_GOODS = 1271;
  public static final long RESOURCE_UJOIN_PURCHASE_ORDER = 1291;
  public static final long RESOURCE_UJOIN_JOINT = 1293;
  public static final long RESOURCE_UJOIN_CONSULTATION = 1311;
  public static final long RESOURCE_UJOIN_COMPLAINT = 1316;
  public static final long RESOURCE_UJOIN_USER = 1366;

  public static final long RESOURCE_UBBS_ARTICLE_POST_MENU = 1891;
  public static final long RESOURCE_UBBS_CUSTOM_MENU = 1893;
  public static final long RESOURCE_UBBS_USER_MENU = 1895;

  public static final long RESOURCE_UBBS_ARTICLE_POST = 1901;
  public static final long RESOURCE_UBBS_REPORT = 1943;
  public static final long RESOURCE_UBBS_USER = 1981;

  public static final long ROLE_TYPE_ADMIN = 1;

  public static final long SHIPPING_WAY_MAIL = 1;
  public static final long SHIPPING_WAY_EXPRESS = 2;
  public static final long SHIPPING_WAY_EMS = 3;
  public static final long SHIPPING_WAY_SMS = 4;
  public static final long SHIPPING_WAY_AUTO = 5;

  public static final long STORE_STATUS_NONE = 0;
  public static final long STORE_STATUS_ONSELL = 1;
  public static final long STORE_STATUS_CLOSED = 2;
  public static final long STORE_STATUS_AUDITING = 3;
  public static final long STORE_STATUS_SUCCESSFUL = 4;
  public static final long STORE_STATUS_REJECTED = 5;

  public static final long SUPER_USER = 1;

  public static final long SYSTEM_CLIENT_PC = 1;
  public static final long SYSTEM_CLIENT_WAP = 2;
  public static final long SYSTEM_CLIENT_APP = 3;
  public static final long SYSTEM_CLIENT_ALL = 10;

  public static final long SYSTEM_MODULE_USHARE = 0;
  public static final long SYSTEM_MODULE_USHOP = 2;
  public static final long SYSTEM_MODULE_UMALL = 3;
  public static final long SYSTEM_MODULE_UGROUP = 4;
  public static final long SYSTEM_MODULE_UJOIN = 7;
  public static final long SYSTEM_MODULE_UBBS = 10;

  public static final long TEMPLATE_TO_ADMIN = 1;
  public static final long TEMPLATE_TO_BUYER = 2;
  public static final long TEMPLATE_TO_SELLER = 3;

  public static final long TEMPLATE_TYPE_EMAIL = 1;
  public static final long TEMPLATE_TYPE_SMS = 2;
  public static final long TEMPLATE_TYPE_WECHAT = 3;

  public static final long USER_TYPE_ADMIN = 1;
  public static final long USER_TYPE_ADMIN_SITE = 2;
  public static final long USER_TYPE_BUYER = 3;
  public static final long USER_TYPE_SELLER = 4;

  /* ======================================================= System Code ======================================================= */
  public static final String ADVERT_MAIN_1 = "advert_1";
  public static final String ADVERT_MAIN_2 = "advert_2";
  public static final String ADVERT_MAIN_3 = "advert_3";
  public static final String ADVERT_MAIN_4 = "advert_4";
  public static final String ADVERT_MAIN_5 = "advert_5";
  public static final String ADVERT_MAIN_6 = "advert_6";
  public static final String ADVERT_MAIN_7 = "advert_7";
  public static final String ADVERT_MAIN_8 = "advert_8";
  public static final String ADVERT_MAIN_9 = "advert_9";
  public static final String ADVERT_MAIN_10 = "advert_10";
  public static final String ADVERT_MAIN_11 = "advert_11";
  public static final String ADVERT_MAIN_12 = "advert_12";
  public static final String ADVERT_MAIN_13 = "advert_13";
  public static final String ADVERT_MAIN_14 = "advert_14";
  public static final String ADVERT_MAIN_15 = "advert_15";
  public static final String ADVERT_MAIN_16 = "advert_16";
  public static final String ADVERT_MAIN_17 = "advert_17";
  public static final String ADVERT_MAIN_18 = "advert_18";
  public static final String ADVERT_MAIN_19 = "advert_19";
  public static final String ADVERT_MAIN_20 = "advert_20";

  public static final String ADVERT_FLOOR_TEMPLATE_1 = "1";
  public static final String ADVERT_FLOOR_TEMPLATE_2 = "2";
  public static final String ADVERT_FLOOR_TEMPLATE_WAP_1 = "wap_1";

  public static final String ARTICLE_CATEGORY_SYSTEM = "system";
  public static final String ARTICLE_CATEGORY_NOTICE = "notice";
  public static final String ARTICLE_CATEGORY_NEWS = "news";
  public static final String ARTICLE_CATEGORY_ACTIVITY = "activity";
  public static final String ARTICLE_CATEGORY_HELP = "help";
  public static final String ARTICLE_CATEGORY_POST = "post";

  public static final String GOODS_MOLD_GOODS = "goods";
  public static final String GOODS_MOLD_COUPON = "goods_coupon";
  public static final String GOODS_MOLD_JOINT = "goods_joint";

  public static final String NAVIGATION_POSITION_HEADER = "header";
  public static final String NAVIGATION_POSITION_SUB_HEADER = "sub_header";
  public static final String NAVIGATION_POSITION_MIDDLE = "middle";
  public static final String NAVIGATION_POSITION_FOOTER = "footer";
  public static final String NAVIGATION_POSITION_FOOTER_1 = "footer1";

  public static final String PARTNER_POSITION_BOTTOM_CONTACT = "bottom_contact";
  public static final String PARTNER_POSITION_BOTTOM_TEXT = "bottom_text";
  public static final String PARTNER_POSITION_BOTTOM_IMAGE = "bottom_image";

  public static final String PAYMENT_ALIPAY = "alipay";
  public static final String PAYMENT_ALIPAY_WAP = "alipay_wap";
  public static final String PAYMENT_WECHAT = "wechat";
  public static final String PAYMENT_WECHAT_WAP = "wechat_wap";
  public static final String PAYMENT_CHINABANK = "chinabank";
  public static final String PAYMENT_CITICBANK = "citicbank";
  public static final String PAYMENT_TENPAY = "tenpay";
  public static final String PAYMENT_KUAIQIAN = "kuaiqian";
  public static final String PAYMENT_PAYPAL = "paypal";
  public static final String PAYMENT_OFFLINE = "offline";
  public static final String PAYMENT_CASH = "cash";
  public static final String PAYMENT_BALANCE = "balance";
  public static final String PAYMENT_CARD = "card";
  public static final String PAYMENT_INTEGRAL = "integral";
  public static final String PAYMENT_COUPON = "coupon";
  public static final String PAYMENT_PSBC = "psbc";
  public static final String PAYMENT_CIBBANK = "cibbank";

  public static final String STORE_TEMPLATE_DEFAULT = "orange";

  public static final String SYSTEM_MODULE_USHARE_CODE = "ushare";
  public static final String SYSTEM_MODULE_USHOP_CODE = "ushop";
  public static final String SYSTEM_MODULE_UMALL_CODE = "umall";
  public static final String SYSTEM_MODULE_UGROUP_CODE = "ugroup";
  public static final String SYSTEM_MODULE_UJOIN_CODE = "ujoin";
  public static final String SYSTEM_MODULE_UBBS_CODE = "ubbs";

  /* ================================= Resource ================================= */
  public static final String RESOURCE_GOODS = "goods";
  public static final String RESOURCE_GOODS_MOLD = "goods_mold";
  public static final String RESOURCE_CLASSIFICATION = "classification";
  public static final String RESOURCE_BRAND = "brand";
  public static final String RESOURCE_BRAND_CATEGORY = "brand_category";
  public static final String RESOURCE_GOODS_TYPE = "goods_type";
  public static final String RESOURCE_GOODS_SPEC = "goods_spec";
  public static final String RESOURCE_GOODS_UNIT = "goods_unit";
  public static final String RESOURCE_SUPPLIER = "supplier";
  public static final String RESOURCE_ALBUM = "album";
  public static final String RESOURCE_ALBUM_WATERWARK = "album_watermark";

  public static final String RESOURCE_STORE = "store";
  public static final String RESOURCE_STORE_BUSINESS = "store_business";
  public static final String RESOURCE_STORE_CATEGORY = "store_category";
  public static final String RESOURCE_STORE_GRADE = "store_grade";
  public static final String RESOURCE_STORE_TEMPLATE = "store_template";
  public static final String RESOURCE_STORE_UPGRADE = "store_upgrade";

  public static final String RESOURCE_PURCHASE_ORDER = "purchase_order";
  public static final String RESOURCE_RETURN_ORDER = "return_order";
  public static final String RESOURCE_REFUND = "refund";
  public static final String RESOURCE_PAYMENT = "payment";
  public static final String RESOURCE_SHIPPING_TEMPLATE = "shipping_template";
  public static final String RESOURCE_SHIPPING_AREA = "shipping_area";
  public static final String RESOURCE_SHIPPING_RULE = "shipping_rule";
  public static final String RESOURCE_SHIPPING_COMPANY = "shipping_company";

  public static final String RESOURCE_CONSULTATION = "consultation";
  public static final String RESOURCE_REPORT = "report";
  public static final String RESOURCE_REPORT_SUBJECT = "report_subject";
  public static final String RESOURCE_COMPLAINT = "complaint";
  public static final String RESOURCE_COMPLAINT_SUBJECT = "complaint_subject";
  public static final String RESOURCE_STATION_APPLY = "station_apply";
  public static final String RESOURCE_SUPPLIER_APPLY = "supplier_apply";

  public static final String RESOURCE_JOINT = "joint";
  public static final String RESOURCE_JOINT_RULE = "joint_rule";

  public static final String RESOURCE_PROMOTION = "promotion";
  public static final String RESOURCE_PROMOTION_TEMPLATE = "promotion_template";
  public static final String RESOURCE_REBATE = "rebate";
  public static final String RESOURCE_COMBINE = "combine";
  public static final String RESOURCE_COUPON = "coupon";
  public static final String RESOURCE_CARD = "card";
  public static final String RESOURCE_FORETASTE = "foretaste";
  public static final String RESOURCE_FORETASTE_REQUEST_REPORT = "foretaste_request_report";

  public static final String RESOURCE_USER = "user";
  public static final String RESOURCE_ACCOUNT_RECHARGE = "account_recharge";
  public static final String RESOURCE_ACCOUNT_CASH = "account_cash";
  public static final String RESOURCE_ACCOUNT_CHANGE = "account_change";
  public static final String RESOURCE_ACCOUNT_LOG = "account_log";
  public static final String RESOURCE_USER_CARD_ITEM = "user_card_item";
  public static final String RESOURCE_USER_CARD_RECHARGE = "user_card_recharge";
  public static final String RESOURCE_USER_CARD_RECHARGE_RULE = "user_card_recharge_rule";
  public static final String RESOURCE_USER_CARD_LOG = "user_card_log";
  public static final String RESOURCE_USER_CARD_CHANGE = "user_card_change";
  public static final String RESOURCE_INTEGRAL_LOG = "integral_log";
  public static final String RESOURCE_INTEGRAL_CHANGE = "integral_change";
  public static final String RESOURCE_PRESTIGE_LOG = "prestige_log";
  public static final String RESOURCE_PRESTIGE_CHANGE = "prestige_change";
  public static final String RESOURCE_EVALUATION = "evaluation";
  public static final String RESOURCE_USER_GRADE = "user_grade";
  public static final String RESOURCE_BLACK_IP = "black_ip";
  public static final String RESOURCE_BLACK_MOBILE = "black_mobile";

  public static final String RESOURCE_NAVIGATION = "navigation";
  public static final String RESOURCE_NAVIGATION_PAGE = "navigation_page";
  public static final String RESOURCE_ADVERT_MAIN = "advert_main";
  public static final String RESOURCE_ADVERT_FLOOR = "advert_floor";
  public static final String RESOURCE_ADVERT_BLOCK = "advert_block";
  public static final String RESOURCE_ARTICLE = "article";
  public static final String RESOURCE_ARTICLE_POST = "article_post";
  public static final String RESOURCE_ARTICLE_REPLY = "article_reply";
  public static final String RESOURCE_ARTICLE_BLOCK = "article_block";
  public static final String RESOURCE_ARTICLE_CATEGORY = "article_category";
  public static final String RESOURCE_ARTICLE_CATEGORY_POST = "article_category_post";
  public static final String RESOURCE_ARTICLE_SENSITIVE_WORD = "article_sensitive_word";
  public static final String RESOURCE_PARTNER = "partner";

  public static final String RESOURCE_SITE = "site";
  public static final String RESOURCE_USER_ADMIN = "user_admin";
  public static final String RESOURCE_ROLE = "role";
  public static final String RESOURCE_AREA = "area";
  public static final String RESOURCE_TEMPLATE = "template";
  public static final String RESOURCE_DATABASE_BACKUP = "database_backup";
  public static final String RESOURCE_CACHE = "cache";
  public static final String RESOURCE_WECHAT_MENU = "wechat_menu";
  public static final String RESOURCE_WECHAT_MESSAGE = "wechat_message";

  public static final String RESOURCE_SYSTEM_CONFIG_SITE_STATE = "system_config_site_state";
  public static final String RESOURCE_SYSTEM_CONFIG_SITE_INFO = "system_config_site_info";
  public static final String RESOURCE_SYSTEM_CONFIG_SITE_SEARCH = "system_config_site_search";
  public static final String RESOURCE_SYSTEM_CONFIG_SITE_OFFICIAL = "system_config_site_official";
  public static final String RESOURCE_SYSTEM_CONFIG_SITE_CAPTCHA = "system_config_site_captcha";
  public static final String RESOURCE_SYSTEM_CONFIG_USER_IN = "system_config_user_in";
  public static final String RESOURCE_SYSTEM_CONFIG_USER_CARD = "system_config_user_card";
  public static final String RESOURCE_SYSTEM_CONFIG_USER_CREDIT_RULE = "system_config_user_credit_rule";
  public static final String RESOURCE_SYSTEM_CONFIG_USER_INTEGRAL = "system_config_integral";
  public static final String RESOURCE_SYSTEM_CONFIG_USER_PRESTIGE = "system_config_prestige";
  public static final String RESOURCE_SYSTEM_CONFIG_STORE_IN = "system_config_store_in";
  public static final String RESOURCE_SYSTEM_CONFIG_STORE_CREDIT_RULE = "system_config_store_credit_rule";
  public static final String RESOURCE_SYSTEM_CONFIG_STORE_SECOND_DOMAIN = "system_config_store_second_domain";
  public static final String RESOURCE_SYSTEM_CONFIG_PAYMENT = "system_config_payment";
  public static final String RESOURCE_SYSTEM_CONFIG_SHIPPING = "system_config_shipping";
  public static final String RESOURCE_SYSTEM_CONFIG_ARTICLE_POST = "system_config_article_post";
  public static final String RESOURCE_SYSTEM_CONFIG_EMAIL = "system_config_email";
  public static final String RESOURCE_SYSTEM_CONFIG_SMS = "system_config_sms";
  public static final String RESOURCE_SYSTEM_CONFIG_KUAIDI100 = "system_config_kuaidi100";
  public static final String RESOURCE_SYSTEM_CONFIG_IMAGE_UPLOAD = "system_config_image_upload";
  public static final String RESOURCE_SYSTEM_CONFIG_IMAGE_DEFAULT = "system_config_image_default";
  public static final String RESOURCE_SYSTEM_CONFIG_IMAGE_SERVER = "system_config_image_server";
  public static final String RESOURCE_SYSTEM_CONFIG_QUARTZ = "system_config_quartz";
  public static final String RESOURCE_SYSTEM_CONFIG_WECHAT_BASIC = "system_config_wechat_basic";
  public static final String RESOURCE_SYSTEM_CONFIG_WECHAT_SHARE = "system_config_wechat_share";

  public static final String RESOURCE_REPORT_PURCHASE_ORDER_SALE_DAY = "report_purchase_order_sale_day";
  public static final String RESOURCE_REPORT_PURCHASE_ORDER_SALE_MONTH = "report_purchase_order_sale_month";
  public static final String RESOURCE_REPORT_USER_INCREASE_DAY = "report_user_increase_day";
  public static final String RESOURCE_REPORT_USER_INCREASE_MONTH = "report_user_increase_month";
  public static final String RESOURCE_REPORT_USER_CONSUME_MONTH = "report_user_consume_month";

  /* ================================= System Config ================================= */
  public static final String SYSTEM_CONFIG_SITE_CLOSE_FLAG = "site_close_flag";
  public static final String SYSTEM_CONFIG_SITE_CLOSE_INFO = "site_close_info";
  public static final String SYSTEM_CONFIG_SITE_DOMAIN = "site_domain";
  public static final String SYSTEM_CONFIG_SITE_LOGO = "site_logo";
  public static final String SYSTEM_CONFIG_SITE_WECHAT = "site_wechat";
  public static final String SYSTEM_CONFIG_SITE_NAME = "site_name";
  public static final String SYSTEM_CONFIG_SITE_TITLE = "site_title";
  public static final String SYSTEM_CONFIG_SITE_FOOTER = "site_footer";
  public static final String SYSTEM_CONFIG_SITE_KEYWORDS = "site_keywords";
  public static final String SYSTEM_CONFIG_SITE_DESCRIPTION = "site_description";
  public static final String SYSTEM_CONFIG_SITE_POWERED_BY = "site_powered_by";
  public static final String SYSTEM_CONFIG_SITE_GENERATOR = "site_generator";
  public static final String SYSTEM_CONFIG_SITE_AUTHOR = "site_author";
  public static final String SYSTEM_CONFIG_SITE_COPYRIGHT = "site_copyright";
  public static final String SYSTEM_CONFIG_SITE_SEARCH_TIP = "site_search_tip";
  public static final String SYSTEM_CONFIG_SITE_HOT_SEARCH = "site_hot_search";
  public static final String SYSTEM_CONFIG_SITE_OFFICIAL_CONTACT = "site_official_contact";
  public static final String SYSTEM_CONFIG_SITE_OFFICIAL_QQ = "site_official_qq";
  public static final String SYSTEM_CONFIG_SITE_CAPTCHA_LOGIN_FLAG = "site_captcha_login_flag";
  public static final String SYSTEM_CONFIG_SITE_CAPTCHA_CONSULTATION_FLAG = "site_captcha_consultation_flag";

  public static final String SYSTEM_CONFIG_USER_REGISTER_FLAG = "user_register_flag";
  public static final String SYSTEM_CONFIG_USER_LOGIN_FLAG = "user_login_flag";
  public static final String SYSTEM_CONFIG_USER_QQ_LOGIN_FLAG = "user_qq_login_flag";
  public static final String SYSTEM_CONFIG_USER_QQ_APP_ID = "user_qq_app_id";
  public static final String SYSTEM_CONFIG_USER_QQ_APP_KEY = "user_qq_app_key";
  public static final String SYSTEM_CONFIG_USER_SINA_LOGIN_FLAG = "user_sina_login_flag";
  public static final String SYSTEM_CONFIG_USER_SINA_APP_ID = "user_sina_app_id";
  public static final String SYSTEM_CONFIG_USER_SINA_APP_KEY = "user_sina_app_key";
  public static final String SYSTEM_CONFIG_USER_WECHAT_LOGIN_FLAG = "user_wechat_login_flag";
  public static final String SYSTEM_CONFIG_USER_WECHAT_APP_ID = "user_wechat_app_id";
  public static final String SYSTEM_CONFIG_USER_WECHAT_APP_KEY = "user_wechat_app_key";
  public static final String SYSTEM_CONFIG_USER_CREDIT_RULE = "user_credit_rule";
  public static final String SYSTEM_CONFIG_USER_CARD_NAME = "user_card_name";
  public static final String SYSTEM_CONFIG_USER_CARD_IMAGE = "user_card_image";

  public static final String SYSTEM_CONFIG_INTEGRAL_FLAG = "integral_flag";
  public static final String SYSTEM_CONFIG_INTEGRAL_REGISTER = "integral_register";
  public static final String SYSTEM_CONFIG_INTEGRAL_LOGIN = "integral_login";
  public static final String SYSTEM_CONFIG_INTEGRAL_WECHAT_SUBSCRIBE = "integral_wechat_subscribe";
  public static final String SYSTEM_CONFIG_INTEGRAL_PURCHASE_ORDER_BUY = "integral_purchase_order_buy";
  public static final String SYSTEM_CONFIG_INTEGRAL_PURCHASE_ORDER_BUY_LIMIT = "integral_purchase_order_buy_limit";
  public static final String SYSTEM_CONFIG_INTEGRAL_PURCHASE_ORDER_EVALUATE = "integral_purchase_order_evaluate";

  public static final String SYSTEM_CONFIG_PRESTIGE_FLAG = "prestige_flag";
  public static final String SYSTEM_CONFIG_PRESTIGE_REGISTER = "prestige_register";
  public static final String SYSTEM_CONFIG_PRESTIGE_LOGIN = "prestige_login";

  public static final String SYSTEM_CONFIG_STORE_REGISTER_FLAG = "store_register_flag";
  public static final String SYSTEM_CONFIG_STORE_CREDIT_RULE = "store_credit_rule";
  public static final String SYSTEM_CONFIG_STORE_SECOND_DOMAIN_FlAG = "store_second_domain_flag";
  public static final String SYSTEM_CONFIG_STORE_SECOND_DOMAIN_EXCLUDED = "store_second_domain_excluded";

  public static final String SYSTEM_CONFIG_PAYMENT_OFFICIAL_FLAG = "payment_official_flag";
  public static final String SYSTEM_CONFIG_PAYMENT_STORE = "payment_store";

  public static final String SYSTEM_CONFIG_SHIPPING_FROM = "shipping_from";

  public static final String SYSTEM_CONFIG_ARTICLE_POST_HOT_REPLY_COUNT = "article_post_hot_reply_count";

  public static final String SYSTEM_CONFIG_EMAIL_FLAG = "email_flag";
  public static final String SYSTEM_CONFIG_EMAIL_HOST = "email_host";
  public static final String SYSTEM_CONFIG_EMAIL_PORT = "email_port";
  public static final String SYSTEM_CONFIG_EMAIL_ACCOUNT = "email_account";
  public static final String SYSTEM_CONFIG_EMAIL_PASSWORD = "email_password";
  public static final String SYSTEM_CONFIG_EMAIL_FROM = "email_from";

  public static final String SYSTEM_CONFIG_SMS_FLAG = "sms_flag";
  public static final String SYSTEM_CONFIG_SMS_HOST = "sms_host";
  public static final String SYSTEM_CONFIG_SMS_ACCOUNT = "sms_account";
  public static final String SYSTEM_CONFIG_SMS_PASSWORD = "sms_password";
  public static final String SYSTEM_CONFIG_SMS_SIGNATURE = "sms_signature";

  public static final String SYSTEM_CONFIG_KUAIDI100_ID = "kuaidi100_id";

  public static final String SYSTEM_CONFIG_IMAGE_SIZE_LIMIT = "image_size_limit";
  public static final String SYSTEM_CONFIG_IMAGE_SUFFIX_LIMIT = "image_suffix_limit";
  public static final String SYSTEM_CONFIG_IMAGE_SMALL_WIDTH = "image_small_width";
  public static final String SYSTEM_CONFIG_IMAGE_SMALL_HEIGHT = "image_small_height";
  public static final String SYSTEM_CONFIG_IMAGE_GOODS_MAIN = "image_goods_main";
  public static final String SYSTEM_CONFIG_IMAGE_STORE_LOGO = "image_store_logo";
  public static final String SYSTEM_CONFIG_IMAGE_USER_PHOTO = "image_user_photo";
  public static final String SYSTEM_CONFIG_IMAGE_ALBUM_COVER = "image_album_cover";
  public static final String SYSTEM_CONFIG_IMAGE_ALBUM_IMAGE = "image_album_image";
  public static final String SYSTEM_CONFIG_IMAGE_SERVER = "image_server";

  public static final String SYSTEM_CONFIG_QUARTZ_PURCHASE_ORDER_AUTO_RECEIVE_INTERVAL = "quartz_purchase_order_auto_receive_interval";
  public static final String SYSTEM_CONFIG_QUARTZ_PURCHASE_ORDER_AUTO_FINISH_INTERVAL = "quartz_purchase_order_auto_finish_interval";
  public static final String SYSTEM_CONFIG_QUARTZ_JOINT_BEGIN_AUTO_FAIL_INTERVAL = "quartz_joint_begin_auto_fail_interval";
  public static final String SYSTEM_CONFIG_QUARTZ_JOINT_END_AUTO_FAIL_INTERVAL = "quartz_joint_end_auto_fail_interval";

  public static final String SYSTEM_CONFIG_WECHAT_TOKEN = "wechat_token";
  public static final String SYSTEM_CONFIG_WECHAT_APP_ID = "wechat_app_id";
  public static final String SYSTEM_CONFIG_WECHAT_APP_SECRET = "wechat_app_secret";
  public static final String SYSTEM_CONFIG_WECHAT_NOTICE_FLAG = "wechat_notice_flag";
  public static final String SYSTEM_CONFIG_WECHAT_SUBSCRIBE_FLAG = "wechat_subscribe_flag";
  public static final String SYSTEM_CONFIG_WECHAT_LOCATION_RANGE = "wechat_location_range";
  public static final String SYSTEM_CONFIG_WECHAT_OPEN_ONLY_FLAG = "wechat_open_only_flag";
  public static final String SYSTEM_CONFIG_WECHAT_USER_ONLY_FLAG = "wechat_user_only_flag";
  public static final String SYSTEM_CONFIG_WECHAT_AUTO_REFUND_FLAG = "wechat_auto_refund_flag";
  public static final String SYSTEM_CONFIG_WECHAT_REFUND_CERT_FILE = "wechat_refund_cert_file";
  public static final String SYSTEM_CONFIG_WECHAT_ACCESS_TOKEN = "wechat_access_token";
  public static final String SYSTEM_CONFIG_WECHAT_ACCESS_TOKEN_EXPIRES_IN = "wechat_access_token_expires_in";
  public static final String SYSTEM_CONFIG_WECHAT_JSAPI_TICKET = "wechat_jsapi_ticket";
  public static final String SYSTEM_CONFIG_WECHAT_JSAPI_TICKET_EXPIRES_IN = "wechat_jsapi_ticket_expires_in";
  public static final String SYSTEM_CONFIG_WECHAT_SHARE_TITLE = "wechat_share_title";
  public static final String SYSTEM_CONFIG_WECHAT_SHARE_DESC = "wechat_share_desc";
  public static final String SYSTEM_CONFIG_WECHAT_SHARE_LINK = "wechat_share_link";
  public static final String SYSTEM_CONFIG_WECHAT_SHARE_IMAGE = "wechat_share_image";
  public static final String SYSTEM_CONFIG_WECHAT_SHARE_DESC_GOODS = "wechat_share_desc_goods";
  public static final String SYSTEM_CONFIG_WECHAT_SHARE_DESC_JOINT = "wechat_share_desc_joint";

  public static final String TEMPLATE_PURCHASE_ORDER_AMOUNT_CHANGED = "purchase_order_amount_changed";
  public static final String TEMPLATE_PURCHASE_ORDER_PAID_SUCCESS = "purchase_order_paid_success";
  public static final String TEMPLATE_PURCHASE_ORDER_AUDIT_PAID_OFFILINE_RECEIVED = "purchase_order_audit_paid_offiline_received";
  public static final String TEMPLATE_PURCHASE_ORDER_AUDIT_REJECTED = "purchase_order_audit_rejected";
  public static final String TEMPLATE_PURCHASE_ORDER_SHIPPED = "purchase_order_shipped";
  public static final String TEMPLATE_PURCHASE_ORDER_COUPON_SENT = "purchase_order_coupon_sent";
  public static final String TEMPLATE_PURCHASE_ORDER_SHIPPING_CHANGED = "purchase_order_shipping_changed";
  public static final String TEMPLATE_PURCHASE_ORDER_UNRECEIVED = "purchase_order_unreceived";
  public static final String TEMPLATE_PURCHASE_ORDER_SHIPPING_ADDRESS_CHANGED = "purchase_order_shipping_address_changed";
  public static final String TEMPLATE_PURCHASE_ORDER_RETURN_APPROVED = "purchase_order_return_approved";
  public static final String TEMPLATE_PURCHASE_ORDER_RETURN_REJECTED = "purchase_order_return_rejected";
  public static final String TEMPLATE_PURCHASE_ORDER_RETURN_SUCCESS = "purchase_order_return_success";
  public static final String TEMPLATE_PURCHASE_ORDER_REFUND_REJECTED = "purchase_order_refund_rejected";
  public static final String TEMPLATE_PURCHASE_ORDER_REFUND_SUCCESS = "purchase_order_refund_success";
  public static final String TEMPLATE_PURCHASE_ORDER_CREATED = "purchase_order_created";
  public static final String TEMPLATE_PURCHASE_ORDER_EVALUATED = "purchase_order_evaluated";
  public static final String TEMPLATE_PURCHASE_ORDER_CANCELED = "purchase_order_canceled";
  public static final String TEMPLATE_PURCHASE_ORDER_RECEIVED = "purchase_order_received";
  public static final String TEMPLATE_PURCHASE_ORDER_RETURN_AUDITING = "purchase_order_return_auditing";
  public static final String TEMPLATE_PURCHASE_ORDER_RETURN_SHIPPED = "purchase_order_return_shipped";
  public static final String TEMPLATE_PURCHASE_ORDER_RETURN_SHIPPING_CHANGED = "purchase_order_return_shipping_changed";
  public static final String TEMPLATE_PURCHASE_ORDER_REFUND_AUDITING = "purchase_order_refund_auditing";
  public static final String TEMPLATE_PURCHASE_ORDER_JOINT_SUCCESS = "purchase_order_joint_success";
  public static final String TEMPLATE_PURCHASE_ORDER_JOINT_LACK = "purchase_order_joint_lack";

  public static final String TEMPLATE_USER_MOBILE_CODE = "user_mobile_code";
  public static final String TEMPLATE_USER_PASSWORD_RETURNED = "user_password_returned";
  public static final String TEMPLATE_USER_CONSULTATION_REPLIED = "user_consultation_replied";
  public static final String TEMPLATE_USER_REPORT_HANDLED = "user_report_handled";
  public static final String TEMPLATE_USER_COMPLAINT_HANDLED = "user_complaint_handled";
  public static final String TEMPLATE_ACCOUNT_RECHARGE_SUCCESS = "account_recharge_success";
  public static final String TEMPLATE_ACCOUNT_CASH_SUCCESS = "account_cash_success";
  public static final String TEMPLATE_ACCOUNT_CASH_REJECTED = "account_cash_rejected";
  public static final String TEMPLATE_USER_CARD_RECHARGE_SUCCESS = "user_card_recharge_success";
  public static final String TEMPLATE_STORE_ENTER_SUCCESS = "store_enter_success";
  public static final String TEMPLATE_STORE_ENTER_REJECTED = "store_enter_rejected";
  public static final String TEMPLATE_STORE_UPGRADE_SUCCESS = "store_upgrade_success";
  public static final String TEMPLATE_USER_CONSULTATION_REPLYING = "user_consultation_replying";
  public static final String TEMPLATE_USER_REPORT_HANDLING = "user_report_handling";
  public static final String TEMPLATE_USER_COMPLAINT_HANDLING = "user_complaint_handling";

  /* ======================================================= Constant Map ======================================================= */
  private static Map<String, String> CONSTANT_MAP = new HashMap<String, String>();

  static {
    try {
      Constant constantUtil = new Constant();
      Field[] fields = Constant.class.getDeclaredFields();
      for (int i = 0; i < fields.length; i++) {
        CONSTANT_MAP.put(fields[i].getName(), fields[i].get(constantUtil) + "");
      }
    } catch (IllegalAccessException iaex) {
      iaex.printStackTrace();
    }
  }

  public static String getValue(String key) {
    return CONSTANT_MAP.get(key);
  }

}
