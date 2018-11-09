package com.zzh;
/**
 This class represent the object AI player
 It contains the function of minimax and a heuristic function
 The technique of alpha-beta pruning and depth-limited are used in the minimax function.
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

    public PcMove MakeOptimalMove(char[][] board,char SEED, int game_state,int alpha, int beta,int depth) {

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

        //Depth-limited
        if (depth <= 11) {
            PcMove best_move = new PcMove();
            //Max function
            if (SEED == Pc_Seed) {
                int best_score = Integer.MIN_VALUE;
                int flag = 0;
                for (int x = 0; x < board.length && flag != 1; x++) {
                    for (int y = 0; y < board[0].length; y++) {
                        int a = 3*(x/3)+y/3+1;
                        int b = 3 * (x - 3 * (x / 3)) + (y - 3 * (y / 3)) + 1;
                        if(Main.restrict&&a!=Main.next_board)
                            continue;
                        if (board[x][y] == Main.EMPTY) {
                            PcMove move = new PcMove();
                            move.x = x;
                            move.y = y;
                            boolean pre_restrict = Main.restrict;
                            int pre_next_board = Main.next_board;
                            //update
                            if(!Main.restrict)
                                Main.restrict = true;
                            Main.next_board = b;
                            board[x][y] = SEED;
                            Main.filled[a]++;
                            if(Main.filled[b]==9)
                                Main.restrict = false;
                            //Update game state
                            if (Main.IsDraw(board))
                                game_state = Main.DRAW;
                            else if (Main.PlayerWon(SEED, move.x, move.y))
                                game_state = SEED == Main.CROSS ? Main.CROSS_WIN : Main.NOUGHT_WIN;

                            move.score = MakeOptimalMove(board, player_seed, game_state, alpha, beta,depth+1).score;

                            //revoke update
                            Main.filled[a]--;
                            board[x][y] = Main.EMPTY;
                            game_state = Main.PLAYING;
                            Main.restrict = pre_restrict;
                            Main.next_board = pre_next_board;
                            //update optimal move
                            if (move.score > best_score) {
                                best_move = move;
                                best_score = move.score;
                            }
                            //alpha-beta pruning
                            alpha = Math.max(best_move.score, alpha);
                            if (beta <= alpha) {
                                flag = 1;
                                break;

                            }
                        }
                    }
                }
            } else { //Min function
                //if(depth==0) System.err.println("Enter min function");
                int best_score = Integer.MAX_VALUE;
                int flag = 0;
                for (int x = 0; x < board.length && flag != 1; x++) {
                    for (int y = 0; y < board[0].length; y++) {
                        int a = 3*(x/3)+y/3+1;
                        int b = 3 * (x - 3 * (x / 3)) + (y - 3 * (y / 3)) + 1;
                        if(Main.restrict&&a!=Main.next_board)
                            continue;
                        if (board[x][y] == Main.EMPTY) {
                            PcMove move = new PcMove();
                            move.x = x;
                            move.y = y;
                            boolean pre_restrict = Main.restrict;
                            int pre_next_board = Main.next_board;
                            //update
                            if(!Main.restrict)
                                Main.restrict = true;
                            Main.next_board = b;
                            Main.filled[a]++;
                            if(Main.filled[b]==9)
                                Main.restrict = false;
                            board[x][y] = SEED;
                            //Update game state
                            if (Main.IsDraw(board))
                                game_state = Main.DRAW;
                            else if (Main.PlayerWon(SEED, move.x, move.y))
                                game_state = SEED == Main.CROSS ? Main.CROSS_WIN : Main.NOUGHT_WIN;

                            move.score = MakeOptimalMove(board, Pc_Seed, game_state, alpha, beta,depth+1).score;
                            //revoke update
                            Main.filled[a]--;
                            board[x][y] = Main.EMPTY;
                            game_state = Main.PLAYING;
                            Main.restrict = pre_restrict;
                            Main.next_board = pre_next_board;
                            //update optimal move
                            if (move.score < best_score) {
                                best_move = move;
                                best_score = move.score;
                            }
                            //alpha-beta pruning
                            beta = Math.min(best_move.score, beta);
                            if (beta <= alpha) {
                                flag = 1;
                                break;
                            }
                        }
                    }
                }
            }
            return best_move;
        }else{
            this_move.score = 0;
            return this_move;
        }
    }

}
