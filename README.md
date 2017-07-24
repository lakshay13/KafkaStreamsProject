# kafka Stream Word Count, User Region Count & Anomaly Detection Application

  > Word Count Algorithm will show the words in a sentence and number of times it have occurred.
  > User Region Algorithm will show the regions and number of times they have occurred (atleast 2 times, all regions whose occurrence is less than 2 are not displayed).
  > Anomaly Detection algo will detect and print the names of the user if the user has accessed 3 times or more within a windowed time of 1 minute.

## 1. Start zookeeper and then kafka.

## 2. Create kafka topics - topic1 and topic2.

## 3a.Start Word count spring boot application &

  ### Send data to the input topic topic1 using the kafka producer

  > ./kafka-console-producer.sh --broker-list localhost:9092 --topic topic1 <br/>
   Hola<br/>
   Hola<br/>
   Hello<br/>

  ### Consume data from the output topic topic2 using the kafka consumer

  > ./kafka-console-consumer.sh --bootstrap-server localhost:9092 \
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
  > ./kafka-console-producer.sh --broker-list localhost:9092 --topic topic1 --property parse.key=true --property key.separator=, <br/>
  >    lakshay,India<ENTER><br/>
  >    ankit,India<ENTER><br/>
  >    manan,India<ENTER><br/>
  >    karna,India<ENTER><br/>
  >    Jack,England<ENTER><br/>
  >    paanshul,India<ENTER><br/>

  ### Consume data from the output topic topic2 using the kafka consumer

  > ./kafka-console-consumer.sh --topic topic2 --from-beginning \
  >       --zookeeper localhost:2181 \
  >       --property print.key=true \
  >       --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer

  ### Output format
  > India, 5

## 3c.Start Anomaly Detection Program &

  ### Send data to the input topic topic1 using the kafka producer
    > ./kafka-console-producer.sh --broker-list localhost:9092 --topic topic1
    >    lakshay
    >    lakshay
    >    lakshay
    >    johnson
    >    johnson
    >    morkel

  ### Consume data from the output topic topic2 using the kafka consumer
    > ./kafka-console-consumer.sh --topic topic2 --from-beginning --new-consumer --bootstrap-server localhost:9092
     --property print.key=true --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer

  ### Output format
    > lakshay 3



