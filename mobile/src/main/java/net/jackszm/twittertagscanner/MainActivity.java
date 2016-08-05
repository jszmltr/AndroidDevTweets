package net.jackszm.twittertagscanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
import com.ataulm.rv.SpacesItemDecoration;


    private RecyclerView recyclerView;
    private TweetsAdapter adapter;
    private static final int SPAN_COUNT = 1;

    private MainActivityPresenter mainActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = ((RecyclerView) findViewById(R.id.recycler_view));
        adapter = new TweetsAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        int spacing = getResources().getDimensionPixelSize(R.dimen.spacing);
        recyclerView.addItemDecoration(SpacesItemDecoration.newInstance(spacing, spacing, SPAN_COUNT));
        recyclerView.setAdapter(adapter);

        mainActivityPresenter = new MainActivityPresenter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainActivityPresenter.startPresenting();
    }

}
