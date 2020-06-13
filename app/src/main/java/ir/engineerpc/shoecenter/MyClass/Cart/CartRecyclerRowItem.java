package ir.engineerpc.shoecenter.MyClass.Cart;

public class CartRecyclerRowItem {
    private String image;
    private String title;
    private String price, size, color, amount;
    private int id;

    public CartRecyclerRowItem(int id, String imageAddres, String title, String price, String size, String color, String amount) {
        this.image = imageAddres;
        this.title = title;
        this.price = price;
        this.size = size;
        this.color = color;
        this.amount = amount;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getAmount() {
        return amount;
    }

    public String getColor() {
        return color;
    }

    public String getPrice() {
        return price;
    }

    public String getSize() {
        return size;
    }
}
