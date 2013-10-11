
//package line only needed by netbeans
//package dameraulevenshtein;

import java.util.*;

/*
 * @author Nick Carlson
 */

public class DamerauLevenshtein {

    //main driver of the program
    public static void main(String[] args) {
        //User chooses which test to run, manual strings or generated DNA
        chooseTest();
    }
    
    //get two acceptable strings from the user and run 2 distance metrics
    public static void getStringsRunMetrics(){
        Scanner reader1 = new Scanner(System.in);
        Scanner reader2 = new Scanner(System.in);
        int dist1;
        int dist2;
        String lineOne;
        String lineTwo;
        
        //prompt user for first string
        System.out.println("Please enter your first string. Note: numeric values are not allowed!");
        System.out.print("Your first string: ");
        lineOne = reader1.nextLine();
        System.out.println("");
        
        //ensure that input string contains no numeric values and is not empty
        isAcceptable(lineOne);
        
        //prompt user for second string
        System.out.println("Please enter your second string. Note: numeric values are not allowed!");
        System.out.print("Your second string: ");
        lineTwo = reader2.nextLine();
        System.out.println("");
        
        //ensure that input string contains no numeric values and is not empty
        isAcceptable(lineTwo);
        
        //compute Damerau-Levenshtein distance and report run time
        long startTime1 = System.nanoTime();
        dist1 = damerauLevenshtein(lineOne,lineTwo);
        long endTime1 = System.nanoTime();
        System.out.println("Damerau-Levenshtein Distance: " + dist1);
        runTime(startTime1,endTime1);
        System.out.println("");
        
        //compute Levenshtein distance and report run time
        long startTime2 = System.nanoTime();
        dist2 = levenshteinDistance(lineOne,lineTwo);
        long endTime2 = System.nanoTime();
        System.out.println("Levenshtein Distance: " + dist2);
        runTime(startTime2,endTime2);
    }
    
    //take in two strings and compute the Damerau-Levenshtein distance between them
    public static int damerauLevenshtein(String str1, String str2){
        int cost;
        
        //create distance table
        int[][] distance = new int[str1.length() + 1][str2.length() + 1];
 
        //label the left-most column with the indices of the characters in the first string
        for (int i = 0; i <= str1.length(); i++){
            distance[i][0] = i;
        }
        //label the top row with the indices of the characters in the second string
        for (int j = 1; j <= str2.length(); j++){
            distance[0][j] = j;
        }
 
        //iterate through the rest of the table and fill in values for operations
        for (int i = 1; i <= str1.length(); i++){
            for (int j = 1; j <= str2.length(); j++){
                //if characters match, cost is 0 to mark no substitution
                if(str1.charAt(i-1) == str2.charAt(j-1)){
                    cost = 0;
                }else{
                    cost = 1;
                }
                
                //calculate table values for deletion, insertion, and substitution, and use the smallest
                distance[i][j] = minimumThree(
                                 distance[i - 1][j] + 1,            // deletion
                                 distance[i][j - 1] + 1,            // insertion
                                 distance[i - 1][j - 1] + cost);    // substitution
                
                //special case to handle transposition, this is the Damerau step, which edits a substring no more than once
                if((i > 1) && (j > 1) && (str1.charAt(i-1) == str2.charAt(j-2)) && (str1.charAt(i-2) == str2.charAt(j-1))){
                    distance[i][j] = minimumTwo(
                                     distance[i][j],                // no change
                                     distance[i-2][j-2] + cost      // transposition
                                     );
                }
            }
        }
        
        //return Damerau-Levenshtein Distance
        return distance[str1.length()][str2.length()];
    }
    
    //determine if string is acceptable as input
    public static void isAcceptable(String str){
        //ensure that input string contains no numeric values and is not empty
        if(str.matches(".*\\d.*")){
            System.out.println("Your input does not meet the criteria stated above.");
            System.exit(0);
        }else if(str.equals("")){
            System.out.println("You didn't enter anything.");
            System.exit(0);
        }
    }
    
    //compute run time of string metric and report
    public static void runTime(long startTime, long endTime){
        long duration = endTime - startTime;
        double elapsedTime = (double) duration / 1000000.0;
        System.out.println("Distance computed in: " + elapsedTime + " milliseconds.");
    }
    
    //return the minimum of three integers
    public static int minimumThree(int a, int b, int c){
        return Math.min(Math.min(a, b), c);
    }
    
    //return the minimum of two integers
    public static int minimumTwo(int a, int b){
        return Math.min(a, b);
    }
    
    //take in two strings and compute the Levenshtein distance between them
    public static int levenshteinDistance(String str1, String str2) {
        int cost;
        
        //create distance table
        int[][] distance = new int[str1.length() + 1][str2.length() + 1];
 
        //label the left-most column with the indices of the characters in the first string
        for (int i = 0; i <= str1.length(); i++){
            distance[i][0] = i;
        }
        //label the top row with the indices of the characters in the second string
        for (int j = 1; j <= str2.length(); j++){
            distance[0][j] = j;
        }
 
        //iterate through the rest of the table and fill in values for operations
        for (int i = 1; i <= str1.length(); i++){
            for (int j = 1; j <= str2.length(); j++){
                //if characters match, cost is 0 to mark no substitution
                if(str1.charAt(i-1) == str2.charAt(j-1)){
                    cost = 0;
                }else{
                    cost = 1;
                }
                
                //calculate table values for deletion, insertion, and substitution, and use the smallest
                distance[i][j] = minimumThree(
                                 distance[i - 1][j] + 1,                // deletion
                                 distance[i][j - 1] + 1,                // insertion
                                 distance[i - 1][j - 1] + cost);        // substitution
            }
        }
        
        //return Levenshtein Distance
        return distance[str1.length()][str2.length()];    
        }
    
    //generates a string of a specified length contaning characters from a specified alphabet
    public static String makeString(int size){
        char[] chars = "ACGT".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        
        //uncomment the line below to print generated strings
        //System.out.println(output);
        
        return output;
    }
    
    //allow the user to decide which of the two tests to run
    public static void chooseTest(){
        Scanner choose = new Scanner(System.in);
        int choice = 0;
        
        //print header
        System.out.println("Enter 1 to specify your own two strings to compare.");
        System.out.println("Enter 2 to specify the size of DNA squences to generate and compare.");
        System.out.print("Your choice: ");
        
        //take in and verify integer
        try{
        choice = choose.nextInt();
        }catch(InputMismatchException e){
            System.out.println("");
            System.out.println("Your input does not meet the criteria stated above.");
            System.exit(0);
        }
        
        //if integer is an acceptable choice, run the appropriate test
        System.out.println("");
        if(choice == 1){
            //get strings from user and run string metrics
            getStringsRunMetrics();
        }else if(choice == 2){
            //get string size from user and generate randomized DNA sequences
            genSeqsAndRunMetrics();
        }else{
            //if not 1 or 2, input in unacceptable
            System.out.println("Your input does not meet the criteria stated above.");
            System.exit(0);
        }
    }
    
    //get string size from user, generate randomized DNA sequences, and run 2 distance metrics
    public static void genSeqsAndRunMetrics(){
        Scanner input = new Scanner(System.in);
        int dist1, dist2;
        int size = 0;
        
        //prompt user for input
        System.out.println("Please enter the size of the randomized DNA sequences you'd like to generate. Integers greater than zero only!");
        System.out.print("Size: ");
        
        //get integer size from user and verify
        try{
        size = input.nextInt();
        }catch(InputMismatchException e){
            System.out.println("");
            System.out.println("Your input does not meet the criteria stated above.");
            System.exit(0);
        }
        
        //if size is acceptable, run string metrics
        if(size > 0){
        System.out.println("");
        String lineOne = makeString(size);
        String lineTwo = makeString(size);
        
        //compute Damerau-Levenshtein distance and report run time
        long startTime1 = System.nanoTime();
        dist1 = damerauLevenshtein(lineOne,lineTwo);
        long endTime1 = System.nanoTime();
        System.out.println("Damerau-Levenshtein Distance: " + dist1);
        runTime(startTime1,endTime1);
        System.out.println("");
        
        //compute Levenshtein distance and report run time
        long startTime2 = System.nanoTime();
        dist2 = levenshteinDistance(lineOne,lineTwo);
        long endTime2 = System.nanoTime();
        System.out.println("Levenshtein Distance: " + dist2);
        runTime(startTime2,endTime2);
        }else{
            System.out.println("");
            System.out.println("Your input does not meet the criteria stated above.");
        }
    }
    
}
