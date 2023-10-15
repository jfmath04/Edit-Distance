/**
 * This file implements the edit distance for the classic Levenshtein distance
 * and a keyboard distance created by us.
 */
public class EditDistance {

    // method to read a file containing a table of weight values and convert
    // into a 2D array of doubles
    private static double[][] readweights(String filename) {
        // weight table, number rows and columns equal to letters in the alphabet
        double[][] weights = new double[26][26];
        In input = new In(filename);
        for (int i = 0; i < 26; i++) {
            for (int j = 0; j < 26; j++) {
                weights[i][j] = input.readDouble();
            }
        }
        return weights;
    }

    // method to return the Levenshtein edit distance between two given strings
    // algorithm here: https://en.wikipedia.org/wiki/Edit_distance#Common_algorithm
    public static int editDistance(String string1, String string2) {
        // table of distances between substrings of the given words
        int[][] distance = new int[string1.length() + 1][string2.length() + 1];
        // fill out first row and column
        for (int i = 0; i < string1.length() + 1; i++) distance[i][0] = i;
        for (int i = 0; i < string2.length() + 1; i++) distance[0][i] = i;
        // iterate over rest of table to fill in distances
        for (int i = 1; i < string1.length() + 1; i++) {
            for (int j = 1; j < string2.length() + 1; j++) {
                if (string1.charAt(i - 1) == string2.charAt(j - 1))
                    distance[i][j] = distance[i - 1][j - 1];
                else { // find the smallest distance from edit options
                    int del = distance[i - 1][j] + 1;
                    int ins = distance[i][j - 1] + 1;
                    int sub = distance[i - 1][j - 1] + 1;
                    int min = Math.min(del, ins);
                    distance[i][j] = Math.min(min, sub);
                }
            }
        }
        // bottom right value of table is the distance we want
        return distance[string1.length()][string2.length()];
    }

    /// method to return the keyboard edit distance between two given strings
    // each substitution has weight based on how far the letters are from each other
    // algorithm here: https://en.wikipedia.org/wiki/Edit_distance#Common_algorithm
    public static double keyboardEditDistance(String string1, String string2) {
        double[][] subWeights = readweights("keyboard.txt"); // read weight table
        // table of distances between substrings of the given words
        double[][] distance = new double[string1.length() + 1][string2.length() + 1];
        // fill out first row and column
        for (int i = 0; i < string1.length() + 1; i++) distance[i][0] = i;
        for (int i = 0; i < string2.length() + 1; i++) distance[0][i] = i;
        // iterate over rest of table to fill in distances
        for (int i = 1; i < string1.length() + 1; i++) {
            for (int j = 1; j < string2.length() + 1; j++) {
                char a = string1.charAt(i - 1);
                char b = string2.charAt(j - 1);
                if (a == b)
                    distance[i][j] = distance[i - 1][j - 1];
                else { // find the smallest distance from edit options
                    double del = distance[i - 1][j] + 1;
                    double ins = distance[i][j - 1] + 1;
                    double sub = distance[i - 1][j - 1] + subWeights[a - 97][b - 97];
                    double min = Math.min(del, ins);
                    distance[i][j] = Math.min(min, sub);
                }
            }
        }
        return distance[string1.length()][string2.length()];
    }

    // test client
    public static void main(String[] args) {
        StdOut.print("Enter the first word: ");
        String string1 = StdIn.readString().replaceAll("[^a-zA-Z]", "").toLowerCase();
        StdOut.print("Enter the second word: ");
        String string2 = StdIn.readString().replaceAll("[^a-zA-Z]", "").toLowerCase();
        StdOut.print("Would you like to use the edit distance (1) or the keyboard distance (2)? ");
        int n = StdIn.readInt();
        double distance = 0;
        if (n == 1) distance = editDistance(string1, string2);
        else if (n == 2) distance = keyboardEditDistance(string1, string2);
        StdOut.println("Distance = " + distance);
    }
}
