package ift4001.tp2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.chocosolver.solver.ResolutionPolicy;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.search.strategy.ISF;
import org.chocosolver.solver.trace.Chatterbox;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VF;
import org.chocosolver.solver.variables.VariableFactory;

import com.google.common.collect.MinMaxPriorityQueue;

public class VigenereSolver {

    public static class Settings {

        private Iterable<Integer> keyLengths = new ArrayList<>(0);
        private Iterable<Language> languages = new ArrayList<>(0);

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

    public List<SolveResult> solve(String cipherText) {
        MinMaxPriorityQueue<SolveResult> results = MinMaxPriorityQueue.maximumSize(10).create();
        StreamSupport.stream(this.settings.keyLengths.spliterator(), true).forEach(keyLength -> {
            StreamSupport.stream(this.settings.languages.spliterator(), true).forEach(language -> {
                for (SolveResult result : this.solveForLanguageAndKeyLength(cipherText, language, keyLength)) {
                    results.offer(result);
                }
            });
        });
        return Stream.generate(() -> results.removeFirst()).limit(results.size()).collect(Collectors.toList());
    }

    private List<SolveResult> solveForLanguageAndKeyLength(String cipherText, Language language, int keyLength) {
        Solver solver = new Solver();

        try {
            File file = new File("ChocoDecision.txt");
            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(file);

            PrintStream printStream = new PrintStream(fos);
            Chatterbox.setOut(printStream);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Chatterbox.showContradiction(solver);
        Chatterbox.showDecisions(solver);

        IntVar[] cipherTextVars = cipherText.chars().filter(c -> c != ' ').map(c -> c - 'A')
                .mapToObj(x -> VF.fixed(x, solver)).toArray(IntVar[]::new);

        int messageLength = cipherTextVars.length;
        int frequencyPrecision = 10000;
        IntVar messageLengthVar = VF.fixed(messageLength, solver);
        IntVar alphabetLengthVar = VF.fixed(language.alphabetLength(), solver);

        IntVar[] keyComplement = VF.enumeratedArray("key", keyLength, 0, language.alphabetLength() - 1, solver);
        IntVar[] intermediateText = VF.enumeratedArray("intermediatetext", messageLength, 0,
                2 * (language.alphabetLength() - 1), solver);
        IntVar[] plainText = VF.enumeratedArray("plaintext", messageLength, 0, language.alphabetLength() - 1, solver);
        for (int i = 0; i < messageLength; ++i) {
            solver.post(ICF.sum(new IntVar[] { cipherTextVars[i], keyComplement[i % keyComplement.length] },
                    intermediateText[i]));
            solver.post(ICF.mod(intermediateText[i], alphabetLengthVar, plainText[i]));
        }

        IntVar[] counts = VF.boundedArray("counts", language.alphabetLength(), 0, messageLength, solver);
        IntVar[] multipliedCounts = VF.boundedArray("multipliedCounts", language.alphabetLength(), 0,
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

        // Custom Heuristics:
        VarSelectorHeuristic varSelector = new VarSelectorHeuristic(cipherText.replaceAll("\\s+", ""), keyLength);
        ValSelectorHeuristic valSelector = new ValSelectorHeuristic(Frequencies.ENGLISH);
        solver.set(ISF.custom(varSelector, valSelector, plainText));

        NBestSolutionsRecorder solutionRecorder = new NBestSolutionsRecorder(10, frequencyDiffsSum);
        solver.set(solutionRecorder);

        solver.findAllOptimalSolutions(ResolutionPolicy.MINIMIZE, frequencyDiffsSum, false);

        Chatterbox.printStatistics(solver);

        SolveResult.Builder builder = new SolveResult.Builder(cipherText, language);
        return solutionRecorder.getSolutions().stream().map(solution -> {
            String key = Arrays.stream(keyComplement)
                    .map(x -> language.letterAt((language.alphabetLength() - solution.getIntVal(x)) % 26))
                    .map(c -> String.valueOf(c)).collect(Collectors.joining());
            return builder.create(key, solution.getIntVal(frequencyDiffsSum));
        }).collect(Collectors.toList());

    }
}
