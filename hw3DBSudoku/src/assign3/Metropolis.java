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
		if(metro.length() == 0 || cont.length() == 0 || pop.length() == 0) return null;
		try{
			String modQuery = "INSERT INTO metropolises VALUES";
			modQuery = modQuery + "(\""+metro+"\",\""+cont+"\",\""+pop+"\");";
			stmt.executeUpdate(modQuery);
			return search(metro,cont,pop,false,true); 
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	
	public ResultSet search(String metro, String cont, String pop, boolean popLarger, boolean exact){
		try{
			String idempQuery = "SELECT * FROM metropolises ";
			idempQuery = idempQuery + predicate(metro,cont,pop,popLarger,exact) + ";";
			ResultSet st = stmt.executeQuery(idempQuery);
			return st; 
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	
	public void re_source(){
		try{
			//This command doesn't work
			//Need to read in from file line by line
			//and even then we have to check if the line is
			//idempotent or destructive (query or update)
			stmt.executeQuery("SOURCE metropolises.sql;");
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	private String predicate(String metro, String cont, String pop, boolean popLarger, boolean exact){
		String query = "WHERE ";
		if(metro.equals("") && cont.equals("") && pop.equals("")){
			return "";
		}
		if(!metro.equals("")){
			if(exact){
				query = query + "metropolis = " + "\"" + metro + "\" ";
			}else{
				query = query + "metropolis LIKE " + "\"%" + metro + "%\" ";
			}
		}
		if(!cont.equals("")){
			if(!metro.equals("")){
				query = query + "AND ";
			}
			if(exact){
				query = query + "continent = " + "\"" + cont + "\" ";
			}else{
				query = query + "continent LIKE " + "\"%" + cont + "%\" ";
			}
		}
		if(!pop.equals("")){
			if(!metro.equals("") || !cont.equals("")){
				query = query + "AND ";
			}
			if(popLarger){
				query = query + "population > " + pop;
			}else{
				query = query + "population <= " + pop;
			}
		}
		return query;
	}
	
	public void openDB(){
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
	
	public void closeDB(){
		try{
			con.close();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}

}
