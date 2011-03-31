package org.doxla.graphflow.domain.graph.index;

import java.util.Map;

import static org.neo4j.helpers.collection.MapUtil.stringMap;

public enum MyIndices {
    TEXT_INDEX {
        public Map<String, String> configuration() {
            return stringMap("provider", "lucene", "type", "fulltext");
        }
    };

    public abstract Map<String, String> configuration();
}
