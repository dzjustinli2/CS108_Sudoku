package assign3;

import java.sql.*;

public class Metropolis {
	private String account = MyDBInfo.MYSQL_USERNAME;
	private String password = MyDBInfo.MYSQL_PASSWORD;
	private String server = MyDBInfo.MYSQL_DATABASE_SERVER;
	private String database = MyDBInfo.MYSQL_DATABASE_NAME;
	private Connection con;
	private Statement stmt;

	public Metropolis() {
		openDB();
		closeDB();
	}
	
	public ResultSet add(String metro, String cont, String pop){
		openDB();
		try{
			String modQuery = "INSERT INTO metrolpolises VALUES";
			modQuery = modQuery + "("+metro+","+cont+","+pop+");";
			stmt.executeUpdate(modQuery);
			closeDB();
			return search(metro,cont,pop,false,true); 
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	
	public ResultSet search(String metro, String cont, String pop, boolean popLarger, boolean exact){
		openDB();
		try{
			String idempQuery = "SELECT * FROM metropolises;";// WHERE ";
			idempQuery = idempQuery + predicate(metro,cont,pop,popLarger,exact);
			ResultSet st = stmt.executeQuery(idempQuery);
			closeDB();
			return st; 
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	
	public void re_source(){
		openDB();
		try{
			stmt.executeUpdate("SOURCE metropolises.sql;");
		}catch(SQLException e){
			e.printStackTrace();
		}
		closeDB();
	}
	
	private String predicate(String metro, String cont, String pop, boolean popLarger, boolean exact){
		return "";
	}
	
	private void openDB(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection
				("jdbc:mysql://" + server,account,password);
			stmt = con.createStatement();
			stmt.executeQuery("USE "+database);
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}	
	}
	
	private void closeDB(){
		try{
			con.close();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}

}
