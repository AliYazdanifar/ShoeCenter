package ir.engineerpc.shoecenter.MyClass.FrgMessage;

public class MessageRecyclerRowItem {
    int id;
    String title, text, img, link;

    public MessageRecyclerRowItem(int id, String title, String text, String img, String link) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.img = img;
        this.link = link;

    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getImg() {
        return img;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }
}
