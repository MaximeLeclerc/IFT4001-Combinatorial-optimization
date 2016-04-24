package ift4001.tp2;

import org.junit.Test;

public class VigenereSolverTest4 {

	public static final String PLAIN_TEXT = "ZJXQKB";
	
	@Test
	public void Instance4_WithKeyLengthOf1() {
		VigenereSolverTestUtil.testSolver(PLAIN_TEXT, "G", false);
	}
	
	@Test
	public void Instance4_WithKeyLengthOf2() {
		VigenereSolverTestUtil.testSolver(PLAIN_TEXT, "GE", false);
	}
	
	@Test
	public void Instance4_WithKeyLengthOf3() {
		VigenereSolverTestUtil.testSolver(PLAIN_TEXT, "GEX", false);
	}
	
	@Test
	public void Instance4_WithKeyLengthOf4() {
		VigenereSolverTestUtil.testSolver(PLAIN_TEXT, "GEXF", false);
	}
	
	@Test
	public void Instance4_WithKeyLengthOf1AndCustomHeuristic() {
		VigenereSolverTestUtil.testSolver(PLAIN_TEXT, "G", true);
	}
	
	@Test
	public void Instance4_WithKeyLengthOf2AndCustomHeuristic() {
		VigenereSolverTestUtil.testSolver(PLAIN_TEXT, "GE", true);
	}
	
	@Test
	public void Instance4_WithKeyLengthOf3AndCustomHeuristic() {
		VigenereSolverTestUtil.testSolver(PLAIN_TEXT, "GEX", true);
	}
	
	@Test
	public void Instance4_WithKeyLengthOf4AndCustomHeuristic() {
		VigenereSolverTestUtil.testSolver(PLAIN_TEXT, "GEXF", true);
	}
}
