package Index;

import Entities.WebDocument;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;

import java.io.IOException;
import java.nio.file.FileSystems;

public class Indexer {
    IndexWriter indexWriter;
    IndexReader indexReader;
    Analyzer analyzer;
    final private String INDEX_DIRECTORY = "./index";
    FieldType urlFieldType;
    FieldType contentFieldType;
    FieldType titleFieldType;

    public Indexer(){
        analyzer = new StandardAnalyzer();
        try{
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            // Set the similarity model, disable to use the default analyzer
            //config.setSimilarity(new BM25Similarity());
            indexWriter = new IndexWriter(new SimpleFSDirectory(FileSystems.getDefault().getPath(INDEX_DIRECTORY)), config);
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        urlFieldType = new FieldType();
        urlFieldType.setIndexOptions(IndexOptions.NONE);
        urlFieldType.setStored(true);


        contentFieldType = new FieldType();
        contentFieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
        contentFieldType.setStored(false);
        contentFieldType.setStoreTermVectors(false);
        contentFieldType.setTokenized(true);

        titleFieldType = new FieldType();
        titleFieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
        titleFieldType.setStored(false);
        titleFieldType.setStoreTermVectors(false);
        titleFieldType.setTokenized(true);
    }


    public void indexDocument(WebDocument webDocument) throws IOException{
        if(webDocument != null && webDocument.url != null && webDocument.title != null && webDocument.content != null){
            Document doc = new Document();
            doc.add(new Field("content",
                    webDocument.content,
                    contentFieldType));

            doc.add(new Field("title",
                    webDocument.title,
                    titleFieldType));

            doc.add(new Field("url",
                    webDocument.url,
                    urlFieldType));

            indexWriter.addDocument(doc);
        }

    }

    public void indexDocuments(Iterable<WebDocument> documents){
        try{
            for(WebDocument webDocument: documents){
                indexDocument(webDocument);
            }
            indexReader = DirectoryReader.open(indexWriter);
            indexWriter.close();
        }
        catch(IOException ex){
            ex.printStackTrace();
            indexReader=null;
        }
    }

    public Query buildQuery(String queryString){
        String[] searchableFields = {"title", "content"};
        BooleanClause.Occur[] flags = {BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD};
        Query query;

        try{
            query= MultiFieldQueryParser.parse(queryString, searchableFields, flags, analyzer);
        }
        catch(ParseException ex){
            query = null;
            ex.printStackTrace();
        }
        return query;
    }

    public String[] getQueryMatches(String queryString, int topN){
        String[] results;
        Query query = buildQuery(queryString);

        IndexSearcher searcher = new IndexSearcher(indexReader);
        TopDocs docs;
        try{
            long t1 = System.nanoTime();
            for(int i=0;i<1000; i++){
                docs = searcher.search(query, topN);
            }

            t1 = System.nanoTime() - t1;
            System.out.println(String.format("Query took %s ms", (float) t1/1000000000));
            docs = searcher.search(query, topN);
        }
        catch(IOException ex){
            ex.printStackTrace();
            docs = null;
        }
        try{
            results = new String[docs.scoreDocs.length];
            for(int i=0; i< docs.scoreDocs.length; i++){
                results[i] = searcher.doc(docs.scoreDocs[i].doc).getField("url").stringValue();
            }
        }
        catch(IOException ex){
            ex.printStackTrace();
            results = null;
        }
        return results;


    }
}
