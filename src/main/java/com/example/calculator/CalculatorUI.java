package com.example.calculator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.text.DecimalFormat;

/**
 * Builds and wires up the calculator's JavaFX UI.
 * Delegates all math to {@link CalculatorEngine}.
 */
public class CalculatorUI {

    private final CalculatorEngine engine = new CalculatorEngine();

    private final Label display = new Label("0");
    private final Label historyLabel = new Label(" ");

    private double currentValue = 0.0;
    private double storedValue = 0.0;
    private String pendingOperator = null;
    private boolean startNewNumber = true;

    private static final DecimalFormat DF = new DecimalFormat("#,##0.##########");

    public Scene createScene() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(16));
        root.setStyle("-fx-background-color: #1e1e1e;");

        display.setStyle("-fx-font-size: 42px; -fx-text-fill: white; -fx-padding: 10 6 0 6;");
        display.setMaxWidth(Double.MAX_VALUE);
        historyLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #9a9a9a; -fx-padding: 0 6 0 6;");
        historyLabel.setMaxWidth(Double.MAX_VALUE);

        VBox displayBox = new VBox(2, historyLabel, display);
        displayBox.setAlignment(Pos.CENTER_RIGHT);

        GridPane grid = buildButtonGrid();
        VBox.setVgrow(grid, Priority.ALWAYS);

        root.getChildren().addAll(displayBox, grid);

        return new Scene(root, 380, 560);
    }

    private GridPane buildButtonGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        for (int i = 0; i < 5; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(20);
            grid.getColumnConstraints().add(col);
        }

        String[][] layout = {
                {"MC", "MR", "M+", "M-", "C"},
                {"sin", "cos", "tan", "log", "ln"},
                {"7", "8", "9", "÷", "√"},
                {"4", "5", "6", "×", "x²"},
                {"1", "2", "3", "-", "1/x"},
                {"±", "0", ".", "+", "="},
                {"CE", "⌫", "%", "", ""}
        };

        for (int row = 0; row < layout.length; row++) {
            for (int col = 0; col < layout[row].length; col++) {
                String label = layout[row][col];
                if (label.isEmpty()) continue;

                Button btn = new Button(label);
                btn.setMaxWidth(Double.MAX_VALUE);
                btn.setMaxHeight(Double.MAX_VALUE);
                btn.setStyle(buttonStyle(label));
                btn.setOnAction(e -> handleButton(label));
                grid.add(btn, col, row);
                GridPane.setHgrow(btn, Priority.ALWAYS);
                GridPane.setVgrow(btn, Priority.ALWAYS);
            }
        }
        return grid;
    }

    private String buttonStyle(String label) {
        String base = "-fx-font-size: 15px; -fx-background-radius: 10; -fx-text-fill: white; -fx-cursor: hand;";
        if (label.matches("[0-9.]")) {
            return base + " -fx-background-color: #3a3a3a;";
        } else if (label.equals("=") || label.matches("[+\\-×÷]")) {
            return base + " -fx-background-color: #ff9500; -fx-font-weight: bold;";
        } else {
            return base + " -fx-background-color: #555555; -fx-font-size: 13px;";
        }
    }

    // ---------- Event handling ----------

    private void handleButton(String label) {
        try {
            switch (label) {
                case "0": case "1": case "2": case "3": case "4":
                case "5": case "6": case "7": case "8": case "9":
                    inputDigit(label);
                    break;
                case ".":
                    inputDecimalPoint();
                    break;
                case "+": case "-": case "×": case "÷":
                    inputOperator(label);
                    break;
                case "=":
                    calculateResult();
                    break;
                case "C":
                    clearAll();
                    break;
                case "CE":
                    clearEntry();
                    break;
                case "⌫":
                    backspace();
                    break;
                case "±":
                    currentValue = engine.negate(currentValue);
                    updateDisplay(currentValue);
                    break;
                case "%":
                    applyUnary(engine::percent);
                    break;
                case "√":
                    applyUnary(engine::squareRoot);
                    break;
                case "x²":
                    applyUnary(engine::square);
                    break;
                case "1/x":
                    applyUnary(engine::reciprocal);
                    break;
                case "sin":
                    applyUnary(engine::sin);
                    break;
                case "cos":
                    applyUnary(engine::cos);
                    break;
                case "tan":
                    applyUnary(engine::tan);
                    break;
                case "log":
                    applyUnary(engine::log10);
                    break;
                case "ln":
                    applyUnary(engine::ln);
                    break;
                case "MC":
                    engine.memoryClear();
                    break;
                case "MR":
                    currentValue = engine.memoryRecall();
                    updateDisplay(currentValue);
                    startNewNumber = true;
                    break;
                case "M+":
                    engine.memoryAdd(currentValue);
                    break;
                case "M-":
                    engine.memorySubtract(currentValue);
                    break;
                default:
                    break;
            }
        } catch (ArithmeticException ex) {
            display.setText("Error");
            startNewNumber = true;
            pendingOperator = null;
        }
    }

    private void inputDigit(String digit) {
        if (startNewNumber || display.getText().equals("0") || display.getText().equals("Error")) {
            display.setText(digit);
            startNewNumber = false;
        } else {
            display.setText(display.getText() + digit);
        }
        currentValue = Double.parseDouble(display.getText());
    }

    private void inputDecimalPoint() {
        if (startNewNumber || display.getText().equals("Error")) {
            display.setText("0.");
            startNewNumber = false;
            return;
        }
        if (!display.getText().contains(".")) {
            display.setText(display.getText() + ".");
        }
    }

    private void inputOperator(String operator) {
        if (pendingOperator != null && !startNewNumber) {
            calculateResult();
        }
        storedValue = currentValue;
        pendingOperator = operator;
        historyLabel.setText(DF.format(storedValue) + " " + operator);
        startNewNumber = true;
    }

    private void calculateResult() {
        if (pendingOperator == null) return;
        double a = storedValue;
        double b = currentValue;
        double result = engine.applyOperator(a, b, pendingOperator);
        historyLabel.setText(DF.format(a) + " " + pendingOperator + " " + DF.format(b) + " =");
        updateDisplay(result);
        currentValue = result;
        pendingOperator = null;
        startNewNumber = true;
    }

    private void clearAll() {
        currentValue = 0.0;
        storedValue = 0.0;
        pendingOperator = null;
        startNewNumber = true;
        display.setText("0");
        historyLabel.setText(" ");
    }

    private void clearEntry() {
        currentValue = 0.0;
        display.setText("0");
        startNewNumber = true;
    }

    private void backspace() {
        String text = display.getText();
        if (text.equals("Error") || text.length() <= 1) {
            display.setText("0");
            startNewNumber = true;
        } else {
            display.setText(text.substring(0, text.length() - 1));
        }
        try {
            currentValue = Double.parseDouble(display.getText());
        } catch (NumberFormatException ex) {
            currentValue = 0.0;
        }
    }

    private interface UnaryOp {
        double apply(double value);
    }

    private void applyUnary(UnaryOp op) {
        double result = op.apply(currentValue);
        currentValue = result;
        updateDisplay(result);
        startNewNumber = true;
    }

    private void updateDisplay(double value) {
        display.setText(DF.format(value));
    }
}
