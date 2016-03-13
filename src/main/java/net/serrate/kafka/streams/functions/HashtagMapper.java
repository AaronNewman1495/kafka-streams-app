package net.serrate.kafka.streams.functions;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KeyValueMapper;

/**
 * Created by mserrate on 13/03/16.
 */
public class HashtagMapper implements KeyValueMapper<String, String, KeyValue<String, String>> {
    public KeyValue<String, String> apply(String key, String value) {
        return new KeyValue<String, String>(value, value);
    }
}
