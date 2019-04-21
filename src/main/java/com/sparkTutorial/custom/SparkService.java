package com.sparkTutorial.custom;

import static java.util.stream.Collectors.toList;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;


public class SparkService {

    
    public static void cacheAnalysis() {
        SparkConf sparkConf = new SparkConf().setAppName("cacheAnalysis1").setMaster("local[1]");
        JavaSparkContext context = new JavaSparkContext(sparkConf);
        SparkSession sparkSession = SparkSession.builder().appName("cacheAnalysis1").master("local[1]").getOrCreate();

        JavaRDD<String> lines = context.textFile("in/cacheTest1.dat");

        //Normal one with TimeGaps and their avg.
        /*JavaRDD<FinalOutPutParameters> rddFinal = lines.map(line -> parseLine(line))
                .mapToPair(s -> new Tuple2<>(s.getAccNum(), s.getDateTime()))
                .reduceByKey((Function2<String, String, String>) (a, b) -> a + "|" + b)
                .map(line -> getTimegaps(line));

        rddFinal.collect().stream().forEach(index -> {
            System.out.println(index);
        });*/

        
        //Bucketizing for each hr: [No.of accounts having frequency 1/2/3... ]
        /*JavaPairRDD<Integer, Map<Integer, Integer>> result = lines.map(line -> parseLine(line))
                .mapToPair(s -> {
                    LocalDateTime timeStamp = LocalDateTime
                            .parse(s.getDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    return new Tuple2<>(timeStamp.getHour(), new AccRecord(s.getAccNum(), timeStamp));
                })
                .groupByKey()
                .mapToPair(record -> new Tuple2<>(record._1, processValue(record._2)));*/

        

        Map<Long, Long> pairs = lines.map(line -> parseLine(line))
                .mapToPair(line -> new Tuple2<>(line.getAccNum(),
                        LocalDateTime.parse(line.getDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
                .groupByKey()
                .mapToPair(record -> new Tuple2<>(record._1, getGapTimes(record._2)))
                .flatMap(tpl -> tpl._2.iterator())
                .countByValue();
        
        /*Dataset<OutputParameters> dataset = sparkSession.createDataset(result.rdd(), Encoders.bean(OutputParameters.class));
        
        dataset.printSchema();
                
        dataset.groupBy("accNum").count().show();*/

        //pairs.entrySet().stream().forEach(one -> System.out.println(one.getKey()+"="+one.getValue()));

        long[] countArray = new long[12];
        pairs.entrySet().stream().forEach(entry -> {
            long bucket = entry.getKey() / 300;
            countArray[(int) bucket] = countArray[(int) bucket]+ (Long) entry.getValue();
        });
        
        for (long l : countArray){
            System.out.println(l);
        }
        
    }

    private static Set<Long> getGapTimes(final Iterable<LocalDateTime> localDateTimes) {
        Set<Long> gapTimes = new HashSet<>();
        List<LocalDateTime> array = new ArrayList<>();
        Stream<LocalDateTime> stream = StreamSupport.stream(localDateTimes.spliterator(), false);
        stream.forEach(timeStamp ->{
            array.add(timeStamp);
        });
        Object[] objects = array.toArray();

        Arrays.sort(objects);
        for (int i = 0; i < objects.length - 1; i++) {
            Duration duration = Duration.between((LocalDateTime)objects[i+1], (LocalDateTime)objects[i]);
            long diff = Math.abs(duration.toMillis())/1000; //to convert into Seconds.
            gapTimes.add(diff);
        }
        
        return gapTimes;
    }

    private static Map<Integer, Integer> processValue(final Iterable<AccRecord> accRecords) {
        Stream<AccRecord> stream = StreamSupport.stream(accRecords.spliterator(), false);
        Map<String, List<LocalDateTime>> collect = stream.collect(
                Collectors.groupingBy(record -> record.getAccNum(), Collectors.mapping(record -> record.getTimeStamp(), toList())));
        
        Map<Integer, Integer> returningMap = new HashMap<>();

        collect.entrySet().stream().forEach(entrySet -> {
            returningMap.computeIfPresent(entrySet.getValue().size(), (k,v)-> v+1);
            returningMap.computeIfAbsent(entrySet.getValue().size(), k->1);
        });
        
        return returningMap;
    }

    private static FinalOutPutParameters getTimegaps(final Tuple2<String, String> line) {
        FinalOutPutParameters f = new FinalOutPutParameters();
        String[] splitStr = line._2.split("\\|");
        LocalDateTime[] dateTimes = new LocalDateTime[splitStr.length];
        for (int index=0; index<splitStr.length; index++) {
            dateTimes[index] = LocalDateTime.parse(splitStr[index], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        Arrays.sort(dateTimes);
        Integer minTime = Integer.MAX_VALUE;
        Integer maxTime = Integer.MIN_VALUE;
        for (int i = 0; i < splitStr.length - 1; i++) {
            Duration duration = Duration.between(dateTimes[i+1], dateTimes[i]);
            long diff = Math.abs(duration.toMillis())/1000; //to convert into Seconds.
            if (diff < minTime)
                minTime = (int) diff;

            if (diff > maxTime)
                maxTime = (int) diff;
        }
        f.setAccNum(line._1);
        f.setFrequency(splitStr.length);
        if(splitStr.length>1){
            f.setMinTimeGap(minTime);
            f.setMaxTimeGap(maxTime);
        }else {
            f.setMinTimeGap(0);
            f.setMaxTimeGap(0);
        }
        return f;
    }

    private static OutputParameters parseLine(final String line) {
        String apiName = line.split("\\|")[0].split("\\^P")[2];
        String corrId = line.split("\\|")[0].split("\\^P")[3];
        String dateTime = line.split("\\|")[0].split("\\^P")[6];
        String accNum = line.split("\\|")[0].split("\\^P")[11].split("\\:")[1];
        String channelName = line.split("\\|")[2].split("\\:")[1];

        OutputParameters p = new OutputParameters(apiName, accNum, dateTime, corrId, channelName);

        return p;
    }
    
    public static void main(String[] args) throws Exception {
        /*int sec= 901;
        int res = ((int)sec / 300);
        System.out.println("hash: "+res);*/
        Logger.getLogger("org").setLevel(Level.ERROR);
        cacheAnalysis();
    }

}
