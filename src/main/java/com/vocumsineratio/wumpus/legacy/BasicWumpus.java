/**
 * Copyright Vast 2018. All Rights Reserved.
 * <p/>
 * http://www.vast.com
 */
package com.vocumsineratio.wumpus.legacy;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.function.IntSupplier;

/**
 * @author Danil Suits (danil@vast.com)
 */
public class BasicWumpus {
    Scanner in = new Scanner(System.in);
    Random random = FeatureFlag.random();

    int S[][] =
            {{2, 5, 8}, {1, 3, 10}, {2, 4, 12}, {3, 5, 14}, {1, 4, 6}
                    , {5, 7, 15}, {6, 8, 17}, {1, 7, 9}, {8, 10, 18}, {2, 9, 11}
                    , {10, 12, 19}, {3, 11, 13}, {12, 14, 20}, {4, 13, 15}, {6, 14, 16}
                    , {15, 17, 20}, {7, 16, 18}, {9, 17, 19}, {11, 18, 20}, {13, 16, 19}
            };

    public static void main(String[] args) {
        new BasicWumpus().run();
    }

    class Game {
        int A = 5;
        int F = 0;
        int L[];

        void onStart(int[] M) {
            L = Arrays.copyOf(M, M.length);
        }

        void onMiss() {
            A = A - 1;
        }

        void onPit() {
            F = -1;
        }

        void onGotByWumpus() {
            F = -1;
        }

        void onShootWumpus() {
            F = 1;
        }

        void onShootHunter() {
            F = -1;
        }

        void onNoMoreArrows() {
            F = -1;
        }

        boolean outOfArrows() {
            return A < 1;
        }

        boolean hunting() {
            return 0 == F;
        }

        boolean won() {
            return F > 0;
        }

        int wumpusMove(int K, int[][] S) {
            return S[L[1] - 1][K - 1];
        }

        boolean arrowFoundHunter(int LL) {
            return L[0] == LL;
        }

        boolean arrowFoundWumpus(int LL) {
            return L[1] == LL;
        }

        boolean wumpusFoundHunter() {
            return L[0] == L[1];
        }

        void onWumpusToRoom(int room) {
            L[1] = room;
        }

        void onHunterToRoom(int room) {
            L[0] = room;
        }

        int hunterAt() {
            return L[0];
        }

        boolean hunterIsAt(int LL) {
            return L[0] == LL;
        }

        int[] hunterTunnels(int[][] S) {
            return S[L[0] - 1];
        }

        boolean notOccupiedBy(int J, int room) {
            return room != L[J - 1];
        }

        boolean hunterFoundBats() {
            return L[0] == L[4] || L[0] == L[5];
        }

        boolean hunterFoundPit() {
            return L[0] == L[2] || L[0] == L[3];
        }

        boolean hunterFoundWumpus() {
            return L[0] == L[1];
        }
    }

    Game game;

    void run() {
        // HUNT THE WUMPUS
        //  BY GREGORY YOB
        System.out.println("INSTRUCTIONS (Y-N)");
        System.out.flush();
        if (!"N".equals(in.nextLine())) {
            gosub1000();
        }
        // ANNOUNCE WUMPUSII FOR ALL AFICIONADOS ... ADDED BY DAVE
        System.out.println();
        System.out.println("     ATTENTION ALL WUMPUS LOVERS!!!");
        System.out.println("     THERE ARE NOW TWO ADDITIONS TO THE WUMPUS FAMILY");
        System.out.println(" OF PROGRAMS.");
        System.out.println();
        System.out.println("     WUMP2:  SOME DIFFERENT CAVE ARRANGEMENTS");
        System.out.println("     WUMP3:  DIFFERENT HAZARDS");
        System.out.println();

        while (true) {
            boolean goto240 = false;
            int[] M = new int[6];

            do {
                goto240 = false;
                // LOCATE L ARRAY ITEMS
                // 1-YOU,2-WUMPUS,3&4-PITS,5&6-BATS
                for (int J = 1; J <= 6; ++J) {
                    M[J - 1] = FNA(0);

                }
                // CHECK FOR CROSSOVERS (IE L(1)=L(2),ETC)
                crossovers:
                for (int J = 1; J <= 6; ++J) {
                    for (int K = J; K <= 6; ++K) {
                        if (K == J) continue;
                        if (M[J - 1] == M[K - 1]) {
                            goto240 = true;
                            break crossovers;
                        }
                    }
                }
            } while (goto240);

            boolean goto360 = true;

            do {
                this.game = new Game();
                // SET# ARROWS
                // TODO:
                game.onStart(M);

                // RUN THE GAME
                System.out.println("HUNT THE WUMPUS");

                do {
                    // HAZARD WARNINGS & LOCATION
                    gosub2000();
                    // MOVE OR SHOOT
                    int O = gosub2500();

                    if (O == 1) {
                        // SHOOT
                        gosub3000();
                    }
                    if (O == 2) {
                        // MOVE
                        gosub4000();
                    }
                } while (game.hunting());

                if (game.won()) {
                    // WIN
                    System.out.println("HEE HEE HEE - THE WUMPUS'LL GETCHA NEXT TIME!!");
                } else {
                    // LOSE
                    System.out.println("HA HA HA - YOU LOSE!");
                }


                System.out.println("SAME SET-UP (Y-N)");
                System.out.flush();
                String I = in.nextLine();
                if (!"Y".equals(I)) {
                    goto360 = false;
                }
            } while (goto360);
        }
    }

    int loop4020() {
        while (true) {
            System.out.println("WHERE TO");
            System.out.flush();

            int LL;
            try {
                LL = Integer.valueOf(in.nextLine());
            } catch (NumberFormatException e) {
                LL = 0;
            }

            if (LL >= 1 && LL <= 20) {
                int[] tunnels = game.hunterTunnels(S);
                for (int room : tunnels) {
                    if (room == LL) {
                        return LL;
                    }
                }

                if (game.hunterIsAt(LL)) return LL;

                System.out.println("NOT POSSIBLE");
            }
        }
    }

    void gosub4000() {
        // MOVE ROUTINE
        int LL = loop4020();

        while (true) {
            // CHECK FOR HAZARDS
            game.onHunterToRoom(LL);
            // WUMPUS
            if (game.hunterFoundWumpus()) {
                // MOVE WUMPUS ROUTINE
                int K = FNC(0);
                Runnable WumpusGotYou = () ->
                        System.out.println("TSK TSK TSK- WUMPUS GOT YOU!");

                onWumpusMove(K, this.S, WumpusGotYou);
                if (!game.hunting()) {
                    return;
                }
            }
            // PIT
            if (game.hunterFoundPit()) {
                System.out.println("YYYIIIIEEEE . . . FELL IN PIT");
                game.onPit();
                return;
            }

            if (!game.hunterFoundBats()) {
                return;
            }
            System.out.println("ZAP--SUPER BAT SNATCH! ELSEWHEREVILLE FOR YOU!");
            LL = FNA(0);
        }
    }

    int FNA(int x) {
        return (int) (20 * random.nextDouble()) + 1;
    }

    int FNB(int x) {
        return (int) (3 * random.nextDouble()) + 1;
    }

    int FNC(int x) {
        return (int) (4 * random.nextDouble()) + 1;
    }

    void gosub1000() {
        System.out.println("WELCOME TO 'HUNT THE WUMPUS'");
        System.out.println("  THE WUMPUS LIVES IN A CAVE OF 20 ROOMS. EACH ROOM");
        System.out.println("HAS 3 TUNNELS LEADING TO OTHER ROOMS. (LOOK AT A");
        System.out.println("DODECAHEDRON TO SEE HOW THIS WORKS-IF YOU DON'T KNOW");
        System.out.println("WHAT A DODECAHEDRON IS, ASK SOMEONE)");
        System.out.println();
        System.out.println("     HAZARDS:");
        System.out.println(" BOTTOMLESS PITS - TWO ROOMS HAVE BOTTOMLESS PITS IN THEM");
        System.out.println("     IF YOU GO THERE, YOU FALL INTO THE PIT (& LOSE!)");
        System.out.println(" SUPER BATS - TWO OTHER ROOMS HAVE SUPER BATS. IF YOU");
        System.out.println("     GO THERE, A BAT GRABS YOU AND TAKES YOU TO SOME OTHER");
        System.out.println("     ROOM AT RANDOM. (WHICH MIGHT BE TROUBLESOME)");
        System.out.println();
        System.out.println("     WUMPUS:");
        System.out.println(" THE WUMPUS IS NOT BOTHERED BY THE HAZARDS (HE HAS SUCKER");
        System.out.println(" FEET AND IS TOO BIG FOR A BAT TO LIFT).  USUALLY");
        System.out.println(" HE IS ASLEEP. TWO THINGS WAKE HIM UP: YOUR ENTERING");
        System.out.println(" HIS ROOM OR YOUR SHOOTING AN ARROW.");
        System.out.println("     IF THE WUMPUS WAKES, HE MOVES (P=.75) ONE ROOM");
        System.out.println(" OR STAYS STILL (P=.25). AFTER THAT, IF HE IS WHERE YOU");
        System.out.println(" ARE, HE EATS YOU UP (& YOU LOSE!)");
        System.out.println();
        System.out.println("     YOU:");
        System.out.println(" EACH TURN YOU MAY MOVE OR SHOOT A CROOKED ARROW");
        System.out.println("   MOVING: YOU CAN GO ONE ROOM (THRU ONE TUNNEL)");
        System.out.println("   ARROWS: YOU HAVE 5 ARROWS. YOU LOSE WHEN YOU RUN OUT.");
        System.out.println("   EACH ARROW CAN GO FROM 1 TO 5 ROOMS. YOU AIM BY TELLING");
        System.out.println("   THE COMPUTER THE ROOM#S YOU WANT THE ARROW TO GO TO.");
        System.out.println("   IF THE ARROW CAN'T GO THAT WAY (IE NO TUNNEL) IT MOVES");
        System.out.println("   AT RAMDOM TO THE NEXT ROOM.");
        System.out.println("     IF THE ARROW HITS THE WUMPUS, YOU WIN.");
        System.out.println("     IF THE ARROW HITS YOU, YOU LOSE.");
        System.out.println();
        System.out.println("    WARNINGS:");
        System.out.println("     WHEN YOU ARE ONE ROOM AWAY FROM WUMPUS OR HAZARD,");
        System.out.println("    THE COMPUTER SAYS:");
        System.out.println(" WUMPUS-  'I SMELL A WUMPUS'");
        System.out.println(" BAT   -  'BATS NEARBY'");
        System.out.println(" PIT   -  'I FEEL A DRAFT'");
        System.out.println("");
        System.out.flush();
        return;
    }

    void gosub2000() {
        // LOCATION & HAZARD WARNINGS
        for (int J = 2; J <= 6; ++J) {
            int[] tunnels = game.hunterTunnels(S);
            for (int room : tunnels) {
                if (game.notOccupiedBy(J, room)) continue;
                if (J == 2) System.out.println("I SMELL A WUMPUS!");
                if (J == 3) System.out.println("I FEEL A DRAFT");
                if (J == 4) System.out.println("I FEEL A DRAFT");
                if (J == 5) System.out.println("BATS NEARBY!");
                if (J == 6) System.out.println("BATS NEARBY!");
            }
        }

        System.out.println("YOU ARE IN ROOM " + game.hunterAt());
        int[] tunnels = game.hunterTunnels(S);
        System.out.println("TUNNELS LEAD TO " + tunnels[0] + " " + tunnels[1] + " " + tunnels[2]);
        System.out.println();
        System.out.flush();
        return;
    }

    int gosub2500() {
        int O;
        // CHOOSE OPTION
        while (true) {
            System.out.println("SHOOT OR MOVE (S-M)");
            System.out.flush();

            String I = in.nextLine();
            if ("S".equals(I)) {
                O = 1;
                return O;
            }

            if ("M".equals(I)) {
                O = 2;
                return O;
            }
        }
    }

    void gosub3000() {
        // ARROW ROUTINE

        // PATH OF ARROW
        int[] P = new int[5];
        int J9 = 0;

        while (J9 < 1 || J9 > 5) {
            System.out.println("NO. OF ROOMS(1-5)");
            System.out.flush();
            try {
                J9 = Integer.valueOf(in.nextLine());
            } catch (NumberFormatException e) {
                J9 = 0;
            }
        }

        for (int K = 1; K <= J9; ++K) {
            boolean goto3080;
            do {
                goto3080 = false;
                System.out.println("ROOM #");
                System.out.flush();
                try {
                    P[K - 1] = Integer.valueOf(in.nextLine());
                } catch (NumberFormatException e) {
                    P[K - 1] = 0;
                }
                if (K > 2) {
                    if (P[K - 1] == P[K - 3]) {
                        System.out.println("ARROWS AREN'T THAT CROOKED - TRY ANOTHER ROOM");
                        goto3080 = true;
                    }
                }
            } while (goto3080);
        }
        // SHOOT ARROW

        // InputEffects
        IntSupplier wumpusRoom = () -> FNC(0);
        IntSupplier randomTunnel = () -> FNB(0);

        // OutputEffects
        Runnable ArrowGotYou = () ->
                System.out.println("OUCH! ARROW GOT YOU!");
        Runnable ArrowMissed = () ->
                System.out.println("MISSED");
        Runnable WumpusGotYou = () ->
                System.out.println("TSK TSK TSK- WUMPUS GOT YOU!");
        Runnable GotTheWumpus = () ->
                System.out.println("AHA! YOU GOT THE WUMPUS!");

        int LL = game.hunterAt();
        for (int K = 1; K <= J9; ++K) {
            boolean Z = false;
            int[] tunnels = S[LL - 1];
            for (int room : tunnels) {
                if (room == P[K - 1]) {
                    LL = P[K - 1];
                    Z = true;
                }
            }
            // NO TUNNEL FOR ARROW
            if (!Z) {
                LL = S[LL - 1][randomTunnel.getAsInt() - 1];
            }

            // SEE IF ARROW IS AT L(1) OR L(2)
            if (game.arrowFoundWumpus(LL)) {
                GotTheWumpus.run();
                game.onShootWumpus();
                return;
            }

            if (game.arrowFoundHunter(LL)) {
                ArrowGotYou.run();
                game.onShootHunter();
                return;
            }
        }

        ArrowMissed.run();

        // MOVE WUMPUS
        // MOVE WUMPUS ROUTINE

        int K = wumpusRoom.getAsInt();


        onWumpusMove(K, this.S, WumpusGotYou);
        // AMMO CHECK
        game.onMiss();
        if (game.outOfArrows()) {
            game.onNoMoreArrows();
        }
    }

    void onWumpusMove(int wumpusMove, int[][] S, Runnable wumpusGotYou) {
        if (4 != wumpusMove) {
            int room = game.wumpusMove(wumpusMove, S);
            game.onWumpusToRoom(room);
        }
        if (game.wumpusFoundHunter()) {
            wumpusGotYou.run();
            game.onGotByWumpus();
        }
    }

}
