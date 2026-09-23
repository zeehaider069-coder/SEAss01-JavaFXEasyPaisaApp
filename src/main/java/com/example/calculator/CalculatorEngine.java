package com.example.calculator;

/**
 * Pure calculation engine for the calculator.
 * Handles basic arithmetic, scientific functions, and memory operations.
 * UI se alag rakha hai taake logic clean aur reusable rahe.
 */
public class CalculatorEngine {

    private double memory = 0.0;

    // ---------- Basic arithmetic ----------

    public double add(double a, double b) {
        return a + b;
    }

    public double subtract(double a, double b) {
        return a - b;
    }

    public double multiply(double a, double b) {
        return a * b;
    }

    public double divide(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Cannot divide by zero");
        }
        return a / b;
    }

    public double applyOperator(double a, double b, String operator) {
        switch (operator) {
            case "+":
                return add(a, b);
            case "-":
                return subtract(a, b);
            case "×":
            case "*":
                return multiply(a, b);
            case "÷":
            case "/":
                return divide(a, b);
            default:
                throw new IllegalArgumentException("Unknown operator: " + operator);
        }
    }

    // ---------- Scientific / single-operand functions ----------

    public double percent(double value) {
        return value / 100.0;
    }

    public double squareRoot(double value) {
        if (value < 0) {
            throw new ArithmeticException("Cannot take square root of a negative number");
        }
        return Math.sqrt(value);
    }

    public double square(double value) {
        return value * value;
    }

    public double reciprocal(double value) {
        if (value == 0) {
            throw new ArithmeticException("Cannot divide by zero");
        }
        return 1.0 / value;
    }

    public double negate(double value) {
        return -value;
    }

    public double sin(double valueDegrees) {
        return Math.sin(Math.toRadians(valueDegrees));
    }

    public double cos(double valueDegrees) {
        return Math.cos(Math.toRadians(valueDegrees));
    }

    public double tan(double valueDegrees) {
        return Math.tan(Math.toRadians(valueDegrees));
    }

    public double log10(double value) {
        if (value <= 0) {
            throw new ArithmeticException("Log undefined for values <= 0");
        }
        return Math.log10(value);
    }

    public double ln(double value) {
        if (value <= 0) {
            throw new ArithmeticException("Ln undefined for values <= 0");
        }
        return Math.log(value);
    }

    // ---------- Memory operations ----------

    public void memoryClear() {
        memory = 0.0;
    }

    public void memoryAdd(double value) {
        memory += value;
    }

    public void memorySubtract(double value) {
        memory -= value;
    }

    public double memoryRecall() {
        return memory;
    }

    public boolean hasMemory() {
        return memory != 0.0;
    }
}


