package tqm.bianfeng.com.tqm.bank.bankinformations;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.pojo.bank.BankInformItem;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class TestRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<BankInformItem> datas;
    Context mContext;

    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;


    public TestRecyclerViewAdapter(Context mContext, List<BankInformItem> datas) {
        this.datas = datas;
        this.mContext = mContext;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_HEADER;
            default:
                return TYPE_CELL;
        }
    }

    @Override
    public int getItemCount() {
        return datas!=null ? datas.size():0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_bankinformation_item, parent, false);

        //        switch (viewType) {
        //            case TYPE_HEADER: {
        //                view = LayoutInflater.from(parent.getContext())
        //                        .inflate(R.layout.list_item_card_big, parent, false);
        //                return new RecyclerView.ViewHolder(view) {
        //                };
        //            }
        //            case TYPE_CELL: {
        //                view = LayoutInflater.from(parent.getContext())
        //                        .inflate(R.layout.list_item_card_small, parent, false);
        //                return new RecyclerView.ViewHolder(view) {
        //                };
        //            }
        //        }
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        switch (getItemViewType(position)) {
//            case TYPE_HEADER:
//                break;
//            case TYPE_CELL:
//                break;
//        }
        ViewHolder viewHolder = (ViewHolder) holder;
        BankInformItem data = datas.get(position);
        viewHolder.institutionNameTv.setText(data.getInstitutionName());
        viewHolder.timeTv.setText(data.getReleaseDate());
        viewHolder.TitleTv.setText(data.getInformTitle());
        viewHolder.ViewsTv.setText(data.getInformViews()+"");
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.Title_tv)
        TextView TitleTv;
        @BindView(R.id.institutionName_tv)
        TextView institutionNameTv;
        @BindView(R.id.time_tv)
        TextView timeTv;
        @BindView(R.id.Views_tv)
        TextView ViewsTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }


    }
}