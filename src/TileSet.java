import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javafx.scene.image.Image;
/**
 * Class provides set of tiles to be used for displaying gameboard
 * @author Team Dependency
 *
 */
public class TileSet {
	private final int PLAYER = 0;
	private final int BG = 1;
	private final int CRATE = 2;
	private final int GOAL = 3;
	private final int WALL = 4;
	private final int PLAYER_UP = 5;
	private final int PLAYER_DOWN = 6;
	private final int PLAYER_LEFT = 7;
	private final int PLAYER_RIGHT = 8;
	

	private ArrayList<Image> tiles;
	
	public TileSet(){
		ArrayList<Image> tiles = new ArrayList<Image>(5);
		// Generate images

		String hero_pathname = "images/test-hero.png";
		String bg_pathname = "images/test-bg.png";
		String crate_pathname = "images/test-crate.png";
		String goal_pathname = "images/test-goal.png";
		String wall_pathname = "images/test-wall.png";
		String heroU_pathname = "images/test-hero-up.png";
		String heroD_pathname = "images/test-hero-down.png";
		String heroL_pathname = "images/test-hero-left.png";
		String heroR_pathname = "images/test-hero-right.png";
		
		javafx.scene.image.Image hero_Image;
		javafx.scene.image.Image bg_Image;
		javafx.scene.image.Image crate_Image;
		javafx.scene.image.Image goal_Image;
		javafx.scene.image.Image wall_Image;
		javafx.scene.image.Image heroU_Image;
		javafx.scene.image.Image heroD_Image;
		javafx.scene.image.Image heroL_Image;
		javafx.scene.image.Image heroR_Image;
		// Default image in case image doesn't load
		File default_file = new File("images/default.png");
		String default_local = default_file.toURI().toString();
		
		try
		{
			File hero_file = new File(hero_pathname);
	    	String hero_local = hero_file.toURI().toString();
	    	hero_Image = new javafx.scene.image.Image(hero_local, false);
		} catch (NullPointerException ne) {
			hero_Image = new javafx.scene.image.Image(default_local);
		}	
    	
		try
		{
			File bg_file = new File(bg_pathname);
	    	String bg_local = bg_file.toURI().toString();
	    	bg_Image = new javafx.scene.image.Image(bg_local, false);
		} catch (NullPointerException ne) {
			bg_Image = new javafx.scene.image.Image(default_local);
		}
		
		try
		{
	    	File crate_file = new File(crate_pathname);
	    	String crate_local = crate_file.toURI().toString();
	    	crate_Image = new javafx.scene.image.Image(crate_local, false);
		} catch (NullPointerException ne) {
			crate_Image = new javafx.scene.image.Image(default_local);
		}
		
		try
		{
	    	File goal_file = new File(goal_pathname);
	    	String goal_local = goal_file.toURI().toString();
	    	goal_Image = new javafx.scene.image.Image(goal_local, false);
		} catch (NullPointerException ne) {
			goal_Image = new javafx.scene.image.Image(default_local);
		}	
		
		try
		{
	    	File wall_file = new File(wall_pathname);
	    	String wall_local = wall_file.toURI().toString();
	    	wall_Image = new javafx.scene.image.Image(wall_local, false);
		} catch (NullPointerException ne) {
			wall_Image = new javafx.scene.image.Image(default_local);
		}
		
		try
		{
			File heroU_file = new File(heroU_pathname);
	    	String heroU_local = heroU_file.toURI().toString();
	    	heroU_Image = new javafx.scene.image.Image(heroU_local, false);
		} catch (NullPointerException ne) {
			heroU_Image = new javafx.scene.image.Image(default_local);
		}
		
		try
		{
			File heroD_file = new File(heroD_pathname);
	    	String heroD_local = heroD_file.toURI().toString();
	    	heroD_Image = new javafx.scene.image.Image(heroD_local, false);
		} catch (NullPointerException ne) {
			heroD_Image = new javafx.scene.image.Image(default_local);
		}
		
		try
		{
			File heroL_file = new File(heroL_pathname);
	    	String heroL_local = heroL_file.toURI().toString();
	    	heroL_Image = new javafx.scene.image.Image(heroL_local, false);
		} catch (NullPointerException ne) {
			heroL_Image = new javafx.scene.image.Image(default_local);
		}	
		
		try
		{
			File heroR_file = new File(heroR_pathname);
	    	String heroR_local = heroR_file.toURI().toString();
	    	heroR_Image = new javafx.scene.image.Image(heroR_local, false);
		} catch (NullPointerException ne) {
			heroR_Image = new javafx.scene.image.Image(default_local);
		}	
		
    	tiles.add(PLAYER, hero_Image);
    	tiles.add(BG, bg_Image);
    	tiles.add(CRATE, crate_Image);
    	tiles.add(GOAL, goal_Image);
    	tiles.add(WALL, wall_Image);
    	tiles.add(PLAYER_UP, heroU_Image);
    	tiles.add(PLAYER_DOWN, heroD_Image);
    	tiles.add(PLAYER_LEFT, heroL_Image);
    	tiles.add(PLAYER_RIGHT, heroR_Image);
    	
		this.tiles = tiles;
		
	}
	/**
	 * Gets the ArrayList of tiles
	 * @return ArrayList of tiles
	 */
	public ArrayList<Image> getTileSet(){
		return tiles;
	}
	
	/**
	 * Sets the player icon to the player up sprite
	 */
	public void setPlayerUp(){
		Image playerImage = tiles.get(PLAYER_UP);
		tiles.set(PLAYER,playerImage);
	}

	/**
	 * Sets the player icon to the player down sprite
	 */
	public void setPlayerDown(){
		Image playerImage = tiles.get(PLAYER_DOWN);
		tiles.set(PLAYER,playerImage);
	}
	
	/**
	 * Sets the player icon to the player left sprite
	 */
	public void setPlayerLeft(){
		Image playerImage = tiles.get(PLAYER_LEFT);
		tiles.set(PLAYER,playerImage);
	}
	
	
	/**
	 * Sets the player icon to the player right sprite
	 */
	public void setPlayerRight(){
		Image playerImage = tiles.get(PLAYER_RIGHT);
		tiles.set(PLAYER,playerImage);
	}
}
