package bug;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private final ForwardingStream stream;
    private final TestProducer producer;

    public Main(String bootstrapServer) {
        stream = new ForwardingStream(bootstrapServer);
        producer = new TestProducer(bootstrapServer);
    }

    private void run() {
        LOGGER.info("Starting...");
        stream.start();
        producer.sendRecords();
        stream.stop();
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println(Main.class.getSimpleName() + " <bootstrap servers>");
            System.exit(1);
        }

        new Main(args[0]).run();
    }
}
