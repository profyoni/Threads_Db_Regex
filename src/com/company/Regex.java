package com.company;

import java.io.Console;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Regex {

    public static void main2(String[] args){
        Console console = System.console();
        if (console == null) {
            System.err.println("No console.");
            System.exit(1);
        }
        while (true) {
// Effective Java new Zigwig(3,4,5); ZigWig.createWithHeight(3).setWeight(4)...
            Pattern pattern = Pattern.compile(console.readLine("%nEnter your regex: "));

            Matcher matcher =
                    pattern.matcher(console.readLine("Enter input string to search: "));

            boolean found = false;
            while (matcher.find()) {
                console.format("I found the text" +
                                " \"%s\" starting at " +
                                "index %d and ending at index %d.%n",
                        matcher.group(),
                        matcher.start(),
                        matcher.end());
                if (matcher.groupCount() > 1) // found capturing groups
                {
                    for (int i=0;i<=matcher.groupCount();i++)
                    {
                        console.format("Group %d: %s%n", i, matcher.group(i));
                    }
                }
                found = true;
            }
            if(!found){
                console.format("No match found.%n");
            }
        }
    }
}