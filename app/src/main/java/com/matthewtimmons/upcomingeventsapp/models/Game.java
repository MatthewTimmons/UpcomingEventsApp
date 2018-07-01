package com.matthewtimmons.upcomingeventsapp.models;

import java.util.ArrayList;
import java.util.List;

public class Game {

    public static List<Game> getPlaceholderGames() {
        List<Game> listOfGames = new ArrayList<>();
        listOfGames.add(new Game("Spiderman", "Playstation 4", "09/07/2018", "spidermanurl.com"));
        listOfGames.add(new Game("Spiderman", "Playstation 4", "09/07/2018", "spidermanurl.com"));
        listOfGames.add(new Game("Spiderman", "Playstation 4", "09/07/2018", "spidermanurl.com"));
        listOfGames.add(new Game("Spiderman", "Playstation 4", "09/07/2018", "spidermanurl.com"));
        listOfGames.add(new Game("Spiderman", "Playstation 4", "09/07/2018", "spidermanurl.com"));
        return listOfGames;
    }
    private String title;
    private String releaseConsoles;
    private String releaseDate;
    private String imageUrl;

    public Game(String title, String releaseConsoles, String releaseDate, String imageUrl) {
        this.title = title;
        this.releaseConsoles = releaseConsoles;
        this.releaseDate = releaseDate;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseConsoles() {
        return releaseConsoles;
    }

    public void setReleaseConsoles(String releaseConsoles) {
        this.releaseConsoles = releaseConsoles;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
