package configserver.configserver.util;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtil {
	
	
	
	public static void postUseOkhttp(String url, String requestBody){
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(mediaType, requestBody))
                .build();
        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(5000, TimeUnit.MILLISECONDS).readTimeout(6000,TimeUnit.MILLISECONDS).build();
        // execute还是会超时
        /*try {
            Response response = okHttpClient.newCall(request).execute();
//            result = response.body().string();
        } catch (IOException e) {
            LOGGER.info(">>>>>>>>>>>>>>>>post请求发生错误,错误信息:{}",e);
        }*/
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                }
            }
        });
    }
}
