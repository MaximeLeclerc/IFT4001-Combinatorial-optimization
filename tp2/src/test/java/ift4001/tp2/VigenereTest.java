package ift4001.tp2;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class VigenereTest {

    private static final String CAESAR_CIPHER_TEXT = "fskhr iyywv kyepc jdkjr gzjic mdngb szxli actob atjft nltyx crasp rxyuw zjsdq uwbjy wxpdh qfztn gqhtr pdncl rgsgj ywfnc admnd jurjy oubnb cqqkx jylay dnymm eturp jzpbk ohugv rqvkt lodbl nbmge vzzba qketg catpg nwvap yqwbu dqgoa qxedu swimv jxmje quzsb iumjb vcbpl gyhpd uykcz rlsrl zjdxm ihmbk cmagl uivbn caoct negup jgcmx pmdhh jirfw ncaoc rtgqu fwaac bsdma vddfm kuwhb kzntq dvjzo mhkwx nzaxf fhomw yjdmf pfsrk pbxca qnyxd tsxmv npdtn ftdp"
            .toUpperCase();
    private static final String PLAIN_TEXT = "FRIEN DSROM ANSCO UNTRY MENLE NDMEY OUREA RSICO METOB URYCA ESARN OTTOP RAISE HIMTH EEVIL THATM ENDOL IVESA FTERT HEMTH EGOOD ISOFT INTER REDWI THTHE IRBON ESSOL ETITB EWITH CAESA RTHEN OBLEB RUTUS HATHT OLDYO UCAES ARWAS AMBIT IOUSI FITWE RESOI TWASA GRIEV OUSFA ULTAN DGRIE VOUSL YHATH CAESA RANSW ERDIT HEREU NDERL EAVEO FBRUT USAND THERE STFOR BRUTU SISAN HONOU RABLE MANSO ARETH EYALL ALLHO NOURA BLEME NCOME ITOSP EAKIN CAESA RSFUN ERAL";

    @Test
    public void encryption_returnsExpectedResult() {
        Vigenere vigenere = new Vigenere(PLAIN_TEXT, Language.ENGLISH);
        String result = vigenere.encrypt("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        assertThat(result, is(equalTo(CAESAR_CIPHER_TEXT)));
    }

    @Test
    public void decryption_returnsExpectedResult() {
        Vigenere vigenere = new Vigenere(CAESAR_CIPHER_TEXT, Language.ENGLISH);
        String result = vigenere.decrypt("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        assertThat(result, is(equalTo(PLAIN_TEXT)));
    }

}
