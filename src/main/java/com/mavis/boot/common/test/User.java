package com.mavis.boot.common.test;

import com.google.common.annotations.Beta;
import org.apache.http.client.HttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

/**
 * @description:
 * @author: admin
 * @date: 2019-02-27 19:32
 */
@Beta
@ConditionalOnClass(HttpClient.class)
public class User {

}
