package com.vk.dwzkf.test;

import java.util.List;

/**
 * @author Roman Shageev
 * @since 12.08.2024
 */
public interface Accumulator {
    /**
     * Принимает в себя N уведомлений
     * @param stateObjects список уведомлений
     */
    void acceptAll(List<StateObject> stateObjects);

    /**
     * Принимает одно уведомление
     * @param stateObject объект уведомления
     */
    void accept(StateObject stateObject);

    /**
     *
     * @param processId ID процесса по которому надо достать уведомления
     * @return согласованный список уведомлений максимальной длины с учетом порядка и приоритета
     */
    List<StateObject> drain(Long processId);
}
