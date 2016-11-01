package jackszm.androiddevtweets.api;

import okhttp3.FormBody;
import okhttp3.RequestBody;

class Request {

    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String HEADER_AUTHORIZATION_BEARER = "Bearer %s";
    private static final String HEADER_AUTHORIZATION_BASIC = "Basic %s";

    private static final String BASIC_AUTHORIZATION_POST_KEY = "grant_type";
    private static final String BASIC_AUTHORIZATION_POST_VALUE = "client_credentials";

    private final okhttp3.Request rawRequest;

    Request(okhttp3.Request rawRequest) {
        this.rawRequest = rawRequest;
    }

    okhttp3.Request rawRequest() {
        return rawRequest;
    }

    public static Builder builder(String baseUrl) {
        return new Builder(new okhttp3.Request.Builder(), baseUrl);
    }

    static class Builder {

        private final okhttp3.Request.Builder requestBuilder;

        private final String baseUrl;

        Builder(okhttp3.Request.Builder requestBuilder, String baseUrl) {
            this.requestBuilder = requestBuilder;
            this.baseUrl = baseUrl;
        }

        public Builder path(String path) {
            requestBuilder.url(baseUrl + path);
            return this;
        }

        public Builder bearerAuthorization(String token) {
            requestBuilder.header(HEADER_AUTHORIZATION, String.format(HEADER_AUTHORIZATION_BEARER, token));
            return this;
        }

        public Builder basicAuthorization(String authorizationKey) {
            requestBuilder.post(basicAuthorizationFormBody());
            requestBuilder.header("Authorization", String.format(HEADER_AUTHORIZATION_BASIC, authorizationKey));
            return this;
        }

        private RequestBody basicAuthorizationFormBody() {
            return new FormBody.Builder()
                    .add(BASIC_AUTHORIZATION_POST_KEY, BASIC_AUTHORIZATION_POST_VALUE)
                    .build();
        }

        Request build() {
            return new Request(requestBuilder.build());
        }
    }

}
