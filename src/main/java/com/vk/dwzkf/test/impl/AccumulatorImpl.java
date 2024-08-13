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

    @Override
    public void accept(StateObject stateObject) {
        Long processId = stateObject.getProcessId();
        PriorityQueue<StateObject> stateObjects = processMap.computeIfAbsent(processId, k ->
                new PriorityQueue<>(Comparator.comparing(StateObject::getState)));

        if (!stateObjects.isEmpty() && isFinalState(stateObjects.peek().getState())) return;

        stateObjects.add(stateObject);
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
        List<StateObject> res = new ArrayList<>();

        if (stateObjects == null) return Collections.emptyList();

        for (StateObject stateObject : stateObjects) {
            if (stateObjects.size() == 1 && lastState == null) {
                if (stateObject.getState() == State.START2) {
                    lastState = stateObject.getState();
                    res.add(stateObject);
                    stateObjects.remove(stateObject);
                }
            } else if (lastState != null && isFinalState(lastState)) break;
            else if (lastState == null || isValidTransition(lastState, stateObject.getState())) {
                res.add(stateObject);
                lastState = stateObject.getState();
            }
        }
        return res;
    }

    private boolean isValidTransition(State lastState, State currentState) {
        return switch (lastState) {
            case START1 ->
                    currentState == State.MID1 || (currentState == State.FINAL1 || currentState == State.FINAL2) &&
                            isFinalState(currentState);
            case START2 ->
                    currentState == State.MID1 || isFinalState(currentState);
            case MID2 ->
                    currentState == State.START1 || currentState == State.MID1 || (currentState == State.FINAL1 ||
                            currentState == State.FINAL2) && isFinalState(currentState);
            case MID1 ->
                    currentState == State.MID2 || (currentState == State.FINAL1 || currentState == State.FINAL2) &&
                            isFinalState(currentState);
            case FINAL2 ->
                    isFinalState(currentState) || currentState == State.START1;
            default -> false;
        };
    }
}