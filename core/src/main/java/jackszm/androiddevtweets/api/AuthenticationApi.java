package jackszm.androiddevtweets.api;

import java.util.concurrent.Callable;

import rx.Observable;

public class AuthenticationApi {

    private static final String BASE_URL = "https://api.twitter.com/";
    private static final String URL_POST_TOKEN = "oauth2/token";

    private final RequestExecutor requestExecutor;

    public static AuthenticationApi newInstance() {
        RequestExecutor requestExecutor = RequestExecutor.newInstance();
        return new AuthenticationApi(requestExecutor);
    }

    public AuthenticationApi(RequestExecutor requestExecutor) {
        this.requestExecutor = requestExecutor;
    }

    public Observable<String> getAccessTokenUsing(final String authorizationKey) {
        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() {
                Request request = buildRequest(authorizationKey);
                return requestExecutor.executeRequest(request);
            }
        });
    }

    private Request buildRequest(String authorizationKey) {
        return Request.builder(BASE_URL)
                .path(URL_POST_TOKEN)
                .basicAuthorization(authorizationKey)
                .build();
    }

}
