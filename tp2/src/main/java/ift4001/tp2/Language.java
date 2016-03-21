package ift4001.tp2;

public enum Language {
    ENGLISH("ABCDEFGHIJKLMNOPQRSTUVWXYZ", Frequencies.ENGLISH);

    private final char[] alphabet;
    private final double[] frequencies;

    Language(String alphabet, double[] frequencies) {
        this.alphabet = alphabet.toUpperCase().toCharArray();
        this.frequencies = frequencies;
    }

    public boolean containsLetter(char letter) {
        for (char c : this.alphabet) {
            if (c == letter) {
                return true;
            }
        }
        return false;
    }

    public int alphabetLength() {
        return this.alphabet.length;
    }

    public int letterIndex(char letter) {
        for (int i = 0; i < this.alphabetLength(); ++i) {
            if (this.alphabet[i] == letter) {
                return i;
            }
        }
        return -1;
    }

    public char letterAt(int i) {
        return alphabet[i];
    }

    public int[] messageToInts(String message) {
        return message.chars().filter(c -> c != ' ').toArray();
    }

    public String intsToMessage(int[] ints) {
        return "";
    }

    public double[] getFrequencies() {
        return this.frequencies;
    }
}
