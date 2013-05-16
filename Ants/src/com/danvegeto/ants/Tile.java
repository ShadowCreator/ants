package com.danvegeto.ants;
import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

public class Tile {
	
	public int x, y;
	
	public double[] rgb;
	
	public double food;
	
	public boolean seed;
	
	public List<Ant> ants;
	
	public Colony colony;
	
	public Tile(int x, int y)
	{
		this.x = x;
		this.y = y;
		
		rgb = new double[3];
		food = 0;
		ants = new LinkedList<Ant>();
		
		seed = false;
	}
	
	public void iterate(double dist, Tile[] adj)
	{	
		if(hasFood())
			food += Game.foodGrowthRate * food * (1 - food);
		else if(seed)
			food += Game.foodSeedSize;
		else if(!nearColony())
		{
			for(int x = 0; x < adj.length; x++)
				if(adj[x] != null && adj[x].food > Game.foodSeedThreshold)
					food = Game.foodSeedSize;
		}
		
		for(int c = 0; c < 3; c++)
			rgb[c] *= 1 - Game.scentFadeRate;
		
		for(int i = 0; i < ants.size(); i++)
		{
			Ant a = ants.get(i);
			
			if(a.age > Game.ageLimit || a.starvation > Game.starvationLimit)
			{
				a.colony.ants.remove(a);
				ants.remove(i);
				
				continue;
			}
			
			for(int c = 0; c < 3; c++)
				rgb[c] = Math.max(rgb[c], 
						rgb[c] * (1 - Game.scentDropRate) + 
						a.scent()[c] * Game.scentDropRate);
			
			
			if(hasFood() && a.food < Game.foodCapacity)
			{
				double f = Math.min(food, Game.foodCapacity - a.food);
				food -= f;
				a.food += f;
			}
			
			
			if(a.food >= Game.foodPerOffspring && nearColony(a))
			{
				while(a.food >= Game.foodPerOffspring)
				{	
					a.food -= Game.foodPerOffspring;
					a.colony.totalFood += Game.foodPerOffspring;
					Ant newAnt = a.crossover(a.colony.last);
					newAnt.mutate();
					a.colony.addAnt(newAnt);
				}
				
				a.colony.last = a;
				a.food = 0;
			}
			
			
			boolean[] open = new boolean[adj.length];
			for(int x = 0; x < 4; x++)
				open[x] = adj[x] != null;
			
			double[][] scents = new double[adj.length][3];
			for(int x = 0; x < 4; x++)
				if(open[x])
					scents[x] = adj[x].rgb;
			
			dist = 1.0 * distToColony(a) / (Game.rows + Game.cols);
			
			a.act(dist, rgb, open, scents);
			
			
		}
		
	}
	
	public boolean nearColony()
	{
		for(Ant a : ants)
			if(nearColony(a))
				return true;
		return false;
	}
	
	public int distToColony(Ant a)
	{
		return
			Math.abs(a.colony.tile.x - x) +
			Math.abs(a.colony.tile.y - y);
	}
	
	public boolean nearColony(Ant a)
	{
		double dist = 
			Math.abs(a.colony.tile.x - x) +
			Math.abs(a.colony.tile.y - y);
		
		return dist <= Game.colonyRange;
	}
	
	public void addFood()
	{
		if(food == 0)
			food = Game.foodSeedSize;
	}
	public void addFood(double f)
	{
		food = f;
	}
	
	public boolean hasAnt()
	{
		return ants.size() > 0;
	}
	
	public boolean hasFood()
	{
		return food > 0;
	}
	
	public Color color()
	{
		return new Color((int)(rgb[0]*255), (int)(rgb[1]*255), (int)(rgb[2]*255));
	}
	
	public Tile copy()
	{
		Tile t = new Tile(x, y);
		t.rgb = rgb.clone();
		t.food = food;
		t.seed = seed;
		
		t.ants = new LinkedList<Ant>();
		t.ants.addAll(ants);
		
		if(colony != null)
		{
			colony.tile = t;
			t.colony = colony;
		}
		
		return t;
	}
}
