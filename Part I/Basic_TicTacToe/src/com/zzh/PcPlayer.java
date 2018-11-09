package com.zzh;
/**
 This class represent the object AI player
 It contains the function of minimax
 **/
public class PcPlayer {
    char Pc_Seed;
    char player_seed;

    //==================================Object Initialization==================================
    //Initialize object AI player by assigning seed

    public PcPlayer(char seed){
        Pc_Seed = seed;
        player_seed = (Pc_Seed==Main.CROSS)?Main.NOUGHT:Main.CROSS;
    }

    //==================================MINIMAX Function=======================================
    //Input: current board, the seed of AI player, current game state
    //Output: The optimal move for AI player

    public PcMove MakeOptimalMove(char[][] board,char SEED, int game_state) {

        // Terminal states
        PcMove this_move = new PcMove();
        if (game_state == Main.DRAW) {
            this_move.score = 0;
            return this_move;
        } else if (game_state == Main.CROSS_WIN || game_state == Main.NOUGHT_WIN) {
            if (SEED == player_seed) {
                this_move.score = 1;
                return this_move;
            } else {
                this_move.score = -1;
                return this_move;
            }
        }

        PcMove best_move = new PcMove();
        //Max function
        if (SEED == Pc_Seed) {
            int best_score = Integer.MIN_VALUE;
            for (int x = 0; x < board.length; x++) {
                for (int y = 0; y < board[0].length; y++) {
                    if (board[x][y] == Main.EMPTY) {
                        PcMove move = new PcMove();
                        move.x = x;
                        move.y = y;
                        board[x][y] = SEED;
                        if (Main.IsDraw(board))
                            game_state = Main.DRAW;
                        else if (Main.PlayerWon(SEED, move.x, move.y))
                            game_state = SEED == Main.CROSS ? Main.CROSS_WIN : Main.NOUGHT_WIN;
                        move.score = MakeOptimalMove(board, player_seed, game_state).score;
                        if (move.score > best_score) {
                            best_move = move;
                            best_score = move.score;
                        }
                        board[x][y] = Main.EMPTY;
                        game_state = Main.PLAYING;
                    }
                }
            }
        }else{ //Min function
            int best_score = Integer.MAX_VALUE;
            for (int x = 0; x < board.length; x++) {
                for (int y = 0; y < board[0].length; y++) {
                    if (board[x][y] == Main.EMPTY) {
                        PcMove move = new PcMove();
                        move.x = x;
                        move.y = y;
                        board[x][y] = SEED;
                        if (Main.IsDraw(board))
                            game_state = Main.DRAW;
                        else if (Main.PlayerWon(SEED, move.x, move.y))
                            game_state = SEED == Main.CROSS ? Main.CROSS_WIN : Main.NOUGHT_WIN;
                        move.score = MakeOptimalMove(board, Pc_Seed, game_state).score;
                        if (move.score < best_score) {
                            best_move = move;
                            best_score = move.score;
                        }
                        board[x][y] = Main.EMPTY;
                        game_state = Main.PLAYING;
                    }
                }
            }
        }
        return best_move;
    }

}
