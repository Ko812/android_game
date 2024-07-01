package com.nusiss.android_game_ca.game;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nusiss.android_game_ca.animators.CardAnimator;

import java.util.List;

import coil.ImageLoader;
import coil.request.ImageRequest;

public class GameCard {
    private int id;
    private int imageIndex;
    TextView cardFront;

    ImageButton cardBack;

    boolean isFlipped;

    MemoryGame game;

    public GameCard(int id, TextView cardFront, ImageButton cardBack){
        this.id = id;
        this.isFlipped = false;
        this.cardFront = cardFront;
        this.cardBack = cardBack;
        this.imageIndex = 0;
    }

    public void setCameraDistance(float scale){
        this.cardFront.setCameraDistance(8000 * scale);
        this.cardBack.setCameraDistance(8000 * scale);
    }

    public void setCardClickListener(CardAnimator animator){
        cardBack.setOnClickListener(view -> {
            if(isFlipped){
                return;
            } else {
                animator.flipCard(cardFront, cardBack, 0L);
                isFlipped = true;
                int flippedCardId = game.getFlippedCardId();
                if(flippedCardId == 0){
                    game.setFlippedCardId(this.id);
                    return;
                } else {
                    if(game.gameCheck(flippedCardId, this.id)){
                        game.gainScore();
                    } else {
                        GameCard flippedCard = game.findGameCardById(flippedCardId);
                        animator.flipCard(cardBack, cardFront, 2500L);
                        animator.flipSecondCard(flippedCard.cardBack, flippedCard.cardFront, 2500L);
                        this.unflip();
                        flippedCard.unflip();
                    }
                    game.setFlippedCardId(0);
                }
            }
        });
    }

    public void setGame(MemoryGame game){
        this.game = game;
    }

    public int getId(){
        return id;
    }

    public void unflip(){
        isFlipped = false;
    }

    public int getCardImageIndex(){
        return imageIndex;
    }

    public void setCardImageIndex(int index){
        this.imageIndex = index;
    }

    public void bindImage (ImageLoader loader, ImageRequest.Builder builder){
        loader.enqueue(builder.target(cardBack).build());
    }

}
