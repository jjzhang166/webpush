package com.github.wuzguo.webpush.common.util;

import com.github.wuzguo.webpush.common.exception.FrameException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 通过HttpClient框架远程调用接口
 *
 * @author wzguo
 */

public class HttpUtils {
    // 日志
    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    // HttpClientBuilder
    private static final HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

    private CloseableHttpClient httpClient = null;

    private String result = null;


    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    private HttpUtils(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    private HttpUtils(CloseableHttpClient httpClient, String result) {
        this.httpClient = httpClient;
        this.result = result;
    }

    public static HttpUtils post(final String url, final HttpEntity httpEntity) throws FrameException {
        return post(url, httpEntity, 0, null);
    }


    public static HttpUtils post(final String url, final HttpEntity httpEntity, final int timeOut) throws FrameException {
        return post(url, httpEntity, timeOut, null);
    }


    public static HttpUtils post(final String url, final HttpEntity httpEntity, final int timeOut, final Header header) throws FrameException {

        if (StringUtils.isEmpty(url) || httpEntity == null) {
            throw new FrameException(" url or httpEntity is null");
        }

        HttpEntityEnclosingRequestBase httpPost = new HttpPost(url);

        httpPost.addHeader(httpEntity.getContentType());
        // 添加参数
        httpPost.setEntity(httpEntity);

        if (null != header) {
            httpPost.addHeader(header);
        }

        if (timeOut > 0) {
            RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(timeOut)
                    .setConnectTimeout(timeOut).setSocketTimeout(timeOut).build();
            httpPost.setConfig(config);
        }

        CloseableHttpClient httpClient = httpClientBuilder.build();
        return new HttpUtils(httpClient, null).submit(httpPost);
    }


    public static HttpUtils put(final String url, final HttpEntity httpEntity) throws FrameException {
        return put(url, httpEntity, 0, null);
    }


    public static HttpUtils put(final String url, final HttpEntity httpEntity, final int timeOut) throws FrameException {
        return put(url, httpEntity, timeOut, null);
    }


    public static HttpUtils put(final String url, final HttpEntity httpEntity, final int timeOut, final Header header) throws FrameException {
        if (StringUtils.isEmpty(url) || httpEntity == null) {
            throw new FrameException(" url or httpEntity is null");
        }
        HttpEntityEnclosingRequestBase httpPut = new HttpPut(url);

        httpPut.addHeader(httpEntity.getContentType());
        // 添加参数
        httpPut.setEntity(httpEntity);

        if (null != header) {
            httpPut.addHeader(header);
        }

        if (timeOut > 0) {
            RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(timeOut)
                    .setConnectTimeout(timeOut).setSocketTimeout(timeOut).build();
            httpPut.setConfig(config);
        }

        CloseableHttpClient httpClient = httpClientBuilder.build();
        return new HttpUtils(httpClient, null).submit(httpPut);
    }


    public static HttpUtils get(final String url, final ContentType contentType) throws FrameException {
        return get(url, contentType, 0, null);
    }


    public static HttpUtils get(final String url, final ContentType contentType, final int timeOut) throws FrameException {
        return get(url, contentType, timeOut, null);
    }


    public static HttpUtils get(final String url, final ContentType contentType, final int timeOut, final Header header) throws FrameException {
        if (StringUtils.isEmpty(url) || contentType == null) {
            throw new FrameException(" url or httpEntity is null");
        }
        HttpRequestBase httpGet = new HttpGet(url);
        httpGet.addHeader(new BasicHeader("Content-Type", contentType.toString()));
        // 添加参数

        if (null != header) {
            httpGet.addHeader(header);
        }

        if (timeOut > 0) {
            RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(timeOut)
                    .setConnectTimeout(timeOut).setSocketTimeout(timeOut).build();
            httpGet.setConfig(config);
        }

        CloseableHttpClient httpClient = httpClientBuilder.build();
        return new HttpUtils(httpClient, null).submit(httpGet);
    }


    /**
     * 调用 API
     *
     * @param type 1不需要转码 2 需要转码
     * @return
     */
    public com.alibaba.fastjson.JSONObject submitFastJson(final int type) {
        logger.info("[HttpUtils] submitFastJson begin ");

        String resContent = this.result;
        if (StringUtils.isEmpty(resContent)) {
            return null;
        }

        if (type == 2) {
            resContent = resContent.replaceAll("\\\\", "");
            resContent = resContent.substring(1, resContent.length() - 1);
        }

        if (!StringUtils.isEmpty(resContent)) {
            logger.info("[HttpUtils] submitFastJson result: " + resContent);
            return com.alibaba.fastjson.JSON.parseObject(resContent);
        }

        logger.info("[HttpUtils] submitFastJson end ");
        return null;
    }

    /**
     * @param type 是否需要转换
     * @return
     */
    public String submitString(final int type) {
        logger.info("[HttpUtils] submitString begin ");

        String resContent = this.result;
        if (StringUtils.isEmpty(resContent)) {
            return null;
        }

        if (type == 2) {
            resContent = resContent.replaceAll("\\\\", "");
            resContent = resContent.substring(1, resContent.length() - 1);
        }

        logger.info("[HttpUtils] submitString end ");
        return resContent;
    }

    /**
     * 通过HTTP请求数据
     *
     * @param httpRequestBase
     * @return
     * @throws FrameException
     */
    private HttpUtils submit(final HttpRequestBase httpRequestBase) throws FrameException {
        this.result = null;
        try {
            HttpResponse response = httpClient.execute(httpRequestBase);
            if (null == response) {
                logger.info("[HttpUtils] rest method call,response is empty.");
                return this;
            }

            logger.info("[HttpUtils] rest method call,response ReasonPhrase： " + response.getStatusLine().getReasonPhrase());

            HttpEntity httpEntity = response.getEntity();
            if (httpEntity == null) {
                logger.info("[HttpUtils] rest method call,return no content.");
                return this;
            }

            // 打印响应长度
            logger.info("[HttpUtils] rest method call,response content length: " + httpEntity.getContentLength());

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                logger.info("[HttpUtils] rest method call,response http code is： " + statusCode);
                return this;
            }

            this.result = EntityUtils.toString(httpEntity, Charset.forName("UTF-8"));
            // 销毁
            EntityUtils.consume(httpEntity);
        } catch (IOException e) {
            logger.error("[HttpUtils] submitString IOException: " + e.getMessage());
            logger.error(e.getMessage(), e);
            throw new FrameException(e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }

        logger.info("[HttpUtils] api result content: " + this.result);
        return this;
    }
}
