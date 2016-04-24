package ift4001.tp2;

import org.junit.Test;

public class VigenereSolverLongTest {
	
	@Test
	public void Instance1_WithKeyLengthOf5() {
		VigenereSolverTestUtil.testSolver(VigenereSolverTest1.PLAIN_TEXT, "GEXFK", false);
	}
	
	@Test
	public void Instance2_WithKeyLengthOf5() {
		VigenereSolverTestUtil.testSolver(VigenereSolverTest2.PLAIN_TEXT, "GEXFK", false);
	}
	
	@Test
	public void Instance3_WithKeyLengthOf5() {
		VigenereSolverTestUtil.testSolver(VigenereSolverTest3.PLAIN_TEXT, "GEXFK", false);
	}
	
	@Test
	public void Instance4_WithKeyLengthOf5() {
		VigenereSolverTestUtil.testSolver(VigenereSolverTest4.PLAIN_TEXT, "GEXFK", false);
	}
	
	@Test
	public void Instance1_WithKeyLengthOf5AndCustomHeuristic() {
		VigenereSolverTestUtil.testSolver(VigenereSolverTest1.PLAIN_TEXT, "GEXFK", true);
	}
	
	@Test
	public void Instance2_WithKeyLengthOf5AndCustomHeuristic() {
		VigenereSolverTestUtil.testSolver(VigenereSolverTest2.PLAIN_TEXT, "GEXFK", true);
	}
	
	@Test
	public void Instance3_WithKeyLengthOf5AndCustomHeuristic() {
		VigenereSolverTestUtil.testSolver(VigenereSolverTest3.PLAIN_TEXT, "GEXFK", true);
	}
	
	@Test
	public void Instance4_WithKeyLengthOf5AndCustomHeuristic() {
		VigenereSolverTestUtil.testSolver(VigenereSolverTest4.PLAIN_TEXT, "GEXFK", true);
	}
}
