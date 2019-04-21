package com.sparkTutorial.custom;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;


public class RddDatasetConversionTest {

    public static void main(String[] args) throws Exception {
        cacheAnalysis();
    }
    
    private static void cacheAnalysis(){
        Logger.getLogger("org").setLevel(Level.ERROR);
        SparkConf conf = new SparkConf().setAppName("StackOverFlowSurvey").setMaster("local[1]");

        JavaSparkContext sc = new JavaSparkContext(conf);

        SparkSession session = SparkSession.builder().appName("StackOverFlowSurvey").master("local[1]").getOrCreate();

        JavaRDD<String> lines = sc.textFile("in/cacheTest1.dat");

        JavaRDD<OutputParameters> responseRDD = lines.map(line -> processLine(line));
        Dataset<OutputParameters> responseDataset = session.createDataset(responseRDD.rdd(), Encoders.bean(OutputParameters.class));

        System.out.println("=== Print out schema ===");
        responseDataset.printSchema();

        System.out.println("=== Print 20 records of responses table ===");
        responseDataset.show(20);

        JavaRDD<OutputParameters> responseJavaRDD = responseDataset.toJavaRDD();
        for (OutputParameters response : responseJavaRDD.collect()) {
            System.out.println(response);
        }


        responseDataset.groupBy("accNum").count().show();
    }

    private static OutputParameters processLine(final String line) {
        String apiName = line.split("\\|")[0].split("\\^P")[2];
        String corrId = line.split("\\|")[0].split("\\^P")[3];
        String dateTime = line.split("\\|")[0].split("\\^P")[6];
        String accNum = line.split("\\|")[0].split("\\^P")[11].split("\\:")[1];
        String channelName = line.split("\\|")[2].split("\\:")[1];

        return new OutputParameters(apiName, accNum, dateTime, corrId, channelName);
    }
}
