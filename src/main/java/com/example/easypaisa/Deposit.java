package com.example.easypaisa;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Deposit {

    public void show(Stage stage, UserStore.User user) {
        HBox header = H.header("Deposit Cash",
                () -> new Dashboard().show(stage, UserStore.get(user.phone)));

        TextField amtF = H.tf("Enter amount");
        Label msg = H.msg();

        int[] presets = {500, 1000, 2000, 5000};
        Button[] pBtns = new Button[4];
        HBox presetRow = new HBox(8);
        for (int i = 0; i < presets.length; i++) {
            Button b = new Button("Rs. " + presets[i]);
            b.setStyle(Styles.PBTN); b.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(b, Priority.ALWAYS);
            final int val = presets[i];
            b.setOnAction(e -> {
                amtF.setText(String.valueOf(val));
                for (Button pb : pBtns) pb.setStyle(Styles.PBTN);
                b.setStyle(Styles.BTN);
            });
            pBtns[i] = b; presetRow.getChildren().add(b);
        }

        Button depBtn = H.btn("Deposit", Styles.BTN);
        depBtn.setOnAction(e -> {
            double amt;
            try { amt = Double.parseDouble(amtF.getText().trim()); }
            catch (Exception ex) { H.setMsg(msg, "Enter a valid amount.", true); return; }
            if (amt <= 0)     { H.setMsg(msg, "Amount must be greater than 0.", true); return; }
            if (amt > 100000) { H.setMsg(msg, "Max deposit is Rs. 1,00,000.", true); return; }
            new PinDialog().show(stage, user, () -> {
                UserStore.addTx(user.phone, "+", "Cash Deposit", amt);
                H.setMsg(msg, "Rs. " + UserStore.fmt(amt) + " deposited! Balance: Rs."
                        + UserStore.fmt(UserStore.get(user.phone).balance), false);
                amtF.clear();
                for (Button pb : pBtns) pb.setStyle(Styles.PBTN);
            });
        });

        VBox card = H.vbox(10,
                H.balRow(user.phone), msg,
                H.flbl("Amount (Rs.)"), amtF,
                presetRow, depBtn
        );
        card.setStyle(Styles.CARD);

        VBox root = H.vbox(0, header, H.scroll(card));
        VBox.setVgrow(root.getChildren().get(1), Priority.ALWAYS);
        root.setStyle("-fx-background-color:#f3f4f6;");
        stage.setScene(new Scene(root, 400, 500));
        stage.show();
    }
}

