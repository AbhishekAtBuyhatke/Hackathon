package lab.abhishek.apiaiimplementation;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import lab.abhishek.apiaiimplementation.Models.Flight.ArrivalFlightInfo;
import lab.abhishek.apiaiimplementation.Models.Flight.FlightDetails;
import lab.abhishek.apiaiimplementation.Models.Flight.FlightJourney;
import lab.abhishek.apiaiimplementation.Models.Flight.FlightOption;
import lab.abhishek.apiaiimplementation.Models.Flight.FlightPricing;

/**
 * Created by Bhargav on 14/06/17.
 */

public class FlightResultAdapter extends RecyclerView.Adapter<FlightResultAdapter.FlightResultItem>{


    private static final String TAG = "FlightResultAdapter";
    private static final String NON_STOP_FLIGHT = "non-stop";
    private FlightJourney flightJourney;
    private Context mContext;
    private boolean isOutbound = true;

    public void appendFlightData(FlightJourney journey) {
        this.flightJourney = journey;
        notifyDataSetChanged();
        //Log.d(TAG, "------------->    " + journey.getFlightOptions().size());
    }

    public FlightResultAdapter(Context context) {
        mContext = context;
    }

    @Override
    public FlightResultItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flight, parent, false);
        return new FlightResultItem(view);
    }

    @Override
    public void onBindViewHolder(FlightResultItem holder, int position) {

        FlightOption flightOption = flightJourney.getFlightOptions().get(position);
        ArrivalFlightInfo flightInfo;
        if (isOutbound)
            flightInfo = flightOption.getDepartureFlightInfo();
        else
            flightInfo = flightOption.getArrivalFlightInfo();

        FlightPricing flightPricing = flightOption.getFlightPricing().get(0);
        FlightDetails flightDetails = flightInfo.getfLightDetails().get(0);

        Picasso.with(mContext).load(flightDetails.getFlightImage()).into(holder.flightImage);
        holder.flightCompany.setText(flightDetails.getFlightName());
        //holder.flightPrice.setText(((int)flightPricing.getFlightPrice()) + "");
        holder.flightPrice.setText(priceFormat(flightPricing.getFlightPrice()));
        holder.flightBookingCompanyName.setText(flightPricing.getAgentName());

        String arrivalTime = formatedFlightTime(flightInfo.getArrivalTime());
        String departureTime = formatedFlightTime(flightInfo.getDepartTime());
        holder.flightTiming.setText(departureTime + " - " + arrivalTime);

        String formatedTime = formatDuration(flightInfo.getDuration());
        if(flightInfo.getFlightStops() != null && flightInfo.getFlightStops().get(0).getStopCount() != 0) {
            int stop = flightInfo.getFlightStops().get(0).getStopCount();
            if (stop == 1)
                formatedTime = formatedTime + " | " + stop + " stop";
            else
                formatedTime = formatedTime + " | " + stop + " stops";
        }
        else
            formatedTime = formatedTime + " | " + NON_STOP_FLIGHT;
        holder.flightDuration.setText(formatedTime);
    }

    private String formatedFlightTime(String departTime) {
        String[] time = departTime.split(":");
        int hours = Integer.parseInt(time[0]);
        int minutes = Integer.parseInt(time[1]);
        String hr = "" + hours;
        String min = "" + minutes;
        if (hours < 10)
            hr = "0"+hr;
        if (minutes < 10)
            min = "0"+min;
        return hr  + ":" + min;

    }

    public String formatDuration(String timeInMins) {
        int time = Integer.parseInt(timeInMins);
        int hours = 0, minute = 0;
        if(time > 0) {
            hours = time / 60;
            minute = time % 60;
        }
        if(hours == 0)
            return minute +"m";
        else if(minute == 0)
            return hours + "h ";
        return hours + "h " + minute +"m";
    }

    @Override
    public int getItemCount() {
        if(flightJourney != null)
            return flightJourney.getFlightOptions().size();
        else
            return 0;
    }

    protected class FlightResultItem extends RecyclerView.ViewHolder implements View.OnClickListener{

        private LinearLayout itemParentLayout;
        private ImageView flightImage;
        private TextView flightTiming, flightPrice, flightCompany, flightDuration, flightBookingCompanyName;
        public FlightResultItem(View itemView) {
            super(itemView);
            itemParentLayout = (LinearLayout) itemView.findViewById(R.id.ll_parent_item);
            flightImage = (ImageView) itemView.findViewById(R.id.iv_flight_logo);
            flightTiming = (TextView) itemView.findViewById(R.id.tv_flight_timing);
            flightPrice = (TextView) itemView.findViewById(R.id.tv_flight_price);
            flightCompany = (TextView) itemView.findViewById(R.id.tv_flight_name);
            flightDuration = (TextView) itemView.findViewById(R.id.tv_flight_duration);
            flightBookingCompanyName = (TextView) itemView.findViewById(R.id.tv_booking_company_name);
            itemParentLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String flightDeepLink = flightJourney.getFlightOptions().get(getAdapterPosition()).
                    getFlightPricing().get(0).getFlightDeepLink();
            Intent flightIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(flightDeepLink));
            mContext.startActivity(flightIntent);
        }
    }

    public String priceFormat(double rawPrice){
        DecimalFormat df = new DecimalFormat("##,##,##0");
        return "₹ "+df.format(rawPrice);
    }
}
