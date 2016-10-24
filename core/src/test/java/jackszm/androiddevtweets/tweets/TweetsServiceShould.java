package jackszm.androiddevtweets.tweets;

import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import jackszm.androiddevtweets.api.Deserializer;
import jackszm.androiddevtweets.api.TwitterApi;
import jackszm.androiddevtweets.domain.Tweet;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.BDDMockito.given;

public class TweetsServiceShould {

    private static final String ANY_ACCES_TOKEN = "access_token";
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    TwitterApi twitterApi;
    @Mock
    Deserializer deserializer;

    private TweetsService tweetsService;

    @Before
    public void setUp() throws Exception {
        tweetsService = new TweetsService(twitterApi, deserializer);
    }

    @Test
    public void loadTweets() {
        given(twitterApi.getAndroidDevTweets(ANY_ACCES_TOKEN)).willReturn(expectedApiTweets());

        Observable<List<Tweet>> listObservable = tweetsService.loadTweets(ANY_ACCES_TOKEN);
        TestSubscriber<List<Tweet>> subscriber = new TestSubscriber<>();
        listObservable.subscribe(subscriber);

        System.out.println(subscriber.getOnNextEvents().get(0));
    }

    private Observable<String> expectedApiTweets() {
        return Observable.just(
                "[" +
                        "  {" +
                        "    \"id\": 786328858542866433," +
                        "    \"retweeted_status\": {" +
                        "      \"text\": \"This looks amazing. I will be checking this out. #AndroidDev #GoogleSamples https://t.co/iDlDHHHyP4\"," +
                        "      \"user\": {" +
                        "        \"name\": \"Alistair Sykes\"," +
                        "        \"screen_name\": \"SykesAlistair\"," +
                        "        \"profile_image_url\": \"http://pbs.twimg.com/profile_images/774589500194447360/4W-h_0iY_normal.jpg\"," +
                        "      }" +
                        "    }" +
                        "  }," +
                        "  {" +
                        "    \"id\": 786326341520154624," +
                        "    \"retweeted_status\": {" +
                        "      \"text\": \"We are hiring: Software Development Engineer, Android Software Development  https://t.co/yqaB0frjTL #job @AmazonMiddleMile #androiddev\"," +
                        "      \"user\": {" +
                        "        \"name\": \"Shauna Goulet\"," +
                        "        \"screen_name\": \"ShaunaGoulet\"," +
                        "        \"profile_image_url\": \"http://pbs.twimg.com/profile_images/558745978560204800/IT2YFjII_normal.jpeg\"," +
                        "      }" +
                        "    }" +
                        "  }" +
                        "]"
        );
    }
}
