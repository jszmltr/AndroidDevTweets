package net.jackszm.twittertagscanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private TweetsAdapter adapter;

    private MainActivityPresenter mainActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = ((RecyclerView) findViewById(R.id.recycler_view));
        adapter = new TweetsAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        mainActivityPresenter = new MainActivityPresenter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainActivityPresenter.startPresenting();
    }

}
