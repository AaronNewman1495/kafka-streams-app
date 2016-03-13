kafka-streams-app
=================

That's a preview feature, so use it at your own risk.

## Prerequisites
Build the trunk of [Apache Kafka](https://github.com/apache/kafka) (0.10)
or use the one by [Confluent](http://www.confluent.io/developer#streamspreview)

### Create Kafka topic
```
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic streams-hashtag-input

bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic streams-hashtag-count-output
```

### Execute producer 
Set up your Twitter credentials at TweetProducer and execute it to store tweets at streams-hashtag-input queue:
```
private static final String CONSUMER_KEY = "";
private static final String CONSUMER_SECRET = "";
private static final String TOKEN = "";
private static final String SECRET = "";
```
### Execute the HashtagJob


### Read the KTable hashtags and counts
```
bin/kafka-console-consumer.sh --zookeeper localhost:2181 \
          --topic streams-hashtag-count-output \
          --from-beginning \
          --formatter kafka.tools.DefaultMessageFormatter \
          --property print.key=true \
          --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
          --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer
```          