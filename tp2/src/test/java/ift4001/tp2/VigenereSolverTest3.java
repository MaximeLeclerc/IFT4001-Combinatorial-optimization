package ift4001.tp2;

import org.junit.Test;

public class VigenereSolverTest3 {

	public static final String PLAIN_TEXT = "HEREUPON LEGRAND AROSE WITH A GRAVE AND STATELY AIR AND BROUGHT ME THE BEETLE FROM A GLASS CASE IN WHICH IT WAS ENCLOSED IT WAS A BEAUTIFUL SCARABAEUS AND AT THAT TIME UNKNOWN TO NATURALISTS OF COURSE A GREAT PRIZE IN A SCIENTIFIC POINT OF VIEW THERE WERE TWO ROUND BLACK SPOTS NEAR ONE EXTREMITY OF THE BACK AND A LONG ONE NEAR THE OTHER THE SCALES WERE EXCEEDINGLY HARD AND GLOSSY WITH ALL THE APPEARANCE OF BURNISHED GOLD THE WEIGHT OF THE INSECT WAS VERY REMARKABLE AND TAKING ALL THINGS INTO CONSIDERATION I COULD HARDLY BLAME JUPITER FOR HIS OPINION RESPECTING IT";
	
	@Test
	public void Instance3_WithKeyLengthOf1() {
		VigenereSolverTestUtil.testSolver(PLAIN_TEXT, "G", false);
	}
	
	@Test
	public void Instance3_WithKeyLengthOf2() {
		VigenereSolverTestUtil.testSolver(PLAIN_TEXT, "GE", false);
	}
	
	@Test
	public void Instance3_WithKeyLengthOf3() {
		VigenereSolverTestUtil.testSolver(PLAIN_TEXT, "GEX", false);
	}
	
	@Test
	public void Instance3_WithKeyLengthOf4() {
		VigenereSolverTestUtil.testSolver(PLAIN_TEXT, "GEXF", false);
	}
	
	@Test
	public void Instance3_WithKeyLengthOf1AndCustomHeuristic() {
		VigenereSolverTestUtil.testSolver(PLAIN_TEXT, "G", true);
	}
	
	@Test
	public void Instance3_WithKeyLengthOf2AndCustomHeuristic() {
		VigenereSolverTestUtil.testSolver(PLAIN_TEXT, "GE", true);
	}
	
	@Test
	public void Instance3_WithKeyLengthOf3AndCustomHeuristic() {
		VigenereSolverTestUtil.testSolver(PLAIN_TEXT, "GEX", true);
	}
	
	@Test
	public void Instance3_WithKeyLengthOf4AndCustomHeuristic() {
		VigenereSolverTestUtil.testSolver(PLAIN_TEXT, "GEXF", true);
	}
}
