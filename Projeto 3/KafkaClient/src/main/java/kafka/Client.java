package kafka;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class Client {
    public static void main(String[] args) throws Exception {

        String creditsTopic = "Credits";
        // String paymentsTopic = "Payments";
        // String dbinfoTopic = "DB Info";

        Properties propsProducer = createPropertiesProducer();
        Properties propsConsumer = createPropertiesConsumer();

        Producer<String, Long> producer = new KafkaProducer<>(propsProducer);

        for (int i = 0; i < 1000; i++) {
            producer.send(new ProducerRecord<String, Long>(creditsTopic, Integer.toString(i),
                    (long) i));
            if (i % 100 == 0)
                System.out.println("Sending message " + (i + 1) + " to topic " + creditsTopic);
        }

        producer.close();

        Consumer<String, Long> consumer = new KafkaConsumer<>(propsConsumer);
        consumer.subscribe(Collections.singletonList(creditsTopic));

        ConsumerRecords<String, Long> records = consumer.poll(Duration.ofMillis(Long.MAX_VALUE));
        for (ConsumerRecord<String, Long> record : records) {
            System.out.println(record.key() + " => " + record.value());
        }

        consumer.close();
    }

    private static Properties createPropertiesProducer() {

        Properties props = new Properties();

        // Assign localhost id
        props.put("bootstrap.servers", "localhost:9092");
        // Set acknowledgements for producer requests.
        props.put("acks", "all");
        // If the request fails, the producer can automatically retry,
        props.put("retries", 0);
        // Specify buffer size in config
        props.put("batch.size", 16384);
        // Reduce the no of requests less than 0
        props.put("linger.ms", 1);
        // The buffer.memory controls the total amount of memory available to the
        // producer for buffering
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.LongSerializer");

        return props;
    }

    private static Properties createPropertiesConsumer() {

        Properties props = new Properties();

        // Assign localhost id
        props.put("bootstrap.servers", "localhost:9092");
        // Set acknowledgements for producer requests.
        props.put("acks", "all");
        // If the request fails, the producer can automatically retry,
        props.put("retries", 0);
        // Specify buffer size in config
        props.put("batch.size", 16384);
        // Reduce the no of requests less than 0
        props.put("linger.ms", 1);
        // The buffer.memory controls the total amount of memory available to the
        // producer for buffering
        props.put("buffer.memory", 33554432);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "KafkaExampleConsumer");
        // Not sure se precisamos de consultar sempre desde o inicio
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.LongDeserializer");

        return props;
    }
}
