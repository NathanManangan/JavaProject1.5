package com.nighthawk.spring_portfolio.models.calculator;

import java.util.Stack;
import java.util.ArrayList;

public class CalculatorRPN {
    ArrayList<String> tokens = new ArrayList<>();
    ArrayList<String> rpnOutput = new ArrayList<>();

    String[] functions = { "sin", "cos", "tan", "ln" };

    // checks for operator
    public boolean isOperator(char c) {
        switch (c) {
            case '+':
                return true;
            case '-':
                return true;
            case '*':
                return true;
            case '/':
                return true;
            case '^':
                return true;
            default:
                return false; // else of switch
        }
    }

    public boolean isOperator(String s) {
        return s.length() == 1 && isOperator(s.charAt(0));
    }

    public boolean isParenthesis(char c) {
        switch (c) {
            case '(':
                return true;
            case ')':
                return true;
            default:
                return false;
        }
    }

    public boolean isFunction(char c) {
        for (String func : functions) {
            if (c == func.charAt(0)) {
                return true;
            }
            if (c == 'o') {
                return true;
            }
        }

        return false;
    }

    public boolean isFunction(String s) {
        return s.length() == 1 && isFunction(s.charAt(0));
    }

    public boolean isNumber(String s) {
        return !isOperator(s) && !isParenthesis(s.charAt(0)) && !isFunction(s);
    }

    public int getPrecedence(char c) {
        switch (c) {
            case '+':
                return 2;
            case '-':
                return 2;
            case '*':
                return 3;
            case '/':
                return 3;
            case '^':
                return 4;
            default:
                return -1;
        }
    }

    public int getPrecedence(String s) {
        if (s.length() == 1) {
            return (getPrecedence(s.charAt(0)));
        } else {
            return -1;
        }
    }

    public String getAssociativity(char c) {
        switch (c) {
            case '+':
                return "left";
            case '-':
                return "left";
            case '*':
                return "left";
            case '/':
                return "left";
            case '^':
                return "right";
            default:
                return "";
        }
    }

    public String getAssociativity(String s) {
        if (s.length() == 1) {
            return getAssociativity(s.charAt(0));
        } else {
            return "";
        }
    }

    public double calculate(char operator, double x1, double x2) {
        switch (operator) {
            case '+':
                return x1 + x2;
            case '-':
                return x1 - x2;
            case '*':
                return x1 * x2;
            case '/':
                return x1 / x2;
            case '^':
                return Math.pow(x1, x2);
            default:
                throw new RuntimeException("Unsupported operator: " + operator);
        }
    }

    public double calculate(String operator, double x1, double x2) {
        if (operator.length() == 1) {
            return calculate(operator.charAt(0), x1, x2);
        } else {
            throw new RuntimeException("Unsupported operator or function: " + operator);
        }
    }

    public double funcCalculate(char function, double x) {
        switch (function) {
            case 's':
                return Math.sin(x);
            case 'c':
                return Math.cos(x);
            case 't':
                return Math.tan(x);
            case 'l':
                return Math.log1p(x);
            case 'o':
                return Math.log10(x);
            default:
                throw new RuntimeException("Unsupported function: " + function);
        }
    }

    public double funcCalculate(String function, double x) {
        if (function.length() == 1) {
            return funcCalculate(function.charAt(0), x);
        } else {
            throw new RuntimeException("Unsupported operator or function: " + function);
        }
    }

    // parse input string as array of tokens
    public ArrayList<String> parse(String input) {
        String s = "";
        for (String func : functions) {
            input = input.replaceAll(func, Character.toString(func.charAt(0)));
        }

        input = input.replaceAll("log", "o");

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (isNumber(String.valueOf(c))) {
                s += c;
            } else {
                s += "\t" + c + "\t";
            }
        }

        String[] splittedTokens = s.split("\t", 0);
        ArrayList<String> tempTokens = new ArrayList<>();
        for (String token : splittedTokens) {
            String trimmedToken = token.trim();
            if (trimmedToken != "") {
                tempTokens.add(trimmedToken);
            }
        }

        if (tempTokens.get(0).equals("-")) { // if breaks, try adding tokens -1, * instead
            tokens.add("0");
        }

        for (int i = 0; i < tempTokens.size(); i++) {
            tokens.add(tempTokens.get(i));
            if (i + 1 >= tempTokens.size()) {
                break;
            }

            if (tempTokens.get(i).equals(")") && tempTokens.get(i + 1).equals("(")) {
                tokens.add("*");
            }

            if (i > 0 && tempTokens.get(i - 1).equals("(") && tempTokens.get(i).equals("-")) {
                tokens.remove(tokens.size() - 1);
                tokens.add("-1");
                tokens.add("*");
            }

            if (isNumber(tempTokens.get(i)) && tempTokens.get(i + 1).equals("(")) {
                tokens.add("*");
            }

            if (tempTokens.get(i).equals(")") && isNumber(tempTokens.get(i + 1))) {
                tokens.add("*");
            }

            if (i > 0 && tempTokens.get(i - 1).equals("^") && tempTokens.get(i).equals("-")) {
                tokens.remove(i);
                tokens.add("^");
                tokens.add("-1");
            }
        }

        return tokens;
    }

    // shunting yard algorithm to convert array to rpn
    public ArrayList<String> shuntingYardAlg() {
        Stack<String> operatorStack = new Stack<>();
        for (String token : tokens) {
            if (isNumber(token)) {
                rpnOutput.add(token);
            } else if (isFunction(token)) {
                operatorStack.push(token);
            } else if (isOperator(token)) {
                while (!operatorStack.isEmpty()) {
                    String o1 = token;
                    String o2 = operatorStack.peek();
                    int o1P = getPrecedence(o1);
                    int o2P = getPrecedence(o2);
                    if ((isOperator(o2) && (o2P > o1P || (o1P == o2P && getAssociativity(o1) == "left")))) {
                        String s = operatorStack.pop();
                        rpnOutput.add(s);
                    } else {
                        break;
                    }
                }

                operatorStack.push(token);

            } else if (token.equals("(")) {
                operatorStack.push(token);
            } else if (token.equals(")")) {
                while (!operatorStack.isEmpty() && !operatorStack.peek().equals("(")) {
                    String s = operatorStack.pop();
                    rpnOutput.add(s);
                }
                operatorStack.pop();
                if (isFunction(operatorStack.peek())) {
                    String s = operatorStack.pop();
                    rpnOutput.add(s);
                }
            }

        }
        while (!operatorStack.isEmpty()) {
            String s = operatorStack.pop();
            rpnOutput.add(s);
        }
        return rpnOutput;
    }

    // evaluate rpn using stack
    public double rpnEvaluate() {
        Stack<String> resultStack = new Stack<>();
        for (String e : rpnOutput) {
            if (isNumber(e)) {
                resultStack.push(e);
            } else if (isOperator(e)) {
                double x2 = Double.valueOf(resultStack.pop());
                double x1 = Double.valueOf(resultStack.pop());

                double r = calculate(e, x1, x2);
                resultStack.push(String.valueOf(r));
            } else if (isFunction(e)) {
                double x = Double.valueOf(resultStack.pop());

                double r = funcCalculate(e, x);
                resultStack.push(String.valueOf(r));
            }
        }
        return Double.valueOf(resultStack.pop());
    }
}
