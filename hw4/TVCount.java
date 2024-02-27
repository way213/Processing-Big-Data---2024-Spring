import java.io.IOException;
import java.util.List;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.StringReader;
import com.opencsv.CSVReader;

public class TVCount {

    public static class TVCountMapper extends Mapper<Object, Text, Text, IntWritable>{

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

            // split the data via CSVREADER

            StringReader stringReader = new StringReader(value.toString());
            CSVReader reader = new CSVReader(stringReader);
            String[] columns = reader.readNext();

            // declare index of relevant data
            int countryIndex = 1;
            int showNameIndex = 3;
            int voteCountIndex = 6;

            // extract relevant data
            String country = columns[countryIndex];
            String showName = columns[showNameIndex]; 
            int voteCount = Integer.parseInt(columns[voteCountIndex]); 

            // check if the country is what we want (japan, us, france)
            if (country.equals("US") || country.equals("JP") || country.equals("FR")) {

                // Convert country abbriviation into full names:
                if (country.equals("US")) {
                    country = "United States";
                }
                if (country.equals("JP")) {
                    country = "Japan";
                }
                if (country.equals("FR")) {
                    country = "France";
                }

                // write the extract data into our program.
                context.write(new Text(country + " - " + showName), new IntWritable(voteCount));
            }
        }
    }
             


    public static class TVReducer extends Reducer<Text,IntWritable,Text,IntWritable> {
        private IntWritable result = new IntWritable();

        public void reduce(Text key, Iterable<IntWritable> values,Context context) throws IOException, InterruptedException {
            // everything has been done by the Mapper already, simply return and do nothing here.
            for (IntWritable val : values) {
                context.write(key, val);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "tv count");

        job.addFileToClassPath(new Path("opencsv.jar"));
        job.setNumReduceTasks(1);

        job.setJarByClass(TVCount.class);
        job.setMapperClass(TVCountMapper.class);
        job.setReducerClass(TVReducer.class);
        job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
