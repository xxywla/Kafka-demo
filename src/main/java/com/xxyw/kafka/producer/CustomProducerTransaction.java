package com.xxyw.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class CustomProducerTransaction {

    public static void main(String[] args) {

        // 0 配置
        Properties properties = new Properties();

        // 链接集群 "bootstrap.servers"
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "hadoop102:9092,hadoop103:9092");

        // 指定对应key和value的序列化类型 "key.serializer"
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // 指定全局唯一的事务id
        properties.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "transaction_id_01");

        // 1.创建kafka生产者对象
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);

        // 初始化事务
        kafkaProducer.initTransactions();

        // 开启事务
        kafkaProducer.beginTransaction();

        try {
            // 2. 发送数据
            for (int i = 0; i < 5; i++) {
                kafkaProducer.send(new ProducerRecord<>("first", "shangguigu" + i));
            }

            // 提交事务
            kafkaProducer.commitTransaction();

        } catch (Exception e) {

            // 终止事务
            kafkaProducer.abortTransaction();

        } finally {
            // 3. 关闭资源
            kafkaProducer.close();
        }


    }
}
