package bug;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class TestProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestProducer.class);

    private final KafkaProducer<String, String> producer;

    public TestProducer(String bootstrapServer) {
        producer = new KafkaProducer<>(createConfig(bootstrapServer), new StringSerializer(), new StringSerializer());
    }

    public void sendRecords() {
        try {
            for (int i = 0; i < 60; i++) {
                producer.send(new ProducerRecord<>("input-topic", "key", "value"));
                LOGGER.info("Sent record");
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            producer.close();
        }

    }

    private Properties createConfig(String bootstrapServer) {
        Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        config.put(ProducerConfig.LINGER_MS_CONFIG, 0);
        return config;
    }
}
