package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Player{
    private float x, y, width, height;
    private Texture texture;
    Body bodyPlayer;
    public Player(float x, float y, float width, float height, Texture texture, World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;

        //Vector2 center = new Vector2(width + width/3.5f, height);
        bodyDef.position.set(x, y);
        bodyPlayer = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        //shape.setAsBox(getTexture().getWidth()/2, getTexture().getHeight()/2);
        shape.setAsBox(40, 40, new Vector2(5, 0), 0);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 10000f;
        fixtureDef.friction = 0;
        fixtureDef.restitution = 0;
        Fixture fixture = bodyPlayer.createFixture(fixtureDef);
        shape.dispose();
        //bodyPlayer.setLinearVelocity(100,0);
        this.texture = texture;
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getX() {
        return bodyPlayer.getPosition().x - width / 2;
    }

    public float getY() {
        return bodyPlayer.getPosition().y - height / 2;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setLinearVelocity(float x, float y){
        //System.out.println("setLinearVelocity = " + x + "  " + y);
        bodyPlayer.setLinearVelocity(100 * x, 100 * y);
        //bodyPlayer.setLinearVelocity(0.4f, 0.4f);
        this.x = bodyPlayer.getPosition().x;
        this.y = bodyPlayer.getPosition().y;
    }

}
