import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

public class DistanceCalculator {
    enum State {
        DELIMETER,
        WORD
    }

    private final String word1;
    private final String word2;
    private String fileName;
    private State state;
    private String buff = "";

    private int firstWord1Index = -1;
    private int firstWord2Index = -1;

    private int word1Index;
    private int word2Index;

    private int wordCount = 0;
    private int nearestDistance = Integer.MAX_VALUE;
    private int longestDistance;

    private boolean isCalculationComplete;


    public DistanceCalculator(String fileName, String word1, String word2) {
        this.word1 = word1;
        this.word2 = word2;
        this.fileName = fileName;
        this.isCalculationComplete = false;
        this.state = State.DELIMETER;
    }

    public int getNearestDistance() {
        checkIfResultsAreReady();
        return nearestDistance;
    }

    public int getLongestDistance() {
        checkIfResultsAreReady();
        return longestDistance;
    }

    private void checkIfResultsAreReady() {
        if (!isCalculationComplete) {
            calculateDistance();
        }
    }

    private void calculateDistance() {
        if (word1 == word2) {
            nearestDistance = 0;
            longestDistance = 0;
            isCalculationComplete = true;
            return;
        }

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File(fileName)));

            int symbol;
            while ((symbol = reader.read()) != -1) {
                processSequence((char) symbol);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        calculateCurrentDistance();
        if (firstWord1Index == -1 || firstWord2Index == -1) {
            longestDistance = 0;
            nearestDistance = 0;
        } else {
            longestDistance = Collections.max(Arrays.asList(Math.abs(firstWord1Index - word2Index), Math.abs(firstWord2Index - word1Index))) - 1;
        }
        isCalculationComplete = true;
    }

    //state machine
    private void processSequence(char c) {
        switch (state) {
            case DELIMETER:
                if (c != ' ') {
                    ++wordCount;
                    state = State.WORD;
                    buff = "" + c;
                }
                break;
            case WORD:
                if (c == ' ') {
                    calculateCurrentDistance();
                    state = State.DELIMETER;
                    buff = "";
                } else {
                    buff += c;
                }
                break;
        }
    }

    private void calculateCurrentDistance() {
        if (buff.equals(word1)) {
            word1Index = wordCount;
            if (firstWord1Index == -1) {
                firstWord1Index = word1Index;
            }
        } else if (buff.equals(word2)) {
            word2Index = wordCount;
            if (firstWord2Index == -1) {
                firstWord2Index = word2Index;
            }
        }
        if (firstWord1Index >= 0 && firstWord2Index >= 0 ) {
            int currentDistance = Math.abs(word1Index - word2Index) - 1;
            if (currentDistance < nearestDistance) {
                nearestDistance = currentDistance;
            }
        }
    }
}
