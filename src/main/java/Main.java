import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

public class Main {

    public static void main(String[] args) {
        DistanceCalculator distanceCalculator = new DistanceCalculator(args[0], args[1], args[2]);
        System.out.println("Nearest distance: " + distanceCalculator.getNearestDistance());
        System.out.println("Longest distance: " + distanceCalculator.getLongestDistance());
    }



}


