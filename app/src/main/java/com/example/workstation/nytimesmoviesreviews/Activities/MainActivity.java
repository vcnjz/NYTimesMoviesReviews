package com.example.workstation.nytimesmoviesreviews.Activities;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.example.workstation.nytimesmoviesreviews.Adapters.ReviewAdapter;
import com.example.workstation.nytimesmoviesreviews.App;
import com.example.workstation.nytimesmoviesreviews.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.ReplaySubject;

public class MainActivity extends AppCompatActivity {

    private static final String RESOURCE_TYPE_ALL = "all";

    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    @BindView(R.id.empty)
    LinearLayout llEmpty;

    @BindView(R.id.btn_retry)
    Button btnRetry;

    private ReplaySubject<Integer> downloadSubject;
    private int offset = 0;
    private ReviewAdapter adapter;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecycler.setLayoutManager(layoutManager);
        adapter = new ReviewAdapter();
        mRecycler.setAdapter(adapter);

        refreshLayout.setOnRefreshListener(this::refresh);
        refresh();
        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (offset > 0 && dy > 0) {
                    if (layoutManager.findLastVisibleItemPosition() >= offset-5) {
                        downloadSubject.onNext(offset);
                    }
                }

            }
        });
        btnRetry.setOnClickListener((view) -> refresh());
    }

    private void refresh() {
        adapter.clear();
        offset = 0;
        downloadSubject = ReplaySubject.create();
        downloadSubject.onNext(offset);

        downloadSubject
                .distinctUntilChanged()
                .observeOn(Schedulers.io())
                .concatMap(offset -> App.getApi().getReviews(RESOURCE_TYPE_ALL, offset, App.API_KEY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(results -> {
                    llEmpty.setVisibility(View.GONE);
                    mRecycler.setVisibility(View.VISIBLE);
                    this.offset = this.offset + results.getResults().size();
                    adapter.add(results.getResults());
                    refreshLayout.setRefreshing(false);
                }, throwable -> {
                    mRecycler.setVisibility(View.GONE);
                    llEmpty.setVisibility(View.VISIBLE);
                });
    }
}
