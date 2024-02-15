import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

public class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

    public void reduce(Text key, Iterable<IntWritable> values, Context context) 
            throws IOException, InterruptedException {
        int maxDeathCount = 0; // Initialize with a value that will be lower than any death count.
        boolean hasValues = false; // Flag to check if there are values for the key.

        // Iterate through all values to find the max death count for the key (year).
        for (IntWritable val : values) {
            maxDeathCount = Math.max(maxDeathCount, val.get());
            hasValues = true; // Set flag to true as there are values.
        }

        // If the year is 2019 and no values are found, explicitly write 2019 with a death count of 0.
        if ("2019".equals(key.toString()) && !hasValues) {
            context.write(new Text("2019"), new IntWritable(0));
        } else {
            // For other years or if values exist, write the year and its maximum death count to the context.
            context.write(key, new IntWritable(maxDeathCount));
        }
    }
}
