package com.nusiss.android_game_ca;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nusiss.android_game_ca.animators.CardAnimator;
import com.nusiss.android_game_ca.exceptions.InvalidGameSetupException;
import com.nusiss.android_game_ca.game.GameCard;
import com.nusiss.android_game_ca.game.MemoryGame;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class GameActivity extends AppCompatActivity {

    private Map<Integer, Integer> cardPairIds = new HashMap<>();
    MemoryGame memoryGame;
    private CardAnimator cardAnimator;

    private Button returnBtn;
    private Button startBtn;
    private Button stopBtn;
    private Button resetBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        MapCards();
        SetupAnimators();
        memoryGame = new MemoryGame(
                BuildGameCards(cardAnimator.getScale()),  // initialized 12 game cards
                (int)(LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(2)) % 1000), // seed for randomizer
                (TextView)findViewById(R.id.scoreBar) // status bar view
        );
        memoryGame.SetupCardClickListener(cardAnimator);
        startBtn = findViewById(R.id.startBtn);
        startBtn.setOnClickListener(this::clickStartBtn);
        stopBtn = findViewById(R.id.stopBtn);
        stopBtn.setOnClickListener(this::clickStopBtn);
        resetBtn = findViewById(R.id.resetBtn);
        stopBtn.setOnClickListener(this::clickStopBtn);
        returnBtn = findViewById(R.id.returnBtn);
        returnBtn.setOnClickListener(this::clickReturnBtn);
    }


    @Override
    protected void onStart() {
        super.onStart();
        memoryGame.Start();
        if(this != null){
            Intent callerIntent = getIntent();
            String url0 = callerIntent.getStringExtra("url_0");
            String url1 = callerIntent.getStringExtra("url_1");
            String url2 = callerIntent.getStringExtra("url_2");
            String url3 = callerIntent.getStringExtra("url_3");
            String url4 = callerIntent.getStringExtra("url_4");
            String url5 = callerIntent.getStringExtra("url_5");
            memoryGame.BindImagesToCard(this, new String[]{url0, url1, url2, url3, url4, url5});
        }

    }

    // Builds 12 game cards
    private List<GameCard> BuildGameCards(float scale){
        List<GameCard> gameCards = new ArrayList<>();
        cardPairIds.forEach((Integer k, Integer v) -> {
            TextView cardFront = findViewById(k);
            ImageButton cardBack = findViewById(v);
            GameCard card = new GameCard(cardFront.getId(), cardFront, cardBack);
            card.setCameraDistance(scale);
            gameCards.add(card);
        });
        return gameCards;
    }

    // Set up the initial 12 images mapping
    private void MapCards(){
        cardPairIds.put(R.id.card1Front, R.id.card1Back);
        cardPairIds.put(R.id.card2Front, R.id.card2Back);
        cardPairIds.put(R.id.card3Front, R.id.card3Back);
        cardPairIds.put(R.id.card4Front, R.id.card4Back);
        cardPairIds.put(R.id.card5Front, R.id.card5Back);
        cardPairIds.put(R.id.card6Front, R.id.card6Back);
        cardPairIds.put(R.id.card7Front, R.id.card7Back);
        cardPairIds.put(R.id.card8Front, R.id.card8Back);
        cardPairIds.put(R.id.card9Front, R.id.card9Back);
        cardPairIds.put(R.id.card10Front, R.id.card10Back);
        cardPairIds.put(R.id.card11Front, R.id.card11Back);
        cardPairIds.put(R.id.card12Front, R.id.card12Back);
    }

    private void SetupAnimators(){
        float scale = getApplicationContext().getResources().getDisplayMetrics().density;

        AnimatorSet frontAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.front_animator);
        AnimatorSet backAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.back_animator);

        cardAnimator = new CardAnimator(scale, frontAnimator, backAnimator);
    }

    private void clickReturnBtn(View view){

    }

    private void clickStartBtn(View view){
        // enable stop button
        stopBtn.setEnabled(true);
        startBtn.setEnabled(false);
        startTimer();
    }

    private void clickResetBtn(View view){
        memoryGame.Start();
        TextView txtView = findViewById(R.id.timeElapsedText);
    }

    private void clickStopBtn(View view){
        if(handler != null){
            handler.removeCallbacksAndMessages(null);
        }
        startBtn.setEnabled(true);
        stopBtn.setEnabled(false);
    }

    private Handler handler;
    private void startTimer() {
        final TextView txtTime = findViewById(R.id.timeElapsedText);
        if(handler == null){
            handler = new Handler();
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                memoryGame.lapsedOneSecond();
                txtTime.setText("Elapsed " + memoryGame.getMinuteElapsed() + ":" + memoryGame.getSecondsPortionOfElapsed());
                handler.postDelayed(this, 1000);
            }
        });
    }

}