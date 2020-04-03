package com.wemall.core.tools;

import com.alibaba.druid.filter.config.ConfigTools;

@SuppressWarnings("all")
public class DruidDecrypt {

	public static void main(String[] args) {
		try {
			ConfigTools configTools = new ConfigTools ();
			System.out.println(configTools.encrypt("168168"));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
