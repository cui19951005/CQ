package com.cq.sdk.utils;


import com.google.gson.Gson;
import java.io.*;
import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by admin on 2016/12/2.
 */
public final class Http {
    public static String post(String url, Object object, boolean json, String encoding) {
        if (json) {
            return post(url, Json.toJson(object), encoding);
        } else {
            Map<String, Object> param = ObjectUtils.copyMap(object);
            return Http.post(url, Http.joinParam(param), encoding);
        }
    }

    public static <T> T post(String url, Object object, boolean json, String encoding, Class<T> clazz) {
        return new Gson().fromJson(Http.post(url, object, json, encoding), clazz);
    }

    public static <T> T post(String url, Map<String, Object> param, String encoding, Class<T> clazz) {
        return new Gson().fromJson(Http.post(url, param, encoding), clazz);
    }

    public static String post(String url, Map<String, Object> param, String encoding) {
        return Http.post(url, Http.joinParam(param), encoding);
    }

    public static String post(String url, String params, String encoding) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type",
                    "x-www-form-urlencoded");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流

            out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), encoding));
            // 发送请求参数
            out.println(params);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), encoding));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            closeStream(out, in);
        }
        return result;
    }

    public static String get(String url, Map<String, Object> param, String encoding) {
        return Http.get(Str.concat(url, "?", Http.joinParam(param, encoding)), encoding);
    }

    public static <T> T get(String url, Map<String, Object> param, String encoding, Class<T> clazz) {

        return new Gson().fromJson(Http.get(url, param, encoding), clazz);
    }

    public static String get(String url, String encoding) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {

            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoInput(true);
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), encoding));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 GET 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            closeStream(out, in);
        }
        return result;
    }

    private static String joinParam(Map<String, Object> map) {
        return Http.joinParam(map, null);
    }

    private static String joinParam(Map<String, Object> map, String encoding) {
        try {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                sb.append(entry.getKey());
                sb.append("=");
                sb.append(encoding != null ? URLEncoder.encode(entry.getValue().toString(), encoding) : entry.getValue().toString());
                sb.append("&");
            }
            if (sb.length() > 0) {
                return sb.substring(0, sb.length() - 1);
            } else {
                return sb.toString();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void closeStream(PrintWriter pw, BufferedReader br) {
        try {
            if (pw != null) {
                pw.close();
            }
            if (br != null) {
                br.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public interface DownloadSchedule {
        void execute(long sum, long now);
    }

    public static ByteSet download(String location, DownloadSchedule downloadSchedule) throws IOException {
        URL url = new URL(location);
        URLConnection connection = url.openConnection();
        long sum = connection.getContentLength();
        InputStream inputStream = connection.getInputStream();
        ByteSet byteSet = new ByteSet();
        while (true) {
            byte[] bytes = new byte[8192];
            int length = inputStream.read(bytes, 0, bytes.length);
            if (downloadSchedule != null) downloadSchedule.execute(sum, byteSet.length());
            if (length <= 0) {
                break;
            }
            byteSet.append(bytes, 0, length);

        }
        inputStream.close();
        return byteSet;
    }
    public static String download(String location, String filePath, DownloadSchedule downloadSchedule)  {
        try {
            FileOutputStream fileOutputStream;
            File file=new File(filePath);
            fileOutputStream = new FileOutputStream(file);
            URL url = new URL(location);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("Content-Type", "multipart/form-data");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 UBrowser/6.0.1471.913 Safari/537.36");
            connection.setRequestProperty("Content-Disposition","attachment;fileName="+file.getName());
            long now = 0;
            InputStream inputStream = connection.getInputStream();
            long sum = connection.getContentLength();
            int length;
            byte[] bytes = new byte[1024];
            while ((length = inputStream.read(bytes, 0, bytes.length)) != -1) {
                now += length;
                if (downloadSchedule != null) downloadSchedule.execute(sum, now);
                fileOutputStream.write(bytes, 0, length);
                bytes = new byte[bytes.length];
            }
            fileOutputStream.close();
            inputStream.close();
            return filePath;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String download(String location, String encoding) throws IOException {
        DownloadSchedule downloadSchedule = null;
        return new String(Http.download(location, downloadSchedule).getByteSet(), encoding);
    }
}
