package ir.engineerpc.shoecenter.MyClass.ViewPd;

public class ViewPdRecyclerRowItems {

    private int id, off, pdId, rate,amount;
    private String cardTitle, madeIn, price, img1, img2, img3, img4, img5, description, size,colors;

    public ViewPdRecyclerRowItems(int id, String title, String madein, String price, int amount, int rate, String img1, String img2, String img3, String img4,
                                  String img5, String descr, int off, String size,String colors, int pdId) {
        this.id = id;
        this.cardTitle = title;
        this.madeIn = madein;
        this.price = price;
        this.amount = amount;
        this.rate = rate;
        this.img1 = img1;
        this.img2 = img2;
        this.img3 = img3;
        this.img4 = img4;
        this.img5 = img5;
        this.description = descr;
        this.off = off;
        this.size = size;
        this.colors = colors;
        this.pdId = pdId;

    }

    public int getId() {
        return id;
    }


    public String getProductTitle() {
        return cardTitle;
    }

    public String getMadeIn() {
        return madeIn;
    }

    public String getProductPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    public int getRate() {
        return rate;
    }

    public String getImg1() {
        return img1;
    }

    public String getImg2() {
        return img2;
    }

    public String getImg3() {
        return img3;
    }

    public String getImg4() {
        return img4;
    }

    public String getImg5() {
        return img5;
    }

    public int getOff() {
        return off;
    }

    public String getDescription() {
        return description;
    }

    public int getPdId() {
        return pdId;
    }

    public String getPrice() {
        return price;
    }

    public String getSize() {
        return size;
    }

    public String getColors() {
        return colors;
    }
}

