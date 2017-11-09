package cs311.project.pkg2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

/**
 *
 * @author Joshua Camacho
 */
public class Cs311Project2 {
    private static int[] theswitch = new int[54];
    private static char[] symbol = new char[1200];
    private static int[] next = new int[1200];
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
//          System.out.print(word);
            parseWord(word+"*");
//          parseWord(word+"*");
        }
        filein.close();
        System.out.print("\n");
      
        //parse through java file
        filein = new Scanner(new FileInputStream("input2.txt"));
        while(filein.hasNext()){
            String line = filein.nextLine();
            //filter out symbols we dont care
            line = line.replaceAll("[\\.;{}()\\[\\]\\/\"<>=*+,:&]", " ");
                
                String[] split = line.split(" ");
                for(int i=0; i<split.length; i++){
                    if(!split[i].matches("\\s*") && !split[i].matches("[0-9]+")){
                       System.out.print(split[i]);
                        parseWord(split[i]+"?");
                        System.out.print(" "); 
                    }
                }
                System.out.print("\n");
            
        }
        
        //Print out Switch
        System.out.print("       ");
        for(int i=0; i<20; i++){
            System.out.printf("%4c ",(char)getCharValueOf(i));
        }
        System.out.print("\n Switch:");
        for(int i=0; i<20; i++){
            System.out.printf("%4d ",theswitch[i]); 
        }
        System.out.print("\n       ");
        for(int i=20; i<40; i++){
            System.out.printf("%4c ",(char)getCharValueOf(i));
        }
        System.out.print("\n Switch:");
        for(int i=20; i<40; i++){
            System.out.printf("%4d ",theswitch[i]); 
        }
        System.out.print("\n       ");
        for(int i=40; i<theswitch.length; i++){
            System.out.printf("%4c ",(char)getCharValueOf(i)); 
        }
        System.out.print("\n Switch:");
        for(int i=40; i<theswitch.length; i++){
            System.out.printf("%4d ",theswitch[i]); 
        }
        
        
        //Print out Symbol / next
        int start=0;
        int j=20;
        boolean exit=false;
        while(true){
            System.out.print("\n\n       ");
            for(int i = start; i<j;i++){
                System.out.printf("%4d ",i);
            }
            System.out.print("\nSymbol:");
            for(int i = start; i<j;i++){
                System.out.printf("%4c ", symbol[i]);
            }
            System.out.print("\n  Next:");
            for(int i = start; i<j;i++){
                System.out.printf("%4d ",next[i]);
            }            
            if(exit) break;
            start=j;
            j+=20;

            if(j>symbol.length) {
                j=symbol.length;
                exit=true;
            }
            
        }
        
//          for(int i=0; i<symbolIndex;i++){
//            System.out.print(symbol[i]);
//        }
        
   
    }

    //Main Algorithm
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
                        System.out.print("@");
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
    
    private static int getCharValueOf(int i){
        if(i<26) return i + 65;
        if(i<52) return i + 65 + 6;
        if(i==52) return Character.valueOf('$');
        if(i==53) return Character.valueOf('_');
        return -1;
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