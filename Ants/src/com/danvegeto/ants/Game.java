package com.danvegeto.ants;
import java.awt.Color;
import java.util.LinkedList;
import java.util.List;


public class Game {
	
	public static final double 
		foodGrowthRate = 0.005,
		foodSeedThreshold = 0.5,
		foodSeedSize = 0.1,
		foodCapacity = 30,
		foodConsumption = 0.05,

		scentDropRate = 0.5,
		scentFadeRate = 0.005;
	
	public static final int
		rows = 60,
		cols = 60,
		startingFood = 	10,
		startingAnts = 4,
		starvationLimit = 20,
		ageLimit = 2000,
		colonyRange = 8,
		
		foodPerOffspring = 8;
	
	public Tile[][] tiles;
	
	public List<Colony> colonies;
	
	public Game()
	{
		colonies = new LinkedList<Colony>();
		
		tiles = new Tile[rows][cols];
		
		for(int i = 0; i < rows; i++)
			for(int j = 0; j < cols; j++)
				tiles[i][j] = new Tile(i, j);
		
		tiles[0][0].seed = true;
		
		for(int x = 0; x < startingFood; x++)
		{
			int i = (int)(Math.random() * rows);
			int j = (int)(Math.random() * cols);
			
			if(!nearColony(tiles[i][j]))
			{
				tiles[i][j].addFood(1);
				tiles[i][j].seed = true;
			}
		}
		
		colonies.add(new Colony(tiles[0][0], Color.red));
		colonies.add(new Colony(tiles[0][cols-1], Color.magenta));
		colonies.add(new Colony(tiles[rows-1][0], Color.blue));
		colonies.add(new Colony(tiles[rows-1][cols-1], Color.yellow));
	}
	
	public void iterate()
	{
		Tile[][] nextTiles = new Tile[rows][cols];
		
		for(int i = 0; i < rows; i++)
		{
			for(int j = 0; j < cols; j++)
			{
				double dist = 1.0 *
					(Math.abs(rows/2 - i) + Math.abs(cols/2 - j)) /
							(rows/2 + cols/2);
				
				nextTiles[i][j] = tiles[i][j].copy();
				nextTiles[i][j].iterate(dist, adj(tiles, i, j));
	
			}
		}
		
		tiles = nextTiles;
		
		for(Colony c : colonies)
		{
			tiles[c.tile.x][c.tile.y].colony = c;
			c.tile = tiles[c.tile.x][c.tile.y];
		}
		
		nextTiles = new Tile[rows][cols];
		
		for(int i = 0; i < rows; i++)
		{
			for(int j = 0; j < cols; j++)
			{
				nextTiles[i][j] = tiles[i][j].copy();
				nextTiles[i][j].ants.clear();
				
			}
		}
		
		for(int i = 0; i < rows; i++)
		{
			for(int j = 0; j < cols; j++)
			{

				Tile[] nextAdj = adj(nextTiles, i, j);
				
				for(int a = 0; a < tiles[i][j].ants.size(); a++)
				{
					while(nextAdj[tiles[i][j].ants.get(a).direction] == null)
						tiles[i][j].ants.get(a).direction = (int)(Math.random()*4);					{
					nextAdj[tiles[i][j].ants.get(a).direction].ants.add(
							tiles[i][j].ants.get(a));

					}
				}
				
			}
		}
		
		tiles = nextTiles;
		
		
		
		for(Colony c : colonies)
		{
			tiles[c.tile.x][c.tile.y].colony = c;
			c.tile = tiles[c.tile.x][c.tile.y];
			
			if(c.ants.size() < startingAnts)
			{
				Ant newAnt = c.last.crossover(c.last);
				newAnt.mutate();
				c.addAnt(newAnt);
			}
		}
	}
	
	private Tile[] adj(Tile[][] t, int i, int j)
	{
		Tile[] adj = new Tile[4];
		
		if(i > 0)
			adj[0] = t[i-1][j];
		if(j < cols - 1)
			adj[1] = t[i][j+1];
		if(i < rows - 1)
			adj[2] = t[i+1][j];
		if(j > 0)
			adj[3] = t[i][j-1];
		
		return adj;
	}
	
	public boolean nearColony(Tile t)
	{
		for(Colony c : colonies)
		{
			double dist = 
				Math.abs(c.tile.x - t.x) +
				Math.abs(c.tile.y - t.y);
			
			if(dist <= colonyRange)
				return true;
		}
		
		return false;
	}
}
