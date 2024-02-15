import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;

public class WordCountMapper extends Mapper<Object, Text, Text, IntWritable>{
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        // split the data
        String line = value.toString();
        String[] columns = line.split(",");


        if(columns.length > 8) { // Ensure there are enough columns
            String date = columns[0].replace("\"", "");
            String year = date.substring(0,4);

            try {
                IntWritable avgDeathCount = new IntWritable(Integer.parseInt(columns[8].replace("\"", "")));
                context.write(new Text(year), avgDeathCount);
            }
            catch (NumberFormatException e) {
            System.err.println("Error parsing integer from column 8: " + columns[8]);
            }
        }
    }
}
