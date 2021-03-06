package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class MyGdxGame extends ApplicationAdapter {
    private boolean increment;
    private float scale = 1;
	private float rotation = 0;
	private PerspectiveCamera cam;
	private Model model;
	private ModelInstance instance;
	private ModelBatch modelBatch;
	private Environment environment;
	private Vector3 position = new Vector3();

	@Override
	public void create () {
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(10f, 10f, 10f);
		cam.lookAt(0, 0,0);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();

		ModelBuilder modelBuilder = new ModelBuilder();
		Material mat = new Material(ColorAttribute.createDiffuse(Color.BLUE));
		model = modelBuilder.createBox(5, 5, 5, mat,
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
		instance = new ModelInstance(model);

		modelBatch = new ModelBatch();
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
	}

	@Override
	public void render () {
		movement();
		Gdx.gl.glViewport(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(cam);
		modelBatch.render(instance, environment);
		modelBatch.end();
		rotate();
		movement();
		scale();
		updateTransformation();
	}
	
	@Override
	public void dispose () {
		model.dispose();
	}

	private void movement() {
		instance.transform.getTranslation(position);
		if(Gdx.input.isKeyPressed(Input.Keys.W)) {
			position.x += Gdx.graphics.getDeltaTime();
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)) {
			position.z += Gdx.graphics.getDeltaTime();
		}
		if(Gdx.input.isKeyPressed(Input.Keys.A)) {
			position.z -= Gdx.graphics.getDeltaTime();
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)) {
			position.x -= Gdx.graphics.getDeltaTime();
		}
	}


	private void rotate() {
		rotation = (rotation + Gdx.graphics.getDeltaTime() * 100) % 360;

//		if(Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
//			instance.transform.setToRotation(Vector3.X, Gdx.graphics.getDeltaTime() * 100);
//		}
//		if(Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
//			instance.transform.setToRotation(Vector3.Y, Gdx.graphics.getDeltaTime() * 100);
//		}
//		if(Gdx.input.isKeyPressed(Input.Keys.NUM_3)) {
//			instance.transform.setToRotation(Vector3.Z, Gdx.graphics.getDeltaTime() * 100);
//		}
	}

	private void updateTransformation() {
		Matrix4 matrix4 = instance.transform.setFromEulerAngles(0, 0, rotation);
		matrix4.trn(position.x, position.y, position.z).scale(scale, scale, scale);
	}

	private void scale() {
	    if(increment) {
	        scale = (scale + Gdx.graphics.getDeltaTime()/5);
	        if(scale >= 1.5f) {
	            increment = false;
            }
        }
        else {
            scale = (scale - Gdx.graphics.getDeltaTime()/5);
            if(scale <= 0.5f) {
                increment = true;
            }
        }
    }
}
