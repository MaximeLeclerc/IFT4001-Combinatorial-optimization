package ift4001.tp2;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class VarSelectorHeuristicTest {

    private static final String CIPHER_TEXT = "ABBCACBCAB";
    private static final List<Integer> BEST_INDEXES_WITH_ONE_LETTER_KEY = Arrays.asList(1, 3, 0);
    private static final List<Integer> BEST_INDEXES_WITH_TWO_LETTER_KEY = Arrays.asList(3, 0, 2, 1);

    @Test
    public void testSetup_WithOneLetterKey_ReturnExpectedResult() {
        VarSelectorHeuristic selector = new VarSelectorHeuristic(CIPHER_TEXT, 1);
        assertThat(selector.bestIndexes, is(equalTo(BEST_INDEXES_WITH_ONE_LETTER_KEY)));
    }

    @Test
    public void testSetup_WithTwoLetterKey_ReturnExpectedResult() {
        VarSelectorHeuristic selector = new VarSelectorHeuristic(CIPHER_TEXT, 2);
        assertThat(selector.bestIndexes, is(equalTo(BEST_INDEXES_WITH_TWO_LETTER_KEY)));
    }
}
