package jackszm.androiddevtweets.api;

import java.io.IOException;

import okhttp3.OkHttpClient;

public class RequestExecutor {

    private final OkHttpClient client;

    public static RequestExecutor newInstance() {
        return new RequestExecutor(new OkHttpClient());
    }

    RequestExecutor(OkHttpClient client) {
        this.client = client;
    }

    public String executeRequest(Request request) {
        try {
            return client.newCall(request.request()).execute().body().string();
        } catch (IOException e) {
            throw new HttpException(e);
        }
    }

    public static class HttpException extends RuntimeException {

        public HttpException(Throwable throwable) {
            super(throwable);
        }
    }

}
