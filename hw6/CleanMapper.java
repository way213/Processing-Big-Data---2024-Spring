import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CleanMapper extends Mapper<LongWritable, Text, NullWritable, Text> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        List<String> fields = parseCSVLine(line);

        // Define the indices of your columns of interest
        int[] columnsOfInterest = {1, 3, 5}; // Update these indices as needed
        StringBuilder cleanLine = new StringBuilder();

        for (int i = 0; i < columnsOfInterest.length; i++) {
            if (i > 0) cleanLine.append(","); // Add comma between columns, but not before the first column
            int index = columnsOfInterest[i];
            if (index < fields.size()) { // Check to prevent IndexOutOfBoundsException
                cleanLine.append(fields.get(index));
            } else {
                cleanLine.append(""); // Append empty string if the column index is out of bounds
            }
        }

        context.write(NullWritable.get(), new Text(cleanLine.toString()));
    }

    /**
     * Parses a CSV line considering commas within quotes.
     * @param line The input CSV line.
     * @return A List of fields parsed from the line.
     */
    private List<String> parseCSVLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean insideQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == ',' && !insideQuotes) {
                fields.add(currentField.toString());
                currentField = new StringBuilder(); // Start a new field
            } else if (c == '\"') {
                insideQuotes = !insideQuotes; // Toggle the insideQuotes flag
            } else {
                currentField.append(c);
            }
        }
        fields.add(currentField.toString()); // Add the last field

        return fields;
    }
}
