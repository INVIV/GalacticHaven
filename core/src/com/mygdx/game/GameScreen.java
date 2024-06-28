package com.mygdx.game;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.HashMap;
import java.util.Map;

public class GameScreen implements Screen {
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Array<Fixture> mapBody;
    private World world;

    //камера и сцена для интерактивных обьъектов (джойстик)
    private OrthographicCamera cameraUI;
    private Stage stageUI;
    private Stage stageGame;
    JoystickTouchpad joystick;
    Player playerBody;
    Texture texturePlayer = new Texture("character_robot_side.png");
    private Box2DDebugRenderer debugRenderer;
    private Image dialog;
    private Label dialogText;

    private Fixture joinFixture;

    public GameScreen() {
        cameraUI = new OrthographicCamera();
        stageUI = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), cameraUI));
        stageGame = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        world = new World(new Vector2(0, 0), true);
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                //System.out.println("beginContact = " );
                //dialogText.setText("Collision Name = " + contact.getFixtureA().getUserData() + "  q= " + contact.getFixtureA().getUserData());
                if(contact.getFixtureA().getUserData() != null && contact.getFixtureA().getUserData().toString().trim().equals("TEST")){
                    joinFixture = contact.getFixtureA();
                    dialogText.setText("Орак, здравия. Спасибо что принялся за должность грузчика на \nстанции. По поводу твоего повышения до должности \nКвартирмейстера мы поговорим после задач, которые я тебе \nпоручу на этой смене. Иди в нижнюю часть станции, разгрузи \nящики из этого отдела в верхний отдел станции.");
                }

                if(contact.getFixtureA().getUserData() != null && contact.getFixtureA().getUserData().toString().trim().contains("CRATE")){
                    joinFixture = contact.getFixtureA();
                    joinFixture.getBody().destroyFixture(joinFixture);
                    dialogText.setText("");

                }
                //contact.getFixtureB().getUserData().equals("p")
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });


        //debugRenderer = new Box2DDebugRenderer();

        float scale = 5f;
        map = new TmxMapLoader().load("mapgame3.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, scale,  stageGame.getBatch());
        mapBody = TilemapConverter.importObjects(map, world, scale);

        joystick = new JoystickTouchpad(stageUI);
        joystick.setJoystickListener(new JoystickListener() {
            @Override
            public void changed(float x, float y) {
                //System.out.println("JOYSTICK = " + 1000 * x + "  "  + 1000 * y);
                playerBody.setLinearVelocity( x, y);
            }
        });
        playerBody = new Player(12240, 9050, 150,150, texturePlayer, world);

        dialog = new Image(new Texture ("dialog.png"));
        dialog.setPosition(stageUI.getHeight() / 2.5f + 50, 50);
        dialog.setWidth(Gdx.graphics.getWidth() - 200 - stageUI.getHeight() / 2.5f);
        dialog.setHeight(Gdx.graphics.getHeight());
        stageUI.addActor(dialog);

        BitmapFont font = new BitmapFont(Gdx.files.internal("font/rus.fnt"));
        dialogText = new Label("Отлично, шаттл прибыл на станцию. Я уже подозревал, что не \nприбуду вовремя. Судя по документу трудоустройства, мне нужно \nпройти на мост - поговорить с капитаном, по поводу моей каторги \nкоторая будет связана с моим повышением. Кажется, стоит идти \nпо белой плитке, чтобы попасть на мост.", new Label.LabelStyle(font, Color.BLACK));
        dialogText.setPosition(stageUI.getHeight() / 2.5f + 70, -80);
        dialogText.setSize(dialog.getWidth(), 500);

        stageUI.addActor(dialogText);
    }

    public void resize(int width, int height) {
    }

    @Override
    public void render(float delta) {
        world.step(1 / 60f, 6, 2);

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        mapRenderer.setView((OrthographicCamera) stageGame.getCamera());
        mapRenderer.render();

        stageGame.getCamera().position.set(playerBody.getX(), playerBody.getY(), 0);
        stageGame.act(delta);
        stageGame.getBatch().begin();
        stageGame.getBatch().draw(playerBody.getTexture(), playerBody.getX(), playerBody.getY(), playerBody.getWidth(), playerBody.getHeight());
        if(joinFixture != null){

        }

        stageGame.getBatch().end();
        stageGame.draw();

        stageUI.draw();
//        debugRenderer.render(world, stageGame.getCamera().combined);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stageUI);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
    }

}
