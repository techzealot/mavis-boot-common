package com.mavis.boot.common;

import com.mavis.boot.common.test.User;
import java.lang.annotation.Annotation;
import org.junit.Test;

/**
 * @description:
 * @author: admin
 * @date: 2019-02-27 19:33
 */
public class ClassloaderTest {

    @Test
    public void testLoad() {
        User user = new User();
        for (Annotation annotation : user.getClass().getAnnotations()) {
            System.out.println(annotation);
            System.out.println(1);
        }
        System.out.println(2);
    }
}
