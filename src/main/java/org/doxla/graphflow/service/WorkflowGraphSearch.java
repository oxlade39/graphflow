package org.doxla.graphflow.service;

import org.doxla.graphflow.domain.graph.index.MyIndices;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.index.IndexManager;

public class WorkflowGraphSearch {

    private GraphDatabaseService graphDatabaseService;

    public WorkflowGraphSearch(GraphDatabaseService graphDatabaseService) {
        this.graphDatabaseService = graphDatabaseService;
    }

    IndexHits<Node> searchNodeIndex(String query) {
        return searchIndex().forNodes(MyIndices.TEXT_INDEX.name()).query(query);
    }

    IndexHits<Relationship> searchRelationshipIndex(String query) {
        return searchIndex().forRelationships(MyIndices.TEXT_INDEX.name()).query(query);
    }

    private IndexManager searchIndex() {
        return graphDatabaseService.index();
    }
}