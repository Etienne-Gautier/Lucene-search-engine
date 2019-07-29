package SearchUI;

import DBAccess.DBaccess;
import DBAccess.WebDocumentDBA;
import Entities.WebDocument;
import Index.Indexer;

import java.util.ArrayList;

public class Model {
    WebDocumentDBA webDocumentDBA;
    Indexer indexer;
    public Model(){
        webDocumentDBA = new WebDocumentDBA("nutchdb");
        webDocumentDBA.setCollection("2_webpage");
        ArrayList<WebDocument> activeWebDocuments = webDocumentDBA.getActiveDocuments();
        System.out.println(String.format("Retrieved parsed documents from DB, %s docs in total", activeWebDocuments.size()));
        long t1 = System.nanoTime();
        indexer = new Indexer();
        indexer.indexDocuments(activeWebDocuments);
        t1 = System.nanoTime() - t1;
        System.out.println(String.format("All documents indexed in %s ms", (float) t1/1000000));
    }


    private ArrayList<WebDocument> searchQuery(String queryString, int topN){
        String[] resultUrls = indexer.getQueryMatches(queryString, topN);

        System.out.println(String.format("%s results found", resultUrls.length));

        return webDocumentDBA.getWebDocument(resultUrls);

    }
    public String searchHTML(String queryString){
        ArrayList<WebDocument> rawResults= searchQuery(queryString, 20);
        StringBuilder result = new StringBuilder();
        for(WebDocument rawResult: rawResults){
            result.append(rawResult.toHTML());
        }
        System.out.println("end of model");
        return result.toString();

    }
}
