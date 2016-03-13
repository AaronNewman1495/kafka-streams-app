package net.serrate.kafka.streams;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import net.serrate.kafka.streams.functions.HashtagFilter;
import net.serrate.kafka.streams.functions.HashtagMapper;
import net.serrate.kafka.streams.functions.HashtagSplitter;
import org.apache.kafka.common.serialization.*;
import org.apache.kafka.connect.json.JsonDeserializer;
import org.apache.kafka.connect.json.JsonSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;

import java.util.*;

/**
 * Created by mserrate on 13/03/16.
 */
public class HashtagJob {
    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.put(StreamsConfig.JOB_ID_CONFIG, "streams-hashtag-processor");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.ZOOKEEPER_CONNECT_CONFIG, "localhost:2181");
        props.put(StreamsConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(StreamsConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(StreamsConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(StreamsConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        // allow us to rerun already processed data
        props.put(StreamsConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KStreamBuilder builder = new KStreamBuilder();

        final Serializer<String> stringSerializer = new StringSerializer();
        final Deserializer<String> stringDeserializer = new StringDeserializer();
        final Serializer<Long> longSerializer = new LongSerializer();
        final Deserializer<Long> longDeserializer = new LongDeserializer();
        final Deserializer<JsonNode> jsonDeserializer = new JsonDeserializer();

        KStream<String, JsonNode> source = builder.stream(stringDeserializer, jsonDeserializer, "streams-hashtag-input");

        KTable<String, Long> counts = source
                .filter(new HashtagFilter())
                .flatMapValues(new HashtagSplitter())
                .map(new HashtagMapper())
                .countByKey(stringSerializer, longSerializer, stringDeserializer, longDeserializer, "Counts");

        counts.to("streams-hashtag-count-output", stringSerializer, longSerializer);


        KafkaStreams streams = new KafkaStreams(builder, props);
        streams.start();

        Thread.sleep(600000);

        streams.close();
    }
}