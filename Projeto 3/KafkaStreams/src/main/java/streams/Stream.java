package streams;

import java.io.IOException;
import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Stream {

    public static void main(String[] args) throws InterruptedException, IOException {

        String creditsTopic = "Credits";
        String resultsTopic = "Results";
        String paymentsTopic = "Payments";
        // String dbinfoTopic = "DB Info";

        // Calculate credits per user
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

        countlines.mapValues((k, v) -> insertCreditsInClientJson(k,
                v)).toStream().to(resultsTopic,
                        Produced.with(Serdes.String(), Serdes.String()));

        KafkaStreams streams = new KafkaStreams(builder.build(), props);

        streams.start();

        java.util.Properties props2 = new Properties();
        props2.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafkastreamsPPU");
        props2.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        props2.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props2.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        StreamsBuilder builder2 = new StreamsBuilder();

        // Calculate payments per user
        KStream<String, String> linesPPU = builder2.stream(paymentsTopic);
        KTable<String, Double> countlinesPPU = linesPPU.mapValues(v -> multiplyJson(v))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double())).reduce(
                        (v1, v2) -> v1 + v2);

        countlinesPPU.mapValues((k, v) -> insertPaymentsInClientJson(k,
                v)).toStream().to(resultsTopic,
                        Produced.with(Serdes.String(), Serdes.String()));

        KafkaStreams streamsPPU = new KafkaStreams(builder2.build(), props2);
        streamsPPU.start();
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
