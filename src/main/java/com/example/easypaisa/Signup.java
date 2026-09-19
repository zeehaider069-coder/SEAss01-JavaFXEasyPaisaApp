package com.example.easypaisa;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Signup {

    public void show(Stage stage) {
        // Header
        VBox header = H.vbox(4,
                H.bold("EasyPaisa", 30, "white"),
                H.lbl("Pakistan's Digital Wallet", 13, "rgba(255,255,255,0.8)")
        );
        header.setAlignment(Pos.CENTER);
        header.setStyle(Styles.HDR + "-fx-padding:30 20;");

        // Step progress bars
        Region bar1 = stepBar(true), bar2 = stepBar(false);
        HBox steps = new HBox(8, bar1, bar2);
        steps.setMaxWidth(Double.MAX_VALUE);

        // ── Step 1 ──
        TextField nameF  = H.tf("Ali Hassan");
        TextField phoneF = H.tf("03001234567");
        PasswordField passF = H.pf("Min 4 characters");
        Label msg1  = H.msg();
        Button nextBtn = H.btn("Continue →", Styles.BTN);

        VBox step1 = H.vbox(10,
                H.bold("Create Account", 20, "#1a1a1a"),
                H.lbl("Step 1 of 2", 13, "#6b7280"),
                msg1,
                H.flbl("Full Name"), nameF,
                H.flbl("Phone Number"), phoneF,
                H.flbl("Password"), passF,
                nextBtn
        );

        // ── Step 2 ──
        VBox balBox = H.vbox(4,
                H.lbl("Your starting balance", 13, Styles.GD),
                H.bold("Rs. 5,000.00", 24, Styles.GD)
        );
        balBox.setAlignment(Pos.CENTER);
        balBox.setStyle(Styles.GLBOX + "-fx-alignment:center;");

        PasswordField pinF = H.pf("Enter 4 digits");
        pinF.setStyle(Styles.INPUT + "-fx-font-size:22;-fx-alignment:center;");

        Label msg2    = H.msg();
        Button createBtn = H.btn("Create Account", Styles.BTN);
        Button backBtn   = H.btn("← Back",         Styles.OUT);

        VBox step2 = H.vbox(10,
                H.bold("Set Your PIN", 20, "#1a1a1a"),
                H.lbl("Step 2 of 2", 13, "#6b7280"),
                msg2, balBox,
                H.flbl("4-Digit Transaction PIN"), pinF,
                H.lbl("Used to confirm every payment", 12, "#9ca3af"),
                createBtn, backBtn
        );
        step2.setVisible(false); step2.setManaged(false);

        // Login link
        Label loginLink = new Label("Already have account? Login");
        loginLink.setStyle("-fx-text-fill:"+Styles.G+";-fx-cursor:hand;-fx-font-size:13;");
        loginLink.setOnMouseClicked(e -> new Login().show(stage, ""));
        HBox linkBox = new HBox(loginLink); linkBox.setAlignment(Pos.CENTER);

        VBox card = H.vbox(0, steps, step1, step2, linkBox);
        VBox.setMargin(steps,   new Insets(0, 0, 14, 0));
        VBox.setMargin(linkBox, new Insets(12, 0, 0, 0));
        card.setStyle(Styles.CARD);

        // Saved temp data
        String[] tmp = new String[3];

        nextBtn.setOnAction(e -> {
            String n = nameF.getText().trim(), p = phoneF.getText().trim(), pw = passF.getText();
            if (n.isEmpty())             { H.setMsg(msg1, "Enter your name.", true); return; }
            if (!p.matches("03\\d{9}")) { H.setMsg(msg1, "Phone must be 11 digits (03XXXXXXXXX).", true); return; }
            if (pw.length() < 4)        { H.setMsg(msg1, "Password must be at least 4 characters.", true); return; }
            if (UserStore.has(p))       { H.setMsg(msg1, "This phone is already registered.", true); return; }
            tmp[0] = n; tmp[1] = p; tmp[2] = pw;
            step1.setVisible(false); step1.setManaged(false);
            step2.setVisible(true);  step2.setManaged(true);
            bar2.setStyle("-fx-background-color:"+Styles.G+";-fx-background-radius:4;");
        });

        backBtn.setOnAction(e -> {
            step2.setVisible(false); step2.setManaged(false);
            step1.setVisible(true);  step1.setManaged(true);
            bar2.setStyle("-fx-background-color:#e5e7eb;-fx-background-radius:4;");
        });

        createBtn.setOnAction(e -> {
            String pin = pinF.getText().trim();
            if (!pin.matches("\\d{4}")) { H.setMsg(msg2, "PIN must be exactly 4 digits.", true); return; }
            UserStore.save(new UserStore.User(tmp[0], tmp[1], tmp[2], pin));
            new Dashboard().show(stage, UserStore.get(tmp[1]));
        });

        VBox root = H.vbox(0, header, H.scroll(card));
        VBox.setVgrow(root.getChildren().get(1), Priority.ALWAYS);
        root.setStyle("-fx-background-color:#f3f4f6;");
        stage.setScene(new Scene(root, 400, 640));
        stage.show();
    }

    static Region stepBar(boolean active) {
        Region r = new Region(); r.setPrefHeight(4);
        r.setStyle("-fx-background-color:" + (active ? Styles.G : "#e5e7eb") + ";-fx-background-radius:4;");
        HBox.setHgrow(r, Priority.ALWAYS);
        return r;
    }
}

