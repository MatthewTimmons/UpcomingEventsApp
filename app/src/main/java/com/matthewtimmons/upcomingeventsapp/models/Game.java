package com.matthewtimmons.upcomingeventsapp.models;

import android.text.TextUtils;

import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Game extends Event implements Serializable {
    private List<String> releaseConsoles;

    public Game(DocumentSnapshot documentSnapshot) {
        super(documentSnapshot);
        this.releaseConsoles = (List<String>) documentSnapshot.get("releaseConsoles");
    }

    public static String fetchGamesAsString(DocumentSnapshot gameDocumentSnapshot) {
        List<String> listOfGames = (List<String>) gameDocumentSnapshot.get("releaseConsoles");
        return TextUtils.join(", ", listOfGames);
    }

    public List<String> getReleaseConsoles() {
        return releaseConsoles;
    }

    public void setReleaseConsoles(List<String> releaseConsoles) {
        this.releaseConsoles = releaseConsoles;
    }

    public String getReleaseConsolesAsString() {
        return TextUtils.join(", ", releaseConsoles);
    }


//
//
//    public static List<Game> getPlaceholderGames() {
//        List<Game> listOfGames = new ArrayList<>();
//        listOfGames.add(new Game("Spiderman", "Playstation 4", "09/07/2018", "https://upload.wikimedia.org/wikipedia/en/thumb/e/e1/Spider-Man_PS4_cover.jpg/220px-Spider-Man_PS4_cover.jpg"));
//        listOfGames.add(new Game("Shadow of the Tomb Raider", "PC, Xbox One, Playstation 4", "09/14/2018", "https://steamcdn-a.akamaihd.net/steamcommunity/public/images/clans/32341898/5b83592ca76d41f1f2264f2122a617c7663efd36.png"));
//        listOfGames.add(new Game("Super Mario Party", "Nintendo Switch", "10/05/2018", "https://m.media-amazon.com/images/M/MV5BOWU2NTBmNjUtN2ExNC00MzdlLWE0ZGYtN2M2YTA1YmZmYzcxXkEyXkFqcGdeQXVyMzY0MDAyMDI@._V1_.jpg"));
//        listOfGames.add(new Game("Call of Duty: Black Ops 4", "PC, Xbox One, Playstation 4", "10/12/2018", "https://ksassets.timeincuk.net/wp/uploads/sites/54/2018/06/screen-call-of-duty-black-ops-4-reveal-trailer-1.jpg"));
//        listOfGames.add(new Game("Red Dead Redemption 2", "Xbox One, Playstation 4", "10/26/2018", "https://media.playstation.com/is/image/SCEA/red-dead-redemption-2-listing-thumb-01-ps4-us-18oct16?$Icon$"));
//        return listOfGames;
//    }

}
