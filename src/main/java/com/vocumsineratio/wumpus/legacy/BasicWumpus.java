/**
 * Copyright Vast 2018. All Rights Reserved.
 * <p/>
 * http://www.vast.com
 */
package com.vocumsineratio.wumpus.legacy;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * @author Danil Suits (danil@vast.com)
 */
public class BasicWumpus {
    Scanner in = new Scanner(System.in);
    Console console = new Console(System.out, this.in);
    EnglishDictionary dict = new EnglishDictionary();

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

    public static void main(String[] args) {
        new BasicWumpus().run();
    }

    void run() {
        // HUNT THE WUMPUS
        //  BY GREGORY YOB
        class InstructionsProtocol {
            final String no;
            String input;

            InstructionsProtocol(String no) {
                this.no = no;
            }

            void onInput(String input) {
                this.input = input;
            }

            boolean instructions () {
                return ! no.equals(input);
            }
        }

        InstructionsProtocol instructionsProtocol = new InstructionsProtocol(dict.no());
        do {
            console.onMessage(dict.instructionsPrompt());
            instructionsProtocol.onInput(console.line());
        } while(false);

        if (instructionsProtocol.instructions()) {
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

                    ActionEncoding actions = new ActionEncoding();

                    int action = action(actions);

                    if (action == actions.shoot()) {
                        // SHOOT
                        int[] P = arrowPath();
                        onShoot(P);
                    }
                    if (action == actions.move()) {
                        // MOVE
                        int room = room();
                        onMove(room);
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

                class SameSetupProtocol {
                    String input;

                    void onInput(String input) {
                        this.input = input;
                    }

                    boolean sameSetup () {
                        return "Y".equals(input);
                    }
                }

                SameSetupProtocol sameSetupProtocol = new SameSetupProtocol();

                do {
                    console.onMessage("SAME SET-UP (Y-N)");
                    sameSetupProtocol.onInput(console.line());
                } while(false);

                goto360 = sameSetupProtocol.sameSetup();

            } while (goto360);
        }
    }

    int room() {

        class PromptProtocol {
            List<String> prompt = new ArrayList<>();
            final String movePrompt;
            final String moveNotPossible;

            PromptProtocol(String movePrompt, String moveNotPossible) {
                this.movePrompt = movePrompt;
                this.moveNotPossible = moveNotPossible;
            }

            Iterable<String> prompt() {
                prompt.add(this.movePrompt);
                return prompt;
            }

            void onInput() {
                prompt.clear();
            }

            void onMoveNotPossible() {
                prompt.add(moveNotPossible);
            }
        }
        
        class MoveProtocol {
            int room;
            boolean running = true;
            final Runnable onMoveNotPossible;

            MoveProtocol(Runnable onMoveNotPossible) {
                this.onMoveNotPossible = onMoveNotPossible;
            }

            int room() {
                return room;
            }

            boolean running() {
                return running;
            }

            void onInput(String input) {
                try {
                    int room = Integer.valueOf(input);
                    if (room >= 1 && room <= 20) {
                        for (K = 1; K <= 3; ++K) {
                            if (S[L[0] - 1][K - 1] == room) {
                                onRoom(room);
                                return;
                            }
                        }

                        if (L[0] == room) {
                            onRoom(room);
                            return;
                        }

                        onMoveNotPossible.run();
                    }
                } catch (NumberFormatException e) {
                    // IGNORE
                }
            }

            void onRoom(int room) {
                this.room = room;
                this.running = false;
            }
        }

        class MoveAdapter {
            final PromptProtocol promptProtocol;
            final MoveProtocol protocol;

            MoveAdapter(PromptProtocol promptProtocol, MoveProtocol protocol) {
                this.promptProtocol = promptProtocol;
                this.protocol = protocol;
            }

            Iterable<String> prompt() {
                return promptProtocol.prompt();
            }

            int room() {
                return protocol.room();
            }

            boolean running() {
                return protocol.running();
            }

            void onInput(String input) {
                promptProtocol.onInput();
                protocol.onInput(input);
            }
        }

        PromptProtocol promptProtocol = new PromptProtocol(dict.movePrompt(), dict.moveNotPossible());
        MoveProtocol moveProtocol = new MoveProtocol(promptProtocol::onMoveNotPossible);

        MoveAdapter moveAdapter = new MoveAdapter(promptProtocol, moveProtocol);

        do {
            moveAdapter.prompt().forEach(console::onMessage);
            moveAdapter.onInput(console.line());
        } while (moveAdapter.running());

        return moveAdapter.room();

    }

    private void onMove(int room) {
        // MOVE ROUTINE
        F = 0;

        LL = room;

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

    int action(ActionEncoding actions) {
        class ActionProtocol {
            final EnglishDictionary dict;
            final ActionEncoding actions;
            int action;
            boolean isRunning = true;

            ActionProtocol(EnglishDictionary dict, ActionEncoding actions) {
                this.dict = dict;
                this.actions = actions;
            }

            String prompt() {
                return dict.actionPrompt();
            }

            void onInput(String input) {
                if (dict.shoot(input)) {
                    onShoot();
                }

                if (dict.move(input)) {
                    onMove();
                }
            }

            void onShoot() {
                onAction(actions.shoot());
            }

            void onMove() {
                onAction(actions.move());
            }

            void onAction(int action) {
                this.action = action;
                this.isRunning = false;
            }

            int action() {
                return this.action;
            }

            boolean isRunning() {
                return this.isRunning;
            }
        }

        ActionProtocol protocol = new ActionProtocol(dict, actions);

        // CHOOSE OPTION
        do {
            console.onMessage(protocol.prompt());
            protocol.onInput(console.line());
        } while (protocol.isRunning());

        return protocol.action();
    }

    private int[] arrowPath() {
        class ArrowDistanceProtocol {
            int distance = 0;

            boolean running() {
                return (distance < 1 || distance > 5);
            }

            void onInput(String input) {
                try {
                    distance = Integer.valueOf(input);
                } catch (NumberFormatException e) {
                    distance = 0;
                }
            }

            int distance () {
                return distance;
            }
        }

        class PromptProtocol {
            final String roomPrompt;
            final String crookedWarning;

            List<String> prompt = new ArrayList<>();

            PromptProtocol(String roomPrompt, String crookedWarning) {
                this.roomPrompt = roomPrompt;
                this.crookedWarning = crookedWarning;
            }

            Iterable<String> prompt() {
                prompt.add(roomPrompt);
                return prompt;
            }

            void onInput() {
                prompt.clear();
            }

            void onCrooked() {
                prompt.add(crookedWarning);
            }
        }

        PromptProtocol promptProtocol = new PromptProtocol(
                "ROOM #",
                "ARROWS AREN'T THAT CROOKED - TRY ANOTHER ROOM"
        );

        class ArrowPathProtocol {
            final int [] P;
            final Runnable onCrooked;
            int K2 = 0;

            ArrowPathProtocol(int[] p, Runnable onCrooked) {
                P = p;
                this.onCrooked = onCrooked;
            }

            boolean running() {
                return K2 < P.length;
            }

            void onRoom() {
                this.K2++;
            }

            void onInput(String input) {
                try {
                    P[K2] = Integer.valueOf(input);
                    if (K2 > 1) {
                        if (P[K2] == P[K2 - 2]) {
                            onCrooked.run();
                            return;
                        }
                    }
                } catch (NumberFormatException ignored) {
                    P[K2] = 0;
                }
                onRoom();
            }
        }

        ArrowDistanceProtocol protocol = new ArrowDistanceProtocol();

        do {
            console.onMessage(dict.arrowDistancePrompt());
            protocol.onInput(console.line());
        } while (protocol.running());

        int[] P = new int[protocol.distance()];

        Runnable onCrooked = () -> promptProtocol.onCrooked();

        ArrowPathProtocol arrowPathProtocol = new ArrowPathProtocol(P, onCrooked);

        class ArrowPathAdapter {
            final PromptProtocol promptProtocol;
            final ArrowPathProtocol arrowPathProtocol;

            ArrowPathAdapter(PromptProtocol promptProtocol, ArrowPathProtocol arrowPathProtocol) {
                this.promptProtocol = promptProtocol;
                this.arrowPathProtocol = arrowPathProtocol;
            }

            void onInput(String input) {
                promptProtocol.onInput();
                arrowPathProtocol.onInput(input);
            }
        }

        ArrowPathAdapter arrowPathAdapter = new ArrowPathAdapter(promptProtocol, arrowPathProtocol);

        do {
            promptProtocol.prompt().forEach(console::onMessage);
            arrowPathAdapter.onInput(console.line());
        } while( arrowPathProtocol.running());

        return P;
    }

    private void onShoot(int[] p) {
        F = 0;
        LL = L[0];
        for (int room : p) {
            boolean Z = false;
            int [] tunnels = S[LL - 1];
            for (int tunnel : tunnels) {
                if (tunnel == room) {
                    LL = room;
                    Z = true;
                }
            }
            // NO TUNNEL FOR ARROW
            if (!Z) {
                LL = tunnels[FNB(0) - 1];
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
            L[1] = S[L[1] - 1][K - 1];
        }
        if (LL == L[1]) {
            System.out.println("TSK TSK TSK- WUMPUS GOT YOU!");
            F = -1;
        }
        return;
    }

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

        String line() {
            return in.nextLine();
        }
    }

    class ActionEncoding {
        int shoot() {
            return 1;
        }

        int move() {
            return 2;
        }
    }

    class EnglishDictionary {
        String actionPrompt() {
            return "SHOOT OR MOVE (S-M)";
        }

        boolean shoot(String input) {
            return "S".equals(input);
        }

        boolean move(String input) {
            return "M".equals(input);
        }

        String movePrompt() {
            return "WHERE TO";
        }

        String moveNotPossible() {
            return "NOT POSSIBLE";
        }

        String arrowDistancePrompt () { return "NO. OF ROOMS(1-5)" ; }

        String instructionsPrompt() {
            return "INSTRUCTIONS (Y-N)";
        }

        String no() { return "N"; }

    }

}
