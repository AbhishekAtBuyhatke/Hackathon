package lab.abhishek.apiaiimplementation;

/**
 * Created by Abhishek on 09-07-2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchResult {

    @Expose
    private String prod;        //product Name
    @Expose
    private Double price;       // product Price
    @Expose
    private String image;       // product image
    @Expose
    private String link;        //product url
    @Expose
    private Integer position;
    @SerializedName("del_time")
    @Expose
    private String delTime;     //product delivery Time
    @SerializedName("del_cost")
    @Expose
    private String delCost;         // product delivery cost
    @SerializedName("seller_review")
    @Expose
    private Double sellerReview;
    @Expose
    private Integer isNew;
    @Expose
    private Integer EMI;
    @Expose
    private Integer COD;
    @Expose
    private Integer inStock;
    @Expose
    private Integer contDetails;
    @Expose
    private String coupon;
    @Expose
    private String siteName;
    @Expose
    private String siteImage;
    @Expose
    private Integer min;            //min price in search result array
    @Expose
    private Integer max;            // max price in search result array
    @Expose
    private Long rangeMin;          // Start showing products starting from this price
    @Expose
    private Integer rangeMax;       // Show prices till this price

    /**
     * @return The prod
     */
    public String getProd() {
        return prod;
    }

    /**
     * @param prod The prod
     */
    public void setProd(String prod) {
        this.prod = prod;
    }

    /**
     * @return The price
     */
    public Double getPrice() {
        return price;
    }

    /**
     * @param price The price
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * @return The image
     */
    public String getImage() {
        return image;
    }

    /**
     * @param image The image
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * @return The link
     */
    public String getLink() {
        return link;
    }

    /**
     * @param link The link
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * @return The position
     */
    public Integer getPosition() {
        return position;
    }

    /**
     * @param position The position
     */
    public void setPosition(Integer position) {
        this.position = position;
    }

    /**
     * @return The delTime
     */
    public String getDelTime() {
        return delTime;
    }

    /**
     * @param delTime The del_time
     */
    public void setDelTime(String delTime) {
        this.delTime = delTime;
    }

    /**
     * @return The delCost
     */
    public String getDelCost() {
        return delCost;
    }

    /**
     * @param delCost The del_cost
     */
    public void setDelCost(String delCost) {
        this.delCost = delCost;
    }

    /**
     * @return The sellerReview
     */
    public Double getSellerReview() {
        return sellerReview;
    }

    /**
     * @param sellerReview The seller_review
     */
    public void setSellerReview(Double sellerReview) {
        this.sellerReview = sellerReview;
    }

    /**
     * @return The isNew
     */
    public Integer getIsNew() {
        return isNew;
    }

    /**
     * @param isNew The isNew
     */
    public void setIsNew(Integer isNew) {
        this.isNew = isNew;
    }

    /**
     * @return The EMI
     */
    public Integer getEMI() {
        return EMI;
    }

    /**
     * @param EMI The EMI
     */
    public void setEMI(Integer EMI) {
        this.EMI = EMI;
    }

    /**
     * @return The COD
     */
    public Integer getCOD() {
        return COD;
    }

    /**
     * @param COD The COD
     */
    public void setCOD(Integer COD) {
        this.COD = COD;
    }

    /**
     * @return The inStock
     */
    public Integer getInStock() {
        return inStock;
    }

    /**
     * @param inStock The inStock
     */
    public void setInStock(Integer inStock) {
        this.inStock = inStock;
    }

    /**
     * @return The contDetails
     */
    public Integer getContDetails() {
        return contDetails;
    }

    /**
     * @param contDetails The contDetails
     */
    public void setContDetails(Integer contDetails) {
        this.contDetails = contDetails;
    }

    /**
     * @return The coupon
     */
    public String getCoupon() {
        return coupon;
    }

    /**
     * @param coupon The coupon
     */
    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    /**
     * @return The siteName
     */
    public String getSiteName() {
        return siteName;
    }

    /**
     * @param siteName The siteName
     */
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    /**
     * @return The siteImage
     */
    public String getSiteImage() {
        return siteImage;
    }

    /**
     * @param siteImage The siteImage
     */
    public void setSiteImage(String siteImage) {
        this.siteImage = siteImage;
    }

    /**
     * @return The min
     */
    public Integer getMin() {
        return min;
    }

    /**
     * @param min The min
     */
    public void setMin(Integer min) {
        this.min = min;
    }

    /**
     * @return The max
     */
    public Integer getMax() {
        return max;
    }

    /**
     * @param max The max
     */
    public void setMax(Integer max) {
        this.max = max;
    }

    /**
     * @return The rangeMin
     */
    public Long getRangeMin() {
        return rangeMin;
    }

    /**
     * @param rangeMin The rangeMin
     */
    public void setRangeMin(Long rangeMin) {
        this.rangeMin = rangeMin;
    }

    /**
     * @return The rangeMax
     */
    public Integer getRangeMax() {
        return rangeMax;
    }

    /**
     * @param rangeMax The rangeMax
     */
    public void setRangeMax(Integer rangeMax) {
        this.rangeMax = rangeMax;
    }

}
