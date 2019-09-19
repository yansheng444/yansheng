package com.yans.source.handler;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.springframework.stereotype.Component;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component("jsonHandler")
public class JsonHandler implements RsHandler<JSONArray> {

	@Override
	public JSONArray handle(ResultSet rs) throws SQLException {

		   //创建一个JSONArray对象
        JSONArray jsonArray = new JSONArray();
        //获得ResultSetMeataData对象
        ResultSetMetaData rsmd = rs.getMetaData();
        while (rs.next()) {
            //定义json对象
            JSONObject obj = new JSONObject();
            //判断数据类型&获取value
            getType(rs, rsmd, obj);
            //将对象添加到JSONArray中
            jsonArray.add(obj);
        }
        return jsonArray;
	}

	private void getType(ResultSet rs, ResultSetMetaData rsmd, JSONObject obj) throws SQLException {
		int total_rows = rsmd.getColumnCount();
		for (int i = 0; i < total_rows; i++) {
			String columnName = rsmd.getColumnLabel(i + 1);
			if (obj.has(columnName)) {
				columnName += "1";
			}
				switch (rsmd.getColumnType(i + 1)) {
				case java.sql.Types.ARRAY:
					obj.put(columnName, rs.getArray(columnName));
					break;
				case java.sql.Types.BIGINT:
					obj.put(columnName, rs.getInt(columnName));
					break;
				case java.sql.Types.BOOLEAN:
					obj.put(columnName, rs.getBoolean(columnName));
					break;
				case java.sql.Types.BLOB:
					obj.put(columnName, rs.getBlob(columnName));
					break;
				case java.sql.Types.DOUBLE:
					obj.put(columnName, rs.getDouble(columnName));
					break;
				case java.sql.Types.FLOAT:
					obj.put(columnName, rs.getFloat(columnName));
					break;
				case java.sql.Types.INTEGER:
					obj.put(columnName, rs.getInt(columnName));
					break;
				case java.sql.Types.NVARCHAR:
					obj.put(columnName, rs.getNString(columnName));
					break;
				case java.sql.Types.VARCHAR:
					obj.put(columnName, rs.getString(columnName));
					break;
				case java.sql.Types.TINYINT:
					obj.put(columnName, rs.getInt(columnName));
					break;
				case java.sql.Types.SMALLINT:
					obj.put(columnName, rs.getInt(columnName));
					break;
				case java.sql.Types.DATE:
					obj.put(columnName, rs.getDate(columnName));
					break;
				case java.sql.Types.BIT:
					obj.put(columnName, rs.getInt(columnName));
					break;
				case java.sql.Types.TIMESTAMP:
//					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
//					String str = sdf.format(rs.getcolumnName); 
					obj.put(columnName, rs.getString(columnName));
					break;
				default:
					obj.put(columnName, rs.getObject(columnName));
					break;
				}
		}
	}

}
