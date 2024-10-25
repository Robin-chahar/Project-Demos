import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Scanner;
import java.sql.Statement;
import java.sql.ResultSet;



public class HotelReservationSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String username = "root";
    private static final String password = "Robin@#123";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try(  Connection connection = DriverManager.getConnection(url, username, password);
        		Statement st=connection.createStatement();
        		){
            Class.forName("com.mysql.cj.jdbc.Driver");
        
            while(true){
                System.out.println();
                System.out.println("HOTEL MANAGEMENT SYSTEM");
                Scanner scanner = new Scanner(System.in);
                System.out.println("1. Reserve a room");
                System.out.println("2. View Reservations");
                System.out.println("3. Get Room Number");
                System.out.println("4. Update Reservations");
                System.out.println("5. Delete Reservations");
                System.out.println("0. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        reserveRoom(connection, scanner,st);
                        break;
                    case 2:
                        viewReservations(connection,st);
                        break;
                    case 3:
                        getRoomNumber(connection, scanner,st);
                        break;
                    case 4:
                        updateReservation(connection, scanner,st);
                        break;
                    case 5:
                        deleteReservation(connection, scanner,st);
                        break;
                    case 0:
                        exit();
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }

        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

    }

    private static void reserveRoom(Connection connection, Scanner scanner,Statement st) {
        try {
            System.out.print("Enter guest name: ");
            String guestName = scanner.next();
            scanner.nextLine();
            System.out.print("Enter room number: ");
            int roomNumber = scanner.nextInt();
            System.out.print("Enter contact number: ");
            String contactNumber = scanner.next();

            String sql = "INSERT INTO reservations (guest_name, room_number, contact_number) " +
                    "VALUES ('" + guestName + "', " + roomNumber + ", '" + contactNumber + "')";

       
                int affectedRows = st.executeUpdate(sql);

                if (affectedRows > 0) {
                    System.out.println("Reservation successful!");
                } else {
                    System.out.println("Reservation failed.");
                }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewReservations(Connection con,Statement st) throws SQLException {
        String sql = "SELECT reservation_id, guest_name, room_number, contact_number, reservation_date FROM reservations";

        try (ResultSet resultSet = st.executeQuery(sql);
        	) {

            System.out.println("Current Reservations:");
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
            System.out.println("| Reservation ID | Guest           | Room Number   | Contact Number      | Reservation Date        |");
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");

            while (resultSet.next()) {
                int reservationId = resultSet.getInt("reservation_id");
                String guestName = resultSet.getString("guest_name");
                int roomNumber = resultSet.getInt("room_number");
                String contactNumber = resultSet.getString("contact_number");
                String reservationDate = resultSet.getTimestamp("reservation_date").toString();

                // Format and display the reservation data in a table-like format
                System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19s   |\n",
                        reservationId, guestName, roomNumber, contactNumber, reservationDate);
            }

            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
        }
    }


    private static void getRoomNumber(Connection connection, Scanner scanner,Statement st) {
    	System.out.print("Enter reservation ID: ");
        int reservationId = scanner.nextInt();
        System.out.print("Enter guest name: ");
        String guestName = scanner.next();
    	String sql = "SELECT room_number FROM reservations " +
                "WHERE reservation_id = " + reservationId +
                " AND guest_name = '" + guestName + "'";
            try (
                 ResultSet resultSet = st.executeQuery(sql)) {            	          
                if (resultSet.next()) {
                    int roomNumber = resultSet.getInt("room_number");
                    System.out.println("Room number for Reservation ID " + reservationId +
                            " and Guest " + guestName + " is: " + roomNumber);
                } else {
                    System.out.println("Reservation not found for the given ID and guest name.");
                }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void updateReservation(Connection connection, Scanner scanner,Statement st) {
    	 System.out.print("Enter new guest name: ");
         String newGuestName = scanner.nextLine();
         System.out.print("Enter new room number: ");
         int newRoomNumber = scanner.nextInt();
         System.out.print("Enter new contact number: ");
         String newContactNumber = scanner.next();
         System.out.print("Enter reservation ID to update: ");
         int reservationId = scanner.nextInt();
         scanner.nextLine(); // Consume the newline character


         String sql = "UPDATE reservations SET guest_name = '" + newGuestName + "', " +
                 "room_number = " + newRoomNumber + ", " +
                 "contact_number = '" + newContactNumber + "' " +
                 "WHERE reservation_id = " + reservationId;

        try {
             if (!reservationExists(connection, reservationId,st)) {
                 System.out.println("Reservation not found for the given ID.");
                 return;
             }           
                int affectedRows = st.executeUpdate(sql);
                if (affectedRows > 0) {
                    System.out.println("Reservation updated successfully!");
                } else {
                    System.out.println("Reservation update failed.");
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteReservation(Connection connection, Scanner scanner,Statement st) {
    	 System.out.print("Enter reservation ID to delete: ");
         int reservationId = scanner.nextInt();

         if (!reservationExists(connection, reservationId,st)) {
             System.out.println("Reservation not found for the given ID.");
             return;
         }

         String sql = "DELETE FROM reservations WHERE reservation_id = " + reservationId;

            try {
            	 
                int affectedRows = st.executeUpdate(sql);

                if (affectedRows > 0) {
                    System.out.println("Reservation deleted successfully!");
                } else {
                    System.out.println("Reservation deletion failed.");
                }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean reservationExists(Connection connection, int reservationId,Statement st) {
        try {
            String sql = "SELECT reservation_id FROM reservations WHERE reservation_id = " + reservationId;

            try (ResultSet resultSet = st.executeQuery(sql)) {

                return resultSet.next(); // If there's a result, the reservation exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Handle database errors as needed
        }
    }


    public static void exit() throws InterruptedException {
        System.out.print("Exiting System");
        int i = 5;
        while(i!=0){
            System.out.print(".");
            Thread.sleep(1000);
            i--;
        }
        System.out.println();
       System.out.println("Thankyou For using Hotel Managment Reservation System!!!");
    }
    }