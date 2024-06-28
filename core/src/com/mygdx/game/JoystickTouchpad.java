package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class JoystickTouchpad {
    private JoystickListener listener;
    private Touchpad touchpad;
    private  boolean touched = false;
    private  boolean isDragged = false;
    private Texture textureJoystick = new Texture("atlas.png");
    public void setJoystickListener(JoystickListener listener){
        this.listener = listener;
    }
    public JoystickTouchpad(Stage stadeUI) {

        TextureRegion arrows, knob;
        TextureRegion tmpLeftRight[][] = TextureRegion.split(textureJoystick, textureJoystick.getWidth() / 2, textureJoystick.getHeight() / 2);
        arrows = tmpLeftRight[0][1];

        TextureRegion rightbot[][] = tmpLeftRight[1][1].split(tmpLeftRight[1][1].getRegionWidth() / 2, tmpLeftRight[1][1].getRegionHeight() / 2);
        knob = rightbot[0][1];
        Touchpad.TouchpadStyle touchpadStyle = new Touchpad.TouchpadStyle(
                new TextureRegionDrawable(arrows),
                new TextureRegionDrawable(knob));

        touchpadStyle.knob.setMinWidth(arrows.getRegionWidth());
        touchpadStyle.knob.setMinHeight(arrows.getRegionWidth());

        touchpad = new Touchpad(15f, touchpadStyle);
        touchpad.setSize(stadeUI.getHeight() / 2.5f, stadeUI.getHeight() / 2.5f);
        touchpad.setBounds(0, 0, stadeUI.getHeight() / 2.5f, stadeUI.getHeight() / 2.5f);
        stadeUI.addActor(touchpad);

        touchpad.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if (touched) return  false;
                touched = true;
                return true;
            }

            public void touchDragged (InputEvent event, float x, float y, int pointer) {
                isDragged = true;
                setTouch();
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                listener.changed(0, 0);
                touched = false;
                isDragged = false;
            }
        });
    }

    public void setTouch(){
        if(!isDragged) return;
        float posX = 0, posY = 0;
        if (Math.abs(touchpad.getKnobPercentX()) > Math.abs(touchpad.getKnobPercentY())) {
            posX = touchpad.getKnobPercentX() < 0 ? -1 : 1;
        } else {
            posY = touchpad.getKnobPercentY() < 0 ? -1 : 1;
        }
        //System.out.println("setTouch = " +posX + "  " + posY);
        listener.changed(posX, posY);
    }
}