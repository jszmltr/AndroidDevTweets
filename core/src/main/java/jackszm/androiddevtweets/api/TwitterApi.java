package jackszm.androiddevtweets.api;

import java.util.concurrent.Callable;

import rx.Observable;

public class TwitterApi {

    private static final String BASE_URL = "https://api.twitter.com/";

    private static final String URL_GET_ANDROID_DEV_TWEETS = "1.1/statuses/user_timeline.json?count=100&screen_name=androiddevRTbot";
    private static final String URL_GET_ACCESS_TOKEN = "oauth2/token";

    private final RequestExecutor requestExecutor;

    public static TwitterApi newInstance() {
        RequestExecutor requestExecutor = RequestExecutor.newInstance();
        return new TwitterApi(requestExecutor);
    }

    TwitterApi(RequestExecutor requestExecutor) {
        this.requestExecutor = requestExecutor;
    }

    public Observable<String> getAndroidDevTweets(final String accessToken) {
        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Request request = Request.builder(BASE_URL)
                        .path(URL_GET_ANDROID_DEV_TWEETS)
                        .bearerAuthorization(accessToken)
                        .build();

                return requestExecutor.executeRequest(request);
            }

        });
    }

}
