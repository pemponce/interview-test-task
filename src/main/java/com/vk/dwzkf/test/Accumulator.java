package com.vk.dwzkf.test;

import java.util.List;

/**
 * @author Roman Shageev
 * @since 12.08.2024
 */
public interface Accumulator {
    void accept(StateObject stateObject);
    List<StateObject> drain(Long contextId);
}
