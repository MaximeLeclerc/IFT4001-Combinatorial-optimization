package ift4001.tp2;

import org.junit.Test;

public class VigenereSolverTest1 {

	public static final String PLAIN_TEXT = "FRIEN DSROM ANSCO UNTRY MENLE NDMEY OUREA RSICO METOB URYCA ESARN OTTOP RAISE HIMTH EEVIL THATM ENDOL IVESA FTERT HEMTH EGOOD ISOFT INTER REDWI THTHE IRBON ESSOL ETITB EWITH CAESA RTHEN OBLEB RUTUS HATHT OLDYO UCAES ARWAS AMBIT IOUSI FITWE RESOI TWASA GRIEV OUSFA ULTAN DGRIE VOUSL YHATH CAESA RANSW ERDIT HEREU NDERL EAVEO FBRUT USAND THERE STFOR BRUTU SISAN HONOU RABLE MANSO ARETH EYALL ALLHO NOURA BLEME NCOME ITOSP EAKIN CAESA RSFUN ERAL";
	
	@Test
	public void Instance1_WithKeyLengthOf1() {
		VigenereSolverTestUtil.testSolver(PLAIN_TEXT, "G", false);
	}
	
	@Test
	public void Instance1_WithKeyLengthOf2() {
		VigenereSolverTestUtil.testSolver(PLAIN_TEXT, "GE", false);
	}
	
	@Test
	public void Instance1_WithKeyLengthOf3() {
		VigenereSolverTestUtil.testSolver(PLAIN_TEXT, "GEX", false);
	}
	
	@Test
	public void Instance1_WithKeyLengthOf4() {
		VigenereSolverTestUtil.testSolver(PLAIN_TEXT, "GEXF", false);
	}
	
	@Test
	public void Instance1_WithKeyLengthOf1AndCustomHeuristic() {
		VigenereSolverTestUtil.testSolver(PLAIN_TEXT, "G", true);
	}
	
	@Test
	public void Instance1_WithKeyLengthOf2AndCustomHeuristic() {
		VigenereSolverTestUtil.testSolver(PLAIN_TEXT, "GE", true);
	}
	
	@Test
	public void Instance1_WithKeyLengthOf3AndCustomHeuristic() {
		VigenereSolverTestUtil.testSolver(PLAIN_TEXT, "GEX", true);
	}
	
	@Test
	public void Instance1_WithKeyLengthOf4AndCustomHeuristic() {
		VigenereSolverTestUtil.testSolver(PLAIN_TEXT, "GEXF", true);
	}
}
