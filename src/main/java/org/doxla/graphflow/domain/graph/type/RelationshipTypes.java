package org.doxla.graphflow.domain.graph.type;

import org.neo4j.graphdb.RelationshipType;

public enum RelationshipTypes implements RelationshipType {
    CURRENT_POSITION,
    TRANSITIONS_TO
}
