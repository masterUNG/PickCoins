package app.masterUNG.EWTC;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class MyMainGame extends ApplicationAdapter {

    //Explicit
	private SpriteBatch batch;  // ส่วนของการวาด Texture
	private Texture imgBackground, imvPig, imvCoins;
    private OrthographicCamera objCamera;   // กำหนดขนาดของจอเกมร์
    private Rectangle pigRectangle, coinsRectangle;
    private Vector3 objVector3;
    private Sound soundPig, soundWaterDrop, soundCoinsDrop;
    private Array<Rectangle> objArray;  // ของ com.badlogit.gdx.math
    private long lastDropTime;
    private Iterator<Rectangle> objIterator;
    private BitmapFont objBitmapFont, objBitmapFontFalse;
    private int intScore, intFalse;
    private String strScore = "0", strFalse = "0";
    private Music objMusic;

    @Override
	public void create () {

        //setUp Screen
        objCamera = new OrthographicCamera();
        objCamera.setToOrtho(false, 1280, 800);
        batch = new SpriteBatch();

        //About Image
        imgBackground = new Texture("bg0.png");
        imvCoins = new Texture("coins.png");
        imvPig = new Texture("pig.png");

        //Create pigRectangle
        pigRectangle = new Rectangle();
        pigRectangle.x = 1280 / 2 - 64 / 2;
        pigRectangle.y = 50;
        pigRectangle.width = 64;
        pigRectangle.height = 64;

        //Create Sound
        soundPig = Gdx.audio.newSound(Gdx.files.internal("pig.wav"));
        soundWaterDrop = Gdx.audio.newSound(Gdx.files.internal("water_drop.wav"));
        soundCoinsDrop = Gdx.audio.newSound(Gdx.files.internal("coins_drop.wav"));

        //Create Array for Random
        objArray = new Array<Rectangle>();
        myRandomCoinsDrop();

        //Create BitMapFont
        objBitmapFont = new BitmapFont();
        objBitmapFont.setColor(Color.WHITE);
        objBitmapFont.setScale(4);

        objBitmapFontFalse = new BitmapFont();
        objBitmapFontFalse.setColor(Color.RED);
        objBitmapFontFalse.setScale(2);

        //About Music
        objMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));


	}   // create




    private void myRandomCoinsDrop() {

        //Create coinsRectangle
        coinsRectangle = new Rectangle();
        coinsRectangle.x = MathUtils.random(0, 1216);
        coinsRectangle.y = 800;
        coinsRectangle.width = 64;
        coinsRectangle.height = 64;

        objArray.add(coinsRectangle);
        lastDropTime = TimeUtils.nanoTime();


    }   // myRandomCoinsDrop



	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //About Camera
        objCamera.update();
        batch.setProjectionMatrix(objCamera.combined);


		batch.begin();

        //set BackGround
        batch.draw(imgBackground, 0, 0);

        //Draw Pig
        batch.draw(imvPig, pigRectangle.x, pigRectangle.y);

        //Draw Coins
        for (Rectangle forRectangle : objArray) {

            batch.draw(imvCoins, forRectangle.x, forRectangle.y);

        }   // for

        //Draw BitMapFont
        objBitmapFont.draw(batch, "Score = " + strScore, 900, 600);
        objBitmapFontFalse.draw(batch, "Coins Drop on Water = " + strFalse, 800, 450);


            batch.end();


        //Get onTouch
        if (Gdx.input.isTouched()) {

            //Play Sound Pig
            soundPig.play();


            //check Touch

            objVector3 = new Vector3();
            objVector3.set(Gdx.input.getX(), Gdx.input.getY(), 0);

            if (objVector3.x < 640) {
                pigRectangle.x -= 10;
            } else {
                pigRectangle.x += 10;
            }   // if2

        }   // if onTouck


        //Control Area
        if (pigRectangle.x < 0) {
            pigRectangle.x = 0;
        }   // if1

        if (pigRectangle.x > 1216) {
            pigRectangle.x = 1216;
        }   // if2


        //Runable Time
        if (TimeUtils.nanoTime() - lastDropTime > 1E9) {
            myRandomCoinsDrop();
        }   // if

        //check True & False
        objIterator = objArray.iterator();

        while (objIterator.hasNext()) {

            Rectangle objMyCoins = objIterator.next();
            objMyCoins.y -= 100 * Gdx.graphics.getDeltaTime();


            //Action When Coins Drop
            if (objMyCoins.y + 64 < 0) {

                objIterator.remove();
                soundWaterDrop.play();
                intFalse += 1;
                strFalse = Integer.toString(intFalse);

                if (intFalse == 5) {
                    Gdx.app.exit();
                }


            }   //if

            //check OverLap
            if (objMyCoins.overlaps(pigRectangle)) {

                objIterator.remove();
                soundCoinsDrop.play();
                intScore += 1;
                strScore = Integer.toString(intScore);

                if (intScore == 5) {
                   // dispose();
                   Gdx.app.exit();
                   // pause();
                }

            }   // if


        }   // while


        objMusic.play();


	}   // render


    @Override
    public void dispose() {
        super.dispose();

        imvPig.dispose();
        imvCoins.dispose();
       // imgBackground.dispose();

        soundPig.dispose();
        soundCoinsDrop.dispose();
        soundWaterDrop.dispose();

      //  batch.dispose();

    }   // dispose

    @Override
    public void pause() {
        super.pause();
    }
}   // Main Class
