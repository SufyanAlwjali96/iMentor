package com.tarmiz.imentor.Utils;

import android.content.Context;
import android.graphics.Typeface;

public class FontTypeface
{
    private Context context;

    public FontTypeface(Context context){
        this.context = context;
    }

    public Typeface getTypefaceAndroid(){
        Typeface typeFace = Typeface.DEFAULT;
        String strFont = "Assets/fonts/droidsansarabic.ttf";
        try {
            String strLeft = strFont.substring(0, 13);
            if (strLeft.equals("Assets/fonts/")){
                typeFace = Typeface.createFromAsset(context.getAssets(), strFont.replace("Assets/", ""));
            } else {
                typeFace = Typeface.createFromFile(strFont);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return typeFace;
    }
}