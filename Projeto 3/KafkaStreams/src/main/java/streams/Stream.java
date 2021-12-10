package streams;

import java.io.IOException;
import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Stream {

    private static final String creditsTable = "credits";

    public static void main(String[] args) throws InterruptedException, IOException {

        String creditsTopic = "Credits";
        String resultsTopic = "Results";
        // String paymentsTopic = "Payments";
        // String dbinfoTopic = "DB Info";

        java.util.Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafkastreams");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> lines = builder.stream(creditsTopic);
        KTable<String, Double> countlines = lines.mapValues(v -> multiplyJson(v))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double())).reduce(
                        (v1, v2) -> v1 + v2);

        countlines.mapValues(v -> "Total: " + v).toStream().to(resultsTopic,
                Produced.with(Serdes.String(), Serdes.String()));
        // ^^ convert to json

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
        /*
         * System.out.println("Press enter when ready...");
         * System.in.read();
         * 
         * while (true) {
         * ReadOnlyKeyValueStore<String, String> keyValueStore =
         * streams.store(creditsTable,
         * QueryableStoreTypes.keyValueStore());
         * 
         * KeyValueIterator<String, String> range = keyValueStore.range("0", "3");
         * while (range.hasNext()) {
         * KeyValue<String, String> next = range.next();
         * System.out.println("count for " + next.key + ": " + next.value);
         * }
         * range.close();
         * Thread.sleep(30000);
         * }
         */
    }

    private static Double multiplyJson(String input) {

        try {
            System.out.println(input);
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(input);

            String amount = (String) obj.get("Amount");
            String conversionRate = (String) obj.get("Currency");

            System.out.println(amount);
            System.out.println(conversionRate);
            System.out.println(Double.parseDouble(amount) * Double.parseDouble(conversionRate));

            return Double.parseDouble(amount) * Double.parseDouble(conversionRate);

        } catch (ParseException pe) {
            return 0.0;
        }
    }
}
