import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;

import java.io.IOException;
import java.util.*; 
import java.util.HashMap;
import java.util.Map;
import java.io.File;  


public class part1 {
    
    public static void main(String[] args) {
        String path = "covid.csv";

        // hashmap to return all the relevant data from the csv file 
        Map<String, Integer> yearToAvgDeathCount = new HashMap<>();
        // open file
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            boolean isFirstLine = true; // To skip the header row
            // read the document line by line
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
            
                String[] values = line.split(",");
                String date = values[0].replace("\"", "");
                String year = date.substring(0, 4);
            
                try {
                    Integer avgDeathCount = Integer.parseInt(values[8].replace("\"", ""));
                    yearToAvgDeathCount.merge(year, avgDeathCount, Integer::max); // This line is key
                } 
                catch (NumberFormatException e) {
                    System.err.println("Error parsing number from line: " + line);
                }
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        }


        // Process to find the highest value for each key

        HashMap<String, Integer> highestValues = new HashMap<>();
        for (Map.Entry<String, Integer> entry : yearToAvgDeathCount.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            // Update the map with the highest value for each key
            highestValues.merge(key, value, Integer::max);
        }

        // Output the result using BufferedWriter
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("results.txt"))) {
            for (Map.Entry<String, Integer> entry : highestValues.entrySet()) {
                writer.write(entry.getKey() + ": " + entry.getValue() + "\n");
            }
            writer.write("2019: 0");
        } catch (IOException e) {
            // Handle exceptions related to FileWriter and BufferedWriter
            e.printStackTrace();
        }
    }
}

