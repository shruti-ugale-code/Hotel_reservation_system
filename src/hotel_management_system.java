
import java.util.Scanner;
import java.sql.*;

public class hotel_management_system {
    private static final String url = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String username = "root";
    private static final String password = "Shruti@123";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            while (true) {
                System.out.println();
                System.out.println("HOTEL MANAGMENT SYSTEM");
                Scanner scanner = new Scanner(System.in);
                System.out.println("1.Reserve a room");
                System.out.println("2.View reservations");
                System.out.println("3.get room number");
                System.out.println("4.Update reservation");
                System.out.println("5 Delete Reservation");
                System.out.println("0.exit");
                System.out.println("Choose an option  :");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        reserveRoom(connection, scanner);
                        break;
                    case 2:
                        viewReservations(connection);
                        break;

                    case 3:
                        getRoomNumber(connection, scanner);
                        break;
                    case 4:
                        updateReservation(connection, scanner);
                        break;
                    case 5:
                        deleteReservation(connection, scanner);
                        break;
                    case 0:
                        exit();
                        scanner.close();
                        return;
                    default:
                        System.out.println("you enter invalid choice");
                }

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void reserveRoom(Connection connection, Scanner scanner) {
        try {
            System.out.println("Enter guest name:");
            String guestName = scanner.next();
            scanner.nextLine();
            System.out.println("Enter room no:");
            int roomNo = scanner.nextInt();
            System.out.println("Enter contact no:");
            String contactNo = scanner.next();

            String sql = "insert into reservations(guest_name,room_number,contact_number)" + "values('" + guestName + "'," + roomNo + ",'" + contactNo + "')";

            try (Statement statement = connection.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);
                if (affectedRows > 0) {
                    System.out.println("reservation sucessfull");
                } else {
                    System.out.println("reservation fail");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }




}
    private static void viewReservations(Connection connection) throws SQLException {
        String sql = "select reservation_id,guest_name,room_number,contact_number,reservation_date from reservations";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            System.out.println("current reservation");
            System.out.println("+--------------+-------------+--------------+----------------+----------------+");
            System.out.println("|Reservation ID| guest       +Room number   +contact no      +Reservation date|");
            System.out.println("+--------------+-------------+--------------+----------------+----------------+");

            while(resultSet.next()){
                int reservationId=resultSet.getInt("reservation_id");
                String guestname=resultSet.getString("guest_name");
                int roomno=resultSet.getInt("room_number");
                String contactno=resultSet.getString("contact_number");
                String reservationdate=resultSet.getString("reservation_date");

                System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19s |\n",
                        reservationId, guestname, roomno, contactno, reservationdate);


            }
            System.out.println("+--------------+-------------+--------------+----------------+----------------+");


        }
    }
    private static void getRoomNumber(Connection connection,Scanner scanner){
        try{
            System.out.println("enter reservation id");
            int reservationId=scanner.nextInt();
            System.out.println("enter guest name:");
            String guestName=scanner.next();

            String sql="select room_number from reservations"+
                    "where reservation_id="+ reservationId+
                    "and guest_name='"+guestName+"'";
            try(Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery(sql)){

                if(resultSet.next()){
                    int roomNumber=resultSet.getInt("room_number");
                    System.out.println("Room number for reservation id"+reservationId+
                            "and Guest"+guestName+"is:"+roomNumber);

                }else{
                    System.out.println("reservation not found for the give idand guest name");

                }
            }


        }catch (SQLException e){
            e.printStackTrace();

        }
    }
    private static void updateReservation(Connection connection,Scanner scanner){
        try{
            System.out.println("enter reservation id to update:");
            int reservation_id=scanner.nextInt();
            scanner.nextLine();

            if(!reservationExists(connection,reservation_id)){
                System.out.println("reservation not found for the given id");
                return;

            }
            System.out.println("Enter new guest name:");
            String newguestname=scanner.nextLine();
            System.out.println("Enter new room no:");
            int newroonNo=scanner.nextInt();
            System.out.println("enter new contact no");
            String newcontact=scanner.next();

            String sql="update reservations set guest_name="+newguestname+"',"+
                    "room_number="+newroonNo+","+
                    "contact_number='"+newcontact+"' "+
                    "where reservation_id="+reservation_id;

            try(Statement statement=connection.createStatement()){
                int affectedRows=statement.executeUpdate(sql);
                if(affectedRows>0){
                    System.out.println("reservation update sucessfull");
                }else{
                    System.out.println("reservation update fail");
                }
            }

        }catch (SQLException e){
            e.printStackTrace();

        }
    }
    private static void deleteReservation(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter reservation ID to delete: ");
            int reservationId = scanner.nextInt();

            if (!reservationExists(connection, reservationId)) {
                System.out.println("Reservation not found for the given ID.");
                return;
            }

            String sql = "DELETE FROM reservations WHERE reservation_id = "+reservationId;
            try (Statement statement= connection.createStatement()){
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows > 0) {
                    System.out.println("Reservation deleted successfully!");
                } else {
                    System.out.println("Reservation deletion failed.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private static boolean reservationExists(Connection connection, int reservation_id) {
        try {
            String sql = "SELECT reservation_id FROM reservations WHERE reservation_id = " + reservation_id;

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Handle database errors as needed
        }
    }
    public static void exit() throws InterruptedException{
        System.out.print("exiting system");
        int i=7;
        while(i!=0){
            System.out.print("*");
            Thread.sleep(300);
            i--;
        }
        System.out.println();
        System.out.println("thankyou for using reservation system");
    }





}

