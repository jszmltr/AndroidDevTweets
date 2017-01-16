package jackszm.androiddevtweets;

import java.util.List;

import jackszm.androiddevtweets.api.AccessTokenService;
import jackszm.androiddevtweets.domain.Tweet;
import jackszm.androiddevtweets.tweets.TweetsService;
import rx.Observer;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

class MainActivityPresenter {

    private final TweetsDisplayer tweetsDisplayer;
    private final Scheduler subscribeOnScheduler;
    private final Scheduler observeOnScheduler;
    private final TweetsService tweetsService;

    static MainActivityPresenter newInstance(MainActivity mainActivity) {
        Scheduler subscribeOnScheduler = Schedulers.io();
        Scheduler observeOnScheduler = AndroidSchedulers.mainThread();
        AccessTokenService accessTokenService = AccessTokenService.newInstance(mainActivity);
        TweetsService tweetsService = TweetsService.newInstance(accessTokenService);
        return new MainActivityPresenter(
                mainActivity,
                tweetsService,
                subscribeOnScheduler,
                observeOnScheduler
        );
    }

    MainActivityPresenter(
            TweetsDisplayer tweetsDisplayer,
            TweetsService tweetsService,
            Scheduler subscribeOnScheduler,
            Scheduler observeOnScheduler
    ) {
        this.tweetsDisplayer = tweetsDisplayer;
        this.tweetsService = tweetsService;
        this.subscribeOnScheduler = subscribeOnScheduler;
        this.observeOnScheduler = observeOnScheduler;
    }

    void startPresenting() {
        loadTweets();
        tweetsDisplayer.setRefreshListener(refreshListener);
    }

    private final RefreshListener refreshListener = new RefreshListener() {
        @Override
        public void refresh() {
            loadTweets();
        }
    };

    private void loadTweets() {
        tweetsService.loadTweets()
                .subscribeOn(subscribeOnScheduler)
                .observeOn(observeOnScheduler)
                .subscribe(new Observer<List<Tweet>>() {
                    @Override
                    public void onNext(List<Tweet> tweets) {
                        tweetsDisplayer.displayTweets(tweets);
                    }

                    @Override
                    public void onCompleted() {
                        // no-op
                    }

                    @Override
                    public void onError(Throwable e) {
                        tweetsDisplayer.displayError(e.getMessage());
                    }
                });
    }

    interface TweetsDisplayer {
        void displayTweets(List<Tweet> tweets);

        void setRefreshListener(RefreshListener refreshListener);

        void displayError(String message);
    }

    interface RefreshListener {
        void refresh();

    }
}
