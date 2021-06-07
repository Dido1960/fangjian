package com.ejiaoyi.common.util;

import cn.hutool.http.Header;
import com.ejiaoyi.common.dto.HttpResponseDTO;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * HTTP 客户端 工具类
 *
 * @author Z0001
 * @since 2020-5-11
 */
@Log4j2
public class HttpClientUtil {

    /**
     * 发送 get请求 接受返回值
     *
     * @param url 请求地址
     * @return HttpResponseDTO
     */
    public static HttpResponseDTO get(String url) {
        HttpResponseDTO httpResponseDTO = null;

        CloseableHttpClient httpClient = null;
        CloseableHttpResponse closeableHttpResponse;

        try  {
            httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            closeableHttpResponse = httpClient.execute(httpGet);

            httpResponseDTO = HttpResponseDTO.builder()
                    .content(EntityUtils.toString(closeableHttpResponse.getEntity(), StandardCharsets.UTF_8))
                    .code(closeableHttpResponse.getStatusLine().getStatusCode())
                    .build();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return httpResponseDTO;
    }

    /**
     * 发送 post请求 接受返回值
     *
     * @param url    请求地址
     * @param params 请求参数列表
     * @return HttpResponseDTO
     */
    public static HttpResponseDTO post(String url, List<NameValuePair> params) {
        HttpResponseDTO httpResponseDTO = null;

        CloseableHttpClient httpClient = null;
        UrlEncodedFormEntity urlEncodedFormEntity;
        CloseableHttpResponse closeableHttpResponse;

        try  {
            httpClient = HttpClients.createDefault();
            urlEncodedFormEntity = new UrlEncodedFormEntity(params, StandardCharsets.UTF_8);
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(urlEncodedFormEntity);
            closeableHttpResponse = httpClient.execute(httpPost);

            httpResponseDTO = HttpResponseDTO.builder()
                    .content(EntityUtils.toString(closeableHttpResponse.getEntity(), StandardCharsets.UTF_8))
                    .code(closeableHttpResponse.getStatusLine().getStatusCode())
                    .build();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return httpResponseDTO;
    }

    /**
     * 发送 post raw 接受返回值
     *
     * @param url  请求地址
     * @param json 请求参数JSON
     * @param connectTimeout  连接超时时间
     * @return HttpResponseDTO
     */
    public static HttpResponseDTO postRaw(String url, String json,int connectTimeout)  {
        HttpResponseDTO httpResponseDTO = null;

        CloseableHttpClient httpClient = null;
        StringEntity stringEntity;
        CloseableHttpResponse closeableHttpResponse;

        try {
            httpClient = HttpClients.createDefault();
            stringEntity = new StringEntity(json, StandardCharsets.UTF_8);

            HttpPost httpPost = new HttpPost(url);

            httpPost.setConfig(RequestConfig.custom()
                    .setConnectTimeout(connectTimeout)
                    .setSocketTimeout(connectTimeout)
                    .build());

            httpPost.setHeader(Header.CONTENT_TYPE.toString(), ContentType.APPLICATION_JSON.toString());
            httpPost.setEntity(stringEntity);
            closeableHttpResponse = httpClient.execute(httpPost);

            httpResponseDTO = HttpResponseDTO.builder()
                    .content(EntityUtils.toString(closeableHttpResponse.getEntity(), StandardCharsets.UTF_8))
                    .code(closeableHttpResponse.getStatusLine().getStatusCode())
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return httpResponseDTO;
    }


    /**
     * 发送 post raw 接受返回值
     *
     * @param url  请求地址
     * @param json 请求参数JSON
     * @return HttpResponseDTO
     * **/
    public static HttpResponseDTO postRaw(String url, String json ){
        return  postRaw(url,json,60000);
    }

    /**
     * 发送 post multipart/form-data 接受返回值
     *
     * @param uri  请求地址
     * @param filePaths 请求文件
     * @param params 请求参数
     * @return HttpResponseDTO
     */
    public static HttpResponseDTO postFormData(String uri, Map<String, String> filePaths, Map<String, String> params) {
        HttpResponseDTO httpResponseDTO = null;
        CloseableHttpResponse closeableHttpResponse;
        CloseableHttpClient httpClient = null;
        try {
            httpClient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(uri);

            MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
            // 封装文件
            FileBody file;
            for (String fileKey : filePaths.keySet()) {
                file = new FileBody(new File(filePaths.get(fileKey)));
                multipartEntity.addPart(fileKey, file);
            }

            // 封装参数
            StringBody value;
            for (String paramKey : params.keySet()) {
                value = new StringBody(params.get(paramKey), ContentType.TEXT_PLAIN);
                multipartEntity.addPart(paramKey, value);
            }

            HttpEntity reqEntity = multipartEntity.build();

            httppost.setEntity(reqEntity);

            closeableHttpResponse = httpClient.execute(httppost);
            httpResponseDTO = HttpResponseDTO.builder()
                    .content(EntityUtils.toString(closeableHttpResponse.getEntity(), StandardCharsets.UTF_8))
                    .code(closeableHttpResponse.getStatusLine().getStatusCode())
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return httpResponseDTO;
    }
}
