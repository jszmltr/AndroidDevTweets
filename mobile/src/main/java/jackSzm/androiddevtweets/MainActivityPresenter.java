package jackszm.androiddevtweets;

import java.util.List;

import jackszm.androiddevtweets.tweets.TweetsService;
import rx.Scheduler;
import rx.functions.Action1;

class MainActivityPresenter {

    private final TweetsDisplayer tweetsDisplayer;
    private final Scheduler subscribeOnScheduler;
    private final Scheduler observeOnScheduler;
    private final TweetsService tweetsService;

    public MainActivityPresenter(
            TweetsService tweetsService,
            TweetsDisplayer tweetsDisplayer,
            Scheduler subscribeOnScheduler,
            Scheduler observeOnScheduler
    ) {
        this.tweetsDisplayer = tweetsDisplayer;
        this.subscribeOnScheduler = subscribeOnScheduler;
        this.observeOnScheduler = observeOnScheduler;
        this.tweetsService = tweetsService;
    }

    public void startPresenting() {
        loadTweets();
    }

    private void loadTweets() {
        tweetsService.loadTweets()
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
