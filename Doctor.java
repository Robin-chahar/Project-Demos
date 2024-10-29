package HospitalManagementSystem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Doctor {
      
	private Connection con;
	
	public Doctor(Connection con) {
		this.con=con;
	}
	public void viewDoctors() {
		String query= "Select * from doctors";
		try {
			PreparedStatement ps=con.prepareStatement(query);
			ResultSet  rs = ps.executeQuery();
			System.out.println("doctors");
			System.out.println("+------------+---------------+----------------+");
			System.out.println("| Doctor Id  | Doctor Name   | Specialization |");
			System.out.println("+------------+---------------+----------------+");
			while(rs.next()) {
				int id=rs.getInt("id");
				String name = rs.getString("name");
				String Specialization = rs.getString("specialization");
				System.out.printf("|      %-5s | %13s | %12s   |\n",id,name,Specialization);
			System.out.printf("+------------+---------------+----------------+\n");
			
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
			public boolean getDoctoryId(int id) {
				String query="select * from doctors where id=?";
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
