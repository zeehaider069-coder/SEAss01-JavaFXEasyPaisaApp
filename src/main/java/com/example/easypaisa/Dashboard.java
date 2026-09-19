package com.example.easypaisa;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Dashboard {

    public void show(Stage stage, UserStore.User user) {
        UserStore.User u = UserStore.get(user.phone);

        // ── Top header ──
        VBox nameBox = H.vbox(2,
                H.lbl("Good day,", 13, "rgba(255,255,255,0.8)"),
                H.bold(u.name, 18, "white")
        );
        Label avatar = H.bold(String.valueOf(u.name.charAt(0)).toUpperCase(), 16, "white");
        avatar.setStyle("-fx-background-color:rgba(255,255,255,0.2);-fx-text-fill:white;"
                +"-fx-font-size:16;-fx-font-weight:bold;-fx-background-radius:20;"
                +"-fx-min-width:38;-fx-min-height:38;-fx-alignment:center;-fx-cursor:hand;");
        avatar.setOnMouseClicked(e -> new Profile().show(stage, u));
        HBox topBar = H.hbox(0, nameBox, H.spacer(), avatar);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle(Styles.HDR + "-fx-padding:12 20 42 20;");

        // ── Balance card (floats over header) ──
        Label balLbl = H.bold("Rs. " + UserStore.fmt(u.balance), 30, "#1a1a1a");
        VBox balCard = H.vbox(4,
                H.lbl("Available Balance", 12, "#6b7280"),
                balLbl,
                H.lbl("Phone: " + u.phone, 12, "#9ca3af")
        );
        balCard.setStyle("-fx-background-color:white;-fx-background-radius:16;"
                +"-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.12),16,0,0,4);-fx-padding:20;");
        VBox.setMargin(balCard, new Insets(12, 0, 0, 0));

        // ── Action grid ──
        String[][] actions = {
                {"↑","Send Money","send",   "#8b5cf6"},
                {"↓","Receive",   "receive","#2563eb"},
                {"+","Deposit",   "deposit","#16a34a"},
                {"−","Withdraw",  "withdraw","#d97706"},
                {"⚡","Bill Pay", "bill",   "#dc2626"},
                {"≡","History",   "history","#6b7280"}
        };
        GridPane grid = new GridPane();
        grid.setHgap(8); grid.setVgap(8);
        for (int i = 0; i < actions.length; i++) {
            String[] a = actions[i];
            Label ico = H.bold(a[0], 22, a[3]);
            Label lbl = H.lbl(a[1], 11, "#374151");
            VBox ab = H.vbox(4, ico, lbl);
            ab.setAlignment(Pos.CENTER);
            ab.setMaxWidth(Double.MAX_VALUE);
            ab.setStyle("-fx-background-color:#f9fafb;-fx-border-color:#e5e7eb;"
                    +"-fx-border-radius:10;-fx-background-radius:10;-fx-padding:12 8;-fx-cursor:hand;");
            final String sc = a[2];
            ab.setOnMouseClicked(e -> navigate(stage, u, sc));
            grid.add(ab, i % 3, i / 3);
            GridPane.setHgrow(ab, Priority.ALWAYS);
        }
        VBox actCard = H.vbox(10, H.bold("Quick Actions", 14, "#1a1a1a"), grid);
        actCard.setStyle(Styles.CARD);

        // ── Recent Transactions ──
        VBox txCard = H.vbox(10);
        txCard.setStyle(Styles.CARD);
        HBox txHead = H.hbox(0, H.bold("Recent Transactions", 15, "#1a1a1a"), H.spacer());
        Label seeAll = H.lbl("See all", 13, Styles.G);
        seeAll.setStyle("-fx-text-fill:"+Styles.G+";-fx-cursor:hand;-fx-font-size:13;");
        seeAll.setOnMouseClicked(e -> new History().show(stage, u));
        txHead.getChildren().add(seeAll);
        txCard.getChildren().add(txHead);

        int cnt = Math.min(5, u.txs.size());
        if (cnt == 0) {
            txCard.getChildren().add(H.lbl("No transactions yet", 13, "#6b7280"));
        } else {
            for (int i = 0; i < cnt; i++)
                txCard.getChildren().add(H.txRow(u.txs.get(i)));
        }

        VBox content = H.vbox(12, balCard, actCard, txCard);
        content.setPadding(new Insets(0, 16, 16, 16));

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background:#f3f4f6;-fx-background-color:#f3f4f6;");

        VBox root = H.vbox(0, topBar, scroll);
        VBox.setVgrow(scroll, Priority.ALWAYS);
        root.setStyle("-fx-background-color:#f3f4f6;");
        stage.setScene(new Scene(root, 400, 640));
        stage.setTitle("EasyPaisa");
        stage.show();
    }

    void navigate(Stage stage, UserStore.User u, String screen) {
        switch (screen) {
            case "send":     new SendMoney().show(stage, u);    break;
            case "receive":  new Receive().show(stage, u);      break;
            case "deposit":  new Deposit().show(stage, u);      break;
            case "withdraw": new Withdraw().show(stage, u);     break;
            case "bill":     new BillPayment().show(stage, u);  break;
            case "history":  new History().show(stage, u);      break;
        }
    }
}

