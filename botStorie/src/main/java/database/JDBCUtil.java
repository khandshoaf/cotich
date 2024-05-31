//package database;
//
//import com.mysql.cj.jdbc.Driver;
//import crawl.Entity.Truyen;
//
//import java.sql.*;
//
//public class JDBCUtil {
//    public static Connection getConnection() {
//        Connection c = null;
//        try {
//            Driver driver = new Driver();
//            DriverManager.registerDriver(new Driver());
//
//            final String URL = "jdbc:mysql://localhost:3306/dbStories";
//            final String USER = "root";
//            final String PASSWORD = "123456";
//
//            String sql = "INSERT INTO truyentranh (title, img, linkUrl, info, sapo) VALUES (?, ?, ?, ?, ?)";
//            Truyen truyen = new Truyen(title, img, linkUrl, info, sapo);
//
//            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
//                 PreparedStatement statement = conn.prepareStatement(sql)) {
//
//                // Kiểm tra sự tồn tại của dữ liệu trước khi chèn
//                String query = "SELECT COUNT(*) AS count FROM truyentranh WHERE title = ? AND linkUrl = ?";
////                    String selectQuery "SELECT COUNT(*) AS count FROM truyentranh WHERE title = ? AND linkUrl = ?";
//                PreparedStatement selectStatement = conn.prepareStatement(query);
//                selectStatement.setString(1, truyen.getHeader());
//                selectStatement.setString(2, truyen.getUrl());
//
//                ResultSet resultSet = selectStatement.executeQuery();
//                if (resultSet.next()) {
//                    int count = resultSet.getInt("count");
//                    if (count == 0) {
//                        // Chèn dữ liệu nếu không có bản ghi trùng lặp
//                        statement.setString(1, truyen.getHeader());
//                        statement.setString(2, truyen.getImg());
//                        statement.setString(3, truyen.getUrl());
//                        statement.setString(4, truyen.getInfo());
//                        statement.setString(5, truyen.getContent());
//                        statement.executeUpdate();
//                    } else {
//                        System.out.println("Bản ghi trùng lặp. Bỏ qua chèn dữ liệu.");
//                    }
//                }
//
//                // Đóng kết nối
//                resultSet.close();
//                selectStatement.close();
//                statement.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return c;
//    }
//
//    public static void colseConection(Connection c){
//        try {
//            c.close();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
