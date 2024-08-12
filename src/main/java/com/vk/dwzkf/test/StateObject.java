package com.vk.dwzkf.test;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Представляет собой объект уведомления
 * @author Roman Shageev
 * @since 12.08.2024
 */
@Getter
public class StateObject {
    private static final Map<Long, AtomicInteger> sequenceGenerator = new HashMap<>();

    /**
     * ID процесса
     */
    private final Long processId;
    /**
     * Статус
     */
    private final State state;
    /**
     * Порядковый номер уведомления
     */
    private final Integer seqNo;


    public StateObject(Long processId, State state) {
        this.processId = processId;
        this.state = state;
        this.seqNo = sequenceGenerator.computeIfAbsent(processId, ctx -> new AtomicInteger())
                .incrementAndGet();
    }
}
