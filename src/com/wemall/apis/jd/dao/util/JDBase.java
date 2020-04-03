package com.wemall.apis.jd.dao.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.wemall.apis.jdutil.HttpClientUtil;

@Repository("jDBase")
public class JDBase {
	protected static String SERVER_URL = "https://gw.api.360buy.com/routerjson";
	public static String accessToken;
	protected static Logger logger = Logger.getLogger(JDBase.class);

	private static final String jdbc = "jdbc:mysql://127.0.0.1:3306/wemall?createDatabaseIfNotExist=true&amp;useUnicode=true&amp;characterEncoding=utf-8";
	
	private static final String user = "root";
	
	private static final String passwd = "root123";
	protected String charset = "utf-8";
	protected HttpClientUtil httpClientUtil = null;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	static {
		setAccessToken();
	}

	public JDBase() {
		this.httpClientUtil = new HttpClientUtil();
	}

	public static void insertGoodsClass(JSONObject jsonResult) {
		Connection conn = null;
		PreparedStatement ps = null;
		String sql = null;
		ResultSet rs = null;
		int pid = 0;

		try {
			Class.forName("org.gjt.mm.mysql.Driver");
			conn = DriverManager.getConnection(jdbc, user, passwd);
			if (jsonResult.getInt("parentId") != 0) {
				sql = "select id from wemall_goodsclass where classification_jd_cid='"
						+ jsonResult.getInt("parentId") + "'";
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				if (rs.next())
					pid = rs.getInt(1);
				rs = null;
			} else
				pid = 0;
			sql = "insert into wemall_goodsclass (classification_jd_cid,deleteStatus,className,display,level,parent_id) value (?,?,?,?,?,?)";
			// {"catId":9987,"parentId":0,"name":"手机","catClass":0,"state":1}
			// {"catId":653,"parentId":9987,"name":"手机通讯","catClass":1,"state":1}
			// {"catId":655,"parentId":653,"name":"手机","catClass":2,"state":1}
			ps = conn.prepareStatement(sql);
			ps.setInt(1, jsonResult.getInt("catId"));
			ps.setInt(2, 0);
			ps.setString(3, jsonResult.getString("name"));
			ps.setInt(4, 1);
			ps.setInt(5, jsonResult.getInt("catClass"));
			ps.setInt(6, pid);
			ps.executeUpdate();

		} catch (Exception e) {
			logger.error("jdBase setToken error:" + e);
			e.printStackTrace();
			try {
				conn.close();
				ps.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				conn.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void setAccessToken() {
		Connection conn = null;
		Statement state = null;
		ResultSet rs = null;
		try {
			Class.forName("org.gjt.mm.mysql.Driver");
			// TODO
			conn = DriverManager.getConnection(jdbc, user, passwd);;

			state = conn.createStatement();
			String sql = "SELECT app_Token FROM apis_jd_configure ";

			rs = state.executeQuery(sql);
			while (rs.next())
				accessToken = rs.getString("app_Token");
		} catch (Exception e) {
			logger.error("jdBase setToken error:" + e);
			e.printStackTrace();
			try {
				conn.close();
				state.close();
				rs.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				conn.close();
				state.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	protected JSONArray returnJDArray(String url, Map<String, String> createMap) {
		String httpResult = null;
		try {
			httpResult = this.httpClientUtil.doPost(url, createMap,
					this.charset);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
		JSONObject json_parent = JSONObject.fromObject(httpResult);
		boolean success = json_parent.getBoolean("success");
		if (!success) {
			String resultMessage = json_parent.getString("resultMessage");
			logger.error(this.sdf.format(new Date())
					+ " jdBase returnjdArray error:" + json_parent + " url:"
					+ url + " map:" + createMap);
			logger.error(this.sdf.format(new Date()) + " JDBaseError:"
					+ resultMessage);
			return null;
		}
		JSONArray jsonResult = JSONArray.fromObject(json_parent.get("result"));
		return jsonResult;
	}

	protected JSONObject returnJDObj(String url, Map<String, String> createMap) {
		String httpResult = null;
		try {
			httpResult = this.httpClientUtil.doPost(url, createMap,
					this.charset);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}

		JSONObject json_parent = JSONObject.fromObject(httpResult);
		boolean success = json_parent.getBoolean("success");
		if (!success) {
			String resultMessage = json_parent.getString("resultMessage");
			logger.error(this.sdf.format(new Date())
					+ " jdBase returnjdObject error:" + json_parent + " url:"
					+ url + " map:" + createMap);
			logger.error(this.sdf.format(new Date()) + " JDBaseError:"
					+ resultMessage);
			return null;
		}
		JSONObject jsonResult = JSONObject
				.fromObject(json_parent.get("result"));
		return jsonResult;
	}
}