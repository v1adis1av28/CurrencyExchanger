//import java.sql.*;
//
//public class SQLiteConnection {
//    protected static Connection conn = connect();
//    private static Connection connect() {
//        try {
//            // db parameters
//            String url = "jdbc:sqlite:C:/sqlite/CurrencyProject.db";
//            // create a connection to the database
//            conn = DriverManager.getConnection(url);
//
//            System.out.println("Connection to SQLite has been established.");
//            Statement stmt = conn.createStatement();
//            ResultSet rs = stmt.executeQuery("SELECT  *  FROM Currencies");
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        } finally {
//            try {
//                if (conn != null) {
//                    conn.close();
//                }
//            } catch (SQLException ex) {
//                System.out.println(ex.getMessage());
//            }
//        }
//        return conn;
//    }
//}