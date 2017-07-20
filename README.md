# kafka Stream Word Count Application

Word Count Algorithm will show the words in a sentence and number of times it have occurred.

## 1. Start zookeeper and then kafka.

## 2. Create kafka topics - topic1 and topic2.

## 3a.Start Word count spring boot application &

  ### Send data to the input topic topic1 using the kafka producer

  > bin/kafka-console-producer.sh --broker-list localhost:9092 --topic topic1  Hola Hola Hello

  ### Consume data from the output topic topic2 using the kafka consumer

  > bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 \
  >     --topic topic2 \
  >     --from-beginning \
  >     --formatter kafka.tools.DefaultMessageFormatter \
  >     --property print.key=true \
  >     --property print.value=true \
  >     --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
  >     --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer

  ### Output format

  >    Hola  2
  >    Hello 1

## 3b.Start User Region Program &

  ### Send data to the input topic topic1 using the kafka producer
  > bin/kafka-console-producer.sh --broker-list localhost:9092 --topic UserRegions --property parse.key=true --property key.separator=,
  >    lakshay,India<ENTER>
  >    ankit,India<ENTER>
  >    manan,India<ENTER>
  >    karna,India<ENTER>
  >    Jack,England<ENTER>
  >    paanshul,India<ENTER>

  ### Consume data from the output topic topic2 using the kafka consumer

  > bin/kafka-console-consumer --topic LargeRegions --from-beginning \
  >       --zookeeper localhost:2181 \
  >       --property print.key=true \
  >       --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer

  ### Output format
  > India, 5






