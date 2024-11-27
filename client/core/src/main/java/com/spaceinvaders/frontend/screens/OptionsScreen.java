package com.spaceinvaders.frontend.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.spaceinvaders.frontend.SpaceInvadersGame;
import com.spaceinvaders.frontend.background.PlanetsBackground;
import com.spaceinvaders.frontend.background.StarsBackground;
import com.spaceinvaders.frontend.utils.ButtonUtils;
import com.spaceinvaders.frontend.utils.LabelUtils;
import com.spaceinvaders.frontend.utils.SliderUtils;

public class OptionsScreen implements Screen {
    private final SpaceInvadersGame game;

    private final OrthographicCamera camera;
    private final Viewport viewport;

    private final float STAGE_WIDTH;

    private final Stage stage;

    private final StarsBackground starsBackground;
    private final PlanetsBackground planetsBackground;

    public OptionsScreen(SpaceInvadersGame game, float WORLD_WIDTH, float WORLD_HEIGHT, float STAGE_WIDTH, float STAGE_HEIGHT, StarsBackground starsBackground, PlanetsBackground planetsBackground) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        Viewport stageViewport = new FitViewport(STAGE_WIDTH, STAGE_HEIGHT);

        this.STAGE_WIDTH = STAGE_WIDTH;

        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();

        stage = new Stage(stageViewport);
        initializeActors();

        this.starsBackground = starsBackground;
        this.planetsBackground = planetsBackground;
    }

    @Override
    public void show() {
        game.musicManager.play("introMusic");
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0,1);

        viewport.apply();
        camera.update();

        game.batch.setProjectionMatrix(camera.combined);
        game.shapeRenderer.setProjectionMatrix(camera.combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);  // Enable blending
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        starsBackground.render(game.shapeRenderer, delta);  // Use delta for time
        planetsBackground.render(game.batch);

        game.batch.begin();
        game.batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int i, int i1) {
        viewport.update(i, i1);
        stage.getViewport().update(i, i1, true);
    }

    @Override
    public void pause() {
        game.musicManager.pause();
    }

    @Override
    public void resume() {
        game.musicManager.resume();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {

    }

    private void initializeActors() {
        // Creating Labels and Buttons for the settings menu
        Label gameplaySettings = LabelUtils.createLabel("Gameplay Settings", game.assetManager.get("fonts/minecraft.fnt", BitmapFont.class));
        ImageTextButton difficultyButton = ButtonUtils.createButton(game, "Difficulty: Easy", "textures/button.png", "textures/button.png");
        ImageTextButton controlsButton = ButtonUtils.createButton(game, "Controls: AWSD", "textures/button.png", "textures/button.png");
        ImageTextButton powerUpsButton = ButtonUtils.createButton(game, "PowerUps: ON", "textures/button.png", "textures/button.png");
        ImageTextButton blackHoleButton = ButtonUtils.createButton(game, "BlackHoles: ON", "textures/button.png", "textures/button.png");

        // Adding listeners to buttons to toggle between different settings
        difficultyButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Reset cursor to default arrow
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);

                // Play button click sound
                game.soundManager.play("buttonClick");

                // Toggle difficulty level between Easy, Medium, and Hard
                if(difficultyButton.getText().toString().equals("Difficulty: Easy")) {
                    difficultyButton.setText("Difficulty: Medium");
                } else if(difficultyButton.getText().toString().equals("Difficulty: Medium")) {
                    difficultyButton.setText("Difficulty: Hard");
                } else if(difficultyButton.getText().toString().equals("Difficulty: Hard")) {
                    difficultyButton.setText("Difficulty: Easy");
                }

                return true;
            }
        });

        controlsButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Reset cursor to default arrow
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);

                // Play button click sound
                game.soundManager.play("buttonClick");

                // Toggle control scheme between AWSD and Arrow keys
                if(controlsButton.getText().toString().equals("Controls: AWSD")) {
                    controlsButton.setText("Controls: Arrow keys");
                } else if(controlsButton.getText().toString().equals("Controls: Arrow keys")) {
                    controlsButton.setText("Controls: AWSD");
                }

                return true;
            }
        });

        powerUpsButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Reset cursor to default arrow
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);

                // Play button click sound
                game.soundManager.play("buttonClick");

                // Toggle power-ups setting between ON and OFF
                if(powerUpsButton.getText().toString().equals("PowerUps: ON")) {
                    powerUpsButton.setText("PowerUps: OFF");
                } else if(powerUpsButton.getText().toString().equals("PowerUps: OFF")) {
                    powerUpsButton.setText("PowerUps: ON");
                }

                return true;
            }
        });

        blackHoleButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Reset cursor to default arrow
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);

                // Play button click sound
                game.soundManager.play("buttonClick");

                // Toggle black hole setting between ON and OFF
                if(blackHoleButton.getText().toString().equals("BlackHoles: ON")) {
                    blackHoleButton.setText("BlackHoles: OFF");
                } else if(blackHoleButton.getText().toString().equals("BlackHoles: OFF")) {
                    blackHoleButton.setText("BlackHoles: ON");
                }

                return true;
            }
        });

        // Create a Table for layout and add the settings components
        Table table = new Table();
        table.setFillParent(true); // Make the table fill the parent container
        table.center(); // Center the table on the screen

        // Add gameplay settings title and buttons to the table
        table.add(gameplaySettings).colspan(2).padBottom(7).center();
        table.row();

        table.add(difficultyButton).width(95).height(15).pad(1); // Button for Difficulty
        table.add(controlsButton).width(95).height(15).pad(1); // Button for Controls
        table.row();

        table.add(powerUpsButton).width(95).height(15).pad(1); // Button for PowerUps
        table.add(blackHoleButton).width(95).height(15).pad(1); // Button for BlackHoles
        table.row();

        // Creating audio settings components
        Label audioSettings = LabelUtils.createLabel("Audio Settings", game.assetManager.get("fonts/minecraft.fnt", BitmapFont.class));

        // Music controls
        Image musicIcon = new Image(game.assetManager.get("textures/music.png", Texture.class));
        Slider musicSlider = SliderUtils.createSlider(game.assetManager);
        Label musicLabel = SliderUtils.getValueLabel(game.assetManager, musicSlider);

        // Create a Table for music controls (Icon, Slider, Label)
        Table musicTable = new Table();
        musicTable.add(musicIcon).size(20).pad(1).left();
        musicTable.add(musicSlider).width(150).pad(1).center();
        musicTable.add(musicLabel).size(5).pad(1).right();

        // Sound controls
        Image soundIcon = new Image(game.assetManager.get("textures/sound.png", Texture.class));
        Slider soundSlider = SliderUtils.createSlider(game.assetManager);
        Label soundLabel = SliderUtils.getValueLabel(game.assetManager, soundSlider);

        // Create a Table for sound controls (Icon, Slider, Label)
        Table soundTable = new Table();
        soundTable.add(soundIcon).size(20).pad(1).left();
        soundTable.add(soundSlider).width(150).pad(1).center();
        soundTable.add(soundLabel).size(5).pad(1).right();

        // Set initial volume for music and sound sliders and add listeners to adjust volume
        musicSlider.setValue(game.musicManager.getVolume() * 200);
        musicSlider.addListener(event -> {
            game.musicManager.setVolume(musicSlider.getValue() / 200f);
            return false;
        });

        soundSlider.setValue(game.soundManager.getVolume() * 200);
        soundSlider.addListener(event -> {
            game.soundManager.setVolume(soundSlider.getValue() / 200f);
            return false;
        });

        // Add audio settings components to the main table
        table.add(audioSettings).colspan(2).padTop(10).center(); // Title spans 3 columns
        table.row();

        // Add Music controls table to the main table
        table.add(musicTable).colspan(2).center();
        table.row();

        // Add Sound controls table to the main table
        table.add(soundTable).colspan(2).center();
        table.row().pad(5);

        // Create labels for player profile section
        Label playerProfile = LabelUtils.createLabel("Player Profile", game.assetManager.get("fonts/minecraft.fnt", BitmapFont.class));
        Label highscore = LabelUtils.createLabel("Highscore: ", game.assetManager.get("fonts/minecraft.fnt", BitmapFont.class));
        Label email = LabelUtils.createLabel("Email: " + game.token, game.assetManager.get("fonts/minecraft.fnt", BitmapFont.class));

        // Create buttons for user interactions
        ImageTextButton resetPasswordButton = ButtonUtils.createButton(game, "Reset Password", "textures/button.png", "textures/button.png");
        ImageTextButton logoutButton = ButtonUtils.createButton(game, "Logout", "textures/button.png", "textures/button.png");

        // Add listener for resetPasswordButton to handle user input
        resetPasswordButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Reset cursor to default arrow when button is clicked
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);

                // Play button click sound
                game.soundManager.play("buttonClick");

                // Navigate to the Signup screen when the button is clicked
                game.screenManager.setScreen(ScreenState.RESET_PASSWORD);
                return true;
            }
        });

        // Layout player profile information in the table
        table.add(playerProfile).colspan(2).padTop(5).center(); // "Player Profile" label spans 2 columns and is centered
        table.row(); // Move to the next row in the table

        // Add highscore and email labels to the table with defined width, height, and padding
        table.add(highscore).width(95).height(10).pad(1);
        table.add(email).width(95).height(10).pad(1);
        table.row(); // Move to the next row

        // Add Reset Password and Logout buttons with defined width, height, and padding
        table.add(resetPasswordButton).width(95).height(15).pad(1);
        table.add(logoutButton).width(95).height(15).pad(1);
        table.row(); // Move to the next row

        // Add the table to the stage
        stage.addActor(table);

        ImageButton backButton = ButtonUtils.createBackButton(this.game, "textures/back-button.png", "textures/back-button.png", 28, 15, 10, 245, game.screenManager.getRecentScreen());
        stage.addActor(backButton);

    }

}
