package com.wemall.apis.jd.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ==========================================================<br>
 * <b>版权</b>：　　 凌书磊 版权所有(c)2017<br>
 * <b>作者</b>：　　 ilvelhs@gmail.com<br>
 * <b>创建日期</b>：　2017年8月1日 下午10:57:22<br>
 * <b>描述</b>：　　　TODO<br>
 * <b>版本</b>：　 V1.1<br>
 * <b>修订历史</b>：　NONE<br>
 * ==========================================================<br>
 */
public class CheckSkuResponse implements Serializable {

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
		private int is7ToReturn;
		private int isCanVAT;
		private int saleState;
		private String name;
		private long skuId;

		public int getIs7ToReturn() {
			return is7ToReturn;
		}

		public void setIs7ToReturn(int is7ToReturn) {
			this.is7ToReturn = is7ToReturn;
		}

		public int getIsCanVAT() {
			return isCanVAT;
		}

		public void setIsCanVAT(int isCanVAT) {
			this.isCanVAT = isCanVAT;
		}

		public int getSaleState() {
			return saleState;
		}

		public void setSaleState(int saleState) {
			this.saleState = saleState;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public long getSkuId() {
			return skuId;
		}

		public void setSkuId(long skuId) {
			this.skuId = skuId;
		}

	}

}
