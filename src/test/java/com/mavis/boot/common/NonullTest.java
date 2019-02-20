package com.mavis.boot.common;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public class NonullTest {

    @Test
    public void test() {
        call(null);
    }

    public void call(@NotNull String name) {
        System.out.println(name.length());
    }

}
