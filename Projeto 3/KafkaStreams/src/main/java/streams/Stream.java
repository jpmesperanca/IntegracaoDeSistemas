package streams;

import java.io.IOException;
import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
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

        lines.groupByKey().reduce(
                (oldval, newval) -> oldval + newval, Materialized.as(creditsTable)).toStream().to(resultsTopic);

        // KTable<String, String> credits =
        // credits.mapValues(v -> "" + v)

        // countlines.mapValues(v -> "" + v).toStream().to(resultsTopic,
        // Produced.with(Serdes.String(), Serdes.String()));

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();

        System.out.println("Press enter when ready...");
        System.in.read();

        while (true) {

            ReadOnlyKeyValueStore<String, String> keyValueStore = streams.store(creditsTable,
                    QueryableStoreTypes.keyValueStore());

            KeyValueIterator<String, String> range = keyValueStore.range("1", "6");
            while (range.hasNext()) {
                KeyValue<String, String> next = range.next();
                System.out.println("count for " + next.key + ": " + next.value);
            }
            range.close();

            System.out.println("\n\n\n");
            Thread.sleep(10000);
        }
    }

    private static Long getValueFromJson(String input) {

        try {

            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(input);

            return (Long) obj.get("Amount");

        } catch (ParseException pe) {
            return 0L;
        }
    }
}
