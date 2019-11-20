package com.example.jianlou.publish.publish;

import android.animation.TypeEvaluator;


/**
 * 看不懂，似乎是处理动画的弹出的速度的
 */
public class KickBackAnimator implements TypeEvaluator<Float> {
    private final float s = 1.70158f;
    float mDuration = 0f;

    public void setDuration(float duration) {
        mDuration = duration;
    }

    public Float evaluate(float fraction, Float startValue, Float endValue) {
        float t = mDuration * fraction;
        float b = startValue.floatValue();
        float c = endValue.floatValue() - startValue.floatValue();
        float d = mDuration;
        float result = calculate(t, b, c, d);
        return result;
    }

    public Float calculate(float t, float b, float c, float d) {
        return c * ((t = t / d - 1) * t * ((s + 1) * t + s) + 1) + b;
    }
}

