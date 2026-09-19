package com.example.easypaisa;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class H {

    static Label lbl(String t, int sz, String color) {
        Label l = new Label(t);
        l.setStyle("-fx-font-size:"+sz+";-fx-text-fill:"+color+";");
        return l;
    }
    static Label bold(String t, int sz, String color) {
        Label l = new Label(t);
        l.setStyle("-fx-font-size:"+sz+";-fx-font-weight:bold;-fx-text-fill:"+color+";");
        return l;
    }
    static Label flbl(String t) {
        Label l = new Label(t);
        l.setStyle("-fx-font-size:13;-fx-font-weight:600;-fx-text-fill:#374151;");
        return l;
    }
    static Label msg() {
        Label l = new Label();
        l.setWrapText(true); l.setVisible(false); l.setManaged(false);
        return l;
    }
    static void setMsg(Label l, String t, boolean err) {
        l.setText(t); l.setStyle(err ? Styles.ERR : Styles.SUC);
        l.setVisible(true); l.setManaged(true);
    }

    static TextField tf(String prompt) {
        TextField f = new TextField(); f.setPromptText(prompt);
        f.setStyle(Styles.INPUT); f.setMaxWidth(Double.MAX_VALUE);
        return f;
    }
    static PasswordField pf(String prompt) {
        PasswordField f = new PasswordField(); f.setPromptText(prompt);
        f.setStyle(Styles.INPUT); f.setMaxWidth(Double.MAX_VALUE);
        return f;
    }
    static Button btn(String t, String style) {
        Button b = new Button(t); b.setStyle(style);
        b.setMaxWidth(Double.MAX_VALUE);
        return b;
    }

    static VBox vbox(int sp, Node... kids) { return new VBox(sp, kids); }
    static HBox hbox(int sp, Node... kids) { return new HBox(sp, kids); }
    static Region spacer() {
        Region r = new Region(); HBox.setHgrow(r, Priority.ALWAYS);
        return r;
    }

    static HBox header(String title, Runnable onBack) {
        Label back = new Label("←");
        back.setStyle("-fx-text-fill:white;-fx-font-size:20;-fx-cursor:hand;");
        back.setOnMouseClicked(e -> onBack.run());
        Label t = bold(title, 17, "white");
        HBox h = hbox(12, back, t);
        h.setAlignment(Pos.CENTER_LEFT);
        h.setStyle(Styles.HDR);
        return h;
    }

    static HBox balRow(String phone) {
        Label l = lbl("Your Balance", 13, Styles.GD);
        Label v = bold("Rs. " + UserStore.fmt(UserStore.get(phone).balance), 13, Styles.GD);
        HBox r = hbox(0, l, spacer(), v);
        r.setAlignment(Pos.CENTER_LEFT);
        r.setStyle(Styles.GLBOX);
        return r;
    }

    static ScrollPane scroll(Node content) {
        VBox wrap = new VBox(content); wrap.setPadding(new Insets(16));
        wrap.setStyle("-fx-background-color:#f3f4f6;");
        ScrollPane sp = new ScrollPane(wrap);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background:#f3f4f6;-fx-background-color:#f3f4f6;");
        return sp;
    }

    // Reusable transaction row
    static HBox txRow(UserStore.Tx tx) {
        boolean in = tx.type.equals("+");
        String col = in ? Styles.GD : "#dc2626";
        String bg  = in ? Styles.GL : "#fef2f2";
        Label ico = new Label(in ? "↓" : "↑");
        ico.setStyle("-fx-background-color:"+bg+";-fx-background-radius:20;"
                +"-fx-min-width:38;-fx-min-height:38;-fx-alignment:center;"
                +"-fx-font-size:16;-fx-text-fill:"+col+";");
        VBox info = vbox(2, bold(tx.label, 14, "#1a1a1a"), lbl(tx.date, 11, "#9ca3af"));
        Label amt = bold((in?"+":"−")+"Rs. "+UserStore.fmt(tx.amount), 14, col);
        HBox row = hbox(10, ico, info, spacer(), amt);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(8, 0, 8, 0));
        row.setStyle("-fx-border-color:transparent transparent #f3f4f6 transparent;");
        return row;
    }
}

