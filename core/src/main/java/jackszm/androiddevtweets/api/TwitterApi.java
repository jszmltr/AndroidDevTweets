package jackszm.androiddevtweets.api;

import java.util.concurrent.Callable;

import rx.Observable;

public class TwitterApi {

    static final String BASE_URL = "https://api.twitter.com/";

    private static final String URL_GET_ANDROID_DEV_TWEETS = "1.1/statuses/user_timeline.json?count=100&screen_name=androiddevRTbot";
    static final String URL_POST_TOKEN = "oauth2/token";

    private final RequestExecutor requestExecutor;
    private final AuthenticationService authenticationService;

    public static TwitterApi newInstance(AuthenticationService authenticationService) {
        RequestExecutor requestExecutor = RequestExecutor.newInstance();
        return new TwitterApi(authenticationService, requestExecutor);
    }

    TwitterApi(AuthenticationService authenticationService, RequestExecutor requestExecutor) {
        this.authenticationService = authenticationService;
        this.requestExecutor = requestExecutor;
    }

    public Observable<String> getAndroidDevTweets() {
        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() {
                String accessToken = authenticationService.getAccessToken();
                Request request = Request.builder(BASE_URL)
                        .path(URL_GET_ANDROID_DEV_TWEETS)
                        .bearerAuthorization(accessToken)
                        .build();

                return requestExecutor.executeRequest(request);
            }
        });
    }

}
