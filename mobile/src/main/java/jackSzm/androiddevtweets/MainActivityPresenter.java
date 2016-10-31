package jackszm.androiddevtweets;

import android.content.res.Resources;

import java.util.List;

import jackszm.androiddevtweets.domain.Tweet;
import jackszm.androiddevtweets.tweets.TweetsService;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

class MainActivityPresenter {

    private final TweetsDisplayer tweetsDisplayer;
    private final Scheduler subscribeOnScheduler;
    private final Scheduler observeOnScheduler;
    private final TweetsService tweetsService;
    private final String accessToken;

    static MainActivityPresenter newInstance(TweetsDisplayer tweetsDisplayer, Resources resources) {
        Scheduler subscribeOnScheduler = Schedulers.io();
        Scheduler observeOnScheduler = AndroidSchedulers.mainThread();
        TweetsService tweetsService = TweetsService.newInstance();
        return new MainActivityPresenter(
                tweetsService,
                tweetsDisplayer,
                subscribeOnScheduler,
                observeOnScheduler,
                resources.getString(R.string.twitter_authorization_key)
        );
    }

    MainActivityPresenter(
            TweetsService tweetsService,
            TweetsDisplayer tweetsDisplayer,
            Scheduler subscribeOnScheduler,
            Scheduler observeOnScheduler,
            String accessToken
    ) {
        this.tweetsDisplayer = tweetsDisplayer;
        this.subscribeOnScheduler = subscribeOnScheduler;
        this.observeOnScheduler = observeOnScheduler;
        this.tweetsService = tweetsService;
        this.accessToken = accessToken;
    }

    void startPresenting() {
        loadTweets();
    }

    private void loadTweets() {
        tweetsService.loadTweets(accessToken)
                .subscribeOn(subscribeOnScheduler)
                .observeOn(observeOnScheduler)
                .subscribe(displayTweets());
    }

    private Action1<List<Tweet>> displayTweets() {
        return new Action1<List<Tweet>>() {
            @Override
            public void call(List<Tweet> tweets) {
                tweetsDisplayer.displayTweets(tweets);
            }
        };
    }

    interface TweetsDisplayer {

        void displayTweets(List<Tweet> tweets);
    }
}
