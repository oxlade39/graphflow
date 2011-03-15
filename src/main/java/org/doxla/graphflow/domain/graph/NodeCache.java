package org.doxla.graphflow.domain.graph;

import org.neo4j.graphdb.Node;

import java.util.HashMap;
import java.util.Map;

public class NodeCache {
    private Map<String, Node> existingNodes = new HashMap<String, Node>();

    public Node named(String name) {
        Node node = getNode(name);
        if (node == null)
            throw new IllegalArgumentException(String.format("No Node by name '%s' has been added", name));
        return node;
    }

    public Node getNode(String name) {
        return existingNodes.get(name);
    }

    public void add(String nodeName, Node node) {
        existingNodes.put(nodeName, node);
    }
}
