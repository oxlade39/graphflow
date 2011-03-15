package org.doxla.graphflow.domain.graph;

import org.neo4j.graphdb.RelationshipType;

public enum MyRelationshipTypes implements RelationshipType {
    CURRENT_POSITION,
    TRANSITIONS_TO;
}
