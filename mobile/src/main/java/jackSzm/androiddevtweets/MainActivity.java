package jackszm.androiddevtweets;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.ataulm.rv.SpacesItemDecoration;

import java.util.List;

import jackszm.androiddevtweets.MainActivityPresenter.RefreshListener;
import jackszm.androiddevtweets.domain.Tweet;

import static android.R.color.*;

public class MainActivity extends AppCompatActivity implements MainActivityPresenter.TweetsDisplayer {

    private static final int SPAN_COUNT = 1;

    private MainActivityPresenter mainActivityPresenter;
    private TweetsAdapter adapter;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new TweetsAdapter();
        RecyclerView recyclerView = ((RecyclerView) findViewById(R.id.recycler_view));
        setupAdapter(recyclerView);

        swipeContainer = ((SwipeRefreshLayout) findViewById(R.id.swipe_container));
        setupSwipeContainer(swipeContainer);

        mainActivityPresenter = MainActivityPresenter.newInstance(this);
    }

    private void setupAdapter(RecyclerView recyclerView) {
        adapter.setHasStableIds(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        int spacing = getResources().getDimensionPixelSize(R.dimen.spacing);
        recyclerView.addItemDecoration(SpacesItemDecoration.newInstance(spacing, spacing, SPAN_COUNT));
        recyclerView.setAdapter(adapter);
    }

    public void setupSwipeContainer(SwipeRefreshLayout swipeContainer) {
        swipeContainer.setColorSchemeResources(
                holo_blue_bright,
                holo_green_light,
                holo_orange_light,
                holo_red_light
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainActivityPresenter.startPresenting();
    }

    @Override
    public void displayTweets(List<Tweet> tweets) {
        adapter.displayTweets(tweets);
        swipeContainer.setRefreshing(false);
    }

    @Override
    public void setRefreshListener(final RefreshListener refreshListener) {
        swipeContainer.setOnRefreshListener(createOnRefreshListener(refreshListener));
    }

    @Override
    public void displayError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }

    private SwipeRefreshLayout.OnRefreshListener createOnRefreshListener(final RefreshListener refreshListener) {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshListener.refresh();
            }
        };
    }

}
