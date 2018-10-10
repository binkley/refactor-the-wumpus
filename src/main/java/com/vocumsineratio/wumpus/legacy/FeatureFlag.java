/**
 * Copyright Vast 2018. All Rights Reserved.
 * <p/>
 * http://www.vast.com
 */
package com.vocumsineratio.wumpus.legacy;

import java.util.Map;
import java.util.Random;

/**
 * @author Danil Suits (danil@vast.com)
 */
class FeatureFlag {
    static Random random() {
        // https://xkcd.com/221/
        if (FeatureFlag.xkcd221()) {
            return new Random(4);

            // Note: on the initial map
            // Player in room #14
            // Wumpus in room #17
            // Pit in room #4
            // Pit in room #19
            // Bats in room #16
            // Bats in room #1
        }

        return new Random();
    }

    static boolean xkcd221() {
        return "com.vocumsineratio.random.XKCD221".equals(
                xkcd221(System.getenv())
        );
    }

    static String xkcd221(Map<String, String> env) {
        return env.get("WUMPUS_SEED_GENERATOR");
    }
}
