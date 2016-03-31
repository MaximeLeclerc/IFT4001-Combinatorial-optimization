package ift4001.tp2;

public class SolveResult implements Comparable<SolveResult> {
    public final String key;
    public final String plainText;
    public final Language language;
    private final int objectiveValue;

    private SolveResult(String key, String plainText, Language language, int objectiveValue) {
        this.key = key;
        this.plainText = plainText;
        this.language = language;
        this.objectiveValue = objectiveValue;
    }

    @Override
    public int compareTo(SolveResult o) {
        return Integer.compare(this.objectiveValue, o.objectiveValue);
    }

    static class Builder {
        private final Language language;
        private final Vigenere vigenere;

        Builder(String cipherText, Language language) {
            this.language = language;
            this.vigenere = new Vigenere(cipherText, language);
        }

        SolveResult create(String key, int objectiveValue) {
            return new SolveResult(key, this.vigenere.decrypt(key), this.language, objectiveValue);
        }
    }
}
