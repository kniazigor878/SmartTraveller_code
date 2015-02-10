package by.iharkaratkou.db;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Map.Entry;

import by.iharkaratkou.*;
import by.iharkaratkou.bsnlogic.BusinessLogicUtils;

public class DatabaseUtils {
	
	public Connection getConnection() throws ClassNotFoundException, SQLException{
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = null;
		String login = "ihar";
		String pass = "ihar";
		conn = DriverManager.getConnection("jdbc:mysql://localhost/test",login,pass);
		return conn;
	}
	
	public ArrayList<ArrayList<String>> execSelect(String query) throws SQLException, ClassNotFoundException{	
		Connection conn = getConnection();
		ArrayList<ArrayList<String>> queryResult = new ArrayList<ArrayList<String>>();
		ArrayList<String> queryResultRow = new ArrayList<String>();
		Statement statement = conn.createStatement();
		ResultSet rs = statement.executeQuery(query);
		ResultSetMetaData meta = rs.getMetaData();
		int colCount = meta.getColumnCount();
		while(rs.next()){
			for (int column = 1; column <= colCount; column++){
				queryResultRow.add(rs.getString(column));
			}
			queryResult.add((ArrayList<String>) queryResultRow.clone());
			queryResultRow.clear();
		}
		return queryResult;
	}
	
}
