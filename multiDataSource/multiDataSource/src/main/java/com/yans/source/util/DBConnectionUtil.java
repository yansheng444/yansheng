package com.yans.source.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DBConnectionUtil {

	private static final Log log = LogFactory.getLog(DBConnectionUtil.class);

	public static  String DriverName;
	public static  String Url ;
	public static  String Username ;
	public static String Password ;

	public static Connection getConnection(String DriverName,String Url,String Username,String Password) throws Exception {
		Connection con = null; // 定义一个MYSQL链接对象
			Class.forName(DriverName).newInstance(); // MYSQL驱动
			con = DriverManager.getConnection(Url, Username, Password); // 链接本地MYSQL
			log.info("创建mysql数据库连接成功");
		return con;
	}

	public static void close(Connection con, Statement pst, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
				log.info("关闭mysql结果集成功");
			} catch (Exception e) {
				log.error("关闭Mysql结果集有误，错误原因：", e);
			}
		}
		if (pst != null) {
			try {
				pst.close();
				log.info("关闭mysql执行成功");
			} catch (Exception e) {
				log.error("关闭Mysql执行有误，错误原因：", e);
			}
		}
		if (con != null) {
			try {
				con.close();
				log.info("关闭mysql连接成功");
			} catch (Exception e) {
				log.error("关闭Mysql连接有误，错误原因：", e);
			}
		}
	}
	
	public static void close(Connection con, PreparedStatement pst) {
		if (pst != null) {
			try {
				pst.close();
				log.info("关闭mysql执行成功");
			} catch (Exception e) {
				log.error("关闭Mysql执行有误，错误原因：", e);
			}
		}
		if (con != null) {
			try {
				con.close();
				log.info("关闭mysql连接成功");
			} catch (Exception e) {
				log.error("关闭Mysql连接有误，错误原因：", e);
			}
		}
	}

}
