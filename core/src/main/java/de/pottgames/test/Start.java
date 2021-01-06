package de.pottgames.test;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffectLoader;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.graphics.g3d.particles.batches.PointSpriteParticleBatch;
import com.badlogic.gdx.math.Vector3;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. Listens to user input. */
public class Start extends ApplicationAdapter implements InputProcessor {
    private PerspectiveCamera cam;
    private ModelBatch        modelBatch;
    private AssetManager      assets;
    private ParticleEffect    currentEffects;
    private ParticleSystem    particleSystem;
    private float             effectScale = 1f;


    @Override
    public void create() {
        Gdx.input.setInputProcessor(this);

        // SETUP STUFF
        this.modelBatch = new ModelBatch();
        this.cam = new PerspectiveCamera();
        this.cam.position.set(0f, 0f, 10f);
        this.cam.near = 1f;
        this.cam.far = 100000f;
        this.cam.update(true);
        this.assets = new AssetManager();

        // SETUP PARTICLE SYSTEM
        this.particleSystem = new ParticleSystem();
        PointSpriteParticleBatch pointSpriteBatch = new PointSpriteParticleBatch();
        pointSpriteBatch.setCamera(this.cam);
        this.particleSystem.add(pointSpriteBatch);

        // LOAD PARTICLE EFFECT
        ParticleEffectLoader.ParticleEffectLoadParameter loadParam = new ParticleEffectLoader.ParticleEffectLoadParameter(this.particleSystem.getBatches());
        this.assets.load("effect.p3d", ParticleEffect.class, loadParam);
        this.assets.finishLoading();

        // INITIALIZE PARTICLE EFFECT
        this.currentEffects = this.assets.get("effect.p3d", ParticleEffect.class).copy();
        this.currentEffects.init();
        this.currentEffects.start();
        this.currentEffects.scale(new Vector3(2f, 2f, 2f));
        this.particleSystem.add(this.currentEffects);
    }


    @Override
    public void resize(final int width, final int height) {
        this.cam.viewportWidth = width;
        this.cam.viewportHeight = height;
        this.cam.update();
    }


    @Override
    public void render() {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        this.modelBatch.begin(this.cam);
        this.particleSystem.update();
        this.particleSystem.begin();
        this.particleSystem.draw();
        this.particleSystem.end();
        this.modelBatch.render(this.particleSystem);
        this.modelBatch.end();
    }


    @Override
    public void dispose() {
        if (this.currentEffects != null) {
            this.currentEffects.dispose();
        }
        this.modelBatch.dispose();
        this.assets.dispose();
    }


    @Override
    public boolean keyDown(int keycode) {
        return false;
    }


    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.SPACE) {
            try {
                Thread.sleep(200L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }


    @Override
    public boolean keyTyped(char character) {
        return false;
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }


    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }


    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }


    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }


    @Override
    public boolean scrolled(float amountX, float amountY) {
        float scale;
        if (amountY > 0f) {
            scale = 1.1f;
        } else {
            scale = 0.9f;
        }
        this.effectScale *= scale;
        this.currentEffects.scale(scale, scale, scale);

        System.out.println("setting scale to: " + this.effectScale);
        this.cam.position.set(0f, 0f, 10f * this.effectScale);
        this.cam.update();
        return true;
    }

}