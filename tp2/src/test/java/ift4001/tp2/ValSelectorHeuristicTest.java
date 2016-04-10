package ift4001.tp2;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ValSelectorHeuristicTest {

    private static final List<Integer> ENGLISH_BEST_LETTERS_INDEXES = Arrays.asList(4, 19, 0, 14, 8, 13, 18, 7, 17, 3,
            11, 2, 20, 12, 22, 5, 6, 24, 15, 1, 21, 10, 9, 23, 16, 25);

    @Test
    public void testSetup_WithEnglish_ReturnIndexesInExpectedOrder() {
        ValSelectorHeuristic selector = new ValSelectorHeuristic(Frequencies.ENGLISH);
        assertThat(selector.bestLettersIndexes, is(equalTo(ENGLISH_BEST_LETTERS_INDEXES)));
    }

}
