import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javafx.scene.image.Image;

public class TileSet {
	private final int PLAYER = 0;
	private final int BG = 1;
	private final int CRATE = 2;
	private final int GOAL = 3;
	private final int WALL = 4;

	ArrayList<Image> tiles;
	
	public TileSet(){
		ArrayList<Image> tiles = new ArrayList<Image>(5);
		// Generate images

		String hero_pathname = "images/test-hero.png";
		String bg_pathname = "images/test-bg.png";
		String crate_pathname = "images/test-crate.png";
		String goal_pathname = "images/test-goal.png";
		String wall_pathname = "images/test-wall.png";
		javafx.scene.image.Image hero_Image;
		javafx.scene.image.Image bg_Image;
		javafx.scene.image.Image crate_Image;
		javafx.scene.image.Image goal_Image;
		javafx.scene.image.Image wall_Image;

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
    	tiles.add(PLAYER, hero_Image);
    	tiles.add(BG, bg_Image);
    	tiles.add(CRATE, crate_Image);
    	tiles.add(GOAL, goal_Image);
    	tiles.add(WALL, wall_Image);
    	
    	
		this.tiles = tiles;
		
	}
	
	public ArrayList<Image> getTileSet(){
		return tiles;
	}
}
