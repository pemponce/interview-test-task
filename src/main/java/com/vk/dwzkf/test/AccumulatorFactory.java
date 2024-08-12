package com.vk.dwzkf.test;

import com.vk.dwzkf.test.impl.AccumulatorImpl;

/**
 * @author Roman Shageev
 * @since 12.08.2024
 */
public class AccumulatorFactory {
    public Accumulator getInstance() {
        return new AccumulatorImpl();
    }
}
