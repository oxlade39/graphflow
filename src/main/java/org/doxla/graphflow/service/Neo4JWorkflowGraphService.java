package org.doxla.graphflow.service;

import org.doxla.graphflow.domain.graph.*;
import org.doxla.graphflow.domain.graph.index.MyIndices;
import org.doxla.graphflow.domain.graph.type.NodeTypes;
import org.doxla.graphflow.domain.graph.type.NodeProperties;
import org.doxla.graphflow.domain.graph.type.RelationshipProperties;
import org.doxla.graphflow.domain.workflow.WorkflowGraphBuilder;
import org.doxla.graphflow.domain.workflow.WorkflowStep;
import org.doxla.graphflow.domain.workflow.WorkflowTransition;
import org.doxla.graphflow.domain.workflow.WorkflowGraph;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.IndexHits;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.doxla.graphflow.domain.workflow.WorkflowGraph.nodeToStep;
import static org.doxla.graphflow.domain.workflow.WorkflowStep.step;
import static org.doxla.graphflow.domain.workflow.WorkflowTransitionBuilder.transition;

@Transactional
public class Neo4JWorkflowGraphService implements WorkflowGraphService {
    private final GraphDatabaseService graphDatabaseService;

    public Neo4JWorkflowGraphService(GraphDatabaseService graphDatabaseService) {
        this.graphDatabaseService = graphDatabaseService;
    }

    @Override
    public List<WorkflowStep> listSteps(WorkflowGraph graph) {
        List<WorkflowStep> allSteps = new ArrayList<WorkflowStep>();
        UUID workflowId = graph.getId();
        IndexHits<Node> hits = searchNodeIndex(String.format("%s:%s AND %s:(%s || %s)", NodeProperties.NODE_ID, workflowId,
                NodeProperties.NODE_TYPE, NodeTypes.WORKFLOW_START_NODE, NodeTypes.WORKFLOW_NODE));
        for (Node hit : hits) {
            allSteps.add(nodeToStep(hit));
        }
        return allSteps;
    }

    @Override
    public List<WorkflowTransition> listTransitions(WorkflowGraph graph) {
        ArrayList<WorkflowTransition> transitions = new ArrayList<WorkflowTransition>();
        IndexHits<Relationship> relationshipIndexHits = searchRelationshipIndex(String.format("%s:%s", RelationshipProperties.RELATIONSHIP_ID, graph.getId()));
        for (Relationship relationshipIndexHit : relationshipIndexHits) {
            String from = nodeToStep(relationshipIndexHit.getStartNode()).name();
            String to = nodeToStep(relationshipIndexHit.getEndNode()).name();
            transitions.add(new WorkflowTransition(from, to));
        }
        return transitions;
    }

    @Override
    public void addTransition(WorkflowGraph workflowGraph, WorkflowTransition transition) {
        UUID workflowId = workflowGraph.getId();
        Node from = nodeForIdAndName(workflowId, transition.from()).getSingle();
        IndexHits<Node> toNode = nodeForIdAndName(workflowId, transition.to());
        Node to;
        NodeCache nodeCache = new NodeCache();
        if(toNode.hasNext()) {
            to = toNode.getSingle();
        } else {
            new GraphBuilder(graphDatabaseService, nodeCache, workflowId).node(transition.to()).build();
            to = nodeForIdAndName(workflowId, transition.to()).getSingle();
        }

        nodeCache.add(transition.to(), to);
        nodeCache.add(transition.from(), from);
        new RelationshipBuilder(graphDatabaseService, nodeCache, workflowId).transition(transition.from(), transition.to());
    }

    @Override
    public List<UUID> listGraphs() {
        ArrayList<UUID> uuids = new ArrayList<UUID>();
        IndexHits<Node> nodeIndexHits = searchNodeIndex(String.format("%s:%s", NodeProperties.NODE_TYPE, NodeTypes.WORKFLOW_START_NODE));
        System.out.println("nodeIndexHits = " + nodeIndexHits.size());
        for (Node nodeIndexHit : nodeIndexHits) {
            uuids.add(UUID.fromString((String) nodeIndexHit.getProperty(NodeProperties.NODE_ID)));
        }
        return uuids;
    }

    @Override
    public WorkflowGraph findById(UUID uuid) {
        return new WorkflowGraph(findStartNode(uuid));
    }

    @Override
    public UUID create() {
        WorkflowGraph start = new WorkflowGraphBuilder(graphDatabaseService)
                                    .with(step("one"))
                                    .with(step("two"))
                                    .start(step("one"))
                                    .with(transition().from("one").to("two"))
                                    .build();
        return start.getId();
    }

    private IndexHits<Node> nodeForIdAndName(UUID workflowId, String nodeName) {
        return searchNodeIndex(String.format("%s:%s AND %s:%s", NodeProperties.NODE_ID, workflowId, NodeProperties.NODE_NAME, nodeName));
    }

    private Node findStartNode(UUID workflowId) {
        String query = String.format("%s:%s AND %s:%s", NodeProperties.NODE_ID, workflowId, NodeProperties.NODE_TYPE, NodeTypes.WORKFLOW_START_NODE);
        IndexHits<Node> indexHits = searchNodeIndex(query);
        return indexHits.getSingle();
    }

    private IndexHits<Node> searchNodeIndex(String query) {
        return graphDatabaseService.index().forNodes(MyIndices.TEXT_INDEX.name()).query(query);
    }

    private IndexHits<Relationship> searchRelationshipIndex(String query) {
        return graphDatabaseService.index().forRelationships(MyIndices.TEXT_INDEX.name()).query(query);
    }
}
