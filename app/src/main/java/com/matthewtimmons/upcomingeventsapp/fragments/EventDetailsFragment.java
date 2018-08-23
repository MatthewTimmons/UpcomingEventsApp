package com.matthewtimmons.upcomingeventsapp.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.constants.FirebaseConstants;
import com.matthewtimmons.upcomingeventsapp.controllers.ConcertsController;
import com.matthewtimmons.upcomingeventsapp.controllers.GamesController;
import com.matthewtimmons.upcomingeventsapp.controllers.MoviesController;
import com.matthewtimmons.upcomingeventsapp.manager.DateHelper;
import com.matthewtimmons.upcomingeventsapp.manager.Firestore;
import com.matthewtimmons.upcomingeventsapp.models.Concert;
import com.matthewtimmons.upcomingeventsapp.models.Event;
import com.matthewtimmons.upcomingeventsapp.models.Game;
import com.matthewtimmons.upcomingeventsapp.models.Movie;
import com.matthewtimmons.upcomingeventsapp.models.User;
import com.matthewtimmons.upcomingeventsapp.models.UserManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class EventDetailsFragment extends Fragment {
    private static final String ARGS_EVENT_ID = "ARGS_EVENT_ID";
    private static final String ARGS_EVENT_TYPE = "ARGS_EVENT_TYPE";
    private static final String ARGS_IS_CUSTOM_EVENT = "ARGS_IS_CUSTOM_EVENT";
    String currentUserId, eventId, eventKey;
    Boolean isCustomEvent;
    ImageView eventPictureImageView;
    TextView titleTextView, subtitleTextView, optionalSecondSubtitleTextView, fourthTextView, fifthTextView;
    CheckBox optionalCheckbox, favoritesCheckbox;
    Spinner optionalSpinner;
    User currentUser;


    public static EventDetailsFragment newInstance(String eventId, String eventKey) {
        EventDetailsFragment instance = new EventDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_EVENT_ID, eventId);
        args.putString(ARGS_EVENT_TYPE, eventKey);
        args.putBoolean(ARGS_IS_CUSTOM_EVENT, false);
        instance.setArguments(args);
        return instance;
    }

    public static EventDetailsFragment newInstance(String eventId, String eventKey, Boolean isCustomEvent) {
        EventDetailsFragment instance = new EventDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_EVENT_ID, eventId);
        args.putString(ARGS_EVENT_TYPE, eventKey);
        args.putBoolean(ARGS_IS_CUSTOM_EVENT, true);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_details_event, container, false);
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            eventId = bundle.getString(ARGS_EVENT_ID);
            eventKey = bundle.getString(ARGS_EVENT_TYPE);
            isCustomEvent = bundle.getBoolean(ARGS_IS_CUSTOM_EVENT);
        }

        currentUser = UserManager.getInstance().getCurrentUser();

        // Assign all views to variables
        eventPictureImageView = view.findViewById(R.id.wide_image);
        titleTextView =view.findViewById(R.id.title);
        subtitleTextView =view.findViewById(R.id.optional_subtitle);
        optionalSecondSubtitleTextView = view.findViewById(R.id.third_info_field);
        fourthTextView = view.findViewById(R.id.fourth_info_field);
        fifthTextView = view.findViewById(R.id.fifth_info_field);
        optionalCheckbox = view.findViewById(R.id.optional_checkbox);
        favoritesCheckbox = view.findViewById(R.id.favorite_checkbox);
        optionalSpinner = view.findViewById(R.id.optional_spinner);

        switch (eventKey) {
            case FirebaseConstants.COLLECTION_CONCERTS:
                if (!isCustomEvent) {
                    ConcertsController.getConcert(eventId, new ConcertsController.GetConcertListener() {
                        @Override
                        public void onConcertRetrieved(Concert concert) {
                            presentConcert(concert);
                        }
                    });
                } else {
                    ConcertsController.getCustomConcert(eventId, currentUserId, new ConcertsController.GetConcertListener() {
                        @Override
                        public void onConcertRetrieved(Concert concert) {
                            presentConcert(concert);
                        }
                    });
                }
                break;
            case FirebaseConstants.COLLECTION_GAMES:
                if (!isCustomEvent) {
                    GamesController.getGame(eventId, new GamesController.GetGameListener() {
                        @Override
                        public void onGameRetrieved(Game game) {
                            presentGame(game);
                        }
                    });
                } else {
                    GamesController.getCustomGame(eventId, currentUserId, new GamesController.GetGameListener() {
                        @Override
                        public void onGameRetrieved(Game game) {
                            presentGame(game);
                        }
                    });
                }
                break;
            case FirebaseConstants.COLLECTION_MOVIES:
                if (!isCustomEvent) {
                    MoviesController.getMovie(eventId, new MoviesController.GetMovieListener() {
                        @Override
                        public void onMovieRetrieved(Movie movie) {
                            presentMovie(movie);
                        }
                    });
                } else {
                    MoviesController.getCustomMovie(eventId, currentUserId, new MoviesController.GetMovieListener() {
                        @Override
                        public void onMovieRetrieved(Movie movie) {
                            presentMovie(movie);
                        }
                    });
                }
                break;
        }
    }

    public void setCheckmarkFunctionality(final String eventId, final FieldPath fieldpathToArray, final CheckBox checkBox, final boolean toggleImage) {
        Task<DocumentSnapshot> task = FirebaseFirestore.getInstance().collection(FirebaseConstants.COLLECTION_USERS)
                .document(currentUserId).get();
        task.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                final DocumentReference currentUserDocument = task.getResult().getReference();
                final List<String> arrayOfAllInstances = (List<String>) task.getResult().get(fieldpathToArray);
                boolean existsInArray = arrayOfAllInstances.contains(eventId);
                checkBox.setChecked(existsInArray);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                        if (checked) {
                            arrayOfAllInstances.add(eventId);
                            currentUserDocument.update(fieldpathToArray, arrayOfAllInstances);
                            if (toggleImage) { compoundButton.setButtonDrawable(R.drawable.ic_star); }
                        } else {
                            arrayOfAllInstances.remove(eventId);
                            currentUserDocument.update(fieldpathToArray, arrayOfAllInstances);
                            if (toggleImage) { compoundButton.setButtonDrawable(R.drawable.ic_hollow_star); }
                        }
                    }
                });
                // TODO Clean this patch up in method
                if (toggleImage) {
                    if (checkBox.isChecked()) { checkBox.setButtonDrawable(R.drawable.ic_star); }
                    else { checkBox.setButtonDrawable(R.drawable.ic_hollow_star); }
                }
            }
        });
    }


    public void produceAlertDialog(final Game game, boolean b) {
                    final List<String> allCheckedBoxes = new ArrayList<>();

                    // Try adding all currently owned consoles if there are any
                    if (!b) {
                        try {
                            Map<String, Object> ownedGames = currentUser.getGamesOwned();
                            List<String> ownedConsolesForThisGame = (List<String>) ownedGames.get(game.getId());
                            allCheckedBoxes.addAll(ownedConsolesForThisGame);
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "App would have crashed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(getContext());
                    View dialogView = getLayoutInflater().inflate(R.layout.dialog_checkboxes_and_confirm, null);

                    LinearLayout checkboxesLinearLayout = dialogView.findViewById(R.id.checkboxes_linear_layout);
                    Button confirmButton = dialogView.findViewById(R.id.confirm_button);
                    Button cancelButton = dialogView.findViewById(R.id.cancel_button);

                    for (final String console : game.getReleaseConsoles()) {
                        CheckBox checkBox = new CheckBox(getContext());
                        checkBox.setText(console);
                        checkBox.setChecked(allCheckedBoxes.contains(console));
                        checkboxesLinearLayout.addView(checkBox);

                        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                if (b) allCheckedBoxes.add(console);
                                else allCheckedBoxes.remove(console);
                            }
                        });
                    }

                    dialogBuilder.setView(dialogView);
                    final android.support.v7.app.AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();

                    confirmButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!allCheckedBoxes.equals(currentUser.getGamesOwned().get(game.getId()))) {
                                if (!allCheckedBoxes.isEmpty()) {
                                    Firestore.updateFirestoreDocument("users/" + currentUserId, "gamesOwned." + game.getId(), allCheckedBoxes);
                                    currentUser.getGamesOwned().put(game.getId(), allCheckedBoxes);
                                    UserManager.getInstance().setCurrentUser();
                                    currentUser = UserManager.getInstance().getCurrentUser();
                                } else {
                                    Firestore.updateFirestoreDocument("users/" + currentUserId, "gamesOwned." + game.getId(), FieldValue.delete());
                                    currentUser.getGamesOwned().put(game.getId(), null);
                                    UserManager.getInstance().setCurrentUser();
                                    currentUser = UserManager.getInstance().getCurrentUser();
                                }
                            }
                            alertDialog.dismiss();
                        }
                    });

                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });

                    alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            optionalCheckbox.setOnCheckedChangeListener(null);
                            optionalCheckbox.setChecked(currentUser.getGamesOwned().get(game.getId()) != null);
                            optionalCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                    produceAlertDialog(game, b);
                                }
                            });
                        }
                    });

//                } else {
//                    Firestore.updateFirestoreDocument("users/" + currentUserId, "gamesOwned." + game.getId(), FieldValue.delete());
//                }
    }

    void presentEvent(Event event, @DrawableRes int backupImageId) {
        Picasso.get().load(event.getImageUrl()).error(backupImageId).into(eventPictureImageView);
        titleTextView.setText(event.getTitle());
        fifthTextView.setText(DateHelper.getHumanReadableFormat(event.getDate()));
        setCheckmarkFunctionality(event.getId(), FieldPath.of("myFavorites", eventKey), favoritesCheckbox, true);
    }

    void presentConcert(Concert concert) {
        fourthTextView.setText(concert.getConcertLocation());
        presentEvent(concert, R.drawable.ic_concerts_blue);
        List<String> listOfBandsAtConcert = concert.getBands();
        titleTextView.setText(listOfBandsAtConcert.get(0));
        if (listOfBandsAtConcert.size() > 1) {
            subtitleTextView.setVisibility(View.VISIBLE);
            subtitleTextView.setText(listOfBandsAtConcert.get(1));
            if (listOfBandsAtConcert.size() > 2) {
                List<String> remainingBandsList = listOfBandsAtConcert.subList(2, listOfBandsAtConcert.size());
                String remainingBandsDisplayText = TextUtils.join(", ", remainingBandsList);
                optionalSecondSubtitleTextView.setVisibility(View.VISIBLE);
                optionalSecondSubtitleTextView.setText(remainingBandsDisplayText);
            }
        }
    }

    void presentGame(final Game game) {
        presentEvent(game, R.drawable.ic_games_blue);
        fourthTextView.setText(game.getReleaseConsolesAsString());

        // Set rating TextView
        optionalSecondSubtitleTextView.setVisibility(View.VISIBLE);
        optionalSecondSubtitleTextView.setText(game.getFormattedRating(getResources()));

        // Set Owned Checkbox functionality
        optionalCheckbox.setVisibility(View.VISIBLE);
        optionalCheckbox.setText("Owned");
        final boolean gameAlreadyOwned = currentUser.getGamesOwned().containsKey(game.getId());
        if (gameAlreadyOwned) optionalCheckbox.setChecked(true);

        if (game.getReleaseConsoles().size() > 1) {
            optionalCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    produceAlertDialog(game, b);
                }
            });
        } else {
            optionalCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) { Firestore.updateFirestoreDocument("users/" + currentUserId, "gamesOwned." + game.getId(), game.getReleaseConsoles().get(0)); }
                    else { Firestore.updateFirestoreDocument("users/" + currentUserId, "gamesOwned." + game.getId(), FieldValue.delete()); }
                    UserManager.getInstance().setCurrentUser();
                    currentUser = UserManager.getInstance().getCurrentUser();
                }
            });
        }
    }

    void presentMovie(Movie movie) {
        presentEvent(movie, R.drawable.ic_movies_blue);
        optionalSecondSubtitleTextView.setVisibility(View.VISIBLE);
        optionalSecondSubtitleTextView.setText(movie.getFormattedRating(getResources()));
        optionalCheckbox.setVisibility(View.VISIBLE);
        optionalCheckbox.setText("Seen");
        fourthTextView.setText(movie.getGenre());
        setCheckmarkFunctionality(movie.getId(), FieldPath.of(FirebaseConstants.KEY_MOVIES_SEEN), optionalCheckbox, false);
    }
}
