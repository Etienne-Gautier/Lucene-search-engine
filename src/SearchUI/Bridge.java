package SearchUI;


public class Bridge {
    private Model model;

    public Bridge(Model model){
        this.model = model;
    }

    public String searchQuery(String queryString) {
        String result;
        try{
            result= model.searchHTML(queryString);
        }
        catch(Exception ex){
            System.out.println("an exception has occured");
            result = "";
        }
        return result;
    }
}