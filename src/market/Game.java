package market;

class Game {
    private String name;
    private String description;
    private double price;
    private String imagePath; // Path to the game's image

    public Game(String name, double price, String description, String imagePath) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;    
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public double getPrice() {
        return price;
    }

    public String getImagePath() {
        return imagePath;
    }
}
