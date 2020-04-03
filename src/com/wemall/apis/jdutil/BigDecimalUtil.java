package com.wemall.apis.jdutil;

import java.math.BigDecimal;

public class BigDecimalUtil {

	public static BigDecimal strToBigDecimal(String str, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal bg = new BigDecimal(str).setScale(scale, 4);
		return bg;
	}

	public static BigDecimal strToBigDecimal(String str) {
		BigDecimal bg = new BigDecimal(str);
		return bg;
	}

	public static BigDecimal add(BigDecimal bg1, BigDecimal bg2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		if (bg1 == null) {
			bg1 = BigDecimal.ZERO;
		}
		if (bg2 == null) {
			bg2 = BigDecimal.ZERO;
		}
		return bg1.add(bg2).setScale(scale, 4);
	}

	public static BigDecimal add(BigDecimal bg1, BigDecimal bg2) {
		if (bg1 == null) {
			bg1 = BigDecimal.ZERO;
		}
		if (bg2 == null) {
			bg2 = BigDecimal.ZERO;
		}
		return bg1.add(bg2);
	}

	public static BigDecimal sub(BigDecimal bg1, BigDecimal bg2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		if (bg1 == null) {
			bg1 = BigDecimal.ZERO;
		}
		if (bg2 == null) {
			bg2 = BigDecimal.ZERO;
		}
		return bg1.subtract(bg2).setScale(scale, 4);
	}

	public static BigDecimal sub(BigDecimal bg1, BigDecimal bg2) {
		if (bg1 == null) {
			bg1 = BigDecimal.ZERO;
		}
		if (bg2 == null) {
			bg2 = BigDecimal.ZERO;
		}
		return bg1.subtract(bg2);
	}

	public static BigDecimal mul(BigDecimal bg1, BigDecimal bg2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		if (bg1 == null) {
			bg1 = BigDecimal.ZERO;
		}
		if (bg2 == null) {
			bg2 = BigDecimal.ZERO;
		}
		return bg1.multiply(bg2).setScale(scale, 4);
	}

	public static BigDecimal mul(BigDecimal bg1, BigDecimal bg2) {
		if (bg1 == null) {
			bg1 = BigDecimal.ZERO;
		}
		if (bg2 == null) {
			bg2 = BigDecimal.ZERO;
		}
		return bg1.multiply(bg2);
	}

	public static BigDecimal mul(BigDecimal bg1, Integer in, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		if (bg1 == null) {
			bg1 = BigDecimal.ZERO;
		}
		if (in == null) {
			in = Integer.valueOf(0);
		}
		BigDecimal bg2 = strToBigDecimal(in.toString(), 0);
		return bg1.multiply(bg2).setScale(scale, 4);
	}

	public static BigDecimal mul(BigDecimal bg1, Integer in) {
		if (bg1 == null) {
			bg1 = BigDecimal.ZERO;
		}
		if (in == null) {
			in = Integer.valueOf(0);
		}
		BigDecimal bg2 = strToBigDecimal(in.toString());
		return bg1.multiply(bg2);
	}

	public static BigDecimal div(BigDecimal bg1, BigDecimal bg2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		if (bg1 == null) {
			bg1 = BigDecimal.ZERO;
		}
		if (bg2 == null) {
			bg2 = BigDecimal.ZERO;
		}
		if (bg2.compareTo(BigDecimal.ZERO) == 0) {
			throw new ArithmeticException("除数不能为零");
		}
		return bg1.divide(bg2, scale, 4);
	}

	public static BigDecimal div2(BigDecimal bg1, BigDecimal bg2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		if (bg1 == null) {
			bg1 = BigDecimal.ZERO;
		}
		if (bg2 == null) {
			bg2 = BigDecimal.ZERO;
		}
		if (bg2.compareTo(BigDecimal.ZERO) == 0) {
			throw new ArithmeticException("除数不能为零");
		}
		return bg1.divide(bg2, scale, 1);
	}

	public static BigDecimal div(BigDecimal bg1, Integer in, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		if (bg1 == null) {
			bg1 = BigDecimal.ZERO;
		}
		if (in == null) {
			in = Integer.valueOf(0);
		}
		BigDecimal bg2 = strToBigDecimal(in.toString(), 0);
		if (bg2.compareTo(BigDecimal.ZERO) == 0) {
			throw new ArithmeticException("除数不能为零");
		}

		return bg1.divide(bg2, scale, 4);
	}

	public static void main(String[] args) {
		BigDecimal bg1 = new BigDecimal("1036");
		Integer bg2 = Integer.valueOf(3);
		System.out.println(div(bg1, bg2, 2));
	}
}
