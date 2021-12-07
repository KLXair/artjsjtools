package com.czc.artjsj.crawl;

import com.czc.artjsj.log.L;
import org.jsoup.select.Elements;

import java.text.ParseException;


public class TestCrawlingMain {
    public static void main(String[] args) throws ParseException {
        String[] webC = {"587786637078", "521903490619", "594078486739", "546646473247", "24185840294", "577271076562",
                "14469270281", "598065029874"};
        for (int i = 0; i < webC.length; i++) {
            Elements elements = CrawlingUtil.crawl().cUrl("https://baidu.com?id=" + webC[i])
                    .cSelect("div[class=tb-pine]").start();
            L.e(webC[i] + "doc content=" + elements.attr("data-catid"));
        }
    }
}
