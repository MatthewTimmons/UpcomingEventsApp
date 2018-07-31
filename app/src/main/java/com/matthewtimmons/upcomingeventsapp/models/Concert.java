package com.matthewtimmons.upcomingeventsapp.models;

import android.text.TextUtils;

import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Concert extends Event implements Serializable {
    private List<String> bands;
    private String concertLocation;

    public Concert(DocumentSnapshot documentSnapshot) {
        super(documentSnapshot);
        this.bands = (List<String>) documentSnapshot.get("concertBandsArray");
        this.concertLocation = documentSnapshot.getString("concertLocation");
    }

    public String getBandName(int position) {
        return bands != null &&
                position >= 0 &&
                position < bands.size() ? bands.get(position) : null;
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

    public String getConcertLocation() { return concertLocation; }

    public void setConcertLocation(String concertLocation) { this.concertLocation = concertLocation; }


//    public static List<Concert> getPlaceholderConcerts() {
//        List<Concert> listOfConcerts = new ArrayList<>();
//        listOfConcerts.add(new Concert(Arrays.asList("Journey", "Def Leppard"),
//                "American Airlines Center, Dallas TX",
//                "07/29/2018",
//                "http://ultimateclassicrock.com/files/2018/05/Logos.jpg?w=980&q=75"));
//        listOfConcerts.add(new Concert(Arrays.asList("Avenged Sevenfold", "Prophets of Rage"),
//                "Dos Equis Pavilion, Dallas Texas",
//                "09/01/2018",
//                "https://www.altitudetickets.com/assets/img/Avenged-Sevenfold-Event-2018-7f0d260668.jpg"));
//        listOfConcerts.add(new Concert(Arrays.asList("Owl City", "Matthew Thiessen", "The Earthquakes"),
//                "House of Blues, Dallas Texas",
//                "09/28/2018",
//                "https://i.ticketweb.com/i/00/07/81/67/09_Edp.jpg?v=10"));
//        listOfConcerts.add(new Concert(Arrays.asList("Blue Oyster Cult", "Mothership", "NovaKain"),
//                "Gas Monkey Live, Dallas Texas",
//                "10/19/2018",
//                "https://image-ticketfly.imgix.net/00/02/91/14/64-og.jpg?w=500&h=334&fit=crop&crop=top"));
//        listOfConcerts.add(new Concert(Arrays.asList("Josh Groban"),
//                "American Airlines Center, Dallas TX",
//                "10/24/2018",
//                "https://images-na.ssl-images-amazon.com/images/I/81mo7OmSa9L._SX355_.jpg"));
//        return listOfConcerts;
//    }
}
