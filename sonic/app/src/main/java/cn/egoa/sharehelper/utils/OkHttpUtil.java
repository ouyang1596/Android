package cn.egoa.sharehelper.utils;

/**
 * Created by jianghua on 2017/7/9 0009.
 */

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cn.egoa.sharehelper.net.HttpNet;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpUtil {
    private static OkHttpClient mOkHttpClient = null;
    public final static int CONNECT_TIMEOUT = 15;
    public final static int READ_TIMEOUT = 60;
    public final static int WRITE_TIMEOUT = 60;
    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("application/json; charset=utf-8");

    public static OkHttpClient getIntance() {
        if (mOkHttpClient == null) {
            Interceptor interceptor = new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request request = chain.request().newBuilder()
//                    .addHeader("Accept-Encoding", "gzip, deflate")
//                    .addHeader("Connection", "keep-alive")
//                    .addHeader("Accept", "*/*")
//                    .addHeader("Content-Type", "application/json; charset=UTF-8")//不能加
                            .addHeader("User-Agent", HttpNet.getUserAgent())
                            .addHeader("X-API-UA", HttpNet.getUserAgent())
                            .addHeader("X-API-USERID", "")
                            .addHeader("X-API-TOKEN", "")
                            .build();
                    return chain.proceed(request);
                }
            };
            mOkHttpClient =
                    new OkHttpClient.Builder()
                            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
                            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)//设置写的超时时间
                            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
                            .followRedirects(true)
                            .followSslRedirects(true)
                            .addInterceptor(interceptor)
                            .build();
        }
        return mOkHttpClient;
    }

//    Interceptor interceptor = new Interceptor() {
//        @Override
//        public Response intercept(Interceptor.Chain chain) throws IOException {
//            Request request = chain.request().newBuilder()
////                    .addHeader("Accept-Encoding", "gzip, deflate")
////                    .addHeader("Connection", "keep-alive")
////                    .addHeader("Accept", "*/*")
////                    .addHeader("Content-Type", "application/json; charset=UTF-8")//不能加
//                    .addHeader("User-Agent","cn.egoa.sharehelper (Android; OS/16; Branchs W818;) Ver/2.2.7 Device/480x854 Ca/0" /*HttpNet.getUserAgent()*/)
//                    .addHeader("X-API-UA","cn.egoa.sharehelper (Android; OS/16; Branchs W818;) Ver/2.2.7 Device/480x854 Ca/0"/* HttpNet.getUserAgent()*/)
//                    .addHeader("X-API-USERID", SharePreferenceHelp.getInstance(ApplicationBase.ctx).getStringValue("userid"))
//                    .addHeader("X-API-TOKEN", SharePreferenceHelp.getInstance(ApplicationBase.ctx).getStringValue("token"))
//                    .build();
//            return chain.proceed(request);
//        }
//    };


    /**
     * okhttp 异步get
     *
     * @param responseCallback
     */
    public static void okGet(String url, Callback responseCallback) {
        Request request = new Request.Builder().url(url).build();
        //根据Request对象发起Get同步Http请求
        getIntance().newCall(request).enqueue(responseCallback);
    }

    /**
     * okhttp 同步get
     */
//    public static Response okGet(String url) throws IOException {
//        url = checkUrlHttp(url);
//        if (!checkIllegalHttp(url))
//            return null;
//        Request request = new Request.Builder().url(url).build();
//        //根据Request对象发起Get同步Http请求
//        return getIntance().newCall(request).execute();
//    }

//    /**
//     * okhttp 异步post
//     *
//     * @param responseCallback
//     */
//    public static void okPost(String url, HttpNet.PostParameterArray parameList, Callback responseCallback) {
//        FormBody.Builder builder = new FormBody.Builder();
//        if (parameList != null && parameList.getListSize() > 0) {
//            try {
//                for (HttpNet.PostParameter pd : parameList.getParmesList()) {
//                    builder.add(pd.Key, pd.Value.toString());
//                }
//            } catch (Exception e) {
//            }
//        }
//
//        RequestBody.create(MediaType.parse("application/json; charset=UTF-8"), "json");
//        FormBody formBody = builder.build();
//        Request request = new Request.Builder().url(url).post(formBody).build();
//        getIntance().newCall(request).enqueue(responseCallback);
//    }
//
//    /**
//     * okhttp 同步post
//     */
//    public static Response okPost(String url, HttpNet.PostParameterArray parameList) throws IOException {
//        url = checkUrlHttp(url);
//        if (!checkIllegalHttp(url))
//            return null;
//        FormBody.Builder builder = new FormBody.Builder();
//        if (parameList != null && parameList.getListSize() > 0) {
//            try {
//                for (HttpNet.PostParameter pd : parameList.getParmesList()) {
//                    builder.add(pd.Key, pd.Value.toString());
//                }
//            } catch (Exception e) {
//            }
//        }
//        RequestBody.create(MediaType.parse("application/json; charset=UTF-8"), "json");
//        FormBody formBody = builder.build();
//        Request request = new Request.Builder().url(url).post(formBody).build();
//        return getIntance().newCall(request).execute();
//    }
//
//    /**
//     * 判断请求地址是否合法
//     *
//     * @param url
//     * @return
//     */
//    private static boolean checkIllegalHttp(String url) {
//        return StringUtil.stringNotEmpty(url);
//    }
//
//    /**
//     * 判断请求地址是否合法
//     *
//     * @param url
//     * @return
//     */
//    private static String checkUrlHttp(String url) {
//        if (StringUtil.stringNotEmpty(url) && !url.startsWith("http://") && !url.startsWith("https://")) {
//            return ("http://" + url);
//        }
//        return url;
//    }


//    /**
//     * 上传头像
//     *
//     * @param filePath
//     * @param callback
//     */
//    public static void okUpHeaderImg(String filePath, Callback callback) {
//        final String url = "http://user." + HttpInterfaceUri.getBaseUrl() + "/upimg.php";
//        File file = new File(filePath);
//        RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);
////        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("image", filePath, fileBody)
//                .build();
//        Request request = new Request.Builder()
//                .url(url)
//                .post(requestBody)
//                .build();
//
//        getIntance().newCall(request).enqueue(callback);
//    }
//
//    /**
//     * 同步上传头像，带参数
//     *
//     * @param paramList 参数以及图片地址
//     */
//    public static Response okUpImg(String url, ArrayList<HttpNet.PostParameter> paramList) throws IOException {
//        MultipartBody.Builder requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM);
//        for (int i = 0; i < paramList.size(); i++) {
//            HttpNet.PostParameter param = paramList.get(i);
//            String key = (String) param.Key;
//            if (param.IsFile) {
//                String filePath = (String) param.Value;
//                File file = new File(filePath);
//                RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);
//                requestBody.addFormDataPart(key, filePath, fileBody);
//            } else {
//                if (param.Value instanceof Integer) {
//                    Integer value = (Integer) param.Value;
//                    requestBody.addFormDataPart(key, "" + value);
//                } else if (param.Value instanceof String) {
//                    String value = (String) param.Value;
//                    requestBody.addFormDataPart(key, value);
//                }
//            }
//        }
//        Request request = new Request.Builder()
//                .url(url)
//                .post(requestBody.build())
//                .build();
//        return getIntance().newCall(request).execute();
//    }
//
//    /**
//     * 注意 ：saveDir只能传文件目录。不带文件名
//     *
//     * @param url      下载连接
//     * @param saveDir  储存下载文件的SDCard目录
//     * @param listener 下载监听
//     */
//    public static void OkDownload(final String url, final String saveDir, final String saveName, final OnDownloadListener listener) {
//        Request request = new Request.Builder().url(url).build();
//        getIntance().newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) { // 下载失败
//                listener.onDownloadFailed();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                InputStream is = null;
//                byte[] buf = new byte[2048];
//                int len;
//                FileOutputStream fos = null;
//                // 储存下载文件的目录
//                MediaManager.CreatecCustomDir(saveDir);
//                try {
//                    is = response.body().byteStream();
//                    long total = response.body().contentLength();
//                    File file = new File(saveDir, saveName);
//                    fos = new FileOutputStream(file);
//                    long sum = 0;
//                    while ((len = is.read(buf)) != -1) {
//                        fos.write(buf, 0, len);
//                        sum += len;
//                        int progress = (int) (sum * 1.0f / total * 100);
//                        listener.onDownloading(progress);//下载中
//                    }
//                    fos.flush();
//                    listener.onDownloadSuccess();//下载完成
//                } catch (Exception e) {
//                    listener.onDownloadFailed();
//                } finally {
//                    try {
//                        if (is != null) is.close();
//                    } catch (IOException e) {
//                    }
//                    try {
//                        if (fos != null) fos.close();
//                    } catch (IOException e) {
//                    }
//                }
//            }
//        });
//    }
//
//    public interface OnDownloadListener {
//        /**
//         * 下载成功
//         */
//        void onDownloadSuccess();
//
//        /**
//         * @param progress * 下载进度
//         */
//        void onDownloading(int progress);
//
//        /**
//         * 下载失败
//         */
//        void onDownloadFailed();
//    }
//
//
//    public static void setmOkHttpClient(OkHttpClient mOkHttpClient) {
//        OkHttpUtil.mOkHttpClient = mOkHttpClient;
//    }
}