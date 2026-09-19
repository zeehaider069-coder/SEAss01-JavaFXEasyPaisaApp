package com.example.easypaisa;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Profile {

    public void show(Stage stage, UserStore.User user) {
        HBox header = H.header("My Profile",
                () -> new Dashboard().show(stage, UserStore.get(user.phone)));

        UserStore.User u = UserStore.get(user.phone);

        // Avatar
        var av = H.bold(String.valueOf(u.name.charAt(0)).toUpperCase(), 28, Styles.G);
        av.setStyle("-fx-font-size:28;-fx-font-weight:bold;-fx-text-fill:" + Styles.G + ";"
                + "-fx-background-color:" + Styles.GL + ";-fx-background-radius:36;"
                + "-fx-min-width:64;-fx-min-height:64;-fx-alignment:center;");

        // Balance box
        VBox balBox = H.vbox(4,
                H.lbl("Account Balance", 12, Styles.GD),
                H.bold("Rs. " + UserStore.fmt(u.balance), 28, Styles.GD)
        );
        balBox.setAlignment(Pos.CENTER);
        balBox.setStyle(Styles.GLBOX + "-fx-alignment:center;");

        // Stats
        HBox stat1 = statRow("Total Transactions", String.valueOf(u.txs.size()));
        HBox stat2 = statRow("Phone Number", u.phone);
        HBox stat3 = statRow("Account Status", "Active ✓");

        VBox statsBox = H.vbox(8, stat1, stat2, stat3);
        statsBox.setStyle("-fx-background-color:#f9fafb;-fx-background-radius:10;-fx-padding:14;");

        var logoutBtn = H.btn("Logout", Styles.RED);
        logoutBtn.setOnAction(e -> new Login().show(stage, ""));

        VBox card = H.vbox(16, av, H.bold(u.name, 20, "#1a1a1a"),
                H.lbl(u.phone, 14, "#6b7280"), balBox, statsBox, logoutBtn);
        card.setAlignment(Pos.CENTER);
        card.setStyle(Styles.CARD);

        VBox root = H.vbox(0, header, H.scroll(card));
        VBox.setVgrow(root.getChildren().get(1), Priority.ALWAYS);
        root.setStyle("-fx-background-color:#f3f4f6;");
        stage.setScene(new Scene(root, 400, 580));
        stage.show();
    }

    HBox statRow(String label, String value) {
        var l = H.lbl(label, 13, "#6b7280");
        var v = H.bold(value, 13, "#1a1a1a");
        HBox r = H.hbox(0, l, H.spacer(), v);
        return r;
    }
}