package com.example.admin.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Utils {

    public static String[] cards = new String[]{
            "1, 2, 3, 5, 8",
            // Standard fibonaci like series of values
            "1, 2, 3, 5, 8, 13, 20, 40, 100",
            // Special card set with "?" for unclear stories
            "1, 2, 3, 5, 8, 13, 20, 40, ?",
            // Powers of two used by other teams
            "0", "1, 2, 4, 8, 16, 32, 64",
            // Card set used to estimate hours as different fractions and multiples of a working day
            "1, 2, 4, 8, 12, 16, 24, 32, 40",
            // Demonstration of the coffee cup card
            "&#9749;, 1, 2, 3, 5, 8, 13, 20, ?",
            // T-shirt Size
            "XXS", "XS, S, M, L, XL, XXL, ?",
            // Fibonacci number
            "1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, &#9749;, ?",
            // Fibonaci series including 0.5
            "0.5, 1, 2, 3, 5, 8, 13, 20, 40, 100",
            // Canadian Cash format
            "1, 2, 5, 10, 20, 50, 100",
            // Standard fibonacci with shrug
            "1, 2, 3, 5, 8, 13, &#F937;",
            //Salesforce Estimates 
            "0.5", "1", "3", "5", "8"};

    public static void hideKeyboard(View view) {

        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0, null);

    }
}
