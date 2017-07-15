package lab.abhishek.apiaiimplementation;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import lab.abhishek.apiaiimplementation.Models.Coupons;

/**
 * Created by Abhishek on 15-Jul-17.
 */

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ViewHolder> {

    private Coupons[] couponlist;

    public CouponAdapter(Coupons[] couponlist){
        this.couponlist = couponlist;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coupons_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Coupons coupons = couponlist[position];
        holder.tv_code.setText(coupons.getCoupon());
        holder.tv_desc.setText(coupons.getText());
        holder.tv_sucess.setText(""+(int)(Double.parseDouble(coupons.getSuccessRate())));
    }

    @Override
    public int getItemCount() {
        return couponlist.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_code, tv_desc, tv_sucess;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_code = (TextView) itemView.findViewById(R.id.coupons_code);
            tv_desc = (TextView) itemView.findViewById(R.id.coupons_text);
            tv_sucess = (TextView) itemView.findViewById(R.id.coupons_success_rate);
        }
    }
}
