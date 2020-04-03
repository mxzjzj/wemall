package com.wemall.apis.jd.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ==========================================================<br>
 * <b>版权</b>： 凌书磊 版权所有(c)2017<br>
 * <b>作者</b>： ilvelhs@gmail.com<br>
 * <b>创建日期</b>： 2017年8月1日 下午10:57:22<br>
 * <b>描述</b>： TODO<br>
 * <b>版本</b>： V1.1<br>
 * <b>修订历史</b>： NONE<br>
 * ==========================================================<br>
 */
public class CheckStockResponse implements Serializable {

	private static final long serialVersionUID = 3778796253701685650L;

	private String resultCode;
	private String resultMessage;
	private boolean success;
	private List<Result> result = new ArrayList<Result>();

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public List<Result> getResult() {
		return result;
	}

	public void setResult(List<Result> result) {
		this.result = result;
	}

	public static class Result implements Serializable {
		private static final long serialVersionUID = 3778796253701685610L;
		private String area;

		private String desc;

		private String sku;

		private int state;

		public String getArea() {
			return area;
		}

		public void setArea(String area) {
			this.area = area;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public String getSku() {
			return sku;
		}

		public void setSku(String sku) {
			this.sku = sku;
		}

		public int getState() {
			return state;
		}

		public void setState(int state) {
			this.state = state;
		}

	}

}
