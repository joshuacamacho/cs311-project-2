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
    private static ArrayList<Character> symbol = new ArrayList<Character>();
    private static ArrayList<Integer> next = new ArrayList<Integer>();
    private static int symbolIndex=0;
    private static boolean keyword=true;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
        Arrays.fill(theswitch, -1);
        //populate key words
        Scanner filein = new Scanner(new FileInputStream("input1.txt"));
        while(filein.hasNext()){
            String line = filein.next();
            System.out.print(line);
            
            parseWord(line,'*');
//            parseWord(line,'*');
        }
        filein.close();
        System.out.print("\n");
        
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
                        System.out.print(split[i]);
                        parseWord(split[i],'?');
                        System.out.print(" ");
                    
                }
                System.out.print("\n");
            }
        }
        
        
        for(char i : symbol){
            System.out.print(i);
        }
    }
    
    private static void parseWord(String line,char ending) {
        if(line == null || line.length()==0) return;
        int position = 0;
        char c = line.charAt(position);
        int switchIndex = indexOfSwitch(c);
        if(switchIndex < 0) return;
        //check if switch array has a starting position in symbol
        if(theswitch[switchIndex]==-1){
            theswitch[switchIndex]= symbolIndex;
            //one letter check
            if(line.length()== 1) insertNewIntoSymbol("",ending);
            else insertNewIntoSymbol(line.substring(1),ending);
        } else {
            if(line.length()== 1) insertIntoSymbol("",theswitch[switchIndex],ending);
            else insertIntoSymbol(line.substring(1),theswitch[switchIndex],ending);
        }
        
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

    private static void insertNewIntoSymbol(String line, char ending) {
        for(int i=0; i<line.length(); i++){
            symbol.add(symbolIndex, line.charAt(i));
            next.add(null);
            symbolIndex++;
        }
        symbol.add(symbolIndex, ending);
        System.out.print(ending);
        next.add(null);
        symbolIndex++;
    }

    private static void insertIntoSymbol(String word,int start, char ending) {
        if(word.length()==0) return;
        int count=0;
        for(int i=start; i<symbol.size();i++){
            
            //if part of word is already in symbol
            if(word.charAt(count)==symbol.get(i)){
                
                
                //if word is completely in symbol
                if(count == word.length()-1){
                    if(symbol.get(i+1)=='*'){
                            System.out.print("*");
                            return;
                    }
                    if(symbol.get(i+1)=='?' && ending=='?'){
                        System.out.print("@");
                        return;
                    }
//                    if(i==symbol.size()-1) insertNewIntoSymbol("",ending);
                    
                        int pos = i+1;
                        
                        while(true){
                           if(ending != symbol.get(pos)){
                                if(next.get(pos)==null){
                                    next.add(pos,symbolIndex);
                                    insertNewIntoSymbol("",ending);
                                    return;
                                }else{
                                    pos = next.get(pos);
                                }
                            }else{
                              //ending matches
                              if(ending =='?') System.out.print("@");
                              else System.out.print("*");
                              return; 
                           }
                        }
                        
                    
                    
                }
                count++;
                //then continues for loop
            }else{
                //if next is not set, insert word starting at free buffer
                if(next.get(i)==null){
                    next.add(i, symbolIndex);
                    insertNewIntoSymbol(word.substring(count),ending);
                    break;
                //if next IS set, insert substring starting at new position
                } else {
                    insertIntoSymbol(word.substring(count),next.get(i),ending);
                    return;
                }
            }
        }
        
        
    }
    
}
