package com.xxyw.kafka.producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class CustomProducerCallbackPart {

    public static void main(String[] args) throws InterruptedException {

        // 0 配置
        Properties properties = new Properties();

        // 链接集群 "bootstrap.servers"
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "hadoop102:9092,hadoop103:9092");

        // 指定对应key和value的序列化类型 "key.serializer"
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // 设置分区器
        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "com.xxyw.kafka.producer.MyPartitioner");

        // 1.创建kafka生产者对象
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);

        // 2. 发送数据
        for (int i = 0; i < 50; i++) {
            kafkaProducer.send(new ProducerRecord<>("first", "atguigu" + i), new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (exception == null) {
                        System.out.println("主题：" + metadata.topic() + " 分区：" + metadata.partition());
                    }
                }
            });
            Thread.sleep(5);
        }

        // 3. 关闭资源
        kafkaProducer.close();
    }
}
