package ift4001.tp2;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

public abstract class VigenereSolverTestUtil {
	
	private static void printTestInfo(VigenereSolver.Settings settings) {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		System.out.println();
		System.out.println("===================================");
		System.out.println(stackTrace[3].getMethodName());
		System.out.println("Looking for " + settings.getNumberOfResultsToReturn() + " results.");
		System.out.println("===================================");
	}

	public static void testSolver(String plainText, String key, boolean useCustomHeuristic) {
		Vigenere vigenere = new Vigenere(plainText, Language.ENGLISH);
		String cipherText = vigenere.encrypt(key);

		VigenereSolver.Settings settings = new VigenereSolver.Settings().setPossibleLanguages(Language.ENGLISH)
				.setPossibleKeyLengths(key.length()).setUseCustomHeuristic(useCustomHeuristic);
		VigenereSolver solver = new VigenereSolver(settings);

		VigenereSolverTestUtil.printTestInfo(settings);
		List<SolveResult> result = solver.solve(cipherText);

		assertThat(result.stream().anyMatch(r -> r.plainText.equals(plainText)), is(true));
	}
}
