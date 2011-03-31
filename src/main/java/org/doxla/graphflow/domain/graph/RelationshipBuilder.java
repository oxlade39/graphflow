package org.doxla.graphflow.domain.graph;

import org.doxla.graphflow.domain.graph.type.RelationshipProperties;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.RelationshipIndex;

import java.util.Iterator;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.doxla.graphflow.domain.graph.index.MyIndices.TEXT_INDEX;
import static org.doxla.graphflow.domain.graph.type.RelationshipTypes.TRANSITIONS_TO;

public class RelationshipBuilder {
    private final NodeCache nodeCache;
    private final UUID graphId;
    private final RelationshipIndex relationshipIndex;


    public RelationshipBuilder(GraphDatabaseService graphDatabaseService, NodeCache nodeCache, UUID graphId) {
        this.nodeCache = nodeCache;
        this.graphId = graphId;
        relationshipIndex = graphDatabaseService.index().forRelationships(TEXT_INDEX.name(), TEXT_INDEX.configuration());
    }

    public RelationshipBuilder transition(String from, String to, String... chain) {
        Node toNode = nodeCache.named(to);
        createRelationship(from, toNode);
        Iterator<String> chainAsList = asList(chain).iterator();
        Node prev = toNode;
        Node next;
        while (chainAsList.hasNext()) {
            next = nodeCache.named(chainAsList.next());
            prev.createRelationshipTo(next, TRANSITIONS_TO);
            prev = next;
        }
        return this;
    }

    private void createRelationship(String from, Node toNode) {
        Relationship relationship = nodeCache.named(from).createRelationshipTo(toNode, TRANSITIONS_TO);
        String property = RelationshipProperties.RELATIONSHIP_ID;
        relationship.setProperty(property, graphId.toString());
        relationshipIndex.add(relationship, property, graphId.toString());
    }
}
