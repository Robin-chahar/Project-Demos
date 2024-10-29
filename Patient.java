package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
     
	private Connection con;
	private Scanner sc;
	
	public Patient(Connection con, Scanner sc) {
		this.con=con;
	    this.sc=sc;
	}
	public void addpatient() {
		//In this table we have already given id that is Auto-increment so here we don't need to add the values for id fields...
		System.out.println("Enter your name: ");
		String name=sc.next();
		System.out.println("Enter your Age: ");
		int age =sc.nextInt();
		System.out.println("Enter Patient Gender: ");
		String gender=sc.next();
		try {
			String query= "Insert into patients(name,age,gender)values(?,?,?)";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1,name);
			ps.setInt(2, age);
			ps.setString(3, gender);
			int affectedRows = ps.executeUpdate();
			if(affectedRows>0) {
				System.out.println("patient added Succesfully!!");
			}
			else
				System.out.println("Failed to add Patient");
		}catch(SQLException e) {
			e.printStackTrace();
		}
		}
	public void viewPatient() {
		String query= "Select * from patients";
		try {
			PreparedStatement ps=con.prepareStatement(query);
			ResultSet  rs = ps.executeQuery();
			System.out.println("patients");
			System.out.println("+------------+---------------+---------+--------+");
			System.out.println("| Patient Id | Patient Name  | Age     | Gender |");
			System.out.println("+------------+---------------+---------+--------+");
			while(rs.next()) {
				int id=rs.getInt("id");
				String name = rs.getString("name");
				int age=rs.getInt("age");
				String gender = rs.getString("gender");
				System.out.printf("|     %-6s | %8s      |%4s     |%5s   |\n",id,name,age,gender);
			System.out.println("+------------+---------------+---------+--------+");
			
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
			public boolean getPatientbyId(int id) {
				String query="select * from patients where id=?";
				try {
					PreparedStatement ps=con.prepareStatement(query);
					ps.setInt(1, id);
					ResultSet rs=ps.executeQuery();
					if(rs.next()) {
						return true;
					}else {
						return false;
					}
				}
				catch(SQLException e) {
					e.printStackTrace();
				}
				return false;

          }
    }

