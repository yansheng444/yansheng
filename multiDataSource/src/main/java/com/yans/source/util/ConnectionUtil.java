package com.yans.source.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ConnectionUtil {
    
    public static ResultSet test(String url ,String driver,String username ,String password,String sql)
    	throws Exception
    	{
        // 定义连接
        Connection connection = null;
        
        // 加载驱动
        Class.forName(driver);
        connection = DriverManager.getConnection(url, username, password);
        
        PreparedStatement preparedStatement = null;
        // 结果集
        ResultSet resultSet = null;
        
        preparedStatement = connection.prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();
        
        return resultSet;
    }
    
}