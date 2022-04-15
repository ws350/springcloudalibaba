package com.vansn.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import com.alibaba.fastjson.JSON;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @Author vansn
 * @Date 2022/4/5 上午9:50
 * @Version 1.0
 * @Description
 */
public class MsgProducer {
    private final static String TOPIC_NAME = "ISORTMSTP";

    public static void main(String[] args) throws InterruptedException {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"42.192.54.61:9092,42.192.54.61:9093");
        //ACKS:0  1  2
        props.put(ProducerConfig.ACKS_CONFIG,"1");
        //失败重试
        props.put(ProducerConfig.RETRIES_CONFIG,3);
        //重试间隔
        props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG,300);
        //本地缓冲区  默认32mb
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG,33554432);
        //批量发送大小
        props.put(ProducerConfig.BATCH_SIZE_CONFIG,16384);
        //默认值是0，意思就是消息必须立即被发送，但这样会影响性能
        //43 一般设置10毫秒左右，就是说这个消息发送完后会进入本地的一个batch，如果10毫秒内，这个batch满了16kb就会随batch一起被发送出去
        //44 如果10毫秒内，batch没满，那么也必须把消息发送出去，不能让消息的发送延迟时间太长
        props.put(ProducerConfig.LINGER_MS_CONFIG,10);
        //把发送的key从字符串序列化为字节数组
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        ////把发送消息value从字符串序列化为字节数组
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        Producer<String,String> producer = new KafkaProducer<String, String>(props);
        int msgNum = 5;
        final CountDownLatch countDownLatch = new CountDownLatch(5);
        for(int i=1;i<=msgNum;i++){
            Order order = new Order(i,100+i,1,1000.00);
            //指定分区
            //ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(TOPIC_NAME, 0, order.getOrderId().toString(), JSON.toJSONString(order));
            //不指定分区
            ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(TOPIC_NAME, order.getOrderId().toString(), JSON.toJSONString(order));

            //等待消息发送成功的同步阻塞方法
            //RecordMetadata metadata = producer.send(producerRecord).get();

            producer.send(producerRecord, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception e) {
                    if(e != null){
                        System.out.println("异步发送消息失败"+ e.getMessage());
                    }
                    if(metadata != null){
                        System.out.println("异步发送消息结果： topic -"+metadata.topic()
                                +" "+ "|partition‐"
                                 + metadata.partition() + "|offset‐" + metadata.offset());
                    }
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await(5, TimeUnit.SECONDS);
        producer.close();
    }

}
