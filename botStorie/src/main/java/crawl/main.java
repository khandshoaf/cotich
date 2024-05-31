package crawl;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class main {
    public static void main(String[] args) {
        String baseUrl = "https://cotich.net/";
        List<BaseBot> bots = new ArrayList<>();
        bots.add(new CotichBot(baseUrl));
        bots.add(new StoryBot(baseUrl));

        BotControl botControl = new BotControl(bots);
        botControl.run();
    }
    public static class BotControl {
        private final List<BaseBot> bots;
        private final Timer timer = new Timer();

        public BotControl(List<BaseBot> bots) {
            this.bots = bots;
            System.out.println("Khởi tạo bots");
            for (BaseBot bot : this.bots) {
                System.out.println("Khởi tạo bot " + bot.getClass().getSimpleName() + " thành công!");
            }
        }

        public void run() {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("Kiểm tra tiến trình");
                    long now = System.currentTimeMillis();
                    for (BaseBot bot : bots) {
                        if (bot.isNeedRun(now)) {
                            bot.startCrawling();
                        }
                    }
                }
            }, 0, 9000);
        }
    }
}
