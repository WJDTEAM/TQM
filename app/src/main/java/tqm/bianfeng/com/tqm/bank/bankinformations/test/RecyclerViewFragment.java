package tqm.bianfeng.com.tqm.bank.bankinformations.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.main.DetailActivity;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.bank.BankInformItem;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class RecyclerViewFragment extends Fragment {

    private static final boolean GRID_LAYOUT = false;
    private static final int ITEM_COUNT = 100;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
//    @BindView(R.id.load_more_list_view_container)
//    LoadMoreListViewContainer loadMoreListViewContainer;

    private int position = 0;
    private int pagNum = 1;
    private int mPagItemSize = 0;
    private CompositeSubscription mCompositeSubscription;
    List<BankInformItem> AllBankInformItems;

    public static RecyclerViewFragment newInstance(int position) {
        RecyclerViewFragment recyclerViewFragment = new RecyclerViewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        recyclerViewFragment.setArguments(bundle);
        return recyclerViewFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt("position");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private void initDate(Integer type, int pagNum) {
        Log.e("Daniel", "---type---" + type);
        Log.e("Daniel", "---pagNum---" + pagNum);
        Subscription getBankInformItem_subscription = NetWork.getBankService()
                .getBankInformItem(type, Constan.HOMESHOW_FALSE, pagNum, Constan.PAGESIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BankInformItem>>() {
                    @Override
                    public void onCompleted() {


                    }

                    @DebugLog
                    @Override
                    public void onError(Throwable e) {
//                        loadMoreListViewContainer.loadMoreError(0, errorMessage);
                    }

                    @DebugLog
                    @Override
                    public void onNext(List<BankInformItem> bankInformItems) {
                        mPagItemSize = bankInformItems.size();
//                        if (AllBankInformItems==null){
//                            AllBankInformItems = new ArrayList<>();
//                        }
//                        AllBankInformItems.addAll(bankInformItems);
                        setBankInformItemAdapter(bankInformItems);

                    }
                });
        mCompositeSubscription.add(getBankInformItem_subscription);
    }

    public interface mListener {
        public void detailActivity(Intent intent);
    }

    private mListener mListener;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (mListener) activity;

    }

    @DebugLog
    private void setBankInformItemAdapter(List<BankInformItem> bankInformItems) {
        //        BankInfromationAdapter bankInfromationAdapter = new BankInfromationAdapter(getActivity(),bankInformItems);
        if (GRID_LAYOUT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        mRecyclerView.setHasFixedSize(true);

        //Use this now
        mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
        final TestRecyclerViewAdapter recyclerViewAdapter = new TestRecyclerViewAdapter(getActivity(), bankInformItems);
        mRecyclerView.setAdapter(recyclerViewAdapter);
//        loadMoreListViewContainer.loadMoreFinish(false, true);
        recyclerViewAdapter.setOnItemClickListener(new TestRecyclerViewAdapter.BankInformItemClickListener() {
            @Override
            public void OnClickListener(int position) {
                //跳转银行贷款详情
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("detailType", "04");
                intent.putExtra("detailId", recyclerViewAdapter.getItem(position).getInformId());
                mListener.detailActivity(intent);
            }
        });

        //        mainPullRefreshLv.setAdapter(new BankInfromationAdapter(getActivity(), bankInformItems));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mCompositeSubscription = new CompositeSubscription();
        //        initRefreshlv();
        initDate(position + 1, pagNum);
//
//        loadMoreListViewContainer.setAutoLoadMore(true);//设置是否自动加载更多
//        loadMoreListViewContainer.useDefaultHeader();
//        //5.添加加载更多的事件监听
//        loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
//            @Override
//            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
//                //模拟加载更多的业务处理
//                loadMoreListViewContainer.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        pagNum = pagNum+1;
//                        initDate(position + 1, pagNum);
//                    }
//                }, 1000);
//            }
//        });
    }
}
