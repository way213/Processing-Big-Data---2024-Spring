import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UniqueRecsMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    private static final IntWritable one = new IntWritable(1);
    private Text word = new Text();

    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
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

        // Now fields list should have correctly parsed CSV fields
        // You can then access the fields as needed, considering the correct indexes
        if(fields.size() > 5) { // Make sure you have all your columns
            word.set("ARREST_DATE:" + fields.get(1));
            context.write(word, one);
            word.set("PD_DESC:" + fields.get(3)); // Assuming 'PD_DESC' is now correctly indexed
            context.write(word, one);
            word.set("OFNS_DESC:" + fields.get(5));
            context.write(word, one);
        }
    }
}
