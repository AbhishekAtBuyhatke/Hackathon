package lab.abhishek.apiaiimplementation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import lab.abhishek.apiaiimplementation.Models.SearchResult;

/**
 * Created by Abhishek on 09-Jul-17.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private SearchResult[] mSearchResults;
    private Context context;

    public SearchAdapter(SearchResult[] searchResults, Context context){
        mSearchResults = searchResults;
        this.context = context;
    }

    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchAdapter.ViewHolder holder, int position) {
        final SearchResult result = mSearchResults[position];
        holder.productName.setText(result.getProd());
        holder.productPrice.setText("\u20B9" + getIntegerFromDouble(result.getPrice()));
        setShippingPriceText(holder, position);
        setShipingTimeText(holder, position);
        String siteIcon = mSearchResults[position].getSiteImage();
        holder.alertIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //               if(appId != null)
               // doSetAlert(mSearchResults[searchPosition].getLink());
            }
        });

        if(siteIcon != null){
            String temp = siteIcon.substring(35);
            String iconUrl= "https://compare.buyhatke.com/images/site_icons_m/"+temp;
            Picasso.with(context).load(iconUrl).fit().centerInside().into(holder.siteIcon);
        }
        if(mSearchResults[position].getImage() != ""){
            Picasso.with(context).load(mSearchResults[position].getImage()).fit().centerInside().into(holder.productImage);
        }
    }

    private int getIntegerFromDouble(double num){
        return (int)num;
    }

    @Override
    public int getItemCount() {
        return mSearchResults.length;
    }

    private void setShippingPriceText(ViewHolder holder, int searchPosition){
        String shippingPrice = mSearchResults[searchPosition].getDelCost();
        if(shippingPrice.equals("0") || shippingPrice.equals("-1")){
            holder.shippingPrice.setText("Free" + "\n" + "Shipping");
        } else {
            holder.shippingPrice.setText(shippingPrice + "\n" + "Shipping");
        }
    }

    private void setShipingTimeText(ViewHolder holder, int searchPosition){
        String shippingTime = mSearchResults[searchPosition].getDelTime();
        if(shippingTime.equals("VARIABLE") || shippingTime.equals("N/A") ){
            holder.shippingTime.setText("N/A");
        } else {
            holder.shippingTime.setText(shippingTime + "\n" + "DAYS");
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //private final ArrayList<Integer> indicesToShow;
        private TextView productName;
        private TextView productPrice;
        private TextView shippingPrice;
        private TextView shippingTime;
        private ImageView productImage;
        private ImageView siteIcon;
        private Button alertIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            //this.indicesToShow = indicesToShow;
            productName = (TextView) itemView.findViewById(R.id.product_title);
            productPrice = (TextView) itemView.findViewById(R.id.product_price);
            shippingPrice = (TextView) itemView.findViewById(R.id.shipping_price);
            shippingTime = (TextView) itemView.findViewById(R.id.shipping_time);
            productImage = (ImageView) itemView.findViewById(R.id.product_image);
            siteIcon = (ImageView) itemView.findViewById(R.id.site_icon);
            alertIcon = (Button) itemView.findViewById(R.id.set_alert);
        }
    }
}
