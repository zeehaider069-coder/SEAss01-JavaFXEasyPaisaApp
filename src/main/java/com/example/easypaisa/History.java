package com.example.easypaisa;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.List;
import java.util.stream.Collectors;

public class History {

    public void show(Stage stage, UserStore.User user) {
        HBox header = H.header("Transaction History",
                () -> new Dashboard().show(stage, UserStore.get(user.phone)));

        // Filter buttons
        Button allBtn = filterBtn("All",       true);
        Button inBtn  = filterBtn("Money In",  false);
        Button outBtn = filterBtn("Money Out", false);
        HBox.setHgrow(allBtn, Priority.ALWAYS);
        HBox.setHgrow(inBtn,  Priority.ALWAYS);
        HBox.setHgrow(outBtn, Priority.ALWAYS);
        HBox filters = new HBox(8, allBtn, inBtn, outBtn);

        // Transaction list container
        VBox txList = new VBox(0);
        txList.setStyle(Styles.CARD);

        buildList(txList, UserStore.get(user.phone).txs, "all");

        allBtn.setOnAction(e -> {
            setActive(allBtn, inBtn, outBtn);
            buildList(txList, UserStore.get(user.phone).txs, "all");
        });
        inBtn.setOnAction(e -> {
            setActive(inBtn, allBtn, outBtn);
            buildList(txList, UserStore.get(user.phone).txs, "+");
        });
        outBtn.setOnAction(e -> {
            setActive(outBtn, allBtn, inBtn);
            buildList(txList, UserStore.get(user.phone).txs, "-");
        });

        VBox content = H.vbox(12, filters, txList);
        content.setPadding(new Insets(16));
        content.setStyle("-fx-background-color:#f3f4f6;");

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background:#f3f4f6;-fx-background-color:#f3f4f6;");

        VBox root = H.vbox(0, header, scroll);
        VBox.setVgrow(scroll, Priority.ALWAYS);
        root.setStyle("-fx-background-color:#f3f4f6;");
        stage.setScene(new Scene(root, 400, 600));
        stage.show();
    }

    void buildList(VBox box, List<UserStore.Tx> all, String filter) {
        box.getChildren().clear();
        List<UserStore.Tx> filtered = all.stream()
                .filter(t -> filter.equals("all") || t.type.equals(filter))
                .collect(Collectors.toList());
        if (filtered.isEmpty()) {
            box.getChildren().add(H.lbl("No transactions found", 13, "#6b7280"));
        } else {
            for (UserStore.Tx tx : filtered) {
                VBox row = H.vbox(0, H.txRow(tx));
                // Show balance after tx
                var balLbl = H.lbl("Balance after: Rs. " + UserStore.fmt(tx.balance), 11, "#9ca3af");
                balLbl.setPadding(new Insets(0, 0, 6, 48));
                box.getChildren().addAll(H.txRow(tx));
            }
        }
    }

    Button filterBtn(String label, boolean active) {
        Button b = new Button(label);
        b.setStyle(active ? Styles.FBTN_ON : Styles.FBTN_OFF);
        b.setMaxWidth(Double.MAX_VALUE); return b;
    }
    void setActive(Button on, Button... off) {
        on.setStyle(Styles.FBTN_ON);
        for (Button b : off) b.setStyle(Styles.FBTN_OFF);
    }
}
