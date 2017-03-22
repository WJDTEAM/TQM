package tqm.bianfeng.com.tqm.bank.bankfinancing;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.pojo.bank.BankFinancItem;
import tqm.bianfeng.com.tqm.pojo.bank.ListItemPositioin;

/**
 * Created by wjy on 2016/11/7.
 */

public class BankFinancingAdapter extends BaseAdapter {

    List<BankFinancItem> datas;

    private Context mContext;
    boolean isFistPage = false;

    public BankFinancingAdapter(Context mContext, List<BankFinancItem> datas, boolean isFistPage) {
        this.datas = datas;
        this.mContext = mContext;
        this.isFistPage = isFistPage;
    }

    @Override
    public int getCount() {
        return datas != null ? datas.size() : 0;
    }

    @Override
    public BankFinancItem getItem(int position) {
        Log.e("Daniel", "----getItem()---position--" + position);
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        Log.e("Daniel", "----getItemId()---position--" + position);
        return position;
    }

    @DebugLog
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.e("Daniel", "----getView()---position--" + position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Log.e("Daniel", "----getCount()-----" + getCount());
        BankFinancItem data = getItem(position);
        holder.financViewsLinear.setVisibility(View.VISIBLE);
        holder.institutionNameLinear.setVisibility(View.VISIBLE);
        holder.investmentTermLinear.setVisibility(View.VISIBLE);
        holder.purchaseMoneyAndRiskGradeNameLinear.setVisibility(View.VISIBLE);
        holder.titleTv.setText(data.getProductName());
        holder.institutionNameTv.setText(data.getInstitutionName());
        holder.annualReturnTv.setText(data.getAnnualReturn() + "%");
        holder.riskGradeNameTv.setText(data.getRiskGradeName());
        holder.investmentTermTv.setText(data.getInvestmentTerm());
        holder.purchaseMoneyTv.setText("" + data.getPurchaseMoney().setScale(0, BigDecimal.ROUND_DOWN));
        holder.financViewsTv.setText("" + data.getFinancViews());
        holder.rateNameTv.setText("预期年化");
        if (!isFistPage) {
            holder.linearlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new ListItemPositioin(position));
                }
            });
        }

        return convertView;
    }

    @DebugLog
    public void setdatas(List<BankFinancItem> decoCompanyItemList) {
        this.datas = decoCompanyItemList;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        @BindView(R.id.annualReturn_tv)
        TextView annualReturnTv;
        @BindView(R.id.rateName_tv)
        TextView rateNameTv;
        @BindView(R.id.title_tv)
        TextView titleTv;
        @BindView(R.id.loanTypeName_tv)
        TextView loanTypeNameTv;
        @BindView(R.id.loanTypeName_linear)
        LinearLayout loanTypeNameLinear;
        @BindView(R.id.riskGradeName_tv)
        TextView riskGradeNameTv;
        @BindView(R.id.riskGradeName_linear)
        LinearLayout riskGradeNameLinear;
        @BindView(R.id.loanMoney_tv)
        TextView loanMoneyTv;
        @BindView(R.id.loanMoney_linear)
        LinearLayout loanMoneyLinear;
        @BindView(R.id.purchaseMoney_tv)
        TextView purchaseMoneyTv;
        @BindView(R.id.purchaseMoney_linear)
        LinearLayout purchaseMoneyLinear;
        @BindView(R.id.investmentTerm_tv)
        TextView investmentTermTv;
        @BindView(R.id.investmentTerm_linear)
        LinearLayout investmentTermLinear;
        @BindView(R.id.institutionName_tv)
        TextView institutionNameTv;
        @BindView(R.id.institutionName_linear)
        LinearLayout institutionNameLinear;
        @BindView(R.id.financViews_tv)
        TextView financViewsTv;
        @BindView(R.id.financViews_linear)
        LinearLayout financViewsLinear;
        @BindView(R.id.linearlayout)
        LinearLayout linearlayout;
        @BindView(R.id.purchaseMoneyAndRiskGradeName_linear)
        LinearLayout purchaseMoneyAndRiskGradeNameLinear;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
