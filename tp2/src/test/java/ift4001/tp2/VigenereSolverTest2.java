package ift4001.tp2;

import org.junit.Test;

public class VigenereSolverTest2 {

	public static final String PLAIN_TEXT = "GENIUS WITHOUT EDUCATION IS LIKE SILVER IN THE MINE";
	
	@Test
	public void Instance2_WithKeyLengthOf1() {
		VigenereSolverTestUtil.testSolver(PLAIN_TEXT, "G", false);
	}
	
	@Test
	public void Instance2_WithKeyLengthOf2() {
		VigenereSolverTestUtil.testSolver(PLAIN_TEXT, "GE", false);
	}
	
	@Test
	public void Instance2_WithKeyLengthOf3() {
		VigenereSolverTestUtil.testSolver(PLAIN_TEXT, "GEX", false);
	}
	
	@Test
	public void Instance2_WithKeyLengthOf4() {
		VigenereSolverTestUtil.testSolver(PLAIN_TEXT, "GEXF", false);
	}
	
	@Test
	public void Instance2_WithKeyLengthOf1AndCustomHeuristic() {
		VigenereSolverTestUtil.testSolver(PLAIN_TEXT, "G", true);
	}
	
	@Test
	public void Instance2_WithKeyLengthOf2AndCustomHeuristic() {
		VigenereSolverTestUtil.testSolver(PLAIN_TEXT, "GE", true);
	}
	
	@Test
	public void Instance2_WithKeyLengthOf3AndCustomHeuristic() {
		VigenereSolverTestUtil.testSolver(PLAIN_TEXT, "GEX", true);
	}
	
	@Test
	public void Instance2_WithKeyLengthOf4AndCustomHeuristic() {
		VigenereSolverTestUtil.testSolver(PLAIN_TEXT, "GEXF", true);
	}
}
