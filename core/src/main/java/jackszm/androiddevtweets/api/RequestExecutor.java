package jackszm.androiddevtweets.api;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Response;

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
            Response response = client.newCall(request.rawRequest()).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                throw new HttpException(response.code());
            }
        } catch (IOException e) {
            throw new HttpException("Bad HTTP request", e);
        }
    }

    public static class HttpException extends RuntimeException {

        private static final int CLIENT_UNHANDLED_ERROR = 499;
        private final int httpCode;

        public HttpException(int httpCode) {
            super("HTTP request returned code: " + httpCode);
            this.httpCode = httpCode;
        }

        public HttpException(String errorMsg, Exception originalException) {
            super(errorMsg, originalException);
            this.httpCode = CLIENT_UNHANDLED_ERROR;
        }

        public int httpCode() {
            return httpCode;
        }
    }

}
