package com.matthewtimmons.upcomingeventsapp.models;

import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Game implements Serializable {

    public static List<Game> getPlaceholderGames() {
        List<Game> listOfGames = new ArrayList<>();
        listOfGames.add(new Game("Spiderman", "Playstation 4", "09/07/2018", "https://upload.wikimedia.org/wikipedia/en/thumb/e/e1/Spider-Man_PS4_cover.jpg/220px-Spider-Man_PS4_cover.jpg"));
        listOfGames.add(new Game("Shadow of the Tomb Raider", "PC, Xbox One, Playstation 4", "09/14/2018", "https://steamcdn-a.akamaihd.net/steamcommunity/public/images/clans/32341898/5b83592ca76d41f1f2264f2122a617c7663efd36.png"));
        listOfGames.add(new Game("Super Mario Party", "Nintendo Switch", "10/05/2018", "https://m.media-amazon.com/images/M/MV5BOWU2NTBmNjUtN2ExNC00MzdlLWE0ZGYtN2M2YTA1YmZmYzcxXkEyXkFqcGdeQXVyMzY0MDAyMDI@._V1_.jpg"));
        listOfGames.add(new Game("Call of Duty: Black Ops 4", "PC, Xbox One, Playstation 4", "10/12/2018", "https://ksassets.timeincuk.net/wp/uploads/sites/54/2018/06/screen-call-of-duty-black-ops-4-reveal-trailer-1.jpg"));
        listOfGames.add(new Game("Red Dead Redemption 2", "Xbox One, Playstation 4", "10/26/2018", "https://media.playstation.com/is/image/SCEA/red-dead-redemption-2-listing-thumb-01-ps4-us-18oct16?$Icon$"));
        return listOfGames;
    }

    public static String fetchGamesAsString(DocumentSnapshot gameDocumentSnapshot) {
        List<String> listOfGames = (ArrayList<String>) gameDocumentSnapshot.get("releaseConsoles");
        String output = "";
        for (int i = 0; i < listOfGames.size() -1; i++) {
            output = output.concat(listOfGames.get(i) + ", ");
        }
        output = output.concat(listOfGames.get(listOfGames.size() -1));
        return output;
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
