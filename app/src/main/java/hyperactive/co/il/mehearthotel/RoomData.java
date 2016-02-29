package hyperactive.co.il.mehearthotel;

/**
 * Created by Tal on 31/01/2016.
 */
public class RoomData {
    int price;
    String type;

    public RoomData(int price, String type) {
        this.price = price;
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
