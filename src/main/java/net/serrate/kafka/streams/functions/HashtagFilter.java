package net.serrate.kafka.streams.functions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.kafka.streams.kstream.Predicate;

/**
 * Created by mserrate on 13/03/16.
 */
public class HashtagFilter implements Predicate<String, JsonNode> {
    public boolean test(String key, JsonNode value) {
        JsonNode hashtags = value.path("entities").path("hashtags");
        if (!hashtags.isMissingNode()) {
            ArrayNode hashtagsNode = (ArrayNode) value.path("entities").path("hashtags");
            return hashtagsNode.size() > 0;
        } else {
            return false;
        }
    }
}
