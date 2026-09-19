package com.example.easypaisa;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class BillPayment {

    public void show(Stage stage, UserStore.User user) {
        HBox header = H.header("Bill Payment",
                () -> new Dashboard().show(stage, UserStore.get(user.phone)));

        ComboBox<String> co = new ComboBox<>();
        co.getItems().addAll("LESCO","SSGC","PTCL","Jazz","Telenor","Zong","Ufone","WASA","KE");
        co.setPromptText("-- Select Company --");
        co.setMaxWidth(Double.MAX_VALUE);
        co.setStyle("-fx-font-size:14;");

        TextField conF = H.tf("Consumer / Reference Number");
        TextField amtF = H.tf("Amount (Rs.)");
        Label msg = H.msg();

        Button payBtn = H.btn("Pay Bill", Styles.BTN);
        payBtn.setOnAction(e -> {
            if (co.getValue() == null)      { H.setMsg(msg, "Please select a company.", true); return; }
            if (conF.getText().isEmpty())   { H.setMsg(msg, "Enter consumer number.", true); return; }
            double amt;
            try { amt = Double.parseDouble(amtF.getText().trim()); }
            catch (Exception ex) { H.setMsg(msg, "Enter a valid amount.", true); return; }
            UserStore.User me = UserStore.get(user.phone);
            if (amt <= 0)        { H.setMsg(msg, "Amount must be greater than 0.", true); return; }
            if (amt > me.balance){ H.setMsg(msg, "Insufficient balance. Have Rs. " + UserStore.fmt(me.balance), true); return; }
            String company = co.getValue();
            new PinDialog().show(stage, user, () -> {
                UserStore.addTx(user.phone, "-", "Bill: " + company, amt);
                H.setMsg(msg, "Bill paid to " + company + "! Rs. " + UserStore.fmt(amt), false);
                co.setValue(null); conF.clear(); amtF.clear();
            });
        });

        VBox card = H.vbox(10,
                H.balRow(user.phone), msg,
                H.flbl("Select Company"), co,
                H.flbl("Consumer Number"), conF,
                H.flbl("Amount (Rs.)"), amtF,
                payBtn
        );
        card.setStyle(Styles.CARD);

        VBox root = H.vbox(0, header, H.scroll(card));
        VBox.setVgrow(root.getChildren().get(1), Priority.ALWAYS);
        root.setStyle("-fx-background-color:#f3f4f6;");
        stage.setScene(new Scene(root, 400, 560));
        stage.show();
    }
}
