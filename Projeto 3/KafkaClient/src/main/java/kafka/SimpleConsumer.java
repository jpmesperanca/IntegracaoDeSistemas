package kafka;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class SimpleConsumer {
	public static void main(String[] args) throws Exception {

		// Assign topicName to string variable
		String topicName = "Credits";

		// create instance for properties to access producer configs
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
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		props.put("key.deserializer",
				"org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer",
				"org.apache.kafka.common.serialization.LongDeserializer");

		Consumer<String, Long> consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Collections.singletonList(topicName));

		while (true) {
			ConsumerRecords<String, Long> records = consumer.poll(Duration.ofMillis(Long.MAX_VALUE));
			for (ConsumerRecord<String, Long> record : records) {
				System.out.println(record.key() + " => " + record.value());
			}
		}
		// consumer.close();
	}
}
