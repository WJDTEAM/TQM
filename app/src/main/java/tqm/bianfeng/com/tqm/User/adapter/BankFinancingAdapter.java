package tqm.bianfeng.com.tqm.User.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.pojo.bank.BankFinancItem;

/**
 * Created by wjy on 2016/11/7.
 */

public class BankFinancingAdapter extends RecyclerView.Adapter<BankFinancingAdapter.ViewHolder> {

    List<BankFinancItem> datas;
    BankFinancingItemClickListener mItemClickListener;



    private Context mContext;
    private LayoutInflater mLayoutInflater;
    BankFinancItem data;


    public int getLayout() {
        return R.layout.listitem;
    }

    public BankFinancingAdapter(List<BankFinancItem> datas, Context mContext) {
        this.mContext = mContext;
        this.datas = datas;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(BankFinancingItemClickListener listener) {
        this.mItemClickListener = listener;
    }


    public interface BankFinancingItemClickListener {
        public void onItemClick(View view, int postion);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(getLayout(), parent, false);

        return new ViewHolder(view, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        data = datas.get(position);
        holder.loanMoneyTv.setVisibility(View.GONE);
        holder.loanTypeNameTv.setVisibility(View.GONE);
        holder.titleTv.setText(data.getProductName());
        holder.institutionNameTv.setText(data.getInstitutionName());
        holder.annualReturnTv.setText(data.getAnnualReturn() + "%");
        holder.riskGradeNameTv.setText("风险：" + data.getRiskGradeName());
        holder.investmentTermTv.setText("期限：" + data.getInvestmentTerm());
        holder.purchaseMoneyTv.setText("起购金额：" + data.getPurchaseMoney() + "");
        holder.financViewsTv.setText("" + data.getFinancViews());
        holder.rateNameTv.setText("预期年化");


    }

    @Override
    public int getItemCount() {
        return datas != null ? datas.size() : 0;
    }

    public void setdatas(List<BankFinancItem> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View rootView;
        BankFinancingItemClickListener mListener;
        @BindView(R.id.annualReturn_tv)
        TextView annualReturnTv;
        @BindView(R.id.rateName_tv)
        TextView rateNameTv;
        @BindView(R.id.title_tv)
        TextView titleTv;
        @BindView(R.id.loanTypeName_tv)
        TextView loanTypeNameTv;
        @BindView(R.id.riskGradeName_tv)
        TextView riskGradeNameTv;
        @BindView(R.id.investmentTerm_tv)
        TextView investmentTermTv;
        @BindView(R.id.loanMoney_tv)
        TextView loanMoneyTv;
        @BindView(R.id.purchaseMoney_tv)
        TextView purchaseMoneyTv;
        @BindView(R.id.institutionName_tv)
        TextView institutionNameTv;
        @BindView(R.id.financViews_tv)
        TextView financViewsTv;
        @BindView(R.id.linearlayout)
        LinearLayout linearlayout;


        ViewHolder(View view, BankFinancingItemClickListener listener) {
            super(view);
            rootView = view;
            ButterKnife.bind(this, view);
            this.mListener = listener;
            view.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(view, getPosition());
            }

        }
    }
}
