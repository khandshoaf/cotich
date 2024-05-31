package crawl;

import crawl.Entity.Truyen;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StoryBot extends BaseBot{
    private final String baseUrl;
    private final ExecutorService executor;
    private static final String URL = "jdbc:mysql://localhost:3306/dbStories";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    public StoryBot(String baseUrl) {
        super(baseUrl,5,1000);
        this.baseUrl = baseUrl;
        this.executor = Executors.newFixedThreadPool(5); // Sử dụng pool với số lượng luồng cố định là 5
    }

    @Override
    protected void crawl(String baseUrl){
        super.crawl(baseUrl);
        for (String url : visitedUrls) {
            processStoryData(url);
        }
    }
    protected void processStoryData(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            String type = ".news-item"; // Loại truyện
            Elements data = doc.select(type);
            String tag = ".title > h1 > a";
            Elements tags = doc.select(tag);
            if (!tags.html().isEmpty()) {
                System.out.println("Tags: " + tags.html());
                String[] parts = url.split("/");
                String pagePart = parts[parts.length - 1];
                String pageNumber = pagePart.split("\\.")[0];
                if (!pageNumber.matches("\\d+")) pageNumber = "1";
                System.out.println("Trang: " + pageNumber);
            }


            for (Element item : data) {
                String title = item.select("h4 > a").html();
                String linkUrl = item.select("h4 > a").attr("href");
                String img = item.select(".img > a > img").attr("src");
                String info = item.select(".info-post").text();
                String sapo = item.select(".sapo").text();

                Truyen truyen = new Truyen(title, img, linkUrl, info, sapo);
                System.out.println(truyen);

                String sql = "INSERT INTO truyentranh (title, img, linkUrl, info, sapo) VALUES (?, ?, ?, ?, ?)";

                try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                     PreparedStatement statement = conn.prepareStatement(sql)) {

                    // Kiểm tra sự tồn tại của dữ liệu trước khi chèn
                    String query = "SELECT COUNT(*) AS count FROM truyentranh WHERE title = ? AND linkUrl = ?";
//                    String selectQuery "SELECT COUNT(*) AS count FROM truyentranh WHERE title = ? AND linkUrl = ?";
                    PreparedStatement selectStatement = conn.prepareStatement(query);
                    selectStatement.setString(1, truyen.getHeader());
                    selectStatement.setString(2, truyen.getUrl());

                    ResultSet resultSet = selectStatement.executeQuery();
                    if (resultSet.next()) {
                        int count = resultSet.getInt("count");
                        if (count == 0) {
                            // Chèn dữ liệu nếu không có bản ghi trùng lặp
                            statement.setString(1, truyen.getHeader());
                            statement.setString(2, truyen.getImg());
                            statement.setString(3, truyen.getUrl());
                            statement.setString(4, truyen.getInfo());
                            statement.setString(5, truyen.getContent());
                            statement.executeUpdate();
                        } else {
                            System.out.println("Bản ghi trùng lặp. Bỏ qua chèn dữ liệu.");
                        }
                    }

                    // Đóng kết nối
                    resultSet.close();
                    selectStatement.close();
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
