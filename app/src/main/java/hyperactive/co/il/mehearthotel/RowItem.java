package hyperactive.co.il.mehearthotel;

import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;

/**
 * Created by Tal on 11/01/2016.
 */
public class RowItem implements Serializable {
    private String description;
    private int image;
    private int price;


    public RowItem(String description, int price, int image) {
        this.description = description;
        this.image = image;
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getImage() {
        return image;
    }
}
