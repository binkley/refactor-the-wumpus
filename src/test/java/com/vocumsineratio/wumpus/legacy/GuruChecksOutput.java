/**
 * Copyright Vast 2018. All Rights Reserved.
 * <p/>
 * http://www.vast.com
 */
package com.vocumsineratio.wumpus.legacy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

/**
 * @author Danil Suits (danil@vast.com)
 */
public class GuruChecksOutput {

    static Stream<Arguments> goldenMasters() {
        return Stream.of(
                Arguments.of("0000.input.empty.txt", "0000.output.txt"),
                Arguments.of("0001.input.show-instructions.txt", "0001.output.txt"),
                Arguments.of("0002.input.hide-instructions.txt", "0002.output.txt"),
                Arguments.of("0003.input.invalid-instructions.txt", "0003.output.txt"),
                Arguments.of("0004.input.fall-in-pit.txt", "0004.output.txt"),
                Arguments.of("0005.input.same-setup-same-pit.txt", "0005.output.txt"),
                Arguments.of("0006.input.change-setup.txt", "0006.output.txt"),
                Arguments.of("0007.input.bats.txt", "0007.output.txt"),
                Arguments.of("0008.input.move-not-possible.txt", "0008.output.txt"),
                Arguments.of("0009.input.arrow-missed.txt", "0009.output.txt"),
                Arguments.of("0010.input.dead-after-five-arrows-miss.txt", "0010.output.txt"),
                Arguments.of("0011.input.crooked-arrow-bug.txt", "0011.output.txt"),
                Arguments.of("0012.input.crooked-arrow.txt", "0012.output.txt"),
                Arguments.of("0013.input.shoot-the-wumpus.txt", "0013.output.txt"),
                Arguments.of("0014.input.walk-to-wumpus.txt", "0014.output.txt"),
                Arguments.of("0015.input.wumpus-finds-hunter.txt", "0015.output.txt"),
                Arguments.of("0016.input.invalid-inputs.txt", "0016.output.txt"),
                Arguments.of("0017.input.bats-feed-wumpus.txt", "0017.output.txt")
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("goldenMasters")
    public void output_matches_golden_master(String inputSource, String expectedOutputSource) throws IOException {
        // https://xkcd.com/221/
        Assumptions.assumeTrue(FeatureFlag.xkcd221());

        // Note: on the initial map
        // Player in room #14
        // Wumpus in room #17
        // Pit in room #4
        // Pit in room #19
        // Bats in room #16
        // Bats in room #1

        InputStream inputAsStream = GuruChecksOutput.class.getResourceAsStream(inputSource);


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos);

        // https://stackoverflow.com/a/1119559/54734
        {
            final InputStream originalIn = System.in;
            final PrintStream originalOut = System.out;

            try {
                System.setIn(inputAsStream);
                System.setOut(out);

                String[] args = new String[0];
                BasicWumpus.main(args);
            } catch (NoSuchElementException ignore) {
            } finally {
                System.setOut(originalOut);
                System.setIn(originalIn);
            }

        }

        InputStream expectedAsStream = GuruChecksOutput.class.getResourceAsStream(expectedOutputSource);

        ByteArrayOutputStream expectedResult = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = expectedAsStream.read(buffer)) != -1) {
            expectedResult.write(buffer, 0, length);
        }

        Assertions.assertEquals(
                expectedResult.toString("UTF-8"),
                baos.toString("UTF-8"));


    }
}
