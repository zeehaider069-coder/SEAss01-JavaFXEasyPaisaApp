package com.example.easypaisa;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Login {

    public void show(Stage stage, String notice) {
        VBox header = H.vbox(4,
                H.bold("EasyPaisa", 30, "white"),
                H.lbl("Pakistan's Digital Wallet", 13, "rgba(255,255,255,0.8)")
        );
        header.setAlignment(Pos.CENTER);
        header.setStyle(Styles.HDR + "-fx-padding:30 20;");

        TextField    phoneF = H.tf("03001234567");
        PasswordField passF = H.pf("Your password");
        Label msg = H.msg();
        if (!notice.isEmpty()) H.setMsg(msg, notice, false);

        Button loginBtn = H.btn("Login", Styles.BTN);

        Label signupLink = new Label("New user? Sign Up");
        signupLink.setStyle("-fx-text-fill:"+Styles.G+";-fx-cursor:hand;-fx-font-size:13;");
        signupLink.setOnMouseClicked(e -> new Signup().show(stage));
        HBox linkBox = new HBox(signupLink); linkBox.setAlignment(Pos.CENTER);

        loginBtn.setOnAction(e -> {
            UserStore.User u = UserStore.get(phoneF.getText().trim());
            if (u == null || !u.pass.equals(passF.getText())) {
                H.setMsg(msg, "Wrong phone or password.", true); return;
            }
            new Dashboard().show(stage, u);
        });
        passF.setOnAction(loginBtn.getOnAction());

        VBox card = H.vbox(10,
                H.bold("Welcome Back!", 20, "#1a1a1a"),
                H.lbl("Login to your account", 13, "#6b7280"),
                msg,
                H.flbl("Phone Number"), phoneF,
                H.flbl("Password"), passF,
                loginBtn, linkBox
        );
        card.setStyle(Styles.CARD);
        VBox.setMargin(linkBox, new Insets(4, 0, 0, 0));

        VBox root = H.vbox(0, header, H.scroll(card));
        VBox.setVgrow(root.getChildren().get(1), Priority.ALWAYS);
        root.setStyle("-fx-background-color:#f3f4f6;");
        stage.setScene(new Scene(root, 400, 560));
        stage.show();
    }
}
