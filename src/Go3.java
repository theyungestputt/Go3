//go pieces ◯ ●

import java.util.Scanner;

public class Go3 {

    //Initializes our empty go board
    static String[][] goBoard = new String[9][9];

    //Helpful test board 
    // static String[][] goBoard = {
    //                                 {null,null,null,null,null,null,null,null,null},
    //                                 {null,null,null,"-#",null,null,null,null,null},
    //                                 {null,null,"-#","-@","-#",null,null,null,null},
    //                                 {null,null,"-#","-@","-#",null,null,null,null},
    //                                 {null,null,"-#","-@","-#",null,null,null,null},
    //                                 {null,null,null,null,null,null,null,null,null},
    //                                 {null,null,null,null,null,null,null,null,null},
    //                                 {null,null,null,null,null,null,null,null,null},
    //                                 {null,null,null,null,null,null,null,null,null},
    //                             };

    //Initializes our boolean lives array
    static boolean[][] lives = new boolean[9][9]; //Default: true

    //Initializes our boolean checked array
    static boolean[][] checked = new boolean[9][9]; //Default: false

    //Initializes black and white scores
    static int blackScore = 0;
    static int whiteScore = 0;

    // Counter to track consecutive passes
    static int consecutivePasses = 0;

    //Checks if a certain piece is out of bounds
    static boolean outOfBounds(int moveY, int moveX, boolean turn){
        //Sets the dimension of our board
        int dimension = goBoard.length - 1;
        if ((moveY > dimension || moveY < 0) || (moveX > dimension || moveX < 0)) {
            System.out.println(
                    "\n\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n\nOut of Bounds!\n\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n\n");
            return turn;
        } else {
            
            //Places piece on valid coordinate
            if (goBoard[moveY][moveX] == null) {
                goBoard[moveY][moveX] = ((turn) ? "-@" : "-#");
                //Changes for the case when our piece is on the left edge
                if (moveX == 0){
                    goBoard[moveY][moveX] = ((turn) ? "@" : "#");
                }
            //Toggle turn only if move is valid
            return !turn; 
            } 
            
            //Checks for pieces already in selected spot 
            else {
                System.out.println(
                        "\n\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n\nThere is already a piece there!\n\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n\n");
                return turn;
            }   
        }
    }


    //Function to check if a piece is surrounded
    static void isSurrounded() {
        System.out.println("Checking if pieces are surrounded...");
        for (int i = 0; i < goBoard.length; i++) {
            for (int j = 0; j < goBoard[i].length; j++) {
                // Check if the cell contains a piece
                if (goBoard[i][j] != null) {
                    System.out.println("Piece found at: " + j + ", " + i);
                    // Check if the piece is surrounded
                    int surrounded_count = 0; 
                    // Check all four directions
                    if (i > 0 && goBoard[i - 1][j] != null && ((goBoard[i - 1][j].endsWith("#")) || (goBoard[i - 1][j].endsWith("@")))){
                        surrounded_count = surrounded_count + 1;
                        System.out.println("North cell contains a piece");
                    }
                    if (i < goBoard.length - 1 && goBoard[i + 1][j] != null && ((goBoard[i + 1][j].endsWith("#")) || (goBoard[i + 1][j].endsWith("@")))) {
                        surrounded_count = surrounded_count + 1;
                        System.out.println("South cell contains a piece");
                    }
                    if (j > 0 && goBoard[i][j - 1] != null && ((goBoard[i][j - 1].endsWith("#")) || (goBoard[i][j -1].endsWith("@")))) {
                        surrounded_count = surrounded_count + 1;
                        System.out.println("West cell contains a piece");
                    }
                    if (j < goBoard[i].length - 1 && goBoard[i][j + 1] != null && ((goBoard[i][j + 1].endsWith("#")) || (goBoard[i][j + 1].endsWith("@")))) {
                        surrounded_count = surrounded_count + 1;
                        System.out.println("East cell contains a piece");
                    }
                    
                    // If the piece is surrounded, set live array to true which sets its "life" to false
                    if (surrounded_count == 4){
                        lives[i][j] = true;
                        System.out.println(goBoard[i][j] + " is surrounded");
                        System.out.println();
                    } 
                    else{
                        System.out.println(goBoard[i][j] + " is not surrounded");
                        System.out.println();
                    }
                }
            }
        }
    }

    //Looks for pieces which are surrounded and begins to check if they are alive
    static void isAlive(){
        System.out.println("Checking if a piece's alive index evaluates to false...");
        for (int i = 0; i < goBoard.length; i++){
            for (int j = 0; j < goBoard.length; j++){
                //Checks if alive index is false
                if (lives[i][j] == true){
                    System.out.println("Piece found at: " + j + ", " + i);
                    //Runs recursive checking on said piece
                    recursiveChecker(i, j);
                }
            }
        }

    }

    //Recursively checks connected pieces to see if a piece is connected to a friend which can breathe 
    static void recursiveChecker(int i, int j){
        //Makes checked array true to avoid infinite recursion
        checked[i][j] = true;
        //Prints out our checked array every function run for readability
        System.out.println("Checked array:");
        for (int k = 0; k < 9; k++){
            for (int l = 0; l < 9; l++){
                if (checked[k][l] == false){
                    System.out.print("F ");
                }

                if (checked[k][l] == true){
                    System.out.print("T ");
                }
                
            }
            System.out.println();
        }
        System.out.println();
        //Determines color of the piece
        boolean black = true;
        if (goBoard[i][j].endsWith("#")){
            black = false;
        }
        //Checks the alive index of all four directions (we only care about those of same color)
        if (i > 0 && goBoard[i - 1][j].endsWith((black) ? "@" : "#") && checked[i - 1][j] == false) {
            System.out.println("North cell contains friendly piece");
            if (lives[i-1][j] == false){
                System.out.println("North cell contains alive friendly piece");
                lives[i][j] = false;
                //Updates all previously checked pieces to alive 
                for (int k = 0; k < 9; k++){
                    for (int l = 0; l < 9; l++){
                        if (checked[k][l] == true){
                            lives[k][l] = false;
                        }
                        
                    }

                }
            }
            else{
                //Recursive Step
                System.out.println("North cell contains dead friendly piece, checking neighbors... ");
                recursiveChecker(i-1, j);
            }
        }
        if (i < goBoard.length - 1 && goBoard[i + 1][j].endsWith((black) ? "@" : "#") && checked[i + 1][j] == false) {
            System.out.println("South cell contains friendly piece");
            if (lives[i+1][j] == false){
                System.out.println("South cell contains alive friendly piece");
                lives[i][j] = false;
                //Updates all previously checked pieces to alive 
                for (int k = 0; k < 9; k++){
                    for (int l = 0; l < 9; l++){
                        if (checked[k][l] == true){
                            lives[k][l] = false;
                        }
                        
                    }

                }
            }
            else{
                //Recursive step
                System.out.println("South cell contains dead friendly piece, checking neighbors... ");
                recursiveChecker(i+1, j);
            }
        }
        if (j > 0 && goBoard[i][j-1].endsWith((black) ? "@" : "#") && checked[i][j-1] == false) {
            System.out.println("West cell contains friendly piece");
            if (lives[i][j-1] == false){
                System.out.println("West cell contains alive friendly piece");
                lives[i][j] = false;
                //Updates all previously checked pieces to alive 
                for (int k = 0; k < 9; k++){
                    for (int l = 0; l < 9; l++){
                        if (checked[k][l] == true){
                            lives[k][l] = false;
                        }
                        
                    }

                }
            }
            else{
                //Recursive step
                System.out.println("West cell contains dead friendly piece, checking neighbors... ");
                recursiveChecker(i, j-1);
            }
        }
        if (j < goBoard[i].length - 1 && goBoard[i][j+1].endsWith((black) ? "@" : "#") && checked[i][j+1] == false) {
            System.out.println("East cell contains friendly piece");
            if (lives[i][j+1] == false){
                System.out.println("East cell contains alive friendly piece");
                lives[i][j] = false;
                //Updates all previously checked pieces to alive 
                for (int k = 0; k < 9; k++){
                    for (int l = 0; l < 9; l++){
                        if (checked[k][l] == true){
                            lives[k][l] = false;
                        }
                        
                    }

                }
            }
            else{
                //Recursive step
                System.out.println("East cell contains dead friendly piece, checking neighbors... ");
                recursiveChecker(i, j+1);
            }
        }
    }

    //Removes casualties and adjusts score accordingly
    static void casualtyRemover(){ 
        //Looks for casulties on the board
        System.out.println("Removing casulties... ");
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++){
                if (lives[i][j] == true){
                    //Checks color and adjusts board
                    if (goBoard[i][j].endsWith("@")){                        
                        blackScore = blackScore - 1;
                    }
                    if (goBoard[i][j].endsWith("#")){
                        whiteScore = whiteScore - 1;
                    }
                    //Replaces captured piece with an empty space
                    goBoard[i][j] = null;

                }
                
            }
        }
    }


    public static void main(String[] args) {
        //Initliazes our continue, turn and move variables
        Boolean cont = true, turn = true;
        int moveX, moveY;

        //Opens and instance of scanner
        Scanner scn = new Scanner(System.in);

        //Allows program to run continues while cont = true
        while (cont) {


            //Constructs our board for alive (true) and dead (false) pieces 
            System.out.println("Lives array:");
            for (int i = 0; i < 9; i++){
                for (int j = 0; j < 9; j++){
                    if (lives[i][j] == false){
                        System.out.print("T ");
                    }

                    if (lives[i][j] == true){
                        System.out.print("F ");
                    }
                    
                }
                System.out.println();
            }
            System.out.println();

            //Resets our checked array to its default
            System.out.println("Checked array:");
            for (int i = 0; i < 9; i++){
                for (int j = 0; j < 9; j++){
                    checked[i][j] = false;
                    if (checked[i][j] == false){
                        System.out.print("F ");
                    }

                    if (checked[i][j] == true){
                        System.out.print("T ");
                    }
                    
                }
                System.out.println();
            }
            System.out.println();

            //Constructs our empty go board
            System.out.println("  0 1 2 3 4 5 6 7 8");
            for (int i = 0; i < goBoard.length; i++) {
                System.out.print(i + " ");
                for (int j = 0; j < goBoard[i].length; j++) {
                    if (goBoard[i][j] == null) {
                        if (j == 0)
                            System.out.print("|");
                        else
                            System.out.print("-|");

                    } else {
                        System.out.print(goBoard[i][j]);
                    }

                }
                System.out.println();
            }

            //Outputs the current score for each player
            System.out.println("Black score: " + blackScore);
            System.out.println("White score: " + whiteScore);



            //Prompts user to move and asks which coordinate they would like to move to
            System.out.println("\n" + ((turn) ? "Black" : "White") + "'s turn to move!\n\n");

            System.out.println("Please enter the x coordinate where you'd like to place a "
                    + ((turn) ? "Black" : "White") + " piece or -1 to pass");

            moveX = scn.nextInt();
            
            //Checks if passing move has occured
            if (moveX == -1){
                System.out.println("Passing " + ((turn) ? "Black's" : "White's") + " turn...");
                consecutivePasses = consecutivePasses + 1;
                //Ends game loop if both players have passed
                if (consecutivePasses >= 2){
                    cont = false;
                    System.out.println("The game is over!");
                    //Outputs the final score for each player
                    System.out.println("Black score: " + blackScore);
                    System.out.println("White score: " + whiteScore);
                    break;
                }
                //Changes turn
                turn = !turn;
                continue;
            }

            System.out.println("Please enter the y coordinate where you'd like to place a "
                    + ((turn) ? "Black" : "White") + " piece");

            moveY = scn.nextInt();

            System.out.println();

            //Checks if the move is out of bounds, places piece if not
            turn = outOfBounds(moveY, moveX, turn);

            isSurrounded();

            isAlive();

            casualtyRemover();


        }

        scn.close();

    }

}

// T put was here