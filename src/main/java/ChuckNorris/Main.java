package ChuckNorris;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class Main extends Application {

    /**
     * Hello Chuck Norris
     * @param args
     */

    public static void main(String[] args) { launch(); }

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ChuckNorrisWindowBuilder.fxml"));
        VBox vBoxLoader = fxmlLoader.load();
        Scene scene = new Scene(vBoxLoader);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
