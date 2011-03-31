package org.doxla.graphflow.domain.graph;

import org.doxla.graphflow.domain.graph.type.NodeTypes;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;

import java.util.UUID;

import static org.doxla.graphflow.domain.graph.index.MyIndices.TEXT_INDEX;
import static org.doxla.graphflow.domain.graph.type.NodeProperties.*;
import static org.doxla.graphflow.domain.graph.type.NodeTypes.POSITION_POINTER;
import static org.doxla.graphflow.domain.graph.type.NodeTypes.WORKFLOW_START_NODE;
import static org.doxla.graphflow.domain.graph.type.RelationshipTypes.CURRENT_POSITION;

public class GraphBuilder {

    private final GraphDatabaseService graphDatabaseService;
    private final UUID id;

    private final NodeCache nodeCache;
    private Index<Node> nodeIndex;
    private Node startNode;

    public GraphBuilder(GraphDatabaseService graphDatabaseService, NodeCache nodeCache, UUID id) {
        this.graphDatabaseService = graphDatabaseService;
        this.nodeCache = nodeCache;
        this.id = id;
        nodeIndex = graphDatabaseService.index().forNodes(TEXT_INDEX.name(), TEXT_INDEX.configuration());
    }

    public GraphBuilder node(String name) {
        Node existingNode = nodeCache.getNode(name);
        if (existingNode == null) {
            workflowNode(name);
        }
        return this;
    }

    public GraphBuilder startsIn(String startNodeName) {
        startNode = nodeCache.named(startNodeName);
        setNodeType(startNode, WORKFLOW_START_NODE);
        positionPointer().createRelationshipTo(startNode, CURRENT_POSITION);
        return this;
    }

    private Node positionPointer() {
        Node positionPointer = graphDatabaseService.createNode();

        setNodeId(positionPointer);
        setNodeType(positionPointer, POSITION_POINTER);

        return positionPointer;
    }

    private Node workflowNode(String nodeName) {
        Node node = graphDatabaseService.createNode();

        setNodeId(node);
        setName(node, nodeName);
        setNodeType(node, NodeTypes.WORKFLOW_NODE);

        nodeCache.add(nodeName, node);
        return node;
    }

    public Node build() {
        return startNode;
    }

    private void setNodeId(Node node) {
        setIndexedProperty(node, NODE_ID, id.toString());
    }

    private void setNodeType(Node node, String nodeType) {
        setIndexedProperty(node, NODE_TYPE, nodeType);
    }

    private void setIndexedProperty(Node node, String propertyKey, String propertyValue) {
        node.setProperty(propertyKey, propertyValue);
        nodeIndex.add(node, propertyKey, propertyValue);
    }

    private void setName(Node node, String name) {
        setIndexedProperty(node, NODE_NAME, name);
    }
}
