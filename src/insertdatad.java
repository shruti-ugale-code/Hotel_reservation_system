// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import java.sql.*;


public class insertdatad {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        String url="jdbc:mysql://localhost:3306/students";
        String username="root";
        String password="Shruti@123";
        String query="delete from employees where id=1;";

        try {
            Class.forName("con.mysql.jdbc.Driver");
            System.out.println("drivers loaded sucessfully");
        }catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try{
            Connection con=DriverManager.getConnection(url,username,password);
            System.out.println("connection establish");
            Statement stat=con.createStatement();
            int roweffected=stat.executeUpdate(query);

            if(roweffected>0){
                System.out.println("deletion sucessfull"+roweffected+"rows affected");
            }else {
                System.out.println("deletion unsussfull");
            }



            stat.close();
            con.close();
            con.close();


        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

    }

}