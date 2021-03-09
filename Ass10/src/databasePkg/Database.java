
package databasePkg;

import java.sql.*;

public class Database {
	static public Connection con;
	static public Statement st;
	
	
	// establishing connection inside constructor 
	public Database() throws ClassNotFoundException, SQLException {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String userName = "db_user1";
		String password = "db_user1";
		
		con = DriverManager.getConnection(url,userName,password);
		st = con.createStatement();
	}
	
	//closes Connection and Statement 
	public void close() throws SQLException {
		st.close();
		con.close();
	}
	
	
}
	





