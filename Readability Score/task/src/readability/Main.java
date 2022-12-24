package readability;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

//        String s = "1 \n2345\t 67  8. 9 10 11 12! 13 14?";

        String s = "";

//        String path = "C:\\Users\\prade\\IdeaProjects\\Readability Score\\Readability Score\\task\\src\\readability\\";

        String path = ".\\";

        try {
            s = readFileAsString(path + args[0]);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        scoreOptions(s);

//        System.out.println(getNoSyllables("you"));
//        System.out.println(getNoPolySyllables("you"));

    }

    private static String readFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }
    private static int getNoChar(String s) {
        String[] t = s.trim().split("[\n \t\r]+");
        String j = String.join("", t);
        return j.length();
    }

    private static int getNoWords(String s) {
        return s.trim().split("[\n \t]+").length;
    }

    private static int getNoSentences(String s) {
        return s.trim().split("[.!?]").length;
    }

    private static double getARIScore(String s) {
        return 4.71 * getNoChar(s) / getNoWords(s) + 0.5 * getNoWords(s) / getNoSentences(s) - 21.43;
    }

    private static int readingLevel(double score) {

        int index = (int) Math.ceil(score);

        switch (index) {
            case 1 -> {
                return 6;
            }
            case 2 -> {
                return 7;
            }
            case 3 -> {
                return 8;
            }
            case 4 -> {
                return 9;
            }
            case 5 -> {
                return 10;
            }
            case 6 -> {
                return 11;
            }
            case 7 -> {
                return 12;
            }
            case 8 -> {
                return 13;
            }
            case 9 -> {
                return 14;
            }
            case 10 -> {
                return 15;
            }
            case 11 -> {
                return 16;
            }
            case 12 -> {
                return 17;
            }
            case 13 -> {
                return 18;
            }
            default -> {
                return 22;
            }
        }
    }

    private static void printResults(String s, int chars, int words, int sentences, int syllables, int polysyll) {
        System.out.println("The text is:");
        System.out.println(s + "\n");
        System.out.println("Words: " + words);
        System.out.println("Sentences: " + sentences);
        System.out.println("Characters: " + chars);
        System.out.println("Syllables: " + syllables);
        System.out.println("Polysyllables: " + polysyll);
//        System.out.printf("The score is: %.2f%n", getARIScore(s));
//        System.out.println("This text should be understood by " + readingLevel((int) Math.ceil(getScore(s))) + " year-olds.");
    }

    private static int getNoSyllables(String s) {
        final String vowels = "aeiouy";
        String[] words = s.toLowerCase().trim().split("[\n \t\r]+");
        int noVowels = 0;

        for (String w : words) {
            noVowels += getSyllablesInOneWord(w);
        }

        return noVowels;
    }

    private static int getNoPolySyllables(String s) {
        final String vowels = "aeiouy";
        String[] words = s.trim().split("[\n \t\r]+");
        int noPolyVowels = 0;

        for (String w : words) {
            if (getSyllablesInOneWord(w) > 2) {
                noPolyVowels++;
            }
        }

        return noPolyVowels;
    }

    private static int getSyllablesInOneWord(String w) {
        final String vowels = "aeiouy";
        int noVowels = 0;

        w = w.toLowerCase();
        w = w.replaceAll("[aeiouy]{2,}", "a");
        w = w.replaceAll("[.!?]", "");

        for (char c : w.toCharArray()) {
            if (vowels.contains("" + c)) noVowels++;
        }
        if (w.endsWith("e")) noVowels--;

        return noVowels <= 0 ? 1 : noVowels;
    }

    private static double fleschKincaidScore(int words, int sentences, int syllables) {
        return 0.39 * words / sentences + 11.8 * syllables / words - 15.59;
    }

    private static double smogScore(int sentences, int polysyll) {
        return 1.043 * Math.sqrt((double) polysyll * 30 / sentences) + 3.1291;
    }

    private static double colemanLiauScore(int characters, int words, int sentences) {
        double L = (double) characters / words * 100;
        double S = (double) sentences / words * 100;
        return 0.0588 * L - 0.296 * S - 15.8;
    }

    private static void scoreOptions(String s) {
        Scanner in = new Scanner(System.in);

        int words = getNoWords(s);
        int chars = getNoChar(s);
        int sentences = getNoSentences(s);
        int syllables = getNoSyllables(s);
        int polysyllables = getNoPolySyllables(s);

        printResults(s, chars, words, sentences, syllables, polysyllables);

        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        String choice = in.nextLine();
        System.out.println();

        switch (choice) {
            case "ARI" -> {
                double score = getARIScore(s);
                System.out.printf("Automated Readability Index: %.2f (about %d-year-olds).", score, readingLevel(score));
            }
            case "FK" -> {
                double score = fleschKincaidScore(words, sentences, syllables);
                System.out.printf("Flesch–Kincaid readability tests: %.2f (about %d-year-olds).", score, readingLevel(score));
            }
            case "SMOG" -> {
                double score = smogScore(sentences, polysyllables);
                System.out.printf("Simple Measure of Gobbledygook: %.2f (about %d-year-olds).", score, readingLevel(score));
            }
            case "CL" -> {
                double score = colemanLiauScore(chars, words, sentences);
                System.out.printf("Coleman–Liau index: %.2f (about %d-year-olds).", score, readingLevel(score));
            }
            case "all" -> {
                double score1 = getARIScore(s);
                double score2 = fleschKincaidScore(words, sentences, syllables);
                double score3 = smogScore(sentences, polysyllables);
                double score4 = colemanLiauScore(chars, words, sentences);

                int readingLevel1 = readingLevel(score1);
                int readingLevel2 = readingLevel(score2);
                int readingLevel3 = readingLevel(score3);
                int readingLevel4 = readingLevel(score4);

                double avgReadingLevel = (double) (readingLevel1 + readingLevel2 + readingLevel3 + readingLevel4) /  4;

                System.out.printf("Automated Readability Index: %.2f (about %d-year-olds).%n", score1, readingLevel(score1));
                System.out.printf("Flesch–Kincaid readability tests: %.2f (about %d-year-olds).%n", score2, readingLevel(score2));
                System.out.printf("Simple Measure of Gobbledygook: %.2f (about %d-year-olds).%n", score3, readingLevel(score3));
                System.out.printf("Coleman–Liau index: %.2f (about %d-year-olds).%n%n", score4, readingLevel(score4));

                System.out.printf("This text should be understood in average by %.2f-year-olds.", avgReadingLevel);
            }
        }
    }
}
