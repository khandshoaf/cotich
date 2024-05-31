package crawl;

import com.sun.source.doctree.SeeTree;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BaseBot{
    private final String baseUrl;
    private final int maxThreads;
    protected boolean isRunning = false;
    protected long lastRun;
    private final long restTime;
    private final ExecutorService executorService;
    final List<String> visitedUrls = new ArrayList<>();
//    final Set<String> visitedUrls;

    public BaseBot(String baseUrl, int maxThreads, long restTime) {
        this.baseUrl = baseUrl;
        this.maxThreads = Math.max(maxThreads, 5);
        this.restTime = restTime;
        this.executorService = Executors.newFixedThreadPool(this.maxThreads);
//        this.visitedUrls = new HashSet<>();
    }

    public void startCrawling() {
        crawl(baseUrl);
        executorService.shutdown();
    }
    public Elements display(String url, String type) {
        Elements datas = null;
        try {
            Document doc = Jsoup.connect(url).get();
            datas = doc.select(type);
        } catch (IOException e) {
        }
        return datas;
    }

    protected void crawl(String url) {
        try {
            if (!visitedUrls.contains(url)) {
                Document doc = Jsoup.connect(url).get();
                visitedUrls.add(url);
                Elements links = doc.select(".menuTop a[href]");
                // Get all the links inside the menuTop class
                for (Element link : links) {
                    String linkHref = link.attr("href");
                    if (!visitedUrls.contains(linkHref)) {
                        visitedUrls.add(linkHref);
                        // Upon there, iterate the web inside
                        crawlPage(linkHref);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Lấy dữ liệu thất bại " + url + ": " + e.getMessage());
        }
    }

    protected void crawlPage(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            Elements pagings = doc.select(".paging a[href]");
            for (Element page : pagings) {
                String pageHref = page.attr("href");
                if (!visitedUrls.contains(pageHref) && !pageHref.endsWith("/1.html")) {
                    visitedUrls.add(pageHref);
                    crawlPage(pageHref);
                }
            }
        } catch (IOException e) {
            System.err.println("Lấy dữ liệu thất bại " + url + ": " + e.getMessage());
        }
    }
//    private void printVisitedUrls() {
//        for(String s : visitedUrls){
//            System.out.println(s);
//        }
//    }

//    public void run() {
//        System.out.println("running");
//        this.isRunning = true;
//    }
//
//    public void complete() {
//        System.out.println("complete");
//        this.isRunning = false;
//        this.lastRun = System.currentTimeMillis();
//    }
//
//    public boolean isRunning() {
//        return this.isRunning;
//    }

    public boolean isNeedRun(long now) {
        System.out.println(String.format("isRunning[%s] - last[%s] - rest[%s] - now[%s]", this.isRunning, this.lastRun, this.restTime, now));
        return !this.isRunning && this.lastRun + this.restTime < now;
    }
}
