package com.suri.kstreams.UserRegionKafkaExample;

import java.util.Properties;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;

/**
 *
 * Steps
 * 1. Start zookeeper and then kafka.
 * 2. Create kafka topics - topic1 and topic2
 * 3. Start spring boot application
 * 4. Send data to the input topic topic1 using the kafka producer where the data sent should be like
 *    lakshay,India
 *    ankit,India
 *    manan,India
 *    karna,India
 *    Jack,England
 *    paanshul,India
 * 5. Consume data from the output topic topic2 using the kafka consumer where the data seen in the output would be like
 *    India, 5
 * 6. Result will be seen as key and value where key is the region name and value is the number of times the region has occured. It will only display
 * the results for the region which has occured more than once.
 *
 * @author lakshay13@gmail.com
 */
public class UserRegionStream {

    public static void main(String[] args) {

        Properties streamsConfiguration = new Properties();
        // streams application is given a unique name.
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "user-region-lambda-example");
        // Kafka broker(s).
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        // ZooKeeper.
        streamsConfiguration.put(StreamsConfig.ZOOKEEPER_CONNECT_CONFIG, "localhost:2181");
        // default serializers/de-serializers for record keys and for record values.
        streamsConfiguration.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        streamsConfiguration.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        final Serde<String> stringSerde = Serdes.String();
        final Serde<Long> longSerde = Serdes.Long();

        KStreamBuilder builder = new KStreamBuilder();

        // user Name (String & key) and region Name (String & value)
        // read from the input queue topic1.
        KTable<String, String> userRegions = builder.table("topic1");

        // Aggregate the user count by region
        KTable<String, Long> regionCounts = userRegions
                .groupBy((userId, region) -> KeyValue.pair(region, region)) // returns a key value pair
                .count("CountsByRegion")
                .filter((regionName, count) -> count >= 2); // filter only those regions with 2 or more user

        KStream<String, Long> regionCountsStream = regionCounts
                .toStream()
                .filter((regionName, count) -> count != null); // only non null record

        // send the stream data to the output queue topic2.
        regionCountsStream.to(stringSerde, longSerde, "topic2");

        KafkaStreams streams = new KafkaStreams(builder, streamsConfiguration);
        streams.start();
    }
}
