package ift4001.tp2;

public class Vigenere {

    private final String message;
    private final Language language;

    public Vigenere(String message, Language language) {
        this.message = message;
        this.language = language;
    }

    public String encrypt(String key) {
        int keyIndex = 0;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.message.length(); ++i) {
            if (this.language.containsLetter(this.message.charAt(i))) {
                int messageLetterIndex = this.language.letterIndex(this.message.charAt(i));
                int keyLetterIndex = this.language.letterIndex(key.charAt(keyIndex));
                int cipherTextLetterIndex = (messageLetterIndex + keyLetterIndex) % this.language.alphabetLength();
                builder.append(this.language.letterAt(cipherTextLetterIndex));
                keyIndex = (keyIndex + 1) % key.length();
            } else {
                builder.append(this.message.charAt(i));
            }
        }
        return builder.toString();
    }

    public String decrypt(String key) {
        int keyIndex = 0;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.message.length(); ++i) {
            if (this.language.containsLetter(this.message.charAt(i))) {
                int messageLetterIndex = this.language.letterIndex(this.message.charAt(i));
                int keyLetterIndex = this.language.letterIndex(key.charAt(keyIndex));
                int plainTextLetterIndex = (messageLetterIndex + this.language.alphabetLength() - keyLetterIndex)
                        % this.language.alphabetLength();
                builder.append(this.language.letterAt(plainTextLetterIndex));
                keyIndex = (keyIndex + 1) % key.length();
            } else {
                builder.append(this.message.charAt(i));
            }
        }
        return builder.toString();
    }
}
