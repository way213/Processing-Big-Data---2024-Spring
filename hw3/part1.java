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
        String path = "COVID-19_Daily_Counts_of_Cases__Hospitalizations__and_Deaths_20240212.csv";
        Map<String, Integer> yearToAvgDeathCount = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            boolean isFirstLine = true; // To skip the header row
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip the first line assuming it's the header
                    continue;
                }
                String[] values = line.split(",");
                String date = values[0];
                // get the year via slicing the string 
                String year = date.substring(date.length()-4);

                Integer avgDeathCount = Integer.parseInt(values[8]);
                yearToAvgDeathCount.put(year, avgDeathCount);
                // Improved print statement for clarity
            }
        } catch (IOException e) {
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
        } catch (IOException e) {
            // Handle exceptions related to FileWriter and BufferedWriter
            e.printStackTrace();
        }
    }
}

