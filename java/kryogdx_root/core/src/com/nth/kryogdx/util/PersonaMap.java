package com.nth.kryogdx.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.nth.kryogdx.entities.*;
import com.nth.kryogdx.entities.Character;
import com.nth.kryogdx.personas.Persona;

import static com.nth.kryogdx.util.Constants.GS;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Stores all of the personas. Holds the personas in the array, then each tick
 * rehashes all of them into the map. Each Grid space that a person occupies
 * will be in the map.
 * Initially authored some time around August 2012
 * Created with IntelliJ IDEA.
 * User: zadjii
 * Date: 5/10/14
 * Time: 11:33 AM
 */
public class PersonaMap {

	private HashMap<Vector2, ArrayList<Persona>> map = new HashMap<Vector2, ArrayList<Persona>>();
	private ArrayList<Persona> personas = new ArrayList<Persona>();

	public void add(Persona person){
		personas.add(person);
		hashPersona(person);
	}
	public void clear(){
		this.personas = new ArrayList<Persona>();
		reseed();
	}
	private void hashPersona(Persona persona){
		Character character = persona.getCharacter();
		Rectangle hitbox = character.getXZHitbox();
		int gx = ((int) (hitbox.getX() / GS));
		int gy = ((int) (hitbox.getY() / GS));
		if(hitbox.getX() < 0)gx--;
		if(hitbox.getY() < 0)gy--;
		int gwidth =  (int)(hitbox.getWidth()/GS) + 1;
		int gheight = (int)(hitbox.getHeight()/GS) + 1;
		for(int dx = 0; dx <= gwidth; dx++){
			for(int dy = 0; dy <= gheight; dy++){

				Vector2 newGridVector2 = new Vector2(gx+dx, gy+dy);
				if(map.get(newGridVector2)==null){
//					System.out.println(
//							"added an arraylist at " + print(newGridVector2)
//					);
					map.put(newGridVector2, new ArrayList<Persona>());
				}

				ArrayList<Persona> list = map.get(newGridVector2);
				list.add(persona);

			}
		}
		//System.out.println("Hashed " + persona);

	}
	private String print(Vector2 p ){
		//return "(" + p.x + "," + p.y + ")";
		return p.toString();
	}
	//	public Persona remove(Vector2 p){
//		Persona removing = map.remove(p);
//		personas.remove(removing);
//		return removing;
//	}
	public void remove(Persona person){
		personas.remove(person);
		//map.remove(person.getCharacter().getAbsoluteLoc());
	}
	//	public void reseed(){
//		ArrayList<Vector2> toBeReseeded = new ArrayList<Vector2>();
//		for (Vector2 point : map.keySet()){
//			Persona person = map.get(point);
//			if(!person.getCharacter().getAbsoluteLoc().equals(point)){
//				toBeReseeded.add(point);
//			}
//		}
//		for(Vector2 point:toBeReseeded){
//			Persona person = map.remove(point);
//			map.put(person.getCharacter().getAbsoluteLoc(), person);
//		}
//	}
	public void reseed(){
		map = new HashMap<Vector2, ArrayList<Persona>>();
		ArrayList<Persona> temp = new ArrayList<Persona>();
		for(Persona persona : personas){
			if(persona.getCharacter().getHP() > 0){
				hashPersona(persona);
			}
			else{
				temp.add(persona);
				//remove the person EVERYWHERE;
			}
		}
		for(Persona p : temp){personas.remove(p);}
//		for(Vector2 p : map.keySet()){
//			System.out.println(
//					Demigods.getLoc(p) + map.get(p)
//			);
//		}
//		System.out.println(
//		);
	}

	/**
	 * The rectangle is an absolute bounds.
	 * @param rectangle
	 * @return
	 */
	public ArrayList<Persona> get(Rectangle rectangle){
		int gx = (int)(rectangle.getX()/GS);
		int gy = (int)(rectangle.getY()/GS);

		if(rectangle.getX() < 0)gx--;
		if(rectangle.getY() < 0)gy--;

		int gwidth =  (int)(rectangle.getWidth()/GS) + 1;
		int gheight = (int)(rectangle.getHeight()/GS) + 1;
		ArrayList<Persona> returning = new ArrayList<Persona>();
		boolean useRect = gwidth*gheight < personas.size();
		if(!useRect){
			for (Persona persona : personas){
				if(persona.getCharacter().getXZHitbox().overlaps(rectangle)){
					if(!returning.contains(persona))returning.add(persona);
				}
			}
		}
		else{
			for(int dx = 0;dx<gwidth;dx++){
				for(int dy = 0;dy<gheight;dy++){

					Vector2 point = new Vector2(gx + dx, gy + dy);
					if(map.get(point)!=null){
						for(Persona persona : map.get(point)){
							if(!returning.contains(persona))returning.add(persona);
						}
					}
				}
			}
		}
		return returning;
	}
	/**
	 * The point is an absolute bounds.
	 * @param point
	 * @return
	 */
//	public ArrayList<Persona> get(Vector2 point){
//		System.out.println(Demigods.getLoc(point));
//		int gx = point.getX()/GS;
//		int gy = point.getY()/GS;
//
//		if(point.getX() < 0)gx--;
//		if(point.getY() < 0)gy--;
//		Vector2 gridVector2 = new Vector2(gx, gy);
//		System.out.println(Demigods.getLoc(gridVector2));
//		ArrayList<Persona> returning = new ArrayList<Persona>();
//
//
//		if(map.get(gridVector2)!=null){
//			for(Persona persona : map.get(gridVector2)){
//				if(!returning.contains(persona))returning.add(persona);
//			}
//		}
//
//
//		return returning;
//	}
	public ArrayList<Persona> get(Vector3 point){
		Rectangle rect = new Rectangle(point.x-8, point.z-8, GS, GS);
		return get(rect);
	}
	public ArrayList<Persona> get(Vector2 point){
		Rectangle rect = new Rectangle(point.x-8, point.y-8, GS, GS);
		return get(rect);
	}

	public Persona getClosest(final Vector2 point){
		ArrayList<Persona> copy = new ArrayList<Persona>(personas);


		//Ofc, gradle is fucking up my ability to use java 8 :C
		//copy.sort((arg0, arg1)->{
		//	if(!(arg0 instanceof Persona)) throw new ClassCastException();
		//	if(!(arg1 instanceof Persona)) throw new ClassCastException();
		//	Character a = ((Persona) arg0).getCharacter();
		//	Character b = ((Persona) arg1).getCharacter();
		//	return a.getXZPos().dst(point) == b.getXZPos().dst(point) ?
		//		0 :
		//		a.getXZPos().dst(point) > b.getXZPos().dst(point) ? 1 : -1;
		//});
		copy.sort(new Comparator<Persona>() {
			@Override
			public int compare(Persona arg0, Persona arg1) {
				if (!(arg0 instanceof Persona)) throw new ClassCastException();
				if (!(arg1 instanceof Persona)) throw new ClassCastException();
				Character a = ((Persona) arg0).getCharacter();
				Character b = ((Persona) arg1).getCharacter();
				return a.getXZPos().dst(point) == b.getXZPos().dst(point) ?
					0 :
					a.getXZPos().dst(point) > b.getXZPos().dst(point) ? 1 : -1;
			}
		});
		System.out.println("Personamap.closest to " + point + " is:" + copy.get(0).getCharacter().getXZPos());
		System.out.println("Personamap.farthes to " + point + " is:" + copy.get(copy.size()-1).getCharacter().getXZPos());

		return copy.get(0);
		//double closestDistance = -1;
		//Persona closest = null;
		//for(Persona persona : personas){
		//	double distance = Maths.dist(persona.getCharacter().getLoc(), point);
		//	if(closest == null || distance < closestDistance){
		//		if(sameTeam){
		//			if(persona.getTeam() == team){
		//				closest = persona;
		//				closestDistance = distance;
		//			}
		//		}
		//		else if(differentTeam){
		//			if(persona.getTeam() != team){
		//				closest = persona;
		//				closestDistance = distance;
		//			}
		//		}
		//		else{
		//			closest = persona;
		//			closestDistance = distance;
		//		}
		//	}
		//}
		//return closest;
	}

	/**
	 * Returns the closest character to this character
	 * @param character
	 * @return
	 */
	public Persona getClosest(Character character){

		return getClosest(character.getXZPos());
		//double closestDistance = -1;
		//Persona closest = null;
		//for(Persona persona : personas){
		//	double distance = Maths.dist(persona.getCharacter().getLoc(), point);
		//	if(closest == null || distance < closestDistance){
		//		if(persona.getCharacter() != character){
		//			closest = persona;
		//			closestDistance = distance;
		//		}
		//	}
		//}
		//return closest;
	}

	//public Persona getClosestEnemy(Entity entity, int team){
	//	return getClosestEnemy(entity.getLoc(), team);
	//}
	//public Persona getClosestEnemy(Vector2 point, int team){
	//	double closestDistance = -1;
	//	Persona closest = null;
	//	for(Persona persona : personas){
	//		double distance = Maths.dist(persona.getCharacter().getLoc(), point);
	//		if(closest == null || distance < closestDistance){
	//			if(persona.getTeam() != team){
	//				closest = persona;
	//				closestDistance = distance;
	//			}
	//		}
	//	}
	//	return closest;
	//}
	//public Persona getClosestEnemy(Vector2 point, int team, Character[] ignored){
	//	double closestDistance = -1;
	//	Persona closest = null;
	//	for(Persona persona : personas){
	//		double distance = Maths.dist(persona.getCharacter().getLoc(), point);
	//		if(closest == null || distance < closestDistance){
	//			DECIDING_STEP:if(persona.getTeam() != team){
	//				for(Character character : ignored){
	//					if(character.equals(persona.getCharacter())) break DECIDING_STEP;
	//				}
	//				closest = persona;
	//				closestDistance = distance;
	//			}
	//		}
	//	}
	//	return closest;
	//}
	public int size(){
		return personas.size();
	}
	public ArrayList<Persona> getPersonas(){
		return this.personas;
	}
	public boolean contains(Persona persona){
		return personas.contains(persona);
	}
	public String toString(){
		return personas.toString();
	}
}
