package ir.engineerpc.shoecenter.MyClass.FrgHistory;

public class HistoryRecyclerRowItem {

    int id, userId;
    String order, details, reciver, address, tell, customerDescr, sellerDescr,payment,refid,date;


    public HistoryRecyclerRowItem(int id, String order, String details, String reciver, String address, String tell,String payment,String refid,String date, String customerDescr, String sellerDescr, int userId) {
        this.id = id;
        this.order = order;
        this.details = details;
        this.reciver = reciver;
        this.address = address;
        this.tell = tell;
        this.customerDescr = customerDescr;
        this.sellerDescr = sellerDescr;
        this.payment=payment;
        this.refid=refid;
        this.date=date;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public String getPayment() {
        return payment;
    }

    public String getDate() {
        return date;
    }

    public String getRefid() {
        return refid;
    }

    public int getUserId() {
        return userId;
    }

    public String getAddress() {
        return address;
    }

    public String getCustomerDescr() {
        return customerDescr;
    }

    public String getDetails() {
        return details;
    }

    public String getOrder() {
        return order;
    }

    public String getReciver() {
        return reciver;
    }

    public String getSellerDescr() {
        return sellerDescr;
    }

    public String getTell() {
        return tell;
    }
}
