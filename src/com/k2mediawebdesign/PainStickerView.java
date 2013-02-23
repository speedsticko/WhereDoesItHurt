package com.k2mediawebdesign;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

public class PainStickerView extends TextView {

	private PainSticker model = new PainSticker();
	
	private String[] pain_type_labels = {"Burning", "Dull", "Numb", "Sharp", "Shooting", "Stabbing", "Tender", "Throbbing", "Tingling"};
	private int[] sticker_colors = { Color.GREEN, Color.YELLOW, Color.RED};
	private int curr_color = 0; 

	public int getPainTypeIndex(String label)
	{
		for(int i = 0; i < pain_type_labels.length; i++)
		{
			if(label.equalsIgnoreCase(pain_type_labels[i])) { return i;}
		}
		return -1;
	}
	
	public PainStickerView(Context context) {
		super(context);
		setBackgroundColor(sticker_colors[0]);
		setTextColor(Color.BLACK);
		setPadding(5, 5, 5, 5);
	}
	public PainStickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBackgroundColor(sticker_colors[0]);
		setTextColor(Color.BLACK);
		setPadding(5, 5, 5, 5);
	}
	public PainStickerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setBackgroundColor(sticker_colors[0]);
		setTextColor(Color.BLACK);
		setPadding(5, 5, 5, 5);
	}

	public void cycleColors(){
		curr_color = (++curr_color) % sticker_colors.length;
		setBackgroundColor(sticker_colors[curr_color]);
		setIntensity(curr_color);
	}
	
	public void setLocation(float top, float left) {
		model.y = top;
		model.x = left;
	}
	
	public void setPainType(int type)
	{
		model.type = type;
		setText(pain_type_labels[type]);
	}
	
	public void setIntensity(int intensity)
	{
		model.intensity = intensity;
		setBackgroundColor(sticker_colors[intensity]);
	}
	
	public PainSticker getModel(){
		return model;
	}
	
}