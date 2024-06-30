package com.nusiss.android_game_ca.game;

import android.animation.AnimatorSet;
import android.content.Context;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nusiss.android_game_ca.animators.CardAnimator;

import java.util.List;
import java.util.Random;

public class MemoryGame {

    public Context context;
    private Random rand;
    private List<GameCard> gameCards;
    private int currentScore = 0;
    public int flippedCardId = 0;

    public MemoryGame(List<GameCard> gameCards, int seed, Context context)  {
        this.gameCards = gameCards;
        rand = new Random(seed);
    }

    private void Initialize(){
        String[] imageNames = {"num1", "num2", "num3", "num4", "num5", "num6"};
        for(GameCard card : gameCards){
            int randomImage;
            do {
                randomImage = rand.nextInt(6);
            } while (paired(imageNames[randomImage]));
            card.setCardImage(imageNames[randomImage]);
        }
    }

    private boolean paired(String imageName){
        long countCards = gameCards
                .stream()
                .filter(gc -> gc.getCardImage().compareTo(imageName) == 0)
                .count();
        return countCards == 2;
    }

    public void Start(){
        Reset();
        Initialize();
    }

    private void Reset(){
        currentScore = 0;
        for(GameCard card : gameCards){
            if(card.isFlipped){
                card.unflip();
            }
        }
    }

    public boolean gameCheck(int card1Id, int card2Id){
        GameCard card1 = findGameCardById(card1Id);
        GameCard card2 = findGameCardById(card2Id);
        return card1.getCardImage() == card2.getCardImage();
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
        if(currentScore == 6){
            Toast.makeText(context, "Game complete! You won!", Toast.LENGTH_LONG).show();
        }
    }

    public GameCard findGameCardById(int id){
        return gameCards
                .stream()
                .filter(gc -> gc.getId() == id)
                .findFirst()
                .get();
    }
}
