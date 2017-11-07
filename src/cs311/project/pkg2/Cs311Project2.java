/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs311.project.pkg2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

/**
 *
 * @author Josh
 */
public class Cs311Project2 {

    

    
    private static int[] theswitch = new int[54];
    private static char[] symbol = new char[1500];
    private static int[] next = new int[1500];
    private static int symbolIndex=0;
    private static int wordIndex;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
        Arrays.fill(theswitch, -1);
        Arrays.fill(symbol, '\0');
        Arrays.fill(next,-1);
        //populate key words
        Scanner filein = new Scanner(new FileInputStream("input1.txt"));
        while(filein.hasNext()){
            String word = filein.next();
            System.out.print(word);
            parseWord(word+"*");
//            parseWord(word+"*");
        }
        filein.close();
        System.out.print("\n");
//        for(int i=0; i<symbolIndex;i++){
//            System.out.print(symbol[i]);
//        }
        //parse through java file
        filein = new Scanner(new FileInputStream("input2.txt"));
        while(filein.hasNext()){
            String line = filein.nextLine();
            //filter out comment lines
            if(line.matches("^\\s*/.*") || line.matches("^\\s*\\*.*")){
//                System.out.println("COMMENT LINE "+ line);
            }else{
                line = line.replaceAll(";|\\{|\\}|\\(.*\\)|[0-9]", "");
                String[] split = line.split(" |\\.");
                for(int i=0; i<split.length; i++){
                    if(!split[i].matches("\\s*")){
                       System.out.print(split[i]);
                        parseWord(split[i]+"?");
                        System.out.print(" "); 
                    }
                }
                System.out.print("\n");
            }
        }
        
        
   
    }

    private static void parseWord(String word) {
        wordIndex=0;
        char c = getNextSymbol(word);
        if(indexOfSwitch(c)==-1) return;
        int ptr = theswitch[indexOfSwitch(c)];
        if(ptr == -1){
           theswitch[indexOfSwitch(c)]=symbolIndex;
           create(word.substring(wordIndex));
        }else{
            // theswitch contains the starting letter
            boolean exit = false;
            c = getNextSymbol(word);
            while(!exit){
                if(symbol[ptr]==c){
                    if(c!='*' && c!='?'){
                        ptr = ptr + 1;
                        if(wordIndex == word.length()-1){
                            if(symbol[ptr]=='*'){
                                System.out.print("*");
                                exit = true;
                            }else if(symbol[ptr]=='?'){
                                System.out.print("@");
                                exit = true;
                            }
                        }
                        c = getNextSymbol(word);
                    }else{
                        System.out.print(c);
                        exit=true;
                    }
                }else if(next[ptr]!=-1){
                    ptr = next[ptr];
                }else{
                    next[ptr]=symbolIndex;
                    create(word.substring(wordIndex-1));
                    exit=true;
                }
            }//while
        }//if
        
    }

    private static char getNextSymbol(String word) {
        char c = word.charAt(wordIndex);
        wordIndex++;
        return c;
    }

    private static void create(String word) {
        for(int i=0; i<word.length();i++){
            symbol[symbolIndex]=word.charAt(i);
            symbolIndex++;
        }
        if(word.charAt(word.length()-1)=='?') System.out.print("?");
    }
    
    private static int indexOfSwitch(char c) {
        if(Character.isAlphabetic(c)){
            if(Character.valueOf(c)<=90 && Character.valueOf(c)>= 65){
                return Character.valueOf(c) - 65;
            }else if(Character.valueOf(c)<=122 && Character.valueOf(c)>= 97){
                return Character.valueOf(c) - 65 - 6;
            }
        }
        if(c=='$') return 52;
        if(c=='_') return 53;
        return -1;
    }
    
}