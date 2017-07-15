package lab.abhishek.apiaiimplementation.Models;

/**
 * Created by Abhishek on 15-Jul-17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Coupons {

    @SerializedName("coupon")
    @Expose
    public String coupon;
    @SerializedName("text")
    @Expose
    public String text;
    @SerializedName("url")
    @Expose
    public String url;
    @SerializedName("successRate")
    @Expose
    public String successRate;

    public Coupons() {
    }

    public Coupons(String coupon, String text, String url, String successRate) {
        this.coupon = coupon;
        this.text = text;
        this.url = url;
        this.successRate = successRate;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(String successRate) {
        this.successRate = successRate;
    }
}