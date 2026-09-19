package com.example.easypaisa;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Receive {

    public void show(Stage stage, UserStore.User user) {
        HBox header = H.header("Receive Money",
                () -> new Dashboard().show(stage, UserStore.get(user.phone)));

        UserStore.User u = UserStore.get(user.phone);

        // Avatar circle
        var av = H.bold(String.valueOf(u.name.charAt(0)).toUpperCase(), 30, Styles.G);
        av.setStyle("-fx-font-size:30;-fx-font-weight:bold;-fx-text-fill:"+Styles.G+";"
                +"-fx-background-color:"+Styles.GL+";-fx-background-radius:40;"
                +"-fx-min-width:72;-fx-min-height:72;-fx-alignment:center;");

        var nameL  = H.bold(u.name, 18, "#1a1a1a");
        var phoneL = H.lbl(u.phone, 18, "#374151");
        phoneL.setStyle("-fx-font-size:18;-fx-font-weight:600;-fx-text-fill:#374151;"
                +"-fx-background-color:#f3f4f6;-fx-background-radius:8;-fx-padding:10 20;"
                +"-fx-letter-spacing:2;");

        var note = H.lbl("Ask sender to use this phone number", 12, "#9ca3af");

        VBox balBox = H.vbox(4,
                H.lbl("Current Balance", 12, Styles.GD),
                H.bold("Rs. " + UserStore.fmt(u.balance), 26, Styles.GD)
        );
        balBox.setAlignment(Pos.CENTER);
        balBox.setStyle(Styles.GLBOX + "-fx-alignment:center;");

        VBox card = H.vbox(14, av, nameL, phoneL, note, balBox);
        card.setAlignment(Pos.CENTER);
        card.setStyle(Styles.CARD);

        VBox root = H.vbox(0, header, H.scroll(card));
        VBox.setVgrow(root.getChildren().get(1), Priority.ALWAYS);
        root.setStyle("-fx-background-color:#f3f4f6;");
        stage.setScene(new Scene(root, 400, 520));
        stage.show();
    }
}
