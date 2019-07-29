package DBAccess;


import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


public class DBaccess {

    protected MongoDatabase mongoDB;
    protected MongoCollection currentCollection;

    protected DBaccess(String dbName){
         mongoDB= MongoClients.create("mongodb://localhost:27017").getDatabase(dbName);
    }

    public void setCollection(String collectionName){
        currentCollection = mongoDB.getCollection(collectionName);
    }
            }
