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
		Integer TIME_WINDOW_VALUE = 2; // EM MINUTOS <------
		// String dbinfoTopic = "DB Info";

		// 7. Calculate credits per user
		java.util.Properties props = new Properties();
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafkastreamsCPU");
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,
				Serdes.String().getClass());
		props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,
				Serdes.String().getClass());
		StreamsBuilder builder = new StreamsBuilder();

		KStream<String, String> linesCPU = builder.stream(creditsTopic);
		KTable<String, Double> countlinesCPU = linesCPU.mapValues(v -> multiplyJson(v))
				.groupByKey(Grouped.with(Serdes.String(), Serdes.Double())).reduce((v1, v2) -> v1 + v2);

		countlinesCPU.mapValues((k, v) -> insertCreditsInClientJson(k,
				v)).toStream().to(resultsTopic,
						Produced.with(Serdes.String(), Serdes.String()));

		KafkaStreams streamsCPU = new KafkaStreams(builder.build(), props);

		streamsCPU.start();

		// 8. Calculate payments per user
		java.util.Properties props2 = new Properties();
		props2.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafkastreamsPPU");
		props2.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		props2.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,
				Serdes.String().getClass());
		props2.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,
				Serdes.String().getClass());
		StreamsBuilder builder2 = new StreamsBuilder();

		KStream<String, String> linesPPU = builder2.stream(paymentsTopic);
		KTable<String, Double> countlinesPPU = linesPPU.mapValues(v -> multiplyJson(v))
				.groupByKey(Grouped.with(Serdes.String(), Serdes.Double())).reduce((v1, v2) -> v1 + v2);

		countlinesPPU.mapValues((k, v) -> insertPaymentsInClientJson(k,
				v)).toStream().to(resultsTopic,
						Produced.with(Serdes.String(), Serdes.String()));

		KafkaStreams streamsPPU = new KafkaStreams(builder2.build(), props2);
		streamsPPU.start();

		// 9. Calculate current balance of client

		java.util.Properties props3 = new Properties();
		props3.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafkastreamsBPU");
		props3.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		props3.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,
				Serdes.String().getClass());
		props3.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,
				Serdes.String().getClass());

		StreamsBuilder builder3 = new StreamsBuilder();

		KStream<String, String> linesBPUCredits = builder3.stream(creditsTopic);
		KStream<String, String> linesBPUPayments = builder3.stream(paymentsTopic);

		KStream<String, String> linesBPU = linesBPUCredits.merge(linesBPUPayments);

		KTable<String, Double> countlinesBPUCredits = linesBPU.mapValues(v -> multiplyJson(v))
				.groupByKey(Grouped.with(Serdes.String(), Serdes.Double())).reduce((v1, v2) -> v1 + v2);

		countlinesBPUCredits.mapValues((k, v) -> insertBalanceInClientJson(k,
				v)).toStream().to(resultsTopic,
						Produced.with(Serdes.String(), Serdes.String()));

		KafkaStreams streamsBPU = new KafkaStreams(builder3.build(), props3);

		streamsBPU.start();

		// 10. Get the total (i.e., sum of all persons) credits.

		java.util.Properties props4 = new Properties();
		props4.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafkastreamsTC");
		props4.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		props4.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,
				Serdes.String().getClass());
		props4.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,
				Serdes.String().getClass());
		StreamsBuilder builder4 = new StreamsBuilder();

		KStream<String, String> linesTC = builder4.stream(creditsTopic);
		KTable<String, Double> countlinesTC = linesTC.mapValues(v -> multiplyJson(v))
				.groupBy((k, v) -> "1", Grouped.with(Serdes.String(),
						Serdes.Double()))
				.reduce((v1, v2) -> v1 + v2);

		countlinesTC.mapValues((k, v) -> insertInTopic("\"Total credits\"",
				v)).toStream().to("Totais",
						Produced.with(Serdes.String(), Serdes.String()));

		KafkaStreams streamsTC = new KafkaStreams(builder4.build(), props4);

		streamsTC.start();

		// 11. Get the total payments.

		java.util.Properties props5 = new Properties();
		props5.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafkastreamsTP");
		props5.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		props5.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,
				Serdes.String().getClass());
		props5.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,
				Serdes.String().getClass());
		StreamsBuilder builder5 = new StreamsBuilder();

		KStream<String, String> linesTP = builder5.stream(paymentsTopic);
		KTable<String, Double> countlinesTP = linesTP.mapValues(v -> multiplyJson(v))
				.groupBy((k, v) -> "2", Grouped.with(Serdes.String(),
						Serdes.Double()))
				.reduce((v1, v2) -> v1 + v2);

		countlinesTP.mapValues((k, v) -> insertInTopic("\"Total payments\"", v)).toStream().to("Totais",
				Produced.with(Serdes.String(), Serdes.String()));

		KafkaStreams streamsTP = new KafkaStreams(builder5.build(), props5);

		streamsTP.start();

		// 12. Get the total balance.

		java.util.Properties props6 = new Properties();
		props6.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafkastreamsTB");
		props6.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		props6.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		props6.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		StreamsBuilder builder6 = new StreamsBuilder();

		KStream<String, String> linesTBCredits = builder6.stream(creditsTopic);
		KStream<String, String> linesTBPayments = builder6.stream(paymentsTopic);

		KStream<String, String> linesTB = linesTBCredits.merge(linesTBPayments);

		KTable<String, Double> countlinesTB = linesTB.mapValues(v -> multiplyJson(v))
				.groupBy((k, v) -> "3", Grouped.with(Serdes.String(), Serdes.Double())).reduce((v1, v2) -> v1 + v2);

		countlinesTB.mapValues((k, v) -> insertInTopic("\"Total balance\"", v)).toStream().to("Totais",
				Produced.with(Serdes.String(), Serdes.String()));

		KafkaStreams streamsTB = new KafkaStreams(builder6.build(), props6);

		streamsTB.start();

		// 13. Compute the bill for each client for the last month (use a tumbling time
		// window).

		java.util.Properties props13 = new Properties();
		props13.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafkastreamsBwTW"); // Bill with Time Window
		props13.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		props13.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		props13.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		StreamsBuilder builder13 = new StreamsBuilder();

		KStream<String, String> linesBwTW = builder13.stream(creditsTopic);
		KTable<Windowed<String>, Double> countlinesBwTW = linesBwTW.mapValues(v -> multiplyJson(v))
				.groupByKey(Grouped.with(Serdes.String(), Serdes.Double()))
				.windowedBy(TimeWindows.of(Duration.ofMinutes(TIME_WINDOW_VALUE)))
				.reduce((v1, v2) -> v1 + v2);

		countlinesBwTW.toStream((wk, v) -> wk.key()).mapValues((k, v) -> insertCreditsTimedInClientJson(k,
				v)).to(resultsTopic, Produced.with(Serdes.String(), Serdes.String()));

		KafkaStreams streamsBwTW = new KafkaStreams(builder13.build(), props13);

		streamsBwTW.start();

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

	private static String insertBalanceInClientJson(String key, Double value) {

		String json = "{\"schema\":{\"type\":\"struct\",\"fields\":[{\"type\":\"int32\",\"optional\":false,\"field\":\"id\"},{\"type\":\"double\",\"optional\":true,\"field\":\"balance\"}],\"optional\":false},\"payload\":{\"id\":"
				+ key + ",\"balance\":" + String.valueOf(value) + "}}";

		return json;
	}

	private static String insertInTopic(String key, Double value) {

		String json = "{\"schema\":{\"type\":\"struct\",\"fields\":[{\"type\":\"string\",\"optional\":false,\"field\":\"topic\"},{\"type\":\"double\",\"optional\":true,\"field\":\"value\"}],\"optional\":false},\"payload\":{\"topic\":"
				+ key + ",\"value\":" + String.valueOf(value) + "}}";

		return json;
	}

	private static String insertCreditsTimedInClientJson(String key, Double value) {

		String json = "{\"schema\":{\"type\":\"struct\",\"fields\":[{\"type\":\"int32\",\"optional\":false,\"field\":\"id\"},{\"type\":\"double\",\"optional\":true,\"field\":\"creditstimed\"}],\"optional\":false},\"payload\":{\"id\":"
				+ key + ",\"creditstimed\":" + String.valueOf(value) + "}}";

		return json;
	}
}
