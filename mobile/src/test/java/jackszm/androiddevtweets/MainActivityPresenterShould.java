package jackszm.androiddevtweets;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import jackszm.androiddevtweets.tweets.TweetsService;
import rx.Observable;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MainActivityPresenterShould {

    private static final List<Tweet> TWEETS = Collections.singletonList(
            Tweet.create("1212", "Tweet Content", "Author", "@author", URI.create("http://image.com/image.jpg"))
    );

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    TweetsService tweetsService;
    @Mock
    MainActivityPresenter.TweetsDisplayer tweetsDisplayer;

    MainActivityPresenter mainActivityPresenter;

    @Before
    public void setUp() throws Exception {
        mainActivityPresenter = new MainActivityPresenter(tweetsService, tweetsDisplayer, Schedulers.immediate(), Schedulers.immediate());
    }

    @Test
    public void displayTweetsReturnedByService_whenStartsPresenting() {
        when(tweetsService.loadTweets()).thenReturn(Observable.just(TWEETS));

        mainActivityPresenter.startPresenting();

        verify(tweetsDisplayer).displayTweets(TWEETS);
    }
}
