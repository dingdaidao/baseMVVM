package com.example.commonlib.api;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSource;

/**
 * @author Army
 * @version V_1.0.0
 * @date 2017/11/6
 * @description token过期的处理   需要的时候 自己写逻辑
 */
public class TokenInterceptor implements Interceptor {

    public TokenInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder().addHeader("", "").build();
        Response response = chain.proceed(request);
        ResponseBody body = response.body();
        if (body != null && response.isSuccessful()) {
            BufferedSource source = body.source();
            source.request(Long.MAX_VALUE);
            MediaType contentType = body.contentType();
            Charset charset = Util.UTF_8;
            if (contentType != null) {
                charset = contentType.charset(Util.UTF_8);
            }
            Buffer buffer = source.buffer().clone();
            String content = buffer.readString(charset);
        }
        return response;
    }

}
