package com.mavis.boot.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.util.Base64Utils;

/**
 * @description:
 * @author: admin
 * @date: 2019-02-19 19:57
 */
public class Test {

    private static final String URL_PREFIX = "https://apis.map.qq.com/ws/staticmap/v2/?";
    private static final String IMG_BASE64_PREFIX = "data:image/png;base64,";

    public static void main(String[] args) throws IOException {
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault(); //1、创建实例
        String url_suffix = "center=39.8802147,116.415794&zoom=10&size=600*300&maptype=roadmap&markers=size:large|color:0xFFCCFF|label:k|39.8802147,116.415794&key=XGJBZ-SPHA4-QPRUV-XBZRS-6ALDE-GGFOR";
        String encodedUrlSuffix = URLEncoder.encode(url_suffix, "UTF-8");
        String url = URL_PREFIX + encodedUrlSuffix;
        HttpGet httpGet = new HttpGet(url); //2、创建请求

        CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpGet); //3、执行
        HttpEntity httpEntity = closeableHttpResponse.getEntity(); //4、获取实体

        if (httpEntity != null) {
            System.out.println("ContentType:" + httpEntity.getContentType().getValue());
            InputStream inputStream = httpEntity.getContent();
            byte[] bytes = Test.toByteArray(inputStream);
            String encode = new String(Base64Utils.encode(bytes), "UTF-8");
            System.out.println(IMG_BASE64_PREFIX + encode);
            FileUtils
                .writeByteArrayToFile(new File("D://xxx.png"), bytes); //将图片保存在本次磁盘D盘，命名为xxx.png
        }

        closeableHttpResponse.close();
        closeableHttpClient.close();
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

}
