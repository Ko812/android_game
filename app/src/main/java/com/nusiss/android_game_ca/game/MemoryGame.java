package com.nusiss.android_game_ca.game;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.nusiss.android_game_ca.animators.CardAnimator;

import java.util.List;
import java.util.Random;

import coil.ImageLoader;
import coil.request.ImageRequest;

public class MemoryGame {

    private Random rand;
    private List<GameCard> gameCards;
    private int currentScore;
    public int flippedCardId = 0;

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
            scoreBar.setText("Matched: 6 of 6. You Won!");
        }
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
}
