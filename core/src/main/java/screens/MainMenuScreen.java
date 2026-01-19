package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.games.shadowrealms.ShadowRealms;

public class MainMenuScreen implements Screen {
    
    private ShadowRealms game;
    private Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
    
    public MainMenuScreen(ShadowRealms game) {
        this.game = game;
        
        if (game.batch == null) {
            game.batch = new com.badlogic.gdx.graphics.g2d.SpriteBatch();
        }
        
        camera = new OrthographicCamera();
        viewport = new FitViewport(320, 240, camera);
        stage = new Stage(viewport, game.batch);
        
        Gdx.input.setInputProcessor(stage);
        
        createMenu();
    }
    
    private void createMenu() {
        Table table = new Table();
        table.setFillParent(true);
        
        BitmapFont font = new BitmapFont();
        font.getData().setScale(1.5f);
        
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        Label titleLabel = new Label("SHADOW REALMS", labelStyle);
        
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.overFontColor = Color.YELLOW;
        
        TextButton newGameButton = new TextButton("New Game", buttonStyle);
        TextButton exitButton = new TextButton("Exit", buttonStyle);
        
        newGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
                dispose();
            }
        });
        
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        
        table.add(titleLabel).padBottom(30).row();
        table.add(newGameButton).width(150).height(40).padBottom(15).row();
        table.add(exitButton).width(150).height(40);
        
        stage.addActor(table);
    }
    
    @Override
    public void show() {
    }
    
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        stage.act(delta);
        stage.draw();
    }
    
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
    
    @Override
    public void pause() {
    }
    
    @Override
    public void resume() {
    }
    
    @Override
    public void hide() {
    }
    
    @Override
    public void dispose() {
        stage.dispose();
    }
}
