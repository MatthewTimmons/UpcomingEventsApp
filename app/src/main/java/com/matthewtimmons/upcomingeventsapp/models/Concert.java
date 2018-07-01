package com.matthewtimmons.upcomingeventsapp.models;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Concert {

    public static List<Concert> getPlaceholderConcerts() {
        List<Concert> listOfConcerts = new ArrayList<>();
        listOfConcerts.add(new Concert(Arrays.asList("Band 1", "Band 2", "Band 3"),
                "Gas Monkey Bar and Grill, Dallas Texas",
                "01/01/2018",
                "https://imageservicestoreag.blob.core.windows.net/imagecatalog/d572abbe68a743c4854fde4deae5e440v1.jpeg"));
        listOfConcerts.add(new Concert(Arrays.asList("Band 1", "Band 2", "Band 3"),
                "Gas Monkey Bar and Grill, Dallas Texas",
                "01/01/2018",
                "https://imageservicestoreag.blob.core.windows.net/imagecatalog/d572abbe68a743c4854fde4deae5e440v1.jpeg"));
        listOfConcerts.add(new Concert(Arrays.asList("Band 1", "Band 2", "Band 3"),
                "Gas Monkey Bar and Grill, Dallas Texas",
                "01/01/2018",
                "https://imageservicestoreag.blob.core.windows.net/imagecatalog/d572abbe68a743c4854fde4deae5e440v1.jpeg"));
        listOfConcerts.add(new Concert(Arrays.asList("Band 1", "Band 2", "Band 3"),
                "Gas Monkey Bar and Grill, Dallas Texas",
                "01/01/2018",
                "https://imageservicestoreag.blob.core.windows.net/imagecatalog/d572abbe68a743c4854fde4deae5e440v1.jpeg"));
        listOfConcerts.add(new Concert(Arrays.asList("Band 1", "Band 2", "Band 3"),
                "Gas Monkey Bar and Grill, Dallas Texas",
                "01/01/2018",
                "https://imageservicestoreag.blob.core.windows.net/imagecatalog/d572abbe68a743c4854fde4deae5e440v1.jpeg"));
        listOfConcerts.add(new Concert(Arrays.asList("Band 1", "Band 2", "Band 3"),
                "Gas Monkey Bar and Grill, Dallas Texas",
                "01/01/2018",
                "https://imageservicestoreag.blob.core.windows.net/imagecatalog/d572abbe68a743c4854fde4deae5e440v1.jpeg"));
        return listOfConcerts;
    }

    private List<String> bands;
    private String location;
    public String date;
    private String imageUrl;

    public Concert(List<String> bands, String location, String date, String imageUrl) {
        this.bands = bands;
        this.location = location;
        this.date = date;
        this.imageUrl = imageUrl;
    }

    public String getBandsDisplayName() {
        return TextUtils.join(",", bands);
    }

    public Date getDateFormattedDate(String input) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd//MM/yyyy");
        Date d = null;
        try {
            d = sdf.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    public List<String> getBands() {
        return bands;
    }

    public void setBands(List<String> bands) {
        this.bands = bands;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
