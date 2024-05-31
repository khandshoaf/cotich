package crawl;

import crawl.Entity.Truyen;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CotichBot extends BaseBot{
    private final String baseUrl;
    private ExecutorService executor;
    public CotichBot(String baseUrl) {
        super(baseUrl, 5, 1000);
        this.baseUrl = baseUrl;
        this.executor = Executors.newFixedThreadPool(5);
    }
    @Override
    protected void crawl(String baseUrl){
        super.crawl(baseUrl);
        for (String url : visitedUrls) {
            processDataHome(url);
        }
    }
    protected void processDataHome( String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            String url2 = "div[class=\"block-news\"]";
            Elements blockNews = doc.select(url2);

            if (url.equals(baseUrl)) {
                Map<String, String> genreUrlMap = extractGenreUrls(doc, ".menuTop a");
                System.out.println("Các thể loại truyện cổ tích :");
                for (Map.Entry<String, String> entry : genreUrlMap.entrySet()) {
                    System.out.println(entry.getKey() + " - " + entry.getValue());
                }
                System.out.println("----------------------------------------------");
            }

            for (Element news : blockNews) {
                String categories = news.select("a").attr("title");
                String urlcategory = news.select("a").attr("href");

                String url3 = "div[class=\"list-news\"]";
                Elements link1 = news.select(url3);
                for (Element link2 : link1) {

                    String url4 = "div[class=\"item \"]";
                    Elements link3 = link2.select(url4);
                    for (Element link4 : link3) {
                        String newsTitle = link4.select("a").attr("title");
                        String newsUrl = link4.select("a").attr("href");
                        String imageUrl = link4.select("img").attr("src");
                        String newsContent = link4.select("p").text();

                        System.out.println("Thể Loại: " + categories);
                        System.out.println("URLCategory: " + urlcategory);
                        System.out.println("Tên Truyện: " + newsTitle);
                        System.out.println("Nội dung: " + newsUrl);
                        System.out.println("urlImg: " + imageUrl);
                        System.out.println("Mô tả: " + newsContent);
                        System.out.println("-----------------------");
                    }
                    String url5 = "div[class=\"item  item3\"]";
                    Elements link5 = link2.select(url5);
                    for (Element link6 : link5) {
                        String newsTitle = link6.select("a").attr("title");
                        String newsUrl = link6.select("a").attr("href");
                        String imageUrl = link6.select("img").attr("src");
                        String newsContent = link6.select("p").text();

                        System.out.println("Thể Loại: " + categories);
                        System.out.println("URLCategory: " + urlcategory);
                        System.out.println("Tên Truyện: " + newsTitle);
                        System.out.println("Nội dung: " + newsUrl);
                        System.out.println("urlImg: " + imageUrl);
                        System.out.println("Mô tả: " + newsContent);
                        System.out.println("-----------------------");
                    }
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }
    private Map<String, String> extractGenreUrls(Document doc, String selector) {
        Elements genreElements = doc.select(selector);
        Map<String, String> genreUrlMap = new HashMap<>();
        for (Element genreElement : genreElements) {
            String genre = genreElement.text();
            String url = genreElement.attr("abs:href");
            genreUrlMap.put(genre, url);
        }
        return genreUrlMap;
    }
}

