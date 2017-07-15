package lab.abhishek.apiaiimplementation.Models;

/**
 * Created by Abhishek on 15-Jul-17.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CouponsStore {

    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("pos")
    @Expose
    public String pos;
    @SerializedName("text")
    @Expose
    public String text;

    public CouponsStore() {
    }

    public CouponsStore(String image, String name, String pos, String text) {
        this.image = image;
        this.name = name;
        this.pos = pos;
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
