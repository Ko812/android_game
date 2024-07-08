package com.nusiss.android_game_ca.game;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.TextView;

import com.nusiss.android_game_ca.R;
import com.nusiss.android_game_ca.GameActivity;
import com.nusiss.android_game_ca.animators.CardAnimator;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import coil.ImageLoader;
import coil.request.ImageRequest;

public class MemoryGame {

    private Random rand;
    private List<GameCard> gameCards;
    private int currentScore;
    public int flippedCardId = 0;
    public int secondsElapsed = 0;
    private ActionListener mActionListener;
    private CardAnimator cardAnimator;
    private Context context;
    private MediaPlayer successSound;
    private MediaPlayer failureSound;
    private MediaPlayer victorySound;

    public interface ActionListener{
        void onGainScore(int score);
        void onGameWin();
    }

    public MemoryGame(Context context, List<GameCard> gameCards, int seed, ActionListener actionListener)  {
        this.context = context;
        this.gameCards = gameCards;
        this.rand = new Random(seed);
        this.currentScore = 0;
        this.flippedCardId = 0;
        this.mActionListener = actionListener;
        initializeSounds();
    }

    private void initializeSounds() {
        successSound = MediaPlayer.create(context, R.raw.success_sound);
        failureSound = MediaPlayer.create(context, R.raw.failure_sound);
        victorySound = MediaPlayer.create(context, R.raw.victory_sound);
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
        gameCards.forEach(new Consumer<GameCard>() {
            @Override
            public void accept(GameCard gameCard) {
                Log.d("aa", gameCard.getCardImageIndex() +"");
            }
        });
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
            if (card.getCardImageIndex() != -1) {
                card.cardFront.setRotationY(0);
                card.cardFront.setAlpha(1.0f);
                card.cardBack.setRotationY(0);
                card.cardBack.setAlpha(0.0f);
                card.setCardImageIndex(-1);
            }
        }
    }

    public boolean gameCheck(int card1Id, int card2Id){
        GameCard card1 = findGameCardById(card1Id);
        GameCard card2 = findGameCardById(card2Id);
        boolean isMatch = card1.getCardImageIndex() == card2.getCardImageIndex();

        if (isMatch) {
            playSuccessSound();
        } else {
            playFailureSound();
        }

        return isMatch;
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
        mActionListener.onGainScore(currentScore);
    }

    public GameCard findGameCardById(int id){
        return gameCards
                .stream()
                .filter(gc -> gc.getId() == id)
                .findFirst()
                .get();
    }

    public void BindImagesToCard(Context context, ImageLoader loader, String[] urls){
        for(GameCard card : gameCards){
            ImageRequest.Builder builder = new ImageRequest.Builder(context).data(urls[card.getCardImageIndex()]);
            card.bindImage(loader, builder);
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

    private void playSuccessSound() {
        if (successSound != null) {
            successSound.start();
        }
    }

    private void playFailureSound() {
        if (failureSound != null) {
            failureSound.start();
        }
    }

    private void playVictorySound() {
        if (victorySound != null) {
            victorySound.start();
        }
    }

    public void release() {
        if (successSound != null) {
            successSound.release();
            successSound = null;
        }
        if (failureSound != null) {
            failureSound.release();
            failureSound = null;
        }
        if (victorySound != null) {
            victorySound.release();
            victorySound = null;
        }
    }
}
