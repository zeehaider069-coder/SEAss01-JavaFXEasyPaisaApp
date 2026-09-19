package com.example.easypaisa;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class SendMoney {

    public void show(Stage stage, UserStore.User user) {
        HBox header = H.header("Send Money",
                () -> new Dashboard().show(stage, UserStore.get(user.phone)));

        TextField toF   = H.tf("Recipient Phone (03XXXXXXXXX)");
        TextField amtF  = H.tf("Amount (Rs.)");
        TextField noteF = H.tf("Note (optional)");
        Label msg = H.msg();

        Button sendBtn = H.btn("Send Money", Styles.BTN);

        sendBtn.setOnAction(e -> {
            String to  = toF.getText().trim();
            double amt;
            try { amt = Double.parseDouble(amtF.getText().trim()); }
            catch (Exception ex) { H.setMsg(msg, "Enter a valid amount.", true); return; }

            UserStore.User me = UserStore.get(user.phone);
            if (!to.matches("03\\d{9}")) { H.setMsg(msg, "Enter valid phone (03XXXXXXXXX).", true); return; }
            if (to.equals(user.phone))   { H.setMsg(msg, "You cannot send money to yourself.", true); return; }
            if (!UserStore.has(to))      { H.setMsg(msg, "Recipient not found in system.", true); return; }
            if (amt <= 0)                { H.setMsg(msg, "Amount must be greater than 0.", true); return; }
            if (amt > me.balance)        { H.setMsg(msg, "Insufficient balance. Have Rs. " + UserStore.fmt(me.balance), true); return; }

            String rName = UserStore.get(to).name;
            new PinDialog().show(stage, user, () -> {
                UserStore.addTx(user.phone, "-", "Sent to " + rName, amt);
                UserStore.addTx(to, "+", "Received from " + me.name, amt);
                H.setMsg(msg, "Rs. " + UserStore.fmt(amt) + " sent to " + rName + "!", false);
                toF.clear(); amtF.clear(); noteF.clear();
            });
        });

        VBox card = H.vbox(10,
                H.balRow(user.phone), msg,
                H.flbl("Recipient Phone"), toF,
                H.flbl("Amount (Rs.)"),    amtF,
                H.flbl("Note (optional)"), noteF,
                sendBtn
        );
        card.setStyle(Styles.CARD);

        VBox root = H.vbox(0, header, H.scroll(card));
        VBox.setVgrow(root.getChildren().get(1), Priority.ALWAYS);
        root.setStyle("-fx-background-color:#f3f4f6;");
        stage.setScene(new Scene(root, 400, 580));
        stage.show();
    }
}

