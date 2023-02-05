/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.bk.services.event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import vn.bk.config.Constant;
import vn.bk.models.event.HistoricalEvent;
import vn.bk.repositories.event.HistoricalEventRepository;
import vn.bk.repositories.event.IHistoricalEventRepository;

/**
 *
 * @author cuongpham
 */
public class CrawlHistoricalEvent implements Runnable {

    private final static List<String> PATTERN_TIME = List.of("Thời gian");
    private final static List<String> PATTERN_LOCATION = List.of("Địa điểm");
    private final static List<String> PATTERN_RESULT = List.of("Kết quả");

    private final IHistoricalEventRepository historicalEventRepository;
    private String urlOrigin;

    public CrawlHistoricalEvent() {
        this.historicalEventRepository = new HistoricalEventRepository();
        this.urlOrigin = Constant.URL_ORIGIN;

        ExecutorService executorService = Executors.newFixedThreadPool(Constant.NUMBER_THREAD);

        executorService.submit(this);

    }

    private List<String> getLinkDetailEventInOnePage(String urlPage) {
        try {
            List<String> linkDetailEvent = new ArrayList<>();
            Document documentPage = Jsoup.connect(urlPage).get();
            Elements elmItems = documentPage.getElementsByClass("com-content-category-blog__items blog-items items-leading ");
            for (Element elmItem : elmItems) {
                Elements elmRows = elmItem.getElementsByTag("h2");

                for (Element elmRow : elmRows) {
                    Elements elmAs = elmRow.getElementsByTag("a");

                    for (Element elmA : elmAs) {
                        String linkDetail = elmA.absUrl("href");
                        linkDetailEvent.add(linkDetail);
                    }
                }

            }
            return linkDetailEvent;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private HistoricalEvent getHistoricalEvent(String url) {
        try {
            HistoricalEvent historicalEvent = new HistoricalEvent();
            Document documentPage = Jsoup.connect(url).get();
            Elements elmItems = documentPage.getElementsByClass("com-content-article item-page");

            for (Element elmItem : elmItems) {

                // get name
                Elements elmHeaders = elmItem.getElementsByClass("page-header");
                for (Element elmHeader : elmHeaders) {
                    Elements elmH1s = elmHeader.getElementsByTag("h1");
                    for (Element elmH1 : elmH1s) {
                        historicalEvent.setName(elmH1.text());
                    }
                }

                // get table
                if (elmItem.getElementsByClass("infobox") != null) {
                    Elements elmTables = elmItem.getElementsByClass("infobox").select("table");
                    for (Element elmTable : elmTables) {
                        Elements elmBodys = elmTable.getElementsByTag("tbody");
                        for (Element elmBody : elmBodys) {
                            Elements elmTrs = elmBody.select("tr");
                            for (Element elmTr : elmTrs) {
                                // get time
                                if (elmTr.getElementsByTag("td") != null
                                        && !elmTr.getElementsByTag("td").isEmpty()
                                        && PATTERN_TIME.contains(elmTr.getElementsByTag("td").get(0).text())) {
                                    historicalEvent.setTime(elmTr.getElementsByTag("td").get(1).text());
                                }

                                // get location
                                if (elmTr.getElementsByTag("td") != null 
                                        && !elmTr.getElementsByTag("td").isEmpty()
                                        && PATTERN_LOCATION.contains(elmTr.getElementsByTag("td").get(0).text())) {
                                    historicalEvent.setLocation(elmTr.getElementsByTag("td").get(1).text());
                                }

                                // get result
                                if (elmTr.getElementsByTag("td") != null 
                                        && !elmTr.getElementsByTag("td").isEmpty()
                                        && PATTERN_RESULT.contains(elmTr.getElementsByTag("td").get(0).text())) {
                                    historicalEvent.setResult(elmTr.getElementsByTag("td").get(1).text());
                                }

                            }
                        }
                    }
                }

                // get note
                Elements elmNotes = elmItem.getElementsByClass("com-content-article__body").select("p");
                for (Element elmNote : elmNotes) {
                    if (!elmNote.text().trim().isEmpty() && !elmNote.text().trim().equals(".")) {
                        historicalEvent.setNote(elmNote.text());
                        break;
                    }
                }
            }

            return historicalEvent;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public void run() {
        if (Constant.CRAWL_HISTORICAL_EVENT) {
            System.out.println("========== Running crawl historical event ==========");
            String url = urlOrigin + "/tu-lieu/quan-su?filter_tag[0]=";
            for (int page = 1; page <= 2; page++) {
                if (page != 1) {
                    url = url + "&start=" + (page - 1) * 5;
                }
                List<String> linkDetailEvents = getLinkDetailEventInOnePage(url);

                linkDetailEvents.forEach(link -> {
                    historicalEventRepository.addOne(getHistoricalEvent(link));
                });

                historicalEventRepository.save();
            }
            System.out.println("========== Done crawl historical event ==========");
        }
    }

}
