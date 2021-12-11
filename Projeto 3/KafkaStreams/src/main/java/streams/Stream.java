package streams;

import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Stream {

    public static void main(String[] args) throws InterruptedException, IOException {

        String creditsTopic = "Credits";
        String resultsTopic = "Results";
        String paymentsTopic = "Payments";
        Integer TIME_WINDOW_VALUE = 1; // EM MINUTOS <------
        // String dbinfoTopic = "DB Info";

        // 7. Calculate credits per user
        java.util.Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafkastreamsCPU");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> linesCPU = builder.stream(creditsTopic);
        KTable<String, Double> countlinesCPU = linesCPU.mapValues(v -> multiplyJson(v))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double())).reduce(
                        (v1, v2) -> v1 + v2);

        countlinesCPU.mapValues((k, v) -> insertCreditsInClientJson(k,
                v)).toStream().to(resultsTopic,
                        Produced.with(Serdes.String(), Serdes.String()));

        KafkaStreams streamsCPU = new KafkaStreams(builder.build(), props);

        streamsCPU.start();

        // 8. Calculate payments per user
        java.util.Properties props2 = new Properties();
        props2.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafkastreamsPPU");
        props2.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        props2.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props2.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        StreamsBuilder builder2 = new StreamsBuilder();

        KStream<String, String> linesPPU = builder2.stream(paymentsTopic);
        KTable<String, Double> countlinesPPU = linesPPU.mapValues(v -> multiplyJson(v))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double())).reduce(
                        (v1, v2) -> v1 + v2);

        countlinesPPU.mapValues((k, v) -> insertPaymentsInClientJson(k,
                v)).toStream().to(resultsTopic,
                        Produced.with(Serdes.String(), Serdes.String()));

        KafkaStreams streamsPPU = new KafkaStreams(builder2.build(), props2);
        streamsPPU.start();

        // 9. Calculate current balance of client

        // 10. Get the total (i.e., sum of all persons) credits.

        // 11. Get the total payments.

        // 12. Get the total balance.

        // 13. Compute the bill for each client for the last month (use a tumbling time
        // window).
        // ----> Soma dos créditos mas com time window?? <----

        java.util.Properties props13 = new Properties();
        props13.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafkastreamsBwTW"); // Bill with Time Window
        props13.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        props13.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props13.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        StreamsBuilder builder13 = new StreamsBuilder();

        KStream<String, String> lines = builder13.stream(creditsTopic);
        KTable<Windowed<String>, Double> countlines = lines.mapValues(v -> multiplyJson(v))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double()))
                .windowedBy(TimeWindows.of(Duration.ofMinutes(TIME_WINDOW_VALUE)))
                .reduce((v1, v2) -> v1 + v2);

        countlines.toStream((wk, v) -> wk.key()).mapValues((k, v) -> insertCreditsInClientJson(k,
                v)).to(resultsTopic,
                        Produced.with(Serdes.String(), Serdes.String()));

        KafkaStreams streams = new KafkaStreams(builder13.build(), props13);

        streams.start();

        // 14. Get the list of clients without payments for the last two months

        // 15. Get the data of the person with the highest outstanding debt (i.e., the
        // most
        // negative current balance).

        // 16. Get the data of the manager who has made the highest revenue in payments
        // from
        // their clients.

    }

    private static Double multiplyJson(String input) {

        try {

            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(input);

            String amount = (String) obj.get("Amount");
            String conversionRate = (String) obj.get("Currency");

            return Double.parseDouble(amount) * Double.parseDouble(conversionRate);

        } catch (ParseException pe) {
            return 0.0;
        }
    }

    private static String insertCreditsInClientJson(String key, Double value) {

        String json = "{\"schema\":{\"type\":\"struct\",\"fields\":[{\"type\":\"int32\",\"optional\":false,\"field\":\"id\"},{\"type\":\"double\",\"optional\":true,\"field\":\"credits\"}],\"optional\":false},\"payload\":{\"id\":"
                + key + ",\"credits\":" + String.valueOf(value) + "}}";

        return json;
    }

    private static String insertPaymentsInClientJson(String key, Double value) {

        String json = "{\"schema\":{\"type\":\"struct\",\"fields\":[{\"type\":\"int32\",\"optional\":false,\"field\":\"id\"},{\"type\":\"double\",\"optional\":true,\"field\":\"payments\"}],\"optional\":false},\"payload\":{\"id\":"
                + key + ",\"payments\":" + String.valueOf(value) + "}}";

        return json;
    }
}
