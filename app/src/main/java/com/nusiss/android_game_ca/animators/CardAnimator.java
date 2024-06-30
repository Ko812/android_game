package com.nusiss.android_game_ca.animators;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.view.View;

import com.nusiss.android_game_ca.R;

public class CardAnimator {

    private float scale;
    private AnimatorSet frontCardAnimator;

    private AnimatorSet backCardAnimator;

    public CardAnimator(float scale, AnimatorSet frontAnimator, AnimatorSet backAnimator){
        this.scale = scale;
        this.frontCardAnimator = frontAnimator;
        this.backCardAnimator = backAnimator;

    }

    public void FlipFront(View cardFront){

    }

    public void FlipBack(View cardBack){

    }

    public float getScale() {
        return scale;
    }

    public AnimatorSet getFrontCardAnimator() {
        return frontCardAnimator;
    }

    public AnimatorSet getBackCardAnimator() {
        return backCardAnimator;
    }

    public AnimatorSet getSecondFrontCardAnimator() {
        return frontCardAnimator.clone();
    }

    public AnimatorSet getSecondBackCardAnimator() {
        return backCardAnimator.clone();
    }

    public void flipCard(View frontView, View backView, long delay){
        frontView.postDelayed(() -> {
            frontCardAnimator.setTarget(frontView);
            frontCardAnimator.start();
        }, delay);
        backView.postDelayed(() -> {
            backCardAnimator.setTarget(backView);
            backCardAnimator.start();
        }, delay);
    }

    public void flipSecondCard(View frontView, View backView, long delay){
        AnimatorSet secondFrontAnimator = getSecondFrontCardAnimator();
        AnimatorSet secondBackAnimator = getSecondBackCardAnimator();
        frontView.postDelayed(() -> {
            secondFrontAnimator.setTarget(frontView);
            secondFrontAnimator.start();
        }, delay);
        backView.postDelayed(() -> {
            secondBackAnimator.setTarget(backView);
            secondBackAnimator.start();
        }, delay);
    }
}
