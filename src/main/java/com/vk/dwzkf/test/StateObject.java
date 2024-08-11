package com.vk.dwzkf.test;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Roman Shageev
 * @since 12.08.2024
 */
@Getter
@AllArgsConstructor
public class StateObject {
    private static final AtomicInteger counter = new AtomicInteger();

    private final Long contextId;
    private final State state;
    private final Integer seqNo = counter.incrementAndGet();
}
