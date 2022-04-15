package com.vansn.kafka;

import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.checkerframework.checker.units.qual.C;

import java.time.Duration;
import java.util.*;
import java.util.logging.Handler;

/**
 * @Author vansn
 * @Date 2022/4/5 上午11:34
 * @Version 1.0
 * @Description
 */
public class MsgConsumer1 {
    private final static String TOPIC_NAME = "ISORTMSTP";
    private final static String CONSUMER_GROUP_NAME = "GJYW";

    public static void main(String[] args){
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"42.192.54.61:9092,42.192.54.61:9093");
        //消费分组 名
        props.put(ConsumerConfig.GROUP_ID_CONFIG,CONSUMER_GROUP_NAME);
        //是否自动提交offset
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"true");
        //自动提交offset时间间隔
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,"1000");

        //当消费主题的是一个新的消费组，或者指定offset的消费方式，offset不存在，那么应该如何消费
        //latest(默认) :只消费自己启动之后发送到主题的消息 earliest:第一次从头开始消费，以后按照消费offset记录继续消费，这个需要区别于consumer.seekToBeginning(每次都从头开始消费) */
        //props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        //心跳
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG,1000);
        //故障判定
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,10*1000);
        //一次poll拉取最大数量
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,500);
        /*
        如果两次poll操作间隔超过了这个时间，broker就会认为这个consumer处理能力太弱，
        会将其踢出消费组，将分区分配给别的consumer消费
        */
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 30 * 1000);

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()); KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        KafkaConsumer<String,String> consumer1 = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList(TOPIC_NAME));

        // 消费指定分区
        //consumer.assign(Arrays.asList(new TopicPartition(TOPIC_NAME, 0)));
        //消息回溯消费
        /*consumer.assign(Arrays.asList(new TopicPartition(TOPIC_NAME, 0)));
          consumer.seekToBeginning(Arrays.asList(new TopicPartition(TOPIC_NAME, 0)));*/
        //指定offset
        //指定offset消费
         /*consumer.assign(Arrays.asList(new TopicPartition(TOPIC_NAME, 0)));
            consumer.seek(new TopicPartition(TOPIC_NAME, 0), 10);*/

         //从指定时间点开始消费
         /*List<PartitionInfo> topicPartitions = consumer.partitionsFor(TOPIC_NAME);
         //从1小时前开始消费
         long fetchDataTime = new Date().getTime() ‐ 1000 * 60 * 60;
         Map<TopicPartition, Long> map = new HashMap<>();
         for (PartitionInfo par : topicPartitions) {
         map.put(new TopicPartition(topicName, par.partition()), fetchDataTime);
         }
         Map<TopicPartition, OffsetAndTimestamp> parMap = consumer.offsetsForTimes(map);
         for (Map.Entry<TopicPartition, OffsetAndTimestamp> entry : parMap.entrySet()) {
         TopicPartition key = entry.getKey();
         OffsetAndTimestamp value = entry.getValue();
         if (key == null || value == null) continue;
         Long offset = value.offset();
         System.out.println("partition‐" + key.partition() + "|offset‐" + offset);
         System.out.println();
         //根据消费里的timestamp确定offset
         if (value != null) {
         consumer.assign(Arrays.asList(key));
         consumer.seek(key, offset);
         }
         }*/
        while(true){
            ConsumerRecords<String,String> records = consumer1.poll((Duration.ofMillis(1000)));
            for(ConsumerRecord<String,String> record:records){
                System.out.printf("收到消息：partition = %d,offset = %d,key = %s,value = %s%n",
                        record.partition(),record.offset(),record.key(),record.value());


            }

        }


    }
}
