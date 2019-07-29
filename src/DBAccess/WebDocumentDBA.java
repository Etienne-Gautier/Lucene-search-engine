package DBAccess;

import Entities.WebDocument;
import com.mongodb.Block;
import org.bson.Document;

import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;

public class WebDocumentDBA extends DBaccess {
    final private static int ACTIVE_STATUS = 2;
    final private static String STATUS_FIELD = "status";
    final private static String URL_FIELD = "baseUrl";

    public WebDocumentDBA(String dbName){
        super(dbName);
    }

    public ArrayList<WebDocument> getActiveDocuments(){
        ArrayList<WebDocument> result = new ArrayList<>();
        this.currentCollection.find(eq(WebDocumentDBA.STATUS_FIELD, WebDocumentDBA.ACTIVE_STATUS)).forEach((Block<Document>) (Document record) -> result.add(
                new WebDocument(
                        record.getString(URL_FIELD),
                        record.getString("title"),
                        record.getString("text")
                )
        ));
        return result;
    }



    public ArrayList<WebDocument>  getWebDocument(String[] urls){

        ArrayList<WebDocument> result = new ArrayList<>(urls.length);
        for(String url: urls){
            Document record = (Document) currentCollection.find(eq(WebDocumentDBA.URL_FIELD, url)).first();
            result.add(
                    new WebDocument(
                            record.getString(URL_FIELD),
                            record.getString("title"),
                            record.getString("text")
                    )
            );
        }


        return result;
    }
}
