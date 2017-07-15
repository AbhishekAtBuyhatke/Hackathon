package lab.abhishek.apiaiimplementation.accessiblity;

public class ProductPageEvent {

    public String product;
    public boolean isProductPage;
    public boolean isProductVisible;
    public int appType;
    public boolean isFlightApp;
    public FlightData flightData;

    public ProductPageEvent(boolean isProductPage, boolean isProductVisible, String product) {
        this.isProductPage = isProductPage;
        this.isProductVisible = isProductVisible;
        this.product = product;
    }


    public ProductPageEvent(FlightData flightData, boolean isFlightPage) {
        this.flightData = flightData;
        this.isFlightApp = isFlightPage;
    }

    public ProductPageEvent() {

    }

    @Override
    public String toString() {
        return "ProductPageEvent{" +
                "product='" + product + '\'' +
                ", isProductPage=" + isProductPage +
                ", isProductVisible=" + isProductVisible +
                ", appType=" + appType +
                ", isFlightApp=" + isFlightApp +
                ", flightData=" + flightData +
                '}';
    }
}
