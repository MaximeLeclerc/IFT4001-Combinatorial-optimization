package ift4001.tp2;

public class Vigenere {

    private final String message;
    private final Language language;

    public Vigenere(String message, Language language) {
        this.message = message;
        this.language = language;
    }

    public String encrypt(String key) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.message.length(); ++i) {
            if (this.language.containsLetter(this.message.charAt(i))) {
                int messageLetterIndex = this.language.letterIndex(this.message.charAt(i));
                int keyLetterIndex = this.language.letterIndex(key.charAt(i % key.length()));
                int cipherTextLetterIndex = (messageLetterIndex + keyLetterIndex) % this.language.alphabetLength();
                builder.append(this.language.letterAt(cipherTextLetterIndex));
            } else {
                builder.append(this.message.charAt(i));
            }
        }
        return builder.toString();
    }

    public String decrypt(String key) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.message.length(); ++i) {
            if (this.language.containsLetter(this.message.charAt(i))) {
                int messageLetterIndex = this.language.letterIndex(this.message.charAt(i));
                int keyLetterIndex = this.language.letterIndex(key.charAt(i % key.length()));
                int plainTextLetterIndex = (messageLetterIndex + this.language.alphabetLength() - keyLetterIndex)
                        % this.language.alphabetLength();
                builder.append(this.language.letterAt(plainTextLetterIndex));
            } else {
                builder.append(this.message.charAt(i));
            }
        }
        return builder.toString();
    }
}
