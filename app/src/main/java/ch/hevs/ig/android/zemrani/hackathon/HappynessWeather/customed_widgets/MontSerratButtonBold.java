package ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.customed_widgets;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

public class MontSerratButtonBold extends AppCompatButton {
    public MontSerratButtonBold( @NonNull Context context , @Nullable AttributeSet attrs ) {
        super( context , attrs );
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"Montserrat-Bold.ttf" );
        setTypeface( typeface );
    }
}