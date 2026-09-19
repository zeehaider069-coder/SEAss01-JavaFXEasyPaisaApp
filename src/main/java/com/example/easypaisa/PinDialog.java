package com.example.easypaisa;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public class PinDialog {

    public void show(Stage owner, UserStore.User user, Runnable onSuccess) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(owner);
        dialog.setTitle("Confirm PIN");
        dialog.setResizable(false);

        Label title = H.bold("Confirm with PIN", 17, "#1a1a1a");
        Label sub   = H.lbl("Enter your 4-digit transaction PIN", 13, "#6b7280");
        Label msg   = H.msg();

        PasswordField pinF = H.pf("••••");
        pinF.setStyle(Styles.INPUT + "-fx-font-size:22;-fx-alignment:center;");

        Button okBtn  = H.btn("Confirm", Styles.BTN);
        Button canBtn = H.btn("Cancel",  Styles.OUT);
        HBox.setHgrow(okBtn, Priority.ALWAYS);
        HBox.setHgrow(canBtn, Priority.ALWAYS);
        HBox btns = H.hbox(10, canBtn, okBtn);

        okBtn.setOnAction(e -> {
            if (!pinF.getText().equals(UserStore.get(user.phone).pin)) {
                H.setMsg(msg, "Wrong PIN. Try again.", true); return;
            }
            dialog.close();
            onSuccess.run();
        });
        canBtn.setOnAction(e -> dialog.close());

        VBox root = H.vbox(12, title, sub, msg, pinF, btns);
        root.setPadding(new Insets(24));
        root.setStyle("-fx-background-color:white;");
        dialog.setScene(new Scene(root, 330, 230));
        dialog.showAndWait();
    }
}
