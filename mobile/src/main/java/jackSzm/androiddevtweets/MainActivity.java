package jackszm.androiddevtweets;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ataulm.rv.SpacesItemDecoration;

import jackszm.androiddevtweets.tweets.TweetsService;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final int SPAN_COUNT = 1;

    private MainActivityPresenter mainActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = ((RecyclerView) findViewById(R.id.recycler_view));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        int spacing = getResources().getDimensionPixelSize(R.dimen.spacing);
        recyclerView.addItemDecoration(SpacesItemDecoration.newInstance(spacing, spacing, SPAN_COUNT));

        TweetsAdapter adapter = new TweetsAdapter();
        recyclerView.setAdapter(adapter);

        TweetsService tweetsService = new TweetsService();
        Scheduler subscribeOnScheduler = Schedulers.io();
        Scheduler observeOnScheduler = AndroidSchedulers.mainThread();

        mainActivityPresenter = new MainActivityPresenter(tweetsService, adapter, subscribeOnScheduler, observeOnScheduler);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainActivityPresenter.startPresenting();
    }

}
