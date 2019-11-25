package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class Main{
	public static void main(String[] args) throws Exception{
	//TODO Auto-generated method stub
	
	createTable();// getConnection and create tables
	}

	public static void createTable() throws Exception{
		try{
			Connection con=getConnection();

			//student 
			PreparedStatement createStudentTable = con.prepareStatement("CREATE TABLE IF NOT EXISTS student(studentId int NOT NULL AUTO_INCREMENT, name varchar(25), email varchar(50), status varchar(25), paymentAccount varchar (25), PRIMARY KEY(studentId))");
			//create.executeUpdate();
			createStudentTable.execute();


			//teacher
			PreparedStatement createTeacherTable = con.prepareStatement("CREATE TABLE IF NOT EXISTS teacher(teacherId int NOT NULL AUTO_INCREMENT, name varchar(25), password varchar(20), email varchar(50), PRIMARY KEY(teacherId))");
			createTeacherTable.excute();

			//course
			PreparedStatement createCourseTable = con.prepareStatement("CREATE TABLE IF NOT EXISTS course(courseId int NOT NULL AUTO_INCREMENT, name varchar(25), PRIMARY KEY(courseId))");
			createCourseTable.excute();

			//courseInfo
			PreparedStatement createCourseInfoTable= con.prepareStatement("CREATE TABLE IF NOT EXISTS courseInfo(courseInfoId int NOT NULL AUTO_INCREMENT,time varchar(25), classroom varchar(20), courseId int, PRIMARY KEY(courseInfoId))");
			createCourseInfoTable.excute();



		}catch(Exception e){System.out.println(e);}
		finally{
			System.out.println("Function Complete.");
		};
	}

	public static Connection getConnection() theows Exception{
	try{
	String driver="com.mysql.jdbc.Driver";
	String url="jdbc:mysql://localhost:3306/database";
	String username="group4";
	String password="lambda";
	Class.forName(driver);

	Connection conn=DriverManager.getConnection(url,username,password);
	System.out.println("SQL Connected");
	return conn;
	}catch(Exception e){System.out.println(e);}

	return null;
	}
}