/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.bk.services.character;

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
import vn.bk.models.character.HistoricalCharacter;
import vn.bk.repositories.character.HistoricalCharacterRepository;
import vn.bk.repositories.character.IHistoricalCharacterRepository;

/**
 *
 * @author cuongpham
 */
public class CrawlHistoricalCharacter implements Runnable {
    private final static List<String> PATTERN_BIRTH = List.of("Sinh");
    private final static List<String> PATTERN_DEATH = List.of("Mất");
    private final static List<String> PATTERN_FATHER = List.of("Thân phụ", "Cha", "Bố", "Ba");
    private final static List<String> PATTERN_MOTHER = List.of("Thân mẫu", "Mẹ");
    private final static List<String> PATTERN_DYNASTY = List.of("Triều đại");
    private final static List<String> PATTERN_REAL_NAME = List.of("Tên húy", "Tên thật");

    private final IHistoricalCharacterRepository historicalCharacterRepository;
    private String urlOrigin;

    public CrawlHistoricalCharacter() {
        this.historicalCharacterRepository = new HistoricalCharacterRepository();
        this.urlOrigin = Constant.URL_ORIGIN;

        ExecutorService executorService = Executors.newFixedThreadPool(Constant.NUMBER_THREAD);

        executorService.submit(this);
    }

    private List<String> getLinkDetailCharacterInOnePage(String urlPage) {
        try {
            List<String> linkDetailCharacter = new ArrayList<>();
            Document documentPage = Jsoup.connect(urlPage).get();
            Elements elmItems = documentPage.getElementsByClass("com-content-category-blog__items blog-items ");
            for (Element elmItem : elmItems) {
                Elements elmRows = elmItem.getElementsByTag("h2");

                for (Element elmRow : elmRows) {
                    Elements elmAs = elmRow.getElementsByTag("a");

                    for (Element elmA : elmAs) {
                        String linkDetail = elmA.absUrl("href");
                        linkDetailCharacter.add(linkDetail);
                    }
                }

            }
            return linkDetailCharacter;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private HistoricalCharacter getHistoricalCharacter(String url) {
        try {
            HistoricalCharacter historicalCharacter = new HistoricalCharacter();
            Document documentPage = Jsoup.connect(url).get();
            Elements elmItems = documentPage.getElementsByClass("com-content-article item-page page-list-items");

            for (Element elmItem : elmItems) {

                // get fullName
                Elements elmHeaders = elmItem.getElementsByClass("page-header");
                for (Element elmHeader : elmHeaders) {
                    Elements elmH2s = elmHeader.getElementsByTag("h2");
                    for (Element elmH2 : elmH2s) {
                        historicalCharacter.setFullName(elmH2.text());
                    }
                }

                // get table
                if (elmItem.getElementsByClass("infobox") != null && elmItem.getElementsByClass("infobox").first() != null) {
                    Elements elmTable = Objects.requireNonNull(elmItem.getElementsByClass("infobox").first()).getElementsByTag("table");
                    Elements elmBody = Objects.requireNonNull(elmTable.first()).getElementsByTag("tbody");
                    for (Element elmTr : Objects.requireNonNull(elmBody.first()).getElementsByTag("tr")) {

                        // get dateOfBirth
                        if (PATTERN_BIRTH.contains(elmTr.getElementsByTag("th").text())) {
                            historicalCharacter.setBirth(elmTr.getElementsByTag("td").text());
                        }

                        // get dateOfDeath
                        if (PATTERN_DEATH.contains(elmTr.getElementsByTag("th").text())) {
                            historicalCharacter.setDeath(elmTr.getElementsByTag("td").text());
                        }

                        // get father
                        if (PATTERN_FATHER.contains(elmTr.getElementsByTag("th").text())) {
                            historicalCharacter.setFather(elmTr.getElementsByTag("td").text());
                        }

                        // get mother
                        if (PATTERN_MOTHER.contains(elmTr.getElementsByTag("th").text())) {
                            historicalCharacter.setMother(elmTr.getElementsByTag("td").text());
                        }

                        // get dynasty
                        if (PATTERN_DYNASTY.contains(elmTr.getElementsByTag("th").text())) {
                            historicalCharacter.setDynasty(elmTr.getElementsByTag("td").text());
                        }

                        // get realName
                        if (PATTERN_REAL_NAME.contains(elmTr.getElementsByTag("th").text())) {
                            historicalCharacter.setRealName(elmTr.getElementsByTag("td").text());
                        }

                    }
                }

                // get note
                Elements elmNotes = elmItem.getElementsByClass("com-content-article__body").select("p");
                historicalCharacter.setNote(Objects.requireNonNull(elmNotes.first()).text());

            }

            return historicalCharacter;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public void run() {
        if (Constant.CRAWL_HISTORICAL_CHARACTER) {
            System.out.println("========== Running crawl historical character ==========");
            String url = urlOrigin + "/nhan-vat";
            for (int page = 1; page <= 2; page++) {
                if (page != 1) {
                    url = url + "?start=" + (page - 1) * 5;
                }
                List<String> linkDetailCharacters = getLinkDetailCharacterInOnePage(url);

                linkDetailCharacters.forEach(link -> {
                    historicalCharacterRepository.addOne(getHistoricalCharacter(link));
                });

                historicalCharacterRepository.save();
            }
            System.out.println("========== Done crawl historical character ==========");
        }
    }

}
