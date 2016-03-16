package ift4001.tp2;

import java.util.Arrays;

import org.chocosolver.solver.ResolutionPolicy;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.trace.Chatterbox;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VF;
import org.chocosolver.solver.variables.VariableFactory;

public class Vigenere {

    static final char[] english_alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    static final double[] english_freq = { 0.08167, 0.01492, 0.02782, 0.04253, 0.12702, 0.02228, 0.02015, 0.06094,
            0.06966, 0.00153, 0.00772, 0.04025, 0.02406, 0.06749, 0.07507, 0.01929, 0.00095, 0.05987, 0.06327, 0.09056,
            0.02758, 0.00978, 0.02361, 0.00150, 0.01974, 0.00074 };

    public static void main(String[] args) {

        String cipherString = "YKBXG WLKHF TGLVH NGMKR FXGEX GWFXR HNKXT KLBVH FXMHU NKRVT XLTKG HMMHI KTBLX ABFMA XXOBE MATMF XGWHE BOXLT YMXKM AXFMA XZHHW BLHYM BGMXK KXWPB MAMAX BKUHG XLLHE XMBMU XPBMA VTXLT KMAXG HUEXU KNMNL ATMAM HEWRH NVTXL TKPTL TFUBM BHNLB YBMPX KXLHB MPTLT ZKBXO HNLYT NEMTG WZKBX OHNLE RATMA VTXLT KTGLP XKWBM AXKXN GWXKE XTOXH YUKNM NLTGW MAXKX LMYHK UKNMN LBLTG AHGHN KTUEX FTGLH TKXMA XRTEE TEEAH GHNKT UEXFX GVHFX BMHLI XTDBG VTXLT KLYNG XKTE";

        Solver solver = new Solver();

        IntVar[] cipherText = cipherString.chars().filter(c -> c != ' ').map(c -> c - 'A')
                .mapToObj(x -> VF.fixed(x, solver)).toArray(IntVar[]::new);

        int messageLength = cipherText.length;
        char[] alphabet = english_alphabet;
        double[] languageFrequencies = english_freq;
        int frequencyPrecision = 10000;
        IntVar messageLengthVar = VF.fixed(messageLength, solver);
        IntVar alphabetLengthVar = VF.fixed(alphabet.length, solver);

        IntVar key = VF.enumerated("key", 0, alphabet.length - 1, solver);
        IntVar[] intermediateText = VF.enumeratedArray("intermediatetext", messageLength, 0, 2 * (alphabet.length - 1),
                solver);
        IntVar[] plainText = VF.enumeratedArray("plaintext", messageLength, 0, alphabet.length - 1, solver);
        for (int i = 0; i < messageLength; ++i) {
            solver.post(ICF.sum(new IntVar[] { cipherText[i], key }, intermediateText[i]));
            solver.post(ICF.mod(intermediateText[i], alphabetLengthVar, plainText[i]));
        }

        IntVar[] counts = VF.boundedArray("counts", alphabet.length, 0, messageLength, solver);
        IntVar[] multipliedCounts = VF.boundedArray("counts", alphabet.length, 0, messageLength * frequencyPrecision,
                solver);
        IntVar[] multipliedFrequencies = VF.boundedArray("frequencies", alphabet.length, 0, frequencyPrecision, solver);

        for (int i = 0; i < alphabet.length; ++i) {
            solver.post(ICF.count(i, plainText, counts[i]));
            solver.post(ICF.times(counts[i], frequencyPrecision, multipliedCounts[i]));
            solver.post(ICF.eucl_div(multipliedCounts[i], messageLengthVar, multipliedFrequencies[i]));
        }

        IntVar[] languageNegativeMultipliedFrequencies = Arrays.stream(languageFrequencies)
                .mapToObj(x -> VF.fixed((int) (x * -frequencyPrecision), solver)).toArray(IntVar[]::new);

        IntVar[] differences = VF.boundedArray("diffs", alphabet.length, -frequencyPrecision, frequencyPrecision,
                solver);
        IntVar[] absDifferences = new IntVar[alphabet.length];

        for (int i = 0; i < alphabet.length; ++i) {
            solver.post(ICF.sum(new IntVar[] { multipliedFrequencies[i], languageNegativeMultipliedFrequencies[i] },
                    differences[i]));
            absDifferences[i] = VF.abs(differences[i]);
        }

        IntVar frequencyDiffsSum = VariableFactory.enumerated("frequencyDiffsSum", 0,
                alphabet.length * frequencyPrecision, solver);
        solver.post(ICF.sum(absDifferences, frequencyDiffsSum));

        solver.findOptimalSolution(ResolutionPolicy.MINIMIZE, frequencyDiffsSum);

        System.out.println(key.getValue());
        for (IntVar i : plainText) {
            System.out.print(alphabet[i.getValue()]);
        }
        System.out.println();

        Chatterbox.printStatistics(solver);
    }
}
