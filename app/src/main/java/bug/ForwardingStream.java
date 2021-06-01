package bug;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Properties;

public class ForwardingStream {

    private final KafkaStreams stream;

    public ForwardingStream(String bootstrapServer) {
        stream = new KafkaStreams(createTopology(), createConfig(bootstrapServer));
    }

    public void start() {
        stream.start();
    }

    public void stop() {
        stream.close();
    }

    private Topology createTopology() {
        StreamsBuilder builder = new StreamsBuilder();

        builder
                .stream("input-topic", Consumed.with(new Serdes.StringSerde(), new Serdes.StringSerde()))
                .to("output-topic", Produced.with(new Serdes.StringSerde(), new Serdes.StringSerde()));

        return builder.build();
    }

    private Properties createConfig(String bootstrapServer) {
        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "forwarding-stream");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        config.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE);
        config.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 10 * 1000);
        config.put(StreamsConfig.producerPrefix(ProducerConfig.LINGER_MS_CONFIG), 5 * 1000);
        config.put(StreamsConfig.producerPrefix(ProducerConfig.BATCH_SIZE_CONFIG), 100 * 1024);
        return config;
    }
}
