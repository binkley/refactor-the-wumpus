/**
 * Copyright Vast 2018. All Rights Reserved.
 * <p/>
 * http://www.vast.com
 */
package com.vocumsineratio.wumpus.legacy;

import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;

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

    int[] L = new int[6];
    int[] M = new int[6];
    int A;
    int LL;
    int F;
    int J;
    int K;
    int K1;
    String I;
    int O;

    public static void main(String[] args) {
        new BasicWumpus().run();
    }

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
            do {
                goto240 = false;
                // LOCATE L ARRAY ITEMS
                // 1-YOU,2-WUMPUS,3&4-PITS,5&6-BATS
                for (J = 1; J <= 6; ++J) {
                    L[J - 1] = FNA(0);
                    M[J - 1] = L[J - 1];
                }
                // CHECK FOR CROSSOVERS (IE L(1)=L(2),ETC)
                crossovers:
                for (J = 1; J <= 6; ++J) {
                    for (K = J; K <= 6; ++K) {
                        if (K == J) continue;
                        if (L[J - 1] == L[K - 1]) {
                            goto240 = true;
                            break crossovers;
                        }
                    }
                }
            } while (goto240);

            boolean goto360 = true;

            do {
                // SET# ARROWS
                A = 5;
                LL = L[0];

                // RUN THE GAME
                System.out.println("HUNT THE WUMPUS");

                do {
                    // HAZARD WARNINGS & LOCATION
                    gosub2000();
                    // MOVE OR SHOOT
                    gosub2500();

                    if (O == 1) {
                        // SHOOT
                        gosub3000();
                    }
                    if (O == 2) {
                        // MOVE
                        gosub4000();
                    }
                } while (0 == F);

                if (F > 0) {
                    // WIN
                    System.out.println("HEE HEE HEE - THE WUMPUS'LL GETCHA NEXT TIME!!");
                } else {
                    // LOSE
                    System.out.println("HA HA HA - YOU LOSE!");
                }
                for (J = 1; J <= 6; ++J) {
                    L[J - 1] = M[J - 1];
                }
                System.out.println("SAME SET-UP (Y-N)");
                System.out.flush();
                I = in.nextLine();
                if (!"Y".equals(I)) {
                    goto360 = false;
                }
            } while (goto360);
        }
    }

    void loop4020() {
        while (true) {
            System.out.println("WHERE TO");
            System.out.flush();

            try {
                LL = Integer.valueOf(in.nextLine());
            } catch (NumberFormatException e) {
                LL = 0;
            }

            if (LL >= 1 && LL <= 20) {
                for (K = 1; K <= 3; ++K) {
                    if (S[L[0] - 1][K - 1] == LL) {
                        return;
                    }
                }

                if (L[0] == LL) return;

                System.out.println("NOT POSSIBLE");
            }
        }
    }

    void gosub4000() {
        // MOVE ROUTINE
        F = 0;

        loop4020();

        while (true) {
            // CHECK FOR HAZARDS
            L[0] = LL;
            // WUMPUS
            if (LL == L[1]) {
                gosub3370();
                if (F != 0) {
                    return;
                }
            }
            // PIT
            if (LL == L[2] || LL == L[3]) {
                System.out.println("YYYIIIIEEEE . . . FELL IN PIT");
                F = -1;
                return;
            }

            if (LL != L[4] && LL != L[5]) {
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
        for (J = 2; J <= 6; ++J) {
            for (K = 1; K <= 3; ++K) {
                if (S[L[0] - 1][K - 1] != L[J - 1]) continue;
                if (J == 2) System.out.println("I SMELL A WUMPUS!");
                if (J == 3) System.out.println("I FEEL A DRAFT");
                if (J == 4) System.out.println("I FEEL A DRAFT");
                if (J == 5) System.out.println("BATS NEARBY!");
                if (J == 6) System.out.println("BATS NEARBY!");
            }
        }

        System.out.println("YOU ARE IN ROOM " + L[0]);
        System.out.println("TUNNELS LEAD TO " + S[L[0] - 1][0] + " " + S[L[0] - 1][1] + " " + S[L[0] - 1][2]);
        System.out.println();
        System.out.flush();
        return;
    }

    void gosub2500() {
        class Console {
            final PrintStream out;
            final Scanner in;

            Console(PrintStream out, Scanner in) {
                this.out = out;
                this.in = in;
            }

            void onMessage(String message) {
                out.println(message);
                out.flush();
            }

            String line () {
                return in.nextLine();
            }
        }

        Console console = new Console(System.out, this.in);
        
        // CHOOSE OPTION
        while (true) {
            console.onMessage("SHOOT OR MOVE (S-M)");

            String input = console.line();
            if ("S".equals(input)) {
                O = 1;
                return;
            }

            if ("M".equals(input)) {
                O = 2;
                return;
            }
        }
    }

    void gosub3000() {
        // ARROW ROUTINE
        F = 0;
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

        for (K = 1; K <= J9; ++K) {
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
        LL = L[0];
        for (K = 1; K <= J9; ++K) {
            boolean Z = false;
            for (K1 = 1; K1 <= 3; ++K1) {
                if (S[LL - 1][K1 - 1] == P[K - 1]) {
                    LL = P[K - 1];
                    Z = true;
                }
            }
            // NO TUNNEL FOR ARROW
            if (!Z) {
                LL = S[LL - 1][FNB(0) - 1];
            }

            // SEE IF ARROW IS AT L(1) OR L(2)
            if (L[1] == LL) {
                System.out.println("AHA! YOU GOT THE WUMPUS!");
                F = 1;
                return;
            }

            if (L[0] == LL) {
                System.out.println("OUCH! ARROW GOT YOU!");
                F = -1;
                return;
            }
        }
        System.out.println("MISSED");
        LL = L[0];
        // MOVE WUMPUS
        gosub3370();
        // AMMO CHECK
        A = A - 1;
        if (A <= 0) {
            F = -1;
        }
    }

    void gosub3370() {
        // MOVE WUMPUS ROUTINE
        K = FNC(0);
        if (4 != K) {
            L[1] = S[L[1]-1][K - 1];
        }
        if (LL == L[1]) {
            System.out.println("TSK TSK TSK- WUMPUS GOT YOU!");
            F = -1;
        }
        return;
    }
}
