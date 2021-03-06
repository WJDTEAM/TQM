package tqm.bianfeng.com.tqm.User.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.CustomView.DefaultLoadView;
import tqm.bianfeng.com.tqm.Institutions.CompanyInfoActivity;
import tqm.bianfeng.com.tqm.Institutions.adapter.LawFirmOrInstitutionListAdapter;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseFragment;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.InstitutionItem;
import tqm.bianfeng.com.tqm.pojo.User;

/**
 * Created by johe on 2017/4/10.
 */
//我的收藏
public class MyCollectionLawAndCompanyFragment extends BaseFragment {

    public int index = 0;
    public static String ARG_TYPE = "arg_type";
    @BindView(R.id.law_or_company_list)
    RecyclerView lawOrCompanyList;
    @BindView(R.id.default_loadview)
    DefaultLoadView defaultLoadview;
    List<InstitutionItem> datas;

    LawFirmOrInstitutionListAdapter lawFirmOrInstitutionListAdapter;

    public static MyCollectionLawAndCompanyFragment newInstance(int position) {
        MyCollectionLawAndCompanyFragment fragment = new MyCollectionLawAndCompanyFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_low_and_company, container, false);
        ButterKnife.bind(this, view);
        defaultLoadview.lodingIsFailOrSucess(1);
        datas = new ArrayList<>();
        //initData();
        initData();

        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            initData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void initData() {

        Subscription getBankFinancItem_subscription = NetWork.getInstitutionService().getCollectInstitutionItem("0" + (index + 1), realm.where(User.class).findFirst().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<InstitutionItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        defaultLoadview.lodingIsFailOrSucess(3);
                    }

                    @Override
                    public void onNext(List<InstitutionItem> institutionItems) {

                        datas=institutionItems;
                        Log.i("gqf", "institutionItems" + institutionItems.toString());

                        if(datas.size()==0){
                            defaultLoadview.lodingIsFailOrSucess(3);
                        }else{
                            defaultLoadview.lodingIsFailOrSucess(2);
                        }

                        initList(datas);
                        //initview();
                    }
                });

        compositeSubscription.add(getBankFinancItem_subscription);
    }


    Intent intent;

    public void initList(List<InstitutionItem> institutionItems) {
        Log.i("gqf","initList"+index);
        if (lawFirmOrInstitutionListAdapter == null) {
            lawFirmOrInstitutionListAdapter = new LawFirmOrInstitutionListAdapter(getActivity(), institutionItems);
            lawFirmOrInstitutionListAdapter.setOnItemClickListener(new LawFirmOrInstitutionListAdapter.MyItemClickListener() {
                @Override
                public void OnClickListener(int position) {
                    intent = new Intent(getActivity(), CompanyInfoActivity.class);
                    intent.putExtra("InstitutionId", datas.get(position).getInstitutionId());
                    CompanyInfoActivity.index = index;
                    EventBus.getDefault().post(intent);
                }

                @Override
                public void changePosition(int position) {
                    //lawFirmOrInstitutionListAdapter.notifyItemChanged(position);
                    datas.remove(position);
                    lawFirmOrInstitutionListAdapter.update(datas);
                    lawFirmOrInstitutionListAdapter.notifyDataSetChanged();
                }
            });
            lawOrCompanyList.setLayoutManager(new LinearLayoutManager(getActivity()));
            lawOrCompanyList.setAdapter(lawFirmOrInstitutionListAdapter);
        } else {
            lawFirmOrInstitutionListAdapter.update(institutionItems);
            lawFirmOrInstitutionListAdapter.notifyDataSetChanged();
        }

    }


    @Override
    public void onDetach() {
        super.onDetach();
    }
}
