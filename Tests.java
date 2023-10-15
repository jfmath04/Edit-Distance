// This file tests all methods in the other two java files
public class Tests {
    public static void main(String[] args) {
        StdOut.println("-------TESTS FOR EDIT DISTANCE-------");
        StdOut.println(EditDistance.editDistance("kitty", "city")); // output 2
        StdOut.println(EditDistance.editDistance("time", "tight")); // output 3
        StdOut.println(EditDistance.keyboardEditDistance("front", "grit")); // output 1.2
        StdOut.println(EditDistance.keyboardEditDistance("coral", "bile")); // output 3.1
        StdOut.println();
        StdOut.println("-------TESTS FOR DOCUMENT SEARCH-------");
        // note: for the map and histogram testing, one must be commented out
        // before running otherwise the histogram will rewrite the map
        DocumentSearch.map("aba", "test.txt", false); // produce 3 pointed star
        // produce histogram with bar heights 3, 6, and 4
        DocumentSearch.histogram("aba", "test.txt", false);
        // produce aba: 3 for distance 0.1 and ab: 6 for distance 1.0
        DocumentSearch.nearMatches("abs", "test.txt", 1, true);
    }
}
