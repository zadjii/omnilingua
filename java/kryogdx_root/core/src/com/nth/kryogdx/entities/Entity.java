package com.nth.kryogdx.entities;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.nth.kryogdx.shaders.Shader;
import com.nth.kryogdx.util.Constants;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: zadjii
 * Date: 5/1/14
 * Time: 10:35 PM
 */
public class Entity implements Serializable {
	protected float x, y, z;
	protected float dx, dy, dz;
	protected float ddx, ddy, ddz;
	protected float w, h, d;//width, height, depth
	private static int nextEntityID = 1;
	private int id ;//= Constants.SERVER;
	public long lastInputProcessed = -1;
	private float baseSpeed = 16.0f;
	private float speed = baseSpeed;

	public Entity(){
		x = y = z = 0;
		dx = dy = dz = 0;
		ddx = ddy = ddz = 0;
		w = h = d = 0;
		this.id = nextEntityID++;
	}

	public void tick(float delta){
		dx += ddx*delta;
		dy += ddy*delta;
		dz += ddz*delta;
		x += dx*delta;
		y += dy*delta;
		z += dz*delta;

	}

	public void setXYZ(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public void setDXDYDZ(float dx, float dy, float dz){
		this.dx = dx;
		this.dy = dy;
		this.dz = dz;
	}
	public Entity setPositionFromOther(Entity other){
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
		this.dx = other.dx;
		this.dy = other.dy;
		this.dz = other.dz;
		this.ddx = other.ddx;
		this.ddy = other.ddy;
		this.ddz = other.ddz;
		this.w = other.w;
		this.h = other.h;
		this.d = other.d;
		return this;
	}
	public Entity(Entity other){
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
		this.dx = other.dx;
		this.dy = other.dy;
		this.dz = other.dz;
		this.ddx = other.ddx;
		this.ddy = other.ddy;
		this.ddz = other.ddz;
		this.w = other.w;
		this.h = other.h;
		this.d = other.d;
		this.lastInputProcessed = other.lastInputProcessed;
		throw new RuntimeException("You shouldn't actually be using this");
	}



	public float getDepth() {
		return d;
	}
	public void setDepth(float d) {
		this.d = d;
	}
	public float getHeight() {
		return h;
	}
	public void setHeight(float h) {
		this.h = h;
	}
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
	public float getZ() {
		return z;
	}
	public void setZ(float z) {
		this.z = z;
	}
	public float getDX() {
		return dx;
	}
	public void setDX(float dx) {
		this.dx = dx;
	}
	public float getDY() {
		return dy;
	}
	public void setDY(float dy) {
		this.dy = dy;
	}
	public float getDZ() {
		return dz;
	}
	public void setDZ(float dz) {
		this.dz = dz;
	}
	public float getDDX() {
		return ddx;
	}
	public void setDDX(float ddx) {
		this.ddx = ddx;
	}
	public float getDDY() {
		return ddy;
	}
	public void setDDY(float ddy) {
		this.ddy = ddy;
	}
	public float getDDZ() {
		return ddz;
	}
	public void setDDZ(float ddz) {
		this.ddz = ddz;
	}
	public float getWidth() {
		return w;
	}
	public void setWidth(float w) {
		this.w = w;
	}
	public int getID() {
		return id;
	}
	public void setID(int id) {
		this.id = id;
	}

	public float getSpeed(){return speed;}


	/**
	 * Returns the hitbox in the XZ plane.
	 * This is typically used for collision detection.
	 */
	public Rectangle getXZHitbox(){
		return new Rectangle(this.x, this.z, this.w, this.d);
	}

	public void render(Matrix4 modl, Shader shader){
		//translate to this.xyz
		modl = modl.cpy().translate(this.x, this.y, this.z);
		shader.updateModel(modl);

	}

	public Vector3 getPos() {
		return new Vector3(x, y, z);
	}
	public Vector2 getXZPos() {
		return new Vector2(x, z);
	}
}