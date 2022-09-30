package com.nighthawk.spring_portfolio.models.calculator;

import net.bytebuddy.asm.Advice.Return;

public class Guessing {
   
    static int b = randomNumber();
    public static int randomNumber() {
        
        double a = (1 + Math.random() * 100);
        return ((int) Math.floor(a));
    }
   
    public static void newNumber(){
        b = randomNumber();
    }

    public static String guessing(String guess) {
       
        
        int c = Integer.valueOf(guess);
        
         if (c != b) {
            if (c > b) {
                return (c + " was not the number," + "\nlower\n");
            }
            if (c < b) {
                return ( c + " was not the number," + "\nhigher\n");
            }
                
        }
         if (c == b) {
            return "You guessed it! The number was " + b;
        }
            return "error";
   }
}
