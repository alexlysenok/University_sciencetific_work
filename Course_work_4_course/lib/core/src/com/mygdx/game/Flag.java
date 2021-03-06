package com.mygdx.game;


import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Flag extends Actor implements Comparable<Flag>  {
	
	Texture flag=new Texture(Gdx.files.internal("flagest3.png"));
	Counter counter; 
	
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		batch.draw(flag, getX(), getY(), flag.getWidth()*getScaleX(), flag.getHeight()*getScaleY());
	}

	@Override
	public void act(float delta) {
		
		super.act(delta);
	}


	
	
	public Flag(float x, float y, Stage stage){
		
		setScale(0.5f);  // ������������� ������ �����
		setPosition(x, y);
		stage.addActor(this);
		
		counter=new Counter(this.getX(),this.getY(), 0);
	}

	
	@Override
	public int compareTo(Flag flag) {
		// TODO Auto-generated method stub
		return (int) (Math.sqrt(Math.pow(this.getX(), 2)+Math.pow(this.getY(), 2))- Math.sqrt(Math.pow(flag.getX(), 2)+Math.pow(flag.getY(), 2)));
		
		
	}
	
}
