package ift4001.tp2;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class VigenereSolverTest {

    private static final String CAESAR_CIPHER_TEXT = "YKBXG WLKHF TGLVH NGMKR FXGEX GWFXR HNKXT KLBVH FXMHU NKRVT XLTKG HMMHI KTBLX ABFMA XXOBE MATMF XGWHE BOXLT YMXKM AXFMA XZHHW BLHYM BGMXK KXWPB MAMAX BKUHG XLLHE XMBMU XPBMA VTXLT KMAXG HUEXU KNMNL ATMAM HEWRH NVTXL TKPTL TFUBM BHNLB YBMPX KXLHB MPTLT ZKBXO HNLYT NEMTG WZKBX OHNLE RATMA VTXLT KTGLP XKWBM AXKXN GWXKE XTOXH YUKNM NLTGW MAXKX LMYHK UKNMN LBLTG AHGHN KTUEX FTGLH TKXMA XRTEE TEEAH GHNKT UEXFX GVHFX BMHLI XTDBG VTXLT KLYNG XKTE";
    private static final String PLAIN_TEXT = "FRIEN DSROM ANSCO UNTRY MENLE NDMEY OUREA RSICO METOB URYCA ESARN OTTOP RAISE HIMTH EEVIL THATM ENDOL IVESA FTERT HEMTH EGOOD ISOFT INTER REDWI THTHE IRBON ESSOL ETITB EWITH CAESA RTHEN OBLEB RUTUS HATHT OLDYO UCAES ARWAS AMBIT IOUSI FITWE RESOI TWASA GRIEV OUSFA ULTAN DGRIE VOUSL YHATH CAESA RANSW ERDIT HEREU NDERL EAVEO FBRUT USAND THERE STFOR BRUTU SISAN HONOU RABLE MANSO ARETH EYALL ALLHO NOURA BLEME NCOME ITOSP EAKIN CAESA RSFUN ERAL";
    
	private static final String PLAIN_TEXT_2 = "GENIUS WITHOUT EDUCATION IS LIKE SILVER IN THE MINE";
    private static final String CAESAR_CIPHER_TEXT_2 = "IGPKWU YKVJQWV GFWECVKQP KU NKMG UKNXGT KP VJG OKPG";
    
    private static final String CAESAR_CIPHER_TEXT_3  = "JHQLXV ZLWKRXW HGXFDWLRQ LV OLNH VLOYHU LQ WKH PLQH";
    private static final String CAESAR_CIPHER_TEXT_4 = "KIRMYW AMXLSYX IHYGEXMSR MW PMOI WMPZIV MR XLI QMRI";
    
    @Test
    public void CaesarCipher_returnsExpectedResult() {
        VigenereSolver.Settings settings = new VigenereSolver.Settings().setPossibleLanguages(Language.ENGLISH)
                .setPossibleKeyLengths(4);
        VigenereSolver solver = new VigenereSolver(settings);

        List<SolveResult> result = solver.solve(CAESAR_CIPHER_TEXT);
        
        result.stream().forEach(c -> System.out.println(c.plainText + " - " + c.key));

        assertThat(result.stream().anyMatch(r -> r.plainText.equals(PLAIN_TEXT)), is(true));
    }
    
    @Test
    public void CaesarCipher_returnsExpectedResult_2() {
        VigenereSolver.Settings settings = new VigenereSolver.Settings().setPossibleLanguages(Language.ENGLISH)
                .setPossibleKeyLengths(1);
        VigenereSolver solver = new VigenereSolver(settings);

        List<SolveResult> result = solver.solve(CAESAR_CIPHER_TEXT_2);

        assertThat(result.stream().anyMatch(r -> r.plainText.equals(PLAIN_TEXT_2)), is(true));
    }
    
    @Test
    public void CaesarCipher_returnsExpectedResult_3() {
        VigenereSolver.Settings settings = new VigenereSolver.Settings().setPossibleLanguages(Language.ENGLISH)
                .setPossibleKeyLengths(1);
        VigenereSolver solver = new VigenereSolver(settings);

        List<SolveResult> result = solver.solve(CAESAR_CIPHER_TEXT_3);

        assertThat(result.stream().anyMatch(r -> r.plainText.equals(PLAIN_TEXT_2)), is(true));
    } 
    
    @Test
    public void CaesarCipher_returnsExpectedResult_12() {
        VigenereSolver.Settings settings = new VigenereSolver.Settings().setPossibleLanguages(Language.ENGLISH)
                .setPossibleKeyLengths(1);
        VigenereSolver solver = new VigenereSolver(settings);

        List<SolveResult> result = solver.solve(CAESAR_CIPHER_TEXT_4);
        
        result.stream().forEach(c -> System.out.println(c.plainText + " - " + c.key));

        assertThat(result.stream().anyMatch(r -> r.plainText.equals(PLAIN_TEXT_2)), is(true));
    } 
}
