/**

 Author: Zhezheng Hong

 Student ID: 31418089

 Date: 9/28/2018

 Description:

 1) This code solves the problem of basic tic-tac-toe.

 2) The Minimax algorithm and state-space paradigm are used in this program.

 **/
package com.zzh;
import java.util.Scanner;

public class Main {
    //Constants of cell contents
    public static final char EMPTY = ' ';
    public static final char CROSS = 'X';
    public static final char NOUGHT = 'O';

    //Constants of the state of the game
    public static final int PLAYING = 0;
    public static final int DRAW = 1;
    public static final int CROSS_WIN = 2;
    public static final int NOUGHT_WIN = 3;

    //Current state of the game
    public static int GAME_STATE;

    //Constants of the lengths of columns and rows of the game board
    public static final int ROWS = 3;
    public static final int COLS = 3;

    //Content of the game board
    public static char[][] BOARD = new char[ROWS][COLS];
    //Target move
    public static int TARGET_COL,TARGET_ROW;

    //Input stream
    public static Scanner in = new Scanner(System.in);


    //==================================Main function==========================================
    // Start the game and keep playing until the game is over.
    // If game is over, start a new game.

    public static void main(String[] args) {
        do {
            System.err.println("Start a new game:");
            PrintRules();
            System.err.print("Type in X or O to choose your seed: ");
            String s = in.next();
            char CHOSEN_SEED  = s.charAt(0);
            //Verify Input is valid or not. Upper and lower case handled.
            if(CHOSEN_SEED=='x'|CHOSEN_SEED=='X') {
                CHOSEN_SEED = 'X';
            }
            else if(CHOSEN_SEED=='o'|CHOSEN_SEED=='O') {
                CHOSEN_SEED = 'O';
            }
            else{
                System.err.println("Invalid Input, Please choose again!");
                continue;
            }
            System.err.println("--------------GAME ON--------------");
            char PLAYER_SEED = CHOSEN_SEED;
            char AI_SEED = (PLAYER_SEED==CROSS)?NOUGHT:CROSS;
            Init_State();
            do {
                if(PLAYER_SEED==CROSS){
                    PlayerMove(PLAYER_SEED);
                    if(GAME_STATE==PLAYING)
                        AiMove(AI_SEED);
                }else{
                    AiMove(AI_SEED);
                    if(GAME_STATE==PLAYING)
                        PlayerMove(PLAYER_SEED);
                }
                if (GAME_STATE == CROSS_WIN)
                    System.err.println("Congratulation! Player X win!!");
                else if (GAME_STATE == NOUGHT_WIN)
                    System.err.println("Congratulation! Player O win!!");
                else if (GAME_STATE == DRAW)
                    System.err.println("It's a draw!");
            } while (GAME_STATE == PLAYING);
            System.err.println("-------------GAME OVER-------------");
        }while(true);
    }


    //============================State Initialization=========================================
    // Initialize the state

    public static void Init_State(){
        for(int i=0;i<ROWS;i++) {
            for (int j=0; j<COLS;j++)
                BOARD[i][j] = EMPTY;
        }
        GAME_STATE = PLAYING;
    }



    //============================Player Move Input============================================
    //Input: player seed
    //Read a move of human player

    public static void TakeAction(char SEED){
        boolean Valid_Input = false;
        do {
            if(SEED == CROSS){
                System.err.print("Player 'X', enter your move : ");
            } else{
                System.err.print("Player 'O', enter your move : ");
            }
            int input_num = in.nextInt();
            int row = (input_num-1)/3;
            int col = (input_num-1)%3;
            if(row>=0 && row<ROWS && col>=0 && col<COLS && BOARD[row][col]== EMPTY){
                TARGET_COL = col;
                TARGET_ROW = row;
                BOARD[row][col] = SEED;
                Valid_Input = true;
            }else{
                System.err.println("Invalid input, plz try again!");
            }

        }while(!Valid_Input);
    }


    //============================Transitional Model===========================================
    //Update the state of the game

    public static void UpdateState(char SEED,int TARGET_ROW,int TARGET_COL){
        if(PlayerWon(SEED,TARGET_ROW,TARGET_COL)){
            GAME_STATE = (SEED == CROSS)?CROSS_WIN:NOUGHT_WIN;
        }
        else if(IsDraw(BOARD)){
            GAME_STATE = DRAW;
        }
        PrintBoard();
    }

    //============================Terminal Tests===============================================
    //Input: current board
    //Output: Draw or not

    public static boolean IsDraw(char[][] board){
        for(int i=0;i<board.length;i++){
            for(int j=0;j<board[0].length;j++)
                if(board[i][j]==EMPTY)
                    return false;
        }
        return true;
    }

    //Input: Current player's seed, position of the move
    //Output: Game winning move or not

    public static boolean PlayerWon(char SEED,int TARGET_ROW,int TARGET_COL){
        return (BOARD[TARGET_ROW][0]==SEED
                &&BOARD[TARGET_ROW][1]==SEED
                &&BOARD[TARGET_ROW][2]==SEED
                ||BOARD[0][TARGET_COL]==SEED
                &&BOARD[1][TARGET_COL]==SEED
                &&BOARD[2][TARGET_COL]==SEED
                ||TARGET_COL==TARGET_ROW
                &&BOARD[0][0]==SEED
                &&BOARD[1][1]==SEED
                &&BOARD[2][2]==SEED
                ||TARGET_COL+TARGET_ROW==2
                &&BOARD[2][0]==SEED
                &&BOARD[1][1]==SEED
                &&BOARD[0][2]==SEED);
    }

    //============================Human move===================================================
    // Human player take a move

    public static void PlayerMove(char PLAYER_SEED){
        TakeAction(PLAYER_SEED);
        UpdateState(PLAYER_SEED, TARGET_ROW, TARGET_COL);
    }

    //============================AI move======================================================
    // AI player take a move

    public static void AiMove(char AI_SEED){
        PcMove aimove;
        PcPlayer AI = new PcPlayer(AI_SEED);
        aimove = AI.MakeOptimalMove(BOARD,AI_SEED,GAME_STATE);
        int AI_MOVE = 3*aimove.x+aimove.y+1;
        System.out.println(AI_MOVE);
        System.err.println("AI took the move: "+AI_MOVE);
        BOARD[aimove.x][aimove.y] = AI_SEED;
        UpdateState(AI_SEED,aimove.x,aimove.y);
    }


    //============================Print board==================================================
    //Print the current board

    public static void PrintBoard(){
        System.err.println();
        for(int row=0;row<ROWS;row++){
            for(int col=0;col<COLS;col++){
                System.err.print("   "+BOARD[row][col]+"   ");
                if(col!=2) System.err.print('|');
            }
            System.err.println();
            if(row!=2) System.err.println("-----------------------");
        }
        System.err.println();
    }

    //============================Print instruction============================================
    //Print the instruction for human player

    public static void PrintRules(){
        System.err.println("-------------RULES-------------");
        System.err.println("Players take turns to make moves,");
        System.err.println("enter number 1-9 to indicate your move");
        int i = 1;
        for(int row=0;row<ROWS;row++){
            for(int col=0;col<COLS;col++){
                System.err.print("   "+i+"   ");
                i++;
                if(col!=2) System.err.print('|');
            }
            System.err.println();
            if(row!=2) System.err.println("-----------------------");
        }
        System.err.println("-------------------------------");
    }

}
