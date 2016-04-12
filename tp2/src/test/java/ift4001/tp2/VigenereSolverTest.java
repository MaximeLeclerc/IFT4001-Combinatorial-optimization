package ift4001.tp2;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class VigenereSolverTest {

	private static final String CAESAR_CIPHER_TEXT = "YKBXG WLKHF TGLVH NGMKR FXGEX GWFXR HNKXT KLBVH FXMHU NKRVT XLTKG HMMHI KTBLX ABFMA XXOBE MATMF XGWHE BOXLT YMXKM AXFMA XZHHW BLHYM BGMXK KXWPB MAMAX BKUHG XLLHE XMBMU XPBMA VTXLT KMAXG HUEXU KNMNL ATMAM HEWRH NVTXL TKPTL TFUBM BHNLB YBMPX KXLHB MPTLT ZKBXO HNLYT NEMTG WZKBX OHNLE RATMA VTXLT KTGLP XKWBM AXKXN GWXKE XTOXH YUKNM NLTGW MAXKX LMYHK UKNMN LBLTG AHGHN KTUEX FTGLH TKXMA XRTEE TEEAH GHNKT UEXFX GVHFX BMHLI XTDBG VTXLT KLYNG XKTE";
	private static final String PLAIN_TEXT = "FRIEN DSROM ANSCO UNTRY MENLE NDMEY OUREA RSICO METOB URYCA ESARN OTTOP RAISE HIMTH EEVIL THATM ENDOL IVESA FTERT HEMTH EGOOD ISOFT INTER REDWI THTHE IRBON ESSOL ETITB EWITH CAESA RTHEN OBLEB RUTUS HATHT OLDYO UCAES ARWAS AMBIT IOUSI FITWE RESOI TWASA GRIEV OUSFA ULTAN DGRIE VOUSL YHATH CAESA RANSW ERDIT HEREU NDERL EAVEO FBRUT USAND THERE STFOR BRUTU SISAN HONOU RABLE MANSO ARETH EYALL ALLHO NOURA BLEME NCOME ITOSP EAKIN CAESA RSFUN ERAL";

	private static final String PLAIN_TEXT_SMALL = "GENIUS WITHOUT EDUCATION IS LIKE SILVER IN THE MINE";
	private static final String PLAIN_TEXT_VERY_SMALL_TEXT = "AEDV";

	private static final String PLAIN_TEXT_GOOD_DIST_OF_LETTER = "HEREUPON LEGRAND AROSE WITH A GRAVE AND STATELY AIR AND BROUGHT ME THE BEETLE FROM A GLASS CASE IN WHICH IT WAS ENCLOSED IT WAS A BEAUTIFUL SCARABAEUS AND AT THAT TIME UNKNOWN TO NATURALISTS OF COURSE A GREAT PRIZE IN A SCIENTIFIC POINT OF VIEW THERE WERE TWO ROUND BLACK SPOTS NEAR ONE EXTREMITY OF THE BACK AND A LONG ONE NEAR THE OTHER THE SCALES WERE EXCEEDINGLY HARD AND GLOSSY WITH ALL THE APPEARANCE OF BURNISHED GOLD THE WEIGHT OF THE INSECT WAS VERY REMARKABLE AND TAKING ALL THINGS INTO CONSIDERATION I COULD HARDLY BLAME JUPITER FOR HIS OPINION RESPECTING IT";
	private static final String PLAIN_TEXT_BAD_DIST_OF_LETTER = "ZJXQKB";

	private static final String CAESAR_CIPHER_TEXT_1 = "HFOJVT XJUIPVU FEVDBUJPO JT MJLF TJMWFS JO UIF NJOF";
	private static final String CAESAR_CIPHER_TEXT_2 = "IGPKWU YKVJQWV GFWECVKQP KU NKMG UKNXGT KP VJG OKPG";
	private static final String CAESAR_CIPHER_TEXT_3 = "JHQLXV ZLWKRXW HGXFDWLRQ LV OLNH VLOYHU LQ WKH PLQH";
	private static final String CAESAR_CIPHER_TEXT_4 = "KIRMYW AMXLSYX IHYGEXMSR MW PMOI WMPZIV MR XLI QMRI";
	private static final String CAESAR_CIPHER_TEXT_5 = "LJSNZX BNYMTZY JIZHFYNTS NX QNPJ XNQAJW NS YMJ RNSJ";

	private Vigenere vigenere;
	private String encrypText = "";

	@Test
	public void CaesarCipher_WithPossibleKeyLengths4AndBigMessage_returnsExpectedResult() {
		VigenereSolver.Settings settings = new VigenereSolver.Settings().setPossibleLanguages(Language.ENGLISH)
				.setPossibleKeyLengths(4);
		VigenereSolver solver = new VigenereSolver(settings);

		List<SolveResult> result = solver.solve(CAESAR_CIPHER_TEXT);

		assertThat(result.stream().anyMatch(r -> r.plainText.equals(PLAIN_TEXT)), is(true));
	}

	@Test
	public void CaesarCipher_WithPossibleKeyLengths1_returnsExpectedResult() {
		VigenereSolver.Settings settings = new VigenereSolver.Settings().setPossibleLanguages(Language.ENGLISH)
				.setPossibleKeyLengths(1);
		VigenereSolver solver = new VigenereSolver(settings);

		List<SolveResult> result = solver.solve(CAESAR_CIPHER_TEXT_1);

		assertThat(result.stream().anyMatch(r -> r.plainText.equals(PLAIN_TEXT_SMALL)), is(true));
	}

	@Test
	public void CaesarCipher_WithPossibleKeyLengths2_returnsExpectedResult() {
		VigenereSolver.Settings settings = new VigenereSolver.Settings().setPossibleLanguages(Language.ENGLISH)
				.setPossibleKeyLengths(2);
		VigenereSolver solver = new VigenereSolver(settings);

		List<SolveResult> result = solver.solve(CAESAR_CIPHER_TEXT_2);

		assertThat(result.stream().anyMatch(r -> r.plainText.equals(PLAIN_TEXT_SMALL)), is(true));
	}

	@Test
	public void CaesarCipher_WithPossibleKeyLengths3_returnsExpectedResult() {
		VigenereSolver.Settings settings = new VigenereSolver.Settings().setPossibleLanguages(Language.ENGLISH)
				.setPossibleKeyLengths(3);
		VigenereSolver solver = new VigenereSolver(settings);

		List<SolveResult> result = solver.solve(CAESAR_CIPHER_TEXT_3);

		assertThat(result.stream().anyMatch(r -> r.plainText.equals(PLAIN_TEXT_SMALL)), is(true));
	}

	@Test
	public void CaesarCipher_WithPossibleKeyLengths4_returnsExpectedResult() {
		VigenereSolver.Settings settings = new VigenereSolver.Settings().setPossibleLanguages(Language.ENGLISH)
				.setPossibleKeyLengths(4);
		VigenereSolver solver = new VigenereSolver(settings);

		List<SolveResult> result = solver.solve(CAESAR_CIPHER_TEXT_4);

		assertThat(result.stream().anyMatch(r -> r.plainText.equals(PLAIN_TEXT_SMALL)), is(true));
	}

	@Test
	public void CaesarCipher_WithPossibleKeyLengths5_returnsExpectedResult() {
		VigenereSolver.Settings settings = new VigenereSolver.Settings().setPossibleLanguages(Language.ENGLISH)
				.setPossibleKeyLengths(5);
		VigenereSolver solver = new VigenereSolver(settings);

		List<SolveResult> result = solver.solve(CAESAR_CIPHER_TEXT_5);

		assertThat(result.stream().anyMatch(r -> r.plainText.equals(PLAIN_TEXT_SMALL)), is(true));
	}

	@Test
	public void CaesarCipher_WithGoodFrequenciesDist_returnsExpectedResult() {
		vigenere = new Vigenere(PLAIN_TEXT_GOOD_DIST_OF_LETTER, Language.ENGLISH);
		encrypText = vigenere.encrypt("G");

		VigenereSolver.Settings settings = new VigenereSolver.Settings().setPossibleLanguages(Language.ENGLISH)
				.setPossibleKeyLengths(1);
		VigenereSolver solver = new VigenereSolver(settings);

		List<SolveResult> result = solver.solve(encrypText);

		assertThat(result.stream().anyMatch(r -> r.plainText.equals(PLAIN_TEXT_GOOD_DIST_OF_LETTER)), is(true));
	}

	@Test
	public void CaesarCipher_WithGoodFrequenciesDistAndKeyLenghtsOf2_returnsExpectedResult() {
		vigenere = new Vigenere(PLAIN_TEXT_GOOD_DIST_OF_LETTER, Language.ENGLISH);
		encrypText = vigenere.encrypt("GE");

		VigenereSolver.Settings settings = new VigenereSolver.Settings().setPossibleLanguages(Language.ENGLISH)
				.setPossibleKeyLengths(2);
		VigenereSolver solver = new VigenereSolver(settings);

		List<SolveResult> result = solver.solve(encrypText);

		assertThat(result.stream().anyMatch(r -> r.plainText.equals(PLAIN_TEXT_GOOD_DIST_OF_LETTER)), is(true));
	}

	@Test
	public void CaesarCipher_WithBadFrequenciesDist_returnsNotExpectedResult() {
		vigenere = new Vigenere(PLAIN_TEXT_BAD_DIST_OF_LETTER, Language.ENGLISH);
		encrypText = vigenere.encrypt("G");

		VigenereSolver.Settings settings = new VigenereSolver.Settings().setPossibleLanguages(Language.ENGLISH)
				.setPossibleKeyLengths(1);
		VigenereSolver solver = new VigenereSolver(settings);

		List<SolveResult> result = solver.solve(encrypText);

		assertThat(result.stream().anyMatch(r -> r.plainText.equals(PLAIN_TEXT_BAD_DIST_OF_LETTER)), is(false));

	}

	@Test
	public void CaesarCipher_WithVerySmallText_returnsExprectedResult() {
		vigenere = new Vigenere(PLAIN_TEXT_VERY_SMALL_TEXT, Language.ENGLISH);
		encrypText = vigenere.encrypt("D");
		VigenereSolver.Settings settings = new VigenereSolver.Settings().setPossibleLanguages(Language.ENGLISH)
				.setPossibleKeyLengths(1);
		VigenereSolver solver = new VigenereSolver(settings);

		List<SolveResult> result = solver.solve(encrypText);

		assertThat(result.stream().anyMatch(r -> r.plainText.equals(PLAIN_TEXT_VERY_SMALL_TEXT)), is(true));
	}

	@Test
	public void CaesarCipher_WithVerySmallTextAndNumberOfResultsReturn50AndKeyLengths2_returnsExprectedResult() {
		vigenere = new Vigenere(PLAIN_TEXT_VERY_SMALL_TEXT, Language.ENGLISH);
		encrypText = vigenere.encrypt("DT");
		VigenereSolver.Settings settings = new VigenereSolver.Settings().setPossibleLanguages(Language.ENGLISH)
				.setPossibleKeyLengths(2).setNumberOfResultsToReturn(50);
		VigenereSolver solver = new VigenereSolver(settings);

		List<SolveResult> result = solver.solve(encrypText);

		assertThat(result.stream().anyMatch(r -> r.plainText.equals(PLAIN_TEXT_VERY_SMALL_TEXT)), is(true));
	}
}
