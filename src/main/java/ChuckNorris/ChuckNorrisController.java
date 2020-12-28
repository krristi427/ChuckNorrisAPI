package ChuckNorris;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class ChuckNorrisController {

    public Label theIDofRandomJoke;
    public Label theRandomJoke;
    public MenuButton categoriesButton;
    public TextField queryField;
    public Button queryBasedJokeButton;
    OkHttpClient okHttpClient = new OkHttpClient();

    public void getRandomJoke(ActionEvent event) {

        Request request = new Request.Builder().url("https://api.chucknorris.io/jokes/random").build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                JsonObject jsonObject = JsonParser.parseString(response.body().string())
                        .getAsJsonObject();
                //this is waaaaaaaay better
                Platform.runLater(() -> {
                    theIDofRandomJoke.setText(jsonObject.get("id").toString());
                    theRandomJoke.setText(jsonObject.get("value").toString());
                });
            }
        });
    }

    public void getJokeOnCategory(ActionEvent event) {

        MenuItem menuItem = (MenuItem) event.getSource(); //bc i know what i expect
        String category = menuItem.getText();
        Request request = new Request.Builder()
                .url("https://api.chucknorris.io/jokes/random?category=" + category)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                JsonObject jsonObject = JsonParser.parseString(response.body().string())
                        .getAsJsonObject();

                //this is waaaaaaaay better
                Platform.runLater(() -> {
                    theIDofRandomJoke.setText(jsonObject.get("id").toString());
                    theRandomJoke.setText(jsonObject.get("value").toString());
                });
            }
        });
    }

    public void getJokeOnQuery(ActionEvent event) {

        String query = queryField.getText();

        //bc appearently they have this restriction
        if ((query.length() < 3) || (query.length() > 120)) {
            Platform.runLater(() -> {
                theIDofRandomJoke.setText("This field is left empty");
                theRandomJoke.setText("There are no jokes with that word larry \n" +
                        "Other than yourself :))");
            });
            return;
        }

        Request request = new Request.Builder()
                .url("https://api.chucknorris.io/jokes/search?query=" + query)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                JsonObject jsonObject = JsonParser.parseString(response.body().string())
                        .getAsJsonObject();

                //converting them both to strings, bc you can't compare a JsonElement with an int
                //yes, it isn't recommended, but i know what i expect soooooo :))
                if (jsonObject.get("total").toString().equals(Integer.toString(0))) {
                    Platform.runLater(() -> {
                        theIDofRandomJoke.setText("This field is left empty");
                        theRandomJoke.setText("There are no jokes with that word larry \n" +
                                "Other than yourself :))");
                    });
                } else {
                    JsonArray resultArray = jsonObject.get("result").getAsJsonArray();

                    //to spice things up :))
                    Random random = new Random();
                    JsonObject resultObject = resultArray.get(random.nextInt(resultArray.size()))
                            .getAsJsonObject();

                    //this is waaaaaaaay better
                    Platform.runLater(() -> {
                        theIDofRandomJoke.setText(resultObject.get("id").toString());
                        theRandomJoke.setText(resultObject.get("value").toString());
                    });
                }
            }
        });
    }

    //bc some of us like working from the keyboard
    public void pressedKeyForQuery(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            getJokeOnQuery(new ActionEvent());
            keyEvent.consume();
        }
    }
}
