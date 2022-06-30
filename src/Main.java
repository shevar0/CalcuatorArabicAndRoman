package src;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

        boolean exitRequest = false;
        String expression;
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.println("Type expression");
            expression = scanner.nextLine().trim();
            System.out.println(calc(expression));
        } while (!exitRequest);

        System.out.println("Quit Program");
    }

    public static String calc(final String input) throws Exception {

        Helper helper = new Helper();

        String first = "";
        String second;
        String operator = "";
        String tempString = "";

        boolean roman = false;
        boolean arabic = false;

        for (Character c : input.toCharArray())
            switch (c) {
                case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
                    if (roman || operator.isEmpty() & !first.isEmpty())
                        throw new CalculatorException("throws Exception //т.к. используются одновременно разные системы счисления");
                    arabic = true;
                    tempString = tempString.concat(c.toString());
                }
                case 'I', 'V', 'X', 'L', 'C', 'D', 'M' -> {
                    if (arabic)
                        throw new CalculatorException("throws Exception //т.к. используются одновременно разные системы счисления");
                    roman = true;
                    tempString = tempString.concat(c.toString());
                }
                case ' ', '+', '-', '/', '*' -> {
                    if (first.isEmpty() & tempString.isEmpty())
                        throw new CalculatorException("throws Exception //т.к. отсутсвует операнд");
                    if (first.isEmpty()) first = tempString;
                    tempString = "";
                    if (!c.equals(' ') & !operator.isEmpty())
                        throw new CalculatorException("throws Exception //т.к. формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");
                    if (!c.equals(' ')) operator = c.toString();
                }
                default -> throw new CalculatorException("throws Exception //т.к. недопустимые данные");
            }

        if (tempString.isEmpty()) throw new CalculatorException("throws Exception //т.к. отсутсвует операнд");
        second = tempString;

        if (first.isEmpty()
                || second.isEmpty())
            throw new CalculatorException("throws Exception //т.к. строка не является математической операцией");

        if (operator.isEmpty())
            throw new CalculatorException("throws Exception //т.к. отсутсвует оператор");

        int firstInt;
        int secondInt;

        if (roman) {
            firstInt = helper.RomToInt(first);
            secondInt = helper.RomToInt(second);
        } else {
            firstInt = Integer.parseInt(first);
            secondInt = Integer.parseInt(second);
        }

        if (firstInt <= 0 || secondInt <= 0) throw new CalculatorException("throws Exception //т.к. операнд меньше 1");
        if (firstInt > 10 || secondInt > 10) throw new CalculatorException("throws Exception //т.к. операнд больше 10");

        int result = 0;

        switch (operator) {
            case "+" -> result = firstInt + secondInt;
            case "-" -> result = firstInt - secondInt;
            case "*" -> result = firstInt * secondInt;
            case "/" -> {
                if (secondInt == 0) throw new CalculatorException("throws Exception //т.к. деление на 0");
                result = firstInt / secondInt;
            }
        }

        String output;
        if (roman) output = helper.IntToRom(result);
        else output = Integer.toString(result);

        return output;
    }
}

class Helper {

    int[] intervals = {0, 1, 4, 5, 9, 10, 40, 50, 90, 100, 400, 500, 900, 1000};
    String[] numerals = {"", "I", "IV", "V", "IX", "X", "XL", "L", "XC", "C", "CD", "D", "CM", "M"};

    int RomToInt(String input) throws CalculatorException {

        String remainder = input;
        int result = 0;

        for (int i = intervals.length - 1; i >= 0; i--) {
            if (numerals[i].length() > 1 & remainder.length() > 1)
                while (remainder.substring(0, 1).equals(numerals[i])) {
                    if (remainder.length() > 1) remainder = remainder.substring(2);
                    else remainder = "";
                    result += intervals[i];
                }
            else {
                if (remainder.length() > 0)
                    while (((Character) remainder.charAt(0)).toString().equals(numerals[i])) {
                        if (remainder.length() > 1) remainder = remainder.substring(1);
                        else remainder = "";
                        result += intervals[i];
                        if (remainder.isEmpty()) break;
                    }
            }
        }

        if (result < 1)
            throw new CalculatorException("throws Exception //т.к. в римской системе нет отрицательных чисел");
        if (result > 3999)
            throw new CalculatorException("throws Exception //т.к. полученное число слишком большое для римской системы чисел");
        String temp = IntToRom(result);
        if (!temp.equals(input))
            throw new CalculatorException("throws Exception //т.к. неверное римское число, было " + input + " надо " + temp);

        return result;
    }

    String IntToRom(final int input) throws CalculatorException {

        if (input < 1)
            throw new CalculatorException("throws Exception //т.к. в римской системе нет отрицательных чисел");
        if (input > 3999)
            throw new CalculatorException("throws Exception //т.к. полученное число слишком большое для римской системы чисел");

        int remainder = input;
        String result = "";

        for (int i = intervals.length - 1; i > 0; i--) {
            while (remainder >= intervals[i]) {
                result = result.concat(numerals[i]);
                remainder -= intervals[i];
            }
        }

        return result;
    }
}