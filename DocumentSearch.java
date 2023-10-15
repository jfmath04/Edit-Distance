import java.awt.Color;

/**
 * This file applies edit distance to a document search using a reference word
 * given by the user and a document chosen by the user. It can output all the
 * near matches, provide a visual map, and a histogram of the distance from that
 * reference word to all the words in the document using the distance metric
 * chosen by the user.
 */

public class DocumentSearch {

    // method takes a reference word and filename of document and prints out
    // all words and their count that have distance (metric chosen by user) equal
    // to or smaller than the max threshold value given
    public static void nearMatches(String word, String filename, double max, boolean keyboard) {
        In input = new In(filename);
        // symbol table with distance as the key and another symbol table as the value
        // embedded symbol table for each distance has the words of that distance away
        // with the count of each word as the values
        ST<Double, ST<String, Integer>> count = new ST<Double, ST<String, Integer>>();
        int words = input.readInt(); // number of words in document
        StdOut.printf("Processing %d words...\n", words);
        // loop to go through each word and update symbol table
        for (int i = 0; i < words; i++) {
            String string = input.readString();
            // normalize string - replace all non-letters and send to lowercase
            string = string.replaceAll("[^a-zA-Z]", "").toLowerCase();
            double distance; // distance between the word and the reference
            if (keyboard) distance = EditDistance.keyboardEditDistance(word, string);
            else distance = EditDistance.editDistance(word, string);
            if (distance <= max) { // distance smaller than the specified threshold
                if (!count.contains(distance)) { // create new entry for distance
                    ST<String, Integer> strings = new ST<String, Integer>();
                    strings.put(string, 1);
                    count.put(distance, strings);
                }
                else { // update existing entry for distance
                    ST<String, Integer> strings = count.get(distance);
                    if (strings.contains(string)) {
                        int n = strings.get(string) + 1;
                        strings.put(string, n);
                    }
                    else strings.put(string, 1);
                }
            }
        }
        // print out the near matches
        for (double distance : count.keys()) {
            StdOut.printf("Words of distance %.1f: \n", distance);
            ST<String, Integer> strings = count.get(distance);
            for (String string : strings.keys()) {
                StdOut.printf("%s: %d\n", string, strings.get(string));
            }
            StdOut.println();
        }
    }

    // method takes a reference word and filename of document and draws a
    // histogram (with randomized color) showing the number of words at each
    // distance value (metric given by user) grouped by integer values
    public static void histogram(String word, String filename, boolean keyboard) {
        In input = new In(filename);
        // symbol table with truncated distance as the key and count as the value
        ST<Integer, Integer> count = new ST<Integer, Integer>();
        int words = input.readInt(); // number of words in document
        StdOut.printf("Processing %d words...\n", words);
        int maxDistance = 0; // maximum distance of all the words
        int maxCount = 0; // maximum count of all the distance buckets
        // loop through all words in document to update symbol table
        for (int i = 0; i < words; i++) {
            String string = input.readString();
            string = string.replaceAll("[^a-zA-Z]", "").toLowerCase(); // normalize string
            // truncate distance to group distances into buckets of integer values
            int distance; // truncated distance between the word and the reference
            if (keyboard) distance = (int) EditDistance.keyboardEditDistance(word, string);
            else distance = EditDistance.editDistance(word, string);
            if (distance > maxDistance) maxDistance = distance;
            if (!count.contains(distance)) count.put(distance, 1);
            else {
                int n = count.get(distance);
                if (n + 1 > maxCount) maxCount = n + 1;
                count.put(distance, n + 1);
            }
        }
        // draw histogram
        StdDraw.clear();
        StdDraw.setXscale(-1, maxDistance + 2);
        StdDraw.setYscale(-1, 10);
        for (int i = 0; i <= maxDistance; i++) {
            if (count.get(i) != null) {
                int c = count.get(i); // count for distance i
                double height = 9 * (double) c / maxCount; // height of bar on histogram
                // randomize color (lighter colors)
                StdDraw.setPenColor((int) (55 * StdRandom.uniform()) + 200,
                                    (int) (55 * StdRandom.uniform()) + 200,
                                    (int) (55 * StdRandom.uniform()) + 200);
                // draw rectangle and label with count
                StdDraw.filledRectangle(i + 0.5, height / 2, 0.5, height / 2);
                StdDraw.setPenColor(Color.BLACK);
                StdDraw.rectangle(i + 0.5, height / 2, 0.5, height / 2);
                StdDraw.text(i + 0.5, height + 0.2, Integer.toString(c));
            }
            else StdDraw.line(i, 0, i + 1, 0); // flat line for 0 count
            StdDraw.setPenColor(Color.BLACK);
            StdDraw.text(i, -0.3, Integer.toString(i)); // label distances underneath
        }
        StdDraw.text(maxDistance + 1, -0.3, Integer.toString(maxDistance + 1));
    }

    // method takes a reference word and filename of document and draws a visual
    // map (randomized colors) going around in a circle where the angle is the
    // corresponding position of each word in the document and the radius is the
    // distance (metric given by user) between that word and the reference word
    public static void map(String word, String filename, boolean keyboard) {
        In input = new In(filename);
        StdDraw.clear();
        StdDraw.setXscale(-10, 10);
        StdDraw.setYscale(-10, 10);
        double words = input.readInt(); // number of words in document
        double n = 2 * Math.PI / (words - 1); // unit angle
        // previous point to connect to new point
        double previousx = 0;
        double previousy = 0;
        // loop through all words and draw map
        for (int i = 0; i < words; i++) {
            // randomize color (darker colors)
            StdDraw.setPenColor((int) (200 * StdRandom.uniform()),
                                (int) (200 * StdRandom.uniform()),
                                (int) (200 * StdRandom.uniform()));
            String string = input.readString();
            string = string.replaceAll("[^a-zA-Z]", "").toLowerCase(); // normalize string
            double distance; // distance between the word and the reference
            if (keyboard) distance = EditDistance.keyboardEditDistance(word, string);
            else distance = EditDistance.editDistance(word, string);
            double angle = i * n; // angle from the positive x-axis counter-clockwise
            if (i != 0) {
                StdDraw.line(previousx, previousy, distance * Math.cos(angle),
                             distance * Math.sin(angle));
            }
            previousx = distance * Math.cos(angle);
            previousy = distance * Math.sin(angle);
        }
    }

    // client to use all methods
    public static void main(String[] args) {
        // options for documents
        StdOut.print("Documents available: \n"
                             + "    MLK Jr. \"I have a dream\" Speech (1)\n"
                             + "    Opening Exercises (2)\n"
                             + "    Shape of You by Ed Sheeran (3)\n"
                             + "    Raven by Edgar Allen Poe (4)\n"
                             + "\nEnter the number of the document you'd like to use: ");
        int option1 = StdIn.readInt();
        String document = "";
        if (option1 == 1) document = "dream.txt";
        else if (option1 == 2) document = "opening-exercises.txt";
        else if (option1 == 3) document = "shapeofyou.txt";
        else if (option1 == 4) document = "raven.txt";

        // loop to repeatedly ask what task to do until user exits
        while (true) {
            // options for tasks
            StdOut.print("\nWhat would you like to do? \n"
                                 + "    Find all near matches (1)\n"
                                 + "    Display visual map of distances (2)\n"
                                 + "    Display histogram of distances (3)\n"
                                 + "    Exit (4)\n"
                                 + "\nEnter the number of the option you'd like to do: ");
            int option2 = StdIn.readInt();
            if (option2 == 4) break; // exit loop
            StdOut.print("\nEnter a reference word: ");
            String word = StdIn.readString();
            // ask which distance metric user wants to use
            StdOut.print(
                    "\nWould you like to use the edit distance (1) or the keyboard distance (2)? ");
            int option3 = StdIn.readInt();
            boolean keyboard = true;
            if (option3 == 1) keyboard = false;
            if (option2 == 1) {
                StdOut.print("\nWhat is the maximum distance you'd like (typically under 2): ");
                double max = StdIn.readDouble(); // user-inputted max threshold
                nearMatches(word, document, max, keyboard);
            }
            else if (option2 == 2) map(word, document, keyboard);
            else if (option2 == 3) histogram(word, document, keyboard);
        }
        StdOut.println("\nThank you for using our program!");
    }
}
