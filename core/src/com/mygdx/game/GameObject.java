package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class GameObject{
    /*
x, y - координаты игрового объекта на экране
width, height - размеры игрового объекта на экране
texture - картинка, связанная с игровым объектом
*/
    private float x, y, width, height;
    private Texture texture;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public GameObject(float x, float y, float width, float height, Texture texture) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.texture = texture;
    }

    public GameObject(float x, float y, Texture texture) {
        this.x = x;
        this.y = y;
        this.width = texture.getWidth();
        this.height = texture.getHeight();
        this.texture = texture;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture image) {
        this.texture = image;
    }

    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
    }

    public void setPosition(Vector2 position){
        this.x = position.x - texture.getWidth() / 2;
        //this.y = Gdx.graphics.getHeight() - position.y;
        this.y = position.y;
    }
}
