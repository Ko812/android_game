package com.nusiss.android_game_ca.game;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.TextView;

import com.nusiss.android_game_ca.animators.CardAnimator;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Random;


public class MemoryGame {

    private Random rand;
    private List<GameCard> gameCards;
    private int currentScore;
    public int flippedCardId = 0;
    public int secondsElapsed = 0;

    private TextView scoreBar;

    public MemoryGame(List<GameCard> gameCards, int seed, TextView scoreBar)  {
        this.gameCards = gameCards;
        this.rand = new Random(seed);
        this.currentScore = 0;
        this.flippedCardId = 0;
        this.scoreBar = scoreBar;
    }

    private void Initialize(){
        int[] imageIndices = {0,1,2,3,4,5};
        for(GameCard card : gameCards){
            int randomImage;
            do {
                randomImage = rand.nextInt(6);
            } while (paired(imageIndices[randomImage]));
            card.setCardImageIndex(imageIndices[randomImage]);
        }
    }

    private boolean paired(int imageIndex){
        long countCards = gameCards
                .stream()
                .filter(gc -> gc.getCardImageIndex() == imageIndex)
                .count();
        return countCards >= 2;
    }

    public void Start(){
        Reset();
        Initialize();
    }

    private void Reset(){
        currentScore = 0;
        flippedCardId = 0;
        secondsElapsed = 0;
        for(GameCard card : gameCards){
            if(card.isFlipped){
                card.unflip();
            }
        }
    }

    public boolean gameCheck(int card1Id, int card2Id){
        GameCard card1 = findGameCardById(card1Id);
        GameCard card2 = findGameCardById(card2Id);
        return card1.getCardImageIndex() == card2.getCardImageIndex();
    }

    public void SetupCardClickListener(CardAnimator animator){
        for(GameCard card : gameCards){
            card.setGame(this);
            card.setCardClickListener(animator);
        }
    }

    public int getFlippedCardId(){
        return this.flippedCardId;
    }

    public void setFlippedCardId(int flippedCardId){
        this.flippedCardId = flippedCardId;
    }

    public void gainScore(){
        currentScore++;
        scoreBar.setText("Matched: "+currentScore+" of 6");
        if(currentScore == 6){
            scoreBar.setText("All matched. You win!");
        }
    }

    public GameCard findGameCardById(int id){
        return gameCards
                .stream()
                .filter(gc -> gc.getId() == id)
                .findFirst()
                .get();
    }

    public void BindImagesToCard(Activity context, String[] urls){
        for(GameCard card : gameCards){
            new Thread(() -> {
                try {
                    Bitmap bm = BitmapFactory.decodeFile(urls[card.getCardImageIndex()]);
                    card.bindImage(context, bm);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }).start();
        }
    }

    public int getSecondsElapsed() {
        return secondsElapsed;
    }

    public void setSecondsElapsed(int secondsElapsed) {
        this.secondsElapsed = secondsElapsed;
    }

    public int getMinuteElapsed(){
        return (getSecondsElapsed() - (getSecondsElapsed() % 60)) / 60;
    }

    public String getSecondsPortionOfElapsed(){
        int sec = getSecondsElapsed() % 60;
        if(sec < 10){
            return "0" + sec;
        }
        return "" + sec;
    }

    public void lapsedOneSecond(){
        secondsElapsed++;
    }

    private final String ua = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36 OPR/38.0.2220.41";
}
