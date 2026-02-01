package com.hubosm.turingsimulator.domain;

import com.hubosm.turingsimulator.dtos.SimulationReturnDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TmSimulatorTest {
    private static final String SEP1 = ",";
    private static final String SEP2 = "->";
    private static final String BLANK = "_";

    private int oldMaxSteps;

    @BeforeEach
    void saveAndSetDefaults() {
        oldMaxSteps = SimulationConfig.maxSteps;
        SimulationConfig.maxSteps = 1000;
    }

    @AfterEach
    void restore() {
        SimulationConfig.maxSteps = oldMaxSteps;
    }

    @Test
    void noTransition_rejectOnNonAcceptFalse_setsHalt_onRoot() {
        TmSimulator sim = new TmSimulator(
                "q0", "qa", "qr",
                List.of(),
                SEP1, SEP2, BLANK,
                1,
                false
        );

        SimulationReturnDto out = sim.createSimulation(List.of("0"));
        Map<Integer, SimulationNode> nodes = out.getNodes();

        assertEquals(1, nodes.size());
        SimulationNode root = nodes.get(0);
        assertNotNull(root);
        assertEquals("HALT", root.getOutput());
        assertTrue(root.getNextIds().isEmpty());
        assertNull(root.getPrevId());
    }

    @Test
    void noTransition_rejectOnNonAcceptTrue_setsReject_onRoot() {
        TmSimulator sim = new TmSimulator(
                "q0", "qa", "qr",
                List.of(),
                SEP1, SEP2, BLANK,
                1,
                true
        );

        SimulationReturnDto out = sim.createSimulation(List.of("0"));
        SimulationNode root = out.getNodes().get(0);

        assertEquals("REJECT", root.getOutput());
        assertTrue(root.getNextIds().isEmpty());
    }

    @Test
    void deterministic_accept_setsAccept_onLeaf() {

        TmSimulator sim = new TmSimulator(
                "q0", "qa", "qr",
                List.of("q0,0->qa,0,S"),
                SEP1, SEP2, BLANK,
                1,
                false
        );

        SimulationReturnDto out = sim.createSimulation(List.of("0"));
        Map<Integer, SimulationNode> nodes = out.getNodes();

        assertEquals(2, nodes.size());

        SimulationNode root = nodes.get(0);
        assertEquals(List.of(1), root.getNextIds());

        SimulationNode n1 = nodes.get(1);
        assertEquals(0, n1.getPrevId());
        assertEquals("ACCEPT", n1.getOutput());
        assertEquals(new State("q0"), n1.getStateBefore());
        assertEquals(new State("qa"), n1.getStateAfter());

        assertEquals(1, n1.getSteps().size());
        SimulationStep step = n1.getSteps().get(0);
        assertEquals(0, step.tapeIndex());
        assertEquals(Transition.TransitionAction.STAY, step.transitionAction());
        assertEquals("0", step.readChar());
        assertEquals("0", step.writtenChar());

        TapeState tapeBefore = step.tapeBefore();
        assertEquals(0, tapeBefore.head());
        assertEquals("0", tapeBefore.tape().get(0));
    }

    @Test
    void deterministic_reject_setsReject_onLeaf() {

        TmSimulator sim = new TmSimulator(
                "q0", "qa", "qr",
                List.of("q0,0->qr,0,S"),
                SEP1, SEP2, BLANK,
                1,
                false
        );

        SimulationReturnDto out = sim.createSimulation(List.of("0"));
        Map<Integer, SimulationNode> nodes = out.getNodes();

        assertEquals(2, nodes.size());
        assertEquals("REJECT", nodes.get(1).getOutput());
        assertEquals(new State("qr"), nodes.get(1).getStateAfter());
    }

    @Test
    void nondeterministic_twoTransitions_createsTwoChildren() {

        TmSimulator sim = new TmSimulator(
                "q0", "qa", "qr",
                List.of(
                        "q0,0->qa,0,S",
                        "q0,0->qr,0,S"
                ),
                SEP1, SEP2, BLANK,
                1,
                false
        );

        SimulationReturnDto out = sim.createSimulation(List.of("0"));
        Map<Integer, SimulationNode> nodes = out.getNodes();

        assertEquals(3, nodes.size());

        SimulationNode root = nodes.get(0);
        assertEquals(List.of(1, 2), root.getNextIds());

        SimulationNode n1 = nodes.get(1);
        SimulationNode n2 = nodes.get(2);

        assertEquals(0, n1.getPrevId());
        assertEquals(0, n2.getPrevId());

        assertEquals("ACCEPT", n1.getOutput());
        assertEquals("REJECT", n2.getOutput());
    }

    @Test
    void branchingTapeCopies_areIndependent() {

        TmSimulator sim = new TmSimulator(
                "q0", "qa", "qr",
                List.of(
                        "q0,0->q1,1,S",
                        "q0,0->q1,2,S",
                        "q1,1->qa,1,S",
                        "q1,2->qa,2,S"
                ),
                SEP1, SEP2, BLANK,
                1,
                false
        );

        SimulationReturnDto out = sim.createSimulation(List.of("0"));
        Map<Integer, SimulationNode> nodes = out.getNodes();

        assertEquals(5, nodes.size());

        SimulationNode root = nodes.get(0);
        assertEquals(List.of(1, 2), root.getNextIds());

        SimulationNode c1 = nodes.get(1);
        SimulationNode c2 = nodes.get(2);
        assertEquals(new State("q1"), c1.getStateAfter());
        assertEquals(new State("q1"), c2.getStateAfter());

        assertEquals("ACCEPT", nodes.get(3).getOutput());
        assertEquals("ACCEPT", nodes.get(4).getOutput());
    }

    @Test
    void stepLimitExceeded_setsLimit_onUnprocessedLeafs_whenRejectOnNonAcceptFalse() {
        SimulationConfig.maxSteps = 2;

        TmSimulator sim = new TmSimulator(
                "q0", "qa", "qr",
                List.of(
                        "q0,0->q0,0,S",
                        "q0,0->q0,0,S"
                ),
                SEP1, SEP2, BLANK,
                1,
                false
        );

        SimulationReturnDto out = sim.createSimulation(List.of("0"));
        Map<Integer, SimulationNode> nodes = out.getNodes();
        assertEquals(3, nodes.size());

        SimulationNode root = nodes.get(0);
        assertEquals(List.of(1, 2), root.getNextIds());

        SimulationNode n1 = nodes.get(1);
        SimulationNode n2 = nodes.get(2);
        assertEquals("LIMIT", n1.getOutput());
        assertEquals("LIMIT", n2.getOutput());
    }

    @Test
    void stepLimitExceeded_setsREJECT_onUnprocessedLeafs_whenRejectOnNonAcceptTrue() {
        SimulationConfig.maxSteps = 2;

        TmSimulator sim = new TmSimulator(
                "q0", "qa", "qr",
                List.of(
                        "q0,0->q0,0,S",
                        "q0,0->q0,0,S"
                ),
                SEP1, SEP2, BLANK,
                1,
                true
        );

        SimulationReturnDto out = sim.createSimulation(List.of("0"));
        Map<Integer, SimulationNode> nodes = out.getNodes();

        assertEquals(3, nodes.size());
        assertEquals("REJECT", nodes.get(1).getOutput());
        assertEquals("REJECT", nodes.get(2).getOutput());
    }
}
