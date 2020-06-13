package ir.engineerpc.shoecenter.MyClass.FrgMain;


public class MainFrgRecyclerRowItems {

    private String imgUrl;
    private String tvTitle, tvDetails, catName;
    int id, pdid;

    public MainFrgRecyclerRowItems(int id, String imgUrl, String title, String details, int pdid, String catName) {
        this.id = id;
        this.imgUrl = imgUrl;
        this.tvTitle = title;
        this.tvDetails = details;
        this.pdid = pdid;
        this.catName = catName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getCatName() {
        return catName;
    }

    public String getTvDetails() {
        return tvDetails;
    }

    public String getTvTitle() {
        return tvTitle;
    }

    public int getId() {
        return id;
    }

    public int getPdid() {
        return pdid;
    }
}
