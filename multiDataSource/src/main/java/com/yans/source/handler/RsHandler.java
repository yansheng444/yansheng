package com.yans.source.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.sf.json.JSONArray;


public interface RsHandler<T> {
    /**
     * Turn the <code>ResultSet</code> into an Object.
     *
     * @param rs 要操作的数据集
     * @return 返回特定格式的resultset对象
     * @throws SQLException 数据库异常
     */
    JSONArray handle(ResultSet rs) throws SQLException;
    
    
}
