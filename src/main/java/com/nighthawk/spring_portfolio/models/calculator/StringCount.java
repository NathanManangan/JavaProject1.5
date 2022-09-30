package com.nighthawk.spring_portfolio.models.calculator;

import java.util.Scanner;

public class StringCount {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        String a = "A";
        int b = 0;
        
        while (!a.isEmpty()) {
            System.out.println("What is your string?");
            a = scanner.nextLine();
            
            if (!a.isEmpty()) {
                b = a.length();
                System.out.println(b);
            }    
        }
        
        System.out.println("Thanks for playing!");
    }
}