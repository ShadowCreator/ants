package com.danvegeto.ants;
import java.awt.Color;
import java.util.LinkedList;
import java.util.List;


public class Colony
{
	Tile tile;
	Color color;
	List<Ant> ants;
	Ant last;
	
	double totalFood;
	int totalAnts;
	
	public Colony(Tile t, Color c)
	{
		tile = t;
		
		color = c;
		
		ants = new LinkedList<Ant>();
		
		for(int i = 0; i < Game.startingAnts; i++)
		{
			addAnt();
		}
		
		last = new Ant(this);
	}
	
	void addAnt()
	{
		addAnt(new Ant(this));
	}
	
	void addAnt(Ant a)
	{
		tile.ants.add(a);
		ants.add(a);
		totalAnts++;
	}

	public Colony copy()
	{

		Colony c = new Colony(tile, color);
		
		c.totalFood = totalFood;
		c.totalAnts = totalAnts;

		c.last = last;
		
		c.ants = new LinkedList<Ant>();
		c.ants.addAll(ants);
		
		return c;
	}
}
