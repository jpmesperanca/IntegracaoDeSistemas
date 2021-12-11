package kafka;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Client {

    public static void main(String[] args) throws Exception {

        String _div = "--------------------------------------------------";
        String _lotsOfWhiteSpaces = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n";

        String creditsTopic = "Credits";
        String dbClients = "DBInfoClients";
        String dbCurr = "DBInfoCurr";
        String paymentsTopic = "Payments";

        // Create consumers
        Properties propsConsumer = createPropertiesConsumer("KafkaConsumerClients");
        Properties propsConsumer2 = createPropertiesConsumer("KafkaConsumerCurr");

        Consumer<String, String> consumerClients = new KafkaConsumer<>(propsConsumer);
        consumerClients.subscribe(Collections.singletonList(dbClients));

        Consumer<String, String> consumerCurr = new KafkaConsumer<>(propsConsumer2);
        consumerCurr.subscribe(Collections.singletonList(dbCurr));

        // Create Producers
        Properties propsProducer = createPropertiesProducer();
        Producer<String, String> producerCredits = new KafkaProducer<>(propsProducer);

        Properties propsProducer2 = createPropertiesProducer();
        Producer<String, String> producerPayments = new KafkaProducer<>(propsProducer2);

        Scanner scan = new Scanner(System.in);
        int num;

        List<String> clients = new ArrayList<>();
        List<String> currencies = new ArrayList<>();

        do {
            System.out.println(_lotsOfWhiteSpaces);
            System.out.println("-------- CLIENT GENERATOR CLI --------");

            System.out.println("1. Generate Credits");
            System.out.println("2. Generate Payments");
            System.out.println("3. Update Client List");
            System.out.println("4. Update Currency List");
            System.out.println();
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            // TODO - crasha se n for numero lol
            num = scan.nextInt();

            switch (num) {
                case 1:
                    System.out.println(_lotsOfWhiteSpaces);
                    generateCredits(producerCredits, clients, currencies, creditsTopic);
                    // Enter to continue
                    scan.nextLine();
                    System.out.println("Press \"ENTER\" to continue...");
                    scan.nextLine();
                    System.out.println(_lotsOfWhiteSpaces);
                    break;

                case 2:
                    System.out.println(_lotsOfWhiteSpaces);
                    generateCredits(producerPayments, clients, currencies, paymentsTopic);
                    // Enter to continue
                    scan.nextLine();
                    System.out.println("Press \"ENTER\" to continue...");
                    scan.nextLine();
                    System.out.println(_lotsOfWhiteSpaces);
                    break;

                case 3:
                    System.out.println(_lotsOfWhiteSpaces);
                    System.out.println("\nUpdating client list.......");
                    clients = updateInfo(consumerClients, clients);
                    System.out.println(_lotsOfWhiteSpaces);
                    System.out.println("\nClient list updated!");
                    // Enter to continue
                    scan.nextLine();
                    System.out.println("Press \"ENTER\" to continue...");
                    scan.nextLine();
                    System.out.println(_lotsOfWhiteSpaces);
                    break;

                case 4:
                    System.out.println(_lotsOfWhiteSpaces);
                    System.out.println("\nUpdating currency list.......");
                    currencies = updateInfo(consumerCurr, currencies);
                    System.out.println(_lotsOfWhiteSpaces);
                    System.out.println("\nCurrency list updated!");
                    // Enter to continue
                    scan.nextLine();
                    System.out.println("Press \"ENTER\" to continue...");
                    scan.nextLine();
                    System.out.println(_lotsOfWhiteSpaces);
                    break;

                default:
                    break;
            }

        } while (num != 5);

        scan.close();
        consumerClients.close();
        consumerCurr.close();
        producerCredits.close();
        producerPayments.close();
    }

    private static void generateCredits(Producer<String, String> producer, List<String> clients,
            List<String> currencies, String topic) {

        if (!clients.isEmpty() && !currencies.isEmpty()) {

            Random rand = new Random();

            for (int i = 0; i < 5; i++) {

                HashMap<String, Object> map = new HashMap<>();

                String client = clients.get(rand.nextInt(clients.size()));
                String curr = currencies.get(rand.nextInt(currencies.size()));
                int amount = rand.nextInt(10000);

                if (topic.equals("Credits"))
                    amount *= -1;

                map.put("Currency", getValueFromJsonString(curr, "conversionrate"));
                map.put("Amount", String.valueOf(amount));

                JSONObject obj = new JSONObject(map);

                System.out.println(getValueFromJsonString(client, "id") + " -> " + amount + " * "
                        + getValueFromJsonString(curr, "conversionrate"));

                producer.send(new ProducerRecord<String, String>(topic, getValueFromJsonString(client, "id"),
                        obj.toString()));
            }
        }
    }

    private static String getValueFromJsonString(String clientJson, String value) {

        try {

            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(clientJson);

            JSONObject payload = (JSONObject) obj.get("payload");

            return String.valueOf(payload.get(value));

        } catch (ParseException pe) {
            return "";
        }
    }

    // TODO - Stays infinite polling if the table is empty.
    // https://stackoverflow.com/questions/36709740/alter-retention-ms-property-for-kafka-topic-deletes-the-old-data
    private static List<String> updateInfo(Consumer<String, String> consumer, List<String> current) {

        List<String> l = new ArrayList<>();

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

            // TODO - Remove? Acho que isto lixa os updates
            // Evita o utilizador ter de esperar pelo próximo successful poll
            if (records.isEmpty() && !current.isEmpty())
                return current;

            // Continua a dar poll ate ter resultados validos.
            if (records.isEmpty() && !l.isEmpty())
                return l;

            long now = System.currentTimeMillis();

            for (ConsumerRecord<String, String> record : records) {

                if (now - record.timestamp() < 20000)
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

    private static Properties createPropertiesConsumer(String grpId) {

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
        props.put(ConsumerConfig.GROUP_ID_CONFIG, grpId);

        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        return props;
    }
}
