package com.vk.dwzkf.test.impl;

import com.vk.dwzkf.test.Accumulator;
import com.vk.dwzkf.test.State;
import com.vk.dwzkf.test.StateObject;

import java.util.*;
/**
 * @author Roman Shageev
 * @since 12.08.2024
 */
public class AccumulatorImpl implements Accumulator {
    private final Map<Long, PriorityQueue<StateObject>> processMap = new HashMap<>();
    private State lastState = null;
    private int count = 0;

    @Override
    public void accept(StateObject stateObject) {
        Long processId = stateObject.getProcessId();
        PriorityQueue<StateObject> stateObjects = processMap.computeIfAbsent(processId, k ->
                new PriorityQueue<>(new StateComparator()));

        if (!stateObjects.isEmpty() && isFinalState(stateObjects.peek().getState())) return;

        stateObjects.add(stateObject);
    }

    private static class StateComparator implements Comparator<StateObject> {
        @Override
        public int compare(StateObject o1, StateObject o2) {
            State state1 = o1.getState();
            State state2 = o2.getState();

            if ((state1 == State.MID1 && state2 == State.MID2) || (state1 == State.MID2 && state2 == State.MID1)) {
                return 3;
            }

            return state1.compareTo(state2);
        }
    }

    private boolean isFinalState(State state) {
        return state == State.FINAL1;
    }

    @Override
    public void acceptAll(List<StateObject> stateObjects) {
        stateObjects.forEach(this::accept);
    }

    @Override
    public List<StateObject> drain(Long processId) {
        PriorityQueue<StateObject> stateObjects = processMap.get(processId);
        List<StateObject> list = new ArrayList<>(stateObjects.stream().toList());
        for(int i = 0; i < count; i++) {
            stateObjects.remove(list.get(i));
        }
        count = 0;
        List<StateObject> res = new ArrayList<>();

        for (StateObject stateObject : stateObjects) {
            if (stateObjects.size() == 1 && lastState == null) {
                if (stateObject.getState() == State.START2) {
                    lastState = stateObject.getState();
                    res.add(stateObject);
                }
            }
            else if (lastState == null || isValidTransition(lastState, stateObject.getState())) {
                res.add(stateObject);
                lastState = stateObject.getState();
            }
            count++;
        }
        return res;
    }

    private boolean isValidTransition(State lastState, State currentState) {
        return switch (lastState) {
            case START1, MID2, START2 ->
                    currentState == State.MID1 || isFinalState(currentState);
            case MID1 ->
                    currentState == State.MID2 || (currentState == State.FINAL1 || currentState == State.FINAL2) &&
                            isFinalState(currentState);
            default -> false;
        };
    }
}