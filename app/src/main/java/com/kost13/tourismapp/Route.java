package com.kost13.tourismapp;

import java.util.List;

public class Route {
    private String title;
    private String description;
    private String image;
    private List<Point> points;
    private String id;

    public Route() {}

    public Route(String title, String description, String image, List<Point> points){
        setTitle(title);
        setDescription(description);
        setImage(image);
        setPoints(points);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public double computeLength(){
        //TODO compute length
        return 10.5;
    }

    public int getPoisNumber(){
        //TODO get pois
        return 2;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
