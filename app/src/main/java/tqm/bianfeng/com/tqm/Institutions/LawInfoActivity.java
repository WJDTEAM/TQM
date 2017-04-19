package tqm.bianfeng.com.tqm.Institutions;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.CustomView.CoordinatorTabLayout;
import tqm.bianfeng.com.tqm.CustomView.MyViewPager;
import tqm.bianfeng.com.tqm.Institutions.adapter.MyPagerAdapter;
import tqm.bianfeng.com.tqm.Institutions.listener.LoadHeaderImagesListener;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseActivity;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.LawFirmOrInstitutionDetail;

/**
 * Created by johe on 2017/4/10.
 */

public class LawInfoActivity extends BaseActivity {

    private final String[] mTitles = {"旗下律师"};
    @BindView(R.id.vp)
    MyViewPager mViewPager;
    @BindView(R.id.coordinatortablayout)
    CoordinatorTabLayout coordinatortablayout;

    private int[] mColorArray;
    private ArrayList<Fragment> fragments;
    private View headerView;

    int InstitutionId;
    LawFirmOrInstitutionDetail data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_info);
        ButterKnife.bind(this);

        InstitutionId = getIntent().getIntExtra("InstitutionId", 0);


        initInfoData(InstitutionId);


    }

    public void initInfoData(int id) {
        Subscription getBankFinancItem_subscription = NetWork.getInstitutionService().getLawFirmDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LawFirmOrInstitutionDetail>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(LawFirmOrInstitutionDetail lawFirmOrInstitutionDetail) {
                                data = lawFirmOrInstitutionDetail;
                                Log.i("gqf", "data" + data.toString());
                                initView(data);

                    }
                });

        compositeSubscription.add(getBankFinancItem_subscription);

    }


    public void initView(LawFirmOrInstitutionDetail lawFirmOrInstitutionDetail) {
        mColorArray = new int[]{
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light};

        initFragment();
        initViewPager();
        initHeaderView();
        coordinatortablayout.setTitle(data.getInstitutionName())
                .setBackEnable(true)
                .setHeaderView(headerView)
                .setContentScrimColorArray(mColorArray)
                .setLoadHeaderImagesListener(new LoadHeaderImagesListener() {
                    @Override
                    public void loadHeaderImages(View mHeaderView, TabLayout.Tab tab) {

                    }
                })
                .setupWithViewPager(mViewPager);
        coordinatortablayout.setMyLinsener(new CoordinatorTabLayout.MyLinsener() {
            @Override
            public void openOrClose(boolean isOpen, int index) {
                if (!isOpen) {
                    setSystemBarColor(mColorArray[index]);
                } else {
                    setSystemBarColor(R.color.gary_dark);
                }
            }

            @Override
            public void onBack() {
                onBackPressed();
            }
        });

    }


    ImageView infoHeaderImg;
    TextView titleTxt;
    TextView profileTxt;
    LinearLayout callLin;
    LinearLayout collectionLin;
    TextView phoneNumTxt;
    TextView addressTxt;
    LinearLayout moreProfileLin;
    public void initHeaderView() {
        headerView = getLayoutInflater().from(this).inflate(R.layout.company_info_header_view, null, true);
        infoHeaderImg=(ImageView)headerView.findViewById(R.id.info_header_img);
        titleTxt=(TextView) headerView.findViewById(R.id.title_txt);
        profileTxt=(TextView) headerView.findViewById(R.id.profile_txt);
        callLin=(LinearLayout) headerView.findViewById(R.id.call_lin);
        collectionLin=(LinearLayout) headerView.findViewById(R.id.collection_lin);
        phoneNumTxt=(TextView) headerView.findViewById(R.id.phone_num_txt);
        addressTxt=(TextView) headerView.findViewById(R.id.address_txt);
        moreProfileLin=(LinearLayout) headerView.findViewById(R.id.more_profile_lin);

        Picasso.with(this).load(NetWork.LOAD+data.getInstitutionIcon()).placeholder(R.drawable.banklogo).into(infoHeaderImg);
        titleTxt.setText(data.getInstitutionName());
        profileTxt.setText("简介："+data.getProfile());
        callLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //电话
                if(data.getContact()!=null){
                    Intent intentPhone = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + data.getContact()));
                    intentPhone.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentPhone);
                }else{

                }
            }
        });
        collectionLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //收藏
            }
        });
        phoneNumTxt.setText("电话："+data.getContact());
        addressTxt.setText("地址："+data.getAddress());
        moreProfileLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更多简介
                Intent intent = new Intent(LawInfoActivity.this,MoreProfileActivity.class);
                intent.putExtra("Profile",data.getProfile());
                startActivity(intent);
            }
        });

    }

    public void initFragment() {
        fragments = new ArrayList<>();
        ActivityLoaninancingLawListFragment activityLoaninancingLawListFragment =ActivityLoaninancingLawListFragment.newInstance(1);
        activityLoaninancingLawListFragment.setLawDatas(data.getLawyers());
        Log.i("gqf","getLawyers"+data.getLawyers());
        fragments.add(activityLoaninancingLawListFragment);

    }

    private void initViewPager() {
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), fragments, mTitles));
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                coordinatortablayout.onTouchEvent(event);

                return false;
            }
        });
    }

}