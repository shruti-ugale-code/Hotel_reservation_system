// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import java.sql.*;


public class Main {
    public static void main(String[] args)throws ClassNotFoundException {
        String url="jdbc:mysql://localhost:3306/students";
        String username="root";
        String password="Shruti@123";
        String query="Select * from employees;";

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
            ResultSet rs=stat.executeQuery(query);
            while(rs.next()){
                int id=rs.getInt("id");
                String name=rs.getString("name");
                double salary=rs.getDouble("salary");
                System.out.println();
                System.out.println("==================");
                System.out.println("ID"+id);
                System.out.println("Name"+name);
                System.out.println("salary"+salary);

            }
            rs.close();
            stat.close();
            con.close();


        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        }

}