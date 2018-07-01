package com.matthewtimmons.upcomingeventsapp.models;

import java.util.ArrayList;
import java.util.List;

public class Game {

    public static List<Game> getPlaceholderGames() {
        List<Game> listOfGames = new ArrayList<>();
        listOfGames.add(new Game("Spiderman", "Playstation 4", "09/07/2018", "spidermanurl.com"));
        listOfGames.add(new Game("Shadow of the Tomb Raider", "PC, Xbox One, Playstation 4", "09/14/2018", "tombraider.com"));
        listOfGames.add(new Game("Super Mario Party", "Nintendo Switch", "10/05/2018", "nintendo.com"));
        listOfGames.add(new Game("Call of Duty: Black Ops 4", "PC, Xbox One, Playstation 4", "10/12/2018", "callofduty.com"));
        listOfGames.add(new Game("Red Dead Redemption 2", "Xbox One, Playstation 4", "10/26/2018", "rockstargames.com"));
        return listOfGames;
    }

    private String gameTitle;
    private String releaseConsoles;
    private String gameReleaseDate;
    private String gameImageUrl;

    public Game(String gameTitle, String releaseConsoles, String gameReleaseDate, String gameImageUrl) {
        this.gameTitle = gameTitle;
        this.releaseConsoles = releaseConsoles;
        this.gameReleaseDate = gameReleaseDate;
        this.gameImageUrl = gameImageUrl;
    }

    public String getTitle() {
        return gameTitle;
    }

    public void setTitle(String title) {
        this.gameTitle = title;
    }

    public String getReleaseConsoles() {
        return releaseConsoles;
    }

    public void setReleaseConsoles(String releaseConsoles) {
        this.releaseConsoles = releaseConsoles;
    }

    public String getGameReleaseDate() {
        return gameReleaseDate;
    }

    public void setGameReleaseDate(String gameReleaseDate) {
        this.gameReleaseDate = gameReleaseDate;
    }

    public String getGameImageUrl() {
        return gameImageUrl;
    }

    public void setGameImageUrl(String gameImageUrl) {
        this.gameImageUrl = gameImageUrl;
    }
}
