package SearchUI;

import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;


public class Main extends Application {
    private Bridge bridge;
    private WebView browser;
    private WebEngine webEngine;
    private JSObject jsobj;
    private AnchorPane root;
    private Scene scene;
    private Stage topStage;


    @Override
    public void start(Stage primaryStage){
        browser = new WebView();
        webEngine = browser.getEngine();
        webEngine.setJavaScriptEnabled(true);

        browser.resize(2000, 1600);

        bridge = new Bridge(new Model());

        // Set the bridge at every new page load
        webEngine.getLoadWorker().stateProperty().addListener(
                (ov, oldState, newState) -> {
                    if (newState == Worker.State.SUCCEEDED) {
                        System.out.println("Ready!");
                        JSObject jso = (JSObject) webEngine.executeScript("window");

                        jso.setMember("myJavaBridge", bridge);
                    }
                });


        root = new AnchorPane(browser);

        scene = new Scene(root);
        primaryStage.setTitle("Yoogle | Your private Google");
        primaryStage.setScene(scene);
        topStage = primaryStage;
        primaryStage.show();

        webEngine.load("http://localhost:8000/SearchUI.html");
    }


    public static void main(String[] args) {
        launch(args);
    }
}
