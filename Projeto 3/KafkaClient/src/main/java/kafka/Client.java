package kafka;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

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
        String dbClients = "DBInfoClients";
        String dbCurr = "DBInfoCurr";
        // String paymentsTopic = "Payments";

        // Create consumers
        Properties propsConsumer = createPropertiesConsumer();

        Consumer<String, String> consumerCurr = new KafkaConsumer<>(propsConsumer);
        consumerCurr.subscribe(Collections.singletonList(dbCurr));

        Consumer<String, String> consumerClients = new KafkaConsumer<>(propsConsumer);
        consumerClients.subscribe(Collections.singletonList(dbClients));

        // Create Producers
        // TODO - Add payments
        Properties propsProducer = createPropertiesProducer();
        Producer<String, String> producer = new KafkaProducer<>(propsProducer);

        Scanner scan = new Scanner(System.in);
        int num;

        List<String> clients = new ArrayList<>();
        List<String> currencies = new ArrayList<>();

        do {

            System.out.println("1. Generate Credits");
            System.out.println("2. Generate Payments");
            System.out.println("3. Update Client List");
            System.out.println("4. Update Currency List");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            num = scan.nextInt();

            switch (num) {
                case 1:
                    generateCredits(producer, clients, creditsTopic);
                    break;

                case 2:
                    break;

                case 3:
                    clients = updateInfo(consumerClients, clients);
                    for (String c : clients)
                        System.out.println(c);
                    break;

                case 4:

                    currencies = updateInfo(consumerCurr, currencies);
                    for (String c : currencies)
                        System.out.println(c);

                    break;

                default:
                    break;
            }

        } while (num != 5);

        scan.close();
        consumerClients.close();
        consumerCurr.close();
        producer.close();
    }

    private static void generateCredits(Producer<String, String> producer, List<String> clients, String creditsTopic) {

        if (!clients.isEmpty()) {
            int i = 0;

            for (String client : clients)
                producer.send(new ProducerRecord<String, String>(creditsTopic, Integer.toString(i++), client));
        }
    }

    // TODO - Stays infinite polling if the table is empty.
    private static List<String> updateInfo(Consumer<String, String> consumer, List<String> current) {

        List<String> l = new ArrayList<>();

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

            System.out.println(records.isEmpty());

            // Evita o utilizador ter de esperar pelo próximo successful poll
            if (records.isEmpty() && !current.isEmpty())
                return current;

            // Continua a dar poll ate ter resultados validos.
            if (records.isEmpty() && !l.isEmpty())
                return l;

            long now = System.currentTimeMillis();

            for (ConsumerRecord<String, String> record : records) {

                if (now - record.timestamp() < 60000)
                    l.add(record.value());
            }
        }
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
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

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
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "ClientGenConsumers");

        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        return props;
    }
}
