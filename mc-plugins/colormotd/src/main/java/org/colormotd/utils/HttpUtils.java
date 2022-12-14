/*
 * Copyright (C) 2019 Bukkit Commons
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.colormotd.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HttpUtils {
    private static final int TIMEOUT_IN_MILLIONS = 5000;

    public interface CallBack {
        void onRequestComplete(String result);
    }

    public static void doGetAsyn(final String urlStr, final CallBack callBack) {
        new Thread(() -> {
            try {
                String result = doGet(urlStr);
                if (callBack != null) {
                    callBack.onRequestComplete(result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();
    }

    public static void doPostAsyn(final String urlStr, final String params,
                                  final CallBack callBack) {
        new Thread(() -> {
            try {
                String result = doPost(urlStr, params);
                if (callBack != null) {
                    callBack.onRequestComplete(result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();

    }
    public static String doGet(String urlStr) {
        URL url = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIMEOUT_IN_MILLIONS);
            conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                baos = new ByteArrayOutputStream();
                int len = -1;
                byte[] buf = new byte[128];

                while ((len = is.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
                baos.flush();
                conn.disconnect();
                return baos.toString();
            } else if (conn.getResponseCode() == 404) {
                //Shit
                return null;
            }

        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                //ignore
            }
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                //ignore
            }

        }
        return null;
    }

    public static String doPost(String url, String ip) {
        // ?????? HttpClient ?????????
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // ?????? HttpPost ??????
        HttpPost httpPost = new HttpPost(url);
        // ???????????????
        httpPost.setHeader("Connection", "keep-alive");
        // ???????????????????????????????????????


        // ?????? HttpPost ??????
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("ip", ip));
        params.add(new BasicNameValuePair("accessKey", "alibaba-inc"));


        CloseableHttpResponse httpResponse = null;
        try {
            // ?????? HttpPost ??????
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            // ??????????????????
            System.out.println(EntityUtils.toString(httpEntity));
            return  EntityUtils.toString(httpEntity);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // ??????????????????????????????
        finally {
            try {
                if (httpResponse != null) {
                    httpResponse.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
