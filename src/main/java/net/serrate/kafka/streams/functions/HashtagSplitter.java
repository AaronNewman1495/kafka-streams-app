package net.serrate.kafka.streams.functions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.kafka.streams.kstream.ValueMapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mserrate on 13/03/16.
 */
public class HashtagSplitter implements ValueMapper<JsonNode, Iterable<String>> {
    public Iterable<String> apply(JsonNode value) {
        ArrayNode hashtagsNode = (ArrayNode) value.path("entities").path("hashtags");
        Iterator<JsonNode> hashtagsIterator = hashtagsNode.elements();
        List<String> hashtags = new ArrayList<String>();
        while (hashtagsIterator.hasNext()) {
            JsonNode hashtagNode = hashtagsIterator.next();
            hashtags.add(hashtagNode.get("text").asText());
        }
        return hashtags;
    }
}
