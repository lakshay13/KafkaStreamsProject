package com.suri.kstreams.WordCountKafkaStream;

import java.util.Arrays;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.stereotype.Component;

/**
 *
 * Steps
 * 1. Start zookeeper and then kafka.
 * 2. Create kafka topics - topic1 and topic2
 * 3. Start spring boot application
 * 4. Send data to the input topic topic1 using the kafka producer
 * 5. Consume data from the output topic topic2 using the kafka consumer
 * 6. Result will be seen as key and value where key is the word and value is the number of times the word occurred.
 *
 * @author lakshay13@gmail.com
 */
@Component("wordCountStream")
public class WordCountStream {

    private String topic = "topic1";
    private KafkaStreams streams;

    @PostConstruct
    public void runStream() {
        Serde<String> stringSerde = Serdes.String();

        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-streams-word-count-example");
        config.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, stringSerde.getClass().getName());
        config.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, stringSerde.getClass().getName());
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(StreamsConfig.ZOOKEEPER_CONNECT_CONFIG, "localhost:2181");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KStreamBuilder builder = new KStreamBuilder();


        // Step 1: Application reads the messages from the input topic (topic1).
        KStream<String, String> textLines = builder.stream(topic);

        // Step 2: Performs the computations (Word Count algorithm).
        KTable<String, Long> wordCounts = textLines
                .flatMapValues(textLine -> Arrays.asList(textLine.toLowerCase().split("\\W+")))
                .groupBy((key, word) -> word)
                .count("Counts");

        // Step 3: Continously writes the current results to the output topic (topic2).
        wordCounts.to(Serdes.String(), Serdes.Long(), "topic2");

        KafkaStreams streams = new KafkaStreams(builder, config);
        streams.start();
    }

    @PreDestroy
    public void closeStream() {
        streams.close();
    }
}
