package org.doxla.graphflow.domain.graph;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;

import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.doxla.graphflow.domain.graph.MyIndices.TEXT_INDEX;
import static org.doxla.graphflow.domain.graph.MyNodeTypes.POSITION_POINTER;
import static org.doxla.graphflow.domain.graph.MyNodeTypes.WORKFLOW_START_NODE;
import static org.doxla.graphflow.domain.graph.MyRelationshipTypes.CURRENT_POSITION;
import static org.doxla.graphflow.domain.graph.NodeProperties.NODE_ID;
import static org.doxla.graphflow.domain.graph.NodeProperties.NODE_NAME;
import static org.doxla.graphflow.domain.graph.NodeProperties.NODE_TYPE;

public class GraphBuilder {

    private final GraphDatabaseService graphDatabaseService;
    private final UUID id = randomUUID();

    private NodeCache nodeCache = new NodeCache();
    private Index<Node> nodeIndex;
    private Node startNode;
    private RelationshipBuilder relationshipBuilder;

    public GraphBuilder(GraphDatabaseService graphDatabaseService) {
        this.graphDatabaseService = graphDatabaseService;
        nodeIndex = graphDatabaseService.index().forNodes(TEXT_INDEX.name(), TEXT_INDEX.configuration());
        relationshipBuilder = new RelationshipBuilder(graphDatabaseService, nodeCache, id);
    }

    public GraphBuilder node(String name) {
        Node existingNode = nodeCache.getNode(name);
        if (existingNode == null) {
            workflowNode(name);
        }
        return this;
    }

    public GraphBuilder transition(String from, String to, String... chain) {
        relationshipBuilder.transition(from, to, chain);
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
        setNodeType(node, MyNodeTypes.WORKFLOW_NODE);

        nodeCache.add(nodeName, node);
        return node;
    }

    public Node build() {
        return startNode;
    }

    private void setNodeId(Node node) {
        setIndexedProperty(node, NODE_ID.name(), id.toString());
    }

    private void setNodeType(Node node, MyNodeTypes nodeType) {
        setIndexedProperty(node, NODE_TYPE.name(), nodeType.name());
    }

    private void setIndexedProperty(Node node, String propertyKey, String propertyValue) {
        node.setProperty(propertyKey, propertyValue);
        nodeIndex.add(node, propertyKey, propertyValue);
    }

    private void setName(Node node, String name) {
        setIndexedProperty(node, NODE_NAME.name(), name);
    }
}
