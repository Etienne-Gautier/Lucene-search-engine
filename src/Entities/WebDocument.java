package Entities;

/**
 * Represent the information contained in a website: url, title and content
 */
public class WebDocument {
    public String url;
    public String title;
    public String content;

    public WebDocument(String url, String title, String content){
        this.url = url;
        this.title = title;
        this.content = content;
    }

    public String toHTML(){
        return String.format("<li><a href='%s'>%s</a><br>%s<br></li><br>", this.url, this.title, this.content.substring(0, Math.min(300, content.length())));
    }
}
