package com.example.easypaisa;
import java.util.*;
import java.text.SimpleDateFormat;

public class UserStore {

    public static class Tx {
        public String type, label, date;
        public double amount, balance;
        public Tx(String type, String label, double amount, double balance) {
            this.type = type; this.label = label;
            this.amount = amount; this.balance = balance;
            this.date = new SimpleDateFormat("dd MMM, hh:mm a").format(new Date());
        }
    }

    public static class User {
        public String name, phone, pass, pin;
        public double balance = 5000;
        public List<Tx> txs = new ArrayList<>();
        public User(String name, String phone, String pass, String pin) {
            this.name = name; this.phone = phone;
            this.pass = pass; this.pin = pin;
            txs.add(new Tx("+", "Welcome Bonus", 5000, 5000));
        }
    }

    private static final HashMap<String, User> db = new HashMap<>();

    public static void   save(User u)        { db.put(u.phone, u); }
    public static User   get(String p)       { return db.get(p); }
    public static boolean has(String p)      { return db.containsKey(p); }

    public static void addTx(String phone, String type, String label, double amt) {
        User u = get(phone);
        u.balance = type.equals("+") ? u.balance + amt : u.balance - amt;
        u.txs.add(0, new Tx(type, label, amt, u.balance));
    }

    public static String fmt(double n) { return String.format("%,.2f", n); }
}