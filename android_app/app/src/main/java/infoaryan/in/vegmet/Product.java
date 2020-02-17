package infoaryan.in.vegmet;

public class Product {


    private String title,desc;
    private int price;

    public Product( String title, String desc, int price) {

        this.title = title;
        this.desc = desc;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public int getPrice() {
        return price;
    }
}
