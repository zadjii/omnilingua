package com.nth.kryogdx.game.states;
import com.badlogic.gdx.InputProcessor;
import com.nth.kryogdx.shaders.Shader;

/**
 * Created with IntelliJ IDEA.
 * User: zadjii
 * Date: 4/7/14
 * Time: 9:41 AM
 */
public abstract class State {

	private float totalTime = 0;
	protected InputProcessor inputProcessor = null;

	public abstract void create();
	public abstract void update(float delta);
	public abstract void render(Shader defaultShader);
	public abstract void resize(int width, int height);
	public abstract void pause();
	public abstract void resume();
	public InputProcessor getInputProcessor(){return inputProcessor;}

}
