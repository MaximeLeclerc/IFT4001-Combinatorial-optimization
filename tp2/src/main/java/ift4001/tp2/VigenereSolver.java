package ift4001.tp2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.chocosolver.solver.ResolutionPolicy;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VF;
import org.chocosolver.solver.variables.VariableFactory;

public class VigenereSolver {

    public static class Settings {

        private Iterable<Integer> keyLengths = new ArrayList<Integer>(0);
        private Iterable<Language> languages = new ArrayList<Language>(0);

        public Settings setPossibleKeyLengths(Integer... lengths) {
            return this.setPossibleKeyLengths(Arrays.asList(lengths));
        }

        public Settings setPossibleKeyLengths(Iterable<Integer> lengths) {
            this.keyLengths = lengths;
            return this;
        }

        public Settings setPossibleLanguages(Language... languages) {
            return this.setPossibleLanguages(Arrays.asList(languages));
        }

        public Settings setPossibleLanguages(Iterable<Language> languages) {
            this.languages = languages;
            return this;
        }
    }

    private final Settings settings;

    public VigenereSolver(Settings settings) {
        this.settings = settings;
    }

    public String solve(String cipherText) {
        for (Language language : this.settings.languages) {
            return this.solveForLanguage(cipherText, language);
        }
        return cipherText;
    }

    private String solveForLanguage(String cipherText, Language language) {
        Solver solver = new Solver();

        IntVar[] cipherTextVars = cipherText.chars().filter(c -> c != ' ').map(c -> c - 'A')
                .mapToObj(x -> VF.fixed(x, solver)).toArray(IntVar[]::new);

        int messageLength = cipherTextVars.length;
        int frequencyPrecision = 10000;
        IntVar messageLengthVar = VF.fixed(messageLength, solver);
        IntVar alphabetLengthVar = VF.fixed(language.alphabetLength(), solver);

        IntVar[] keyComplement = VF.enumeratedArray("key", settings.keyLengths.iterator().next(), 0,
                language.alphabetLength() - 1, solver);
        IntVar[] intermediateText = VF.enumeratedArray("intermediatetext", messageLength, 0,
                2 * (language.alphabetLength() - 1), solver);
        IntVar[] plainText = VF.enumeratedArray("plaintext", messageLength, 0, language.alphabetLength() - 1, solver);
        for (int i = 0; i < messageLength; ++i) {
            solver.post(ICF.sum(new IntVar[] { cipherTextVars[i], keyComplement[i % keyComplement.length] },
                    intermediateText[i]));
            solver.post(ICF.mod(intermediateText[i], alphabetLengthVar, plainText[i]));
        }

        IntVar[] counts = VF.boundedArray("counts", language.alphabetLength(), 0, messageLength, solver);
        IntVar[] multipliedCounts = VF.boundedArray("counts", language.alphabetLength(), 0,
                messageLength * frequencyPrecision, solver);
        IntVar[] multipliedFrequencies = VF.boundedArray("frequencies", language.alphabetLength(), 0,
                frequencyPrecision, solver);

        for (int i = 0; i < language.alphabetLength(); ++i) {
            solver.post(ICF.count(i, plainText, counts[i]));
            solver.post(ICF.times(counts[i], frequencyPrecision, multipliedCounts[i]));
            solver.post(ICF.eucl_div(multipliedCounts[i], messageLengthVar, multipliedFrequencies[i]));
        }

        IntVar[] languageNegativeMultipliedFrequencies = Arrays.stream(language.getFrequencies())
                .mapToObj(x -> VF.fixed((int) (x * -frequencyPrecision), solver)).toArray(IntVar[]::new);

        IntVar[] differences = VF.boundedArray("diffs", language.alphabetLength(), -frequencyPrecision,
                frequencyPrecision, solver);
        IntVar[] absDifferences = new IntVar[language.alphabetLength()];

        for (int i = 0; i < language.alphabetLength(); ++i) {
            solver.post(ICF.sum(new IntVar[] { multipliedFrequencies[i], languageNegativeMultipliedFrequencies[i] },
                    differences[i]));
            absDifferences[i] = VF.abs(differences[i]);
        }

        IntVar frequencyDiffsSum = VariableFactory.enumerated("frequencyDiffsSum", 0,
                language.alphabetLength() * frequencyPrecision, solver);
        solver.post(ICF.sum(absDifferences, frequencyDiffsSum));

        solver.findOptimalSolution(ResolutionPolicy.MINIMIZE, frequencyDiffsSum);

        String key = Arrays.stream(keyComplement).map(x -> language.letterAt(language.alphabetLength() - x.getValue()))
                .map(c -> String.valueOf(c)).collect(Collectors.joining());
        System.out.println("Key: " + key);
        Vigenere vigenere = new Vigenere(cipherText, language);
        return vigenere.decrypt(key);
    }

}
