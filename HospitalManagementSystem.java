package HospitalManagementSystem;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class HospitalManagementSystem {
     private static final  String url="jdbc:mysql://127.0.0.1:3306/hospital";
     private static final String username="root";
     private static final String password="Robin@#123";
	public static void main(String[] args) {
         try {
        	 Class.forName("com.mysql.cj.jdbc.Driver");
         }
         catch(ClassNotFoundException e) {
        	 e.printStackTrace();
         }
         Scanner sc=new Scanner(System.in);
         try {
        	Connection con=DriverManager.getConnection(url,username,password);
        	Patient pt=new Patient(con,sc);
        	Doctor doc=new Doctor(con);
        	while(true) {
        		System.out.println("----HOSPITAL MANGEMENT SYSTEM ++++]----");
        		System.out.println("1. Add patient");
        		System.out.println("2. View patients");
        		System.out.println("3. View Doctors");
        		System.out.println("4. Book Appointment");
        		System.out.println("5. Exit");
        		System.out.println("Enter Your Choice");
        		int choice=sc.nextInt();
        		switch(choice){
        		   case 1:
        			    //Add patient
        			   pt.addpatient();
        			   System.out.println();
        			   break;
        			case 2:
        				//view patient
        				pt.viewPatient();
        				System.out.println();
        				break;
        			case 3:
        				//View doctors
        				doc.viewDoctors();
        				System.out.println();
        				break;
        			case 4:
        				//Book Appointment
        				bookAppointment(pt,doc,con,sc);
        				System.out.println();
        				break;
        				
        			case 5:
        				return;
        			default:
        			    System.out.println("Enter valid choice");
        				
        		}
        				
        		
        	}
         }catch(SQLException e) {
        	 e.printStackTrace();
         }
         
	}
      public static void bookAppointment(Patient pt,Doctor doc,Connection con,Scanner sc) {
    	  System.out.println("Enter patient ID:");
    	  int patientid=sc.nextInt();
    	  System.out.println("Enter Doctor ID");
    	  int doctorId=sc.nextInt();
    	  System.out.println("Enter Appointment Date (YYYY-MM-DD)");
    	  String appointmentDate=sc.next();
    	  if(pt.getPatientbyId(patientid) && doc.getDoctoryId(doctorId)) {
    		  if(checkDoctorAvailability(doctorId,appointmentDate,con)) {
    			  String query="Insert into appointments(patient_id, doctor_id, appointment_date) values(?,?,?)";
    			  try {
    				  PreparedStatement ps=con.prepareStatement(query);
    				  ps.setInt(1,patientid);
    				  ps.setInt(2, doctorId);
    				  ps.setString(3,appointmentDate);
    				  int rowsAffected=ps.executeUpdate();
    				  if(rowsAffected>0) {
    					  System.out.println("Appointment Booked");
    				  }
    				  else
    				  {
    					  System.out.println("Failed to the Book appointment");
    				  }
    			  }catch(SQLException e) {
    				  e.printStackTrace();
    			  }
    		  }
    		  else {
    			  System.out.println("Doctor is not available for this Date");
    		  }
    	  }
    	  else {
    		  System.out.println("Either doctor or patient doesn't exist!!!");
    	  }
      }
	private static boolean checkDoctorAvailability(int doctorId, String appointmentDate,Connection con) {
		String query="Select count(*) from appointments where doctor_id=? And appointment_date=?";
		try {
			PreparedStatement ps= con.prepareStatement(query);
			ps.setInt(1, doctorId);
			ps.setString(2, appointmentDate);
			ResultSet rs=ps.executeQuery();
			if(rs.next()) {
				int count=rs.getInt(1);
				if(count==0) {
					return true;
				}
				else {
					return false;
				}
						}
		} 
		catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
