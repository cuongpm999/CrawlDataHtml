package vn.bk.services.dynasty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import vn.bk.config.Constant;
import vn.bk.models.dynasty.Dynasty;
import vn.bk.repositories.dynasty.DynastyRepository;
import vn.bk.repositories.dynasty.IDynastyRepository;

public class CrawlDynasty implements Runnable{
    
    private String urlOrigin;
    private IDynastyRepository dynastyRepository;
    
    public CrawlDynasty(){
        this.dynastyRepository = new DynastyRepository();
        this.urlOrigin = Constant.URL_ORIGIN;

        ExecutorService executorService = Executors.newFixedThreadPool(Constant.NUMBER_THREAD);

        executorService.submit(this);
    }
    
    private int getNumTotalPage(String urlAccess){
        try {
            Document document = Jsoup.connect(urlAccess).get();
            Elements elms = document.getElementsByClass("com-content-category-blog__counter counter float-end pt-3 pe-2");
        return elms.isEmpty() ? 0 : Integer.parseInt(elms.text().split("/")[1].trim());
        } catch (IOException ex) {
            ex.printStackTrace();
            return 0;
        }
    }
    
    private int getMaxTitleInOnePage(String urlAccess){
        try {
            Document document = Jsoup.connect(urlAccess).get();
            return document.getElementsByClass("page-header").size();
        } catch (IOException ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    private List<Dynasty> crawlAllDynasty(){
        List<Dynasty> dynasties = new ArrayList<>();
        try {
            String category = "/dong-lich-su";
            String urlAccess = urlOrigin + category;
            
            int totalPage = getNumTotalPage(urlAccess);
            int maxNumInOnePage = getMaxTitleInOnePage(urlAccess);
            int currentPage = 1;
            
            String title = "";
            String description = "";
            
            Document document;
            
            Dynasty dynasty;
            
            if(maxNumInOnePage > 0){
                while(currentPage <= totalPage){
                    if (currentPage > 1){
                    urlAccess = urlOrigin + category + "?start=" + (currentPage - 1) * maxNumInOnePage;
                }
                document = Jsoup.connect(urlAccess).get();
                Elements elmBlockContents = document.getElementsByClass("item-content");
                for(int i = 0; i < elmBlockContents.size(); i++){
                    dynasty = new Dynasty();
                    description = "";
                    title = elmBlockContents.get(i).getElementsByClass("page-header")
                            .select("h2").select("a[href]").text();
                    Elements elmDescriptions = elmBlockContents.get(i).select("h1, h2, h3, h4, h5, h6, p").not(".readmore");
                    for(int j = 0; j < elmDescriptions.size(); j++){
                        if (elmDescriptions.get(j).text().equals("")){
                            continue;
                        }
                        if (j > 0){
                            description += " ";
                        }
                        description += elmDescriptions.get(j).text();
                        if(j == 0){
                            description += ".";
                        }
                    }
                    dynasty.setTitle(title);
                    dynasty.setDescription(description);
                    dynasties.add(dynasty);
                }
                currentPage++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dynasties;
    }
    
    @Override
    public void run() {
        if (Constant.CRAWL_DYNASTY) {
            System.out.println("========== Start crawl dynasty ==========");
            List<Dynasty> dynasties = crawlAllDynasty();
            dynastyRepository.addMany(dynasties);
            dynastyRepository.save();
            System.out.println("========== Done crawl dynasty ==========");
        }
    }
}
