import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.NullWritable;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Clean {
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: Clean <input path> <output path>");
            System.exit(-1);
        }

        Job job = Job.getInstance(new Configuration());
        job.setJarByClass(Clean.class);
        job.setJobName("Data Cleaning");

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

// Set the job's output key and value classes (final output)
job.setOutputKeyClass(Text.class);
job.setOutputValueClass(IntWritable.class);

// Set the Mapper's output key and value classes
job.setMapOutputKeyClass(NullWritable.class);
job.setMapOutputValueClass(Text.class);

// Set Mapper and Reducer classes
job.setMapperClass(CleanMapper.class);
job.setReducerClass(CleanReducer.class); // Ensure this matches or is logically consistent with Mapper output

// If the Reducer is also supposed to emit NullWritable keys and Text values, adjust the final output types accordingly
// job.setOutputKeyClass(NullWritable.class);
// job.setOutputValueClass(Text.class);


        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
