package jackszm.androiddevtweets.tweets;

import java.util.List;

import jackszm.androiddevtweets.api.Deserializer;
import jackszm.androiddevtweets.api.TwitterApi;
import jackszm.androiddevtweets.domain.Tweet;
import jackszm.androiddevtweets.domain.api.ApiTweet;
import rx.Observable;
import rx.functions.Func1;

public class TweetsService {

    private final TwitterApi twitterApi;
    private final Deserializer deserializer;
    private final TweetsConverter converter;

    public static TweetsService newInstance() {
        TwitterApi twitterApi = TwitterApi.newInstance();
        Deserializer deserializer = Deserializer.newInstance();
        TweetsConverter converter = new TweetsConverter();
        return new TweetsService(twitterApi, deserializer, converter);
    }

    TweetsService(TwitterApi twitterApi, Deserializer deserializer, TweetsConverter converter) {
        this.twitterApi = twitterApi;
        this.deserializer = deserializer;
        this.converter = converter;
    }

    public Observable<List<Tweet>> loadTweets(String accessToken) {
        return twitterApi.getAndroidDevTweets(accessToken)
                .map(deserialize())
                .map(convert());
    }

    private Func1<String, List<ApiTweet>> deserialize() {
        return new Func1<String, List<ApiTweet>>() {
            @Override
            public List<ApiTweet> call(String json) {
                return deserializer.deserializeList(json, ApiTweet.class);
            }
        };
    }

    private Func1<List<ApiTweet>, List<Tweet>> convert() {
        return new Func1<List<ApiTweet>, List<Tweet>>() {
            @Override
            public List<Tweet> call(List<ApiTweet> apiTweets) {
                return converter.convert(apiTweets);
            }
        };
    }

}


