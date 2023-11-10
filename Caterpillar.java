package assignment2;

import java.awt.Color;
import java.util.Random;
import java.util.Stack;

import assignment2.food.*;


public class Caterpillar {
	// All the fields have been declared public for testing purposes
	public Segment head;
	public Segment tail;
	public int length;
	public EvolutionStage stage;

	public Stack<Position> positionsPreviouslyOccupied;
	public int goal;
	public int turnsNeededToDigest;


	public static Random randNumGenerator = new Random(1);


	// Creates a Caterpillar with one Segment. It is up to students to decide how to implement this. 
	// Caterpillar(Position p, Color c, int goal) : creates a caterpillar with one single segment of the given color, in the given position. The input goal is used to set the number of segments the caterpillar needs to acquire before becoming a butterfly. Whenever a caterpillar is first created, it finds itself in a feeding stage, ready to devour every delicacy within its reach.
	// This method runs in O(1).

	public Caterpillar(Position p, Color c, int goal) {
		this.head = new Segment(p, c);
		this.tail = this.head;
		this.length = 1;
		this.stage = EvolutionStage.FEEDING_STAGE;
		this.positionsPreviouslyOccupied = new Stack<Position>();
		this.goal = goal;
	}

	
	public EvolutionStage getEvolutionStage() {
		return this.stage;
	}

	public Position getHeadPosition() {
		return this.head.position;
	}

	public int getLength() {
		return this.length;
	}


	//getSegmentColor(Position p) : returns the color of the segment in the given position. If the caterpillar, does not have a segment placed upon the given position, then the method returns null.
	//This method runs in O(n), where n is the number of segments
	public Color getSegmentColor(Position p) {
		for(Segment check = this.head; check.next != null; check = check.next){
			if (check.position == p){
				return check.color;}
		}
		return null;
	}


	//  if possible the caterpillar moves its head (and all its body) to the input position. If the input position is out of reach, i.e. if when seen as a point on a Cartesian plane it is not orthogonally connected to the head’s position, then an IllegalArgumentException should be raised. If the position is within reach, but it is already occupied by a segment of the caterpillar’s body, then moving will generate a collision leading to the caterpillar being in an ENTANGLED stage (unfortunately, caterpillars do not recover from this stage and the game will end). If on the other hand, the position is reachable and moving to it would not lead to a collision, then the body of the caterpillar should be updated accordingly: all the positions in the segments should be updated to represent the caterpillar moving forward to the input position, while the colors remain the same.
	public void move(Position p) {
		if (Position.getDistance(head.position,p) > 1){
			throw new IllegalArgumentException();
		}
		this.head.position = p;
		for(Segment check = this.head; check.next != null; check = check.next) {
			check.position = check.next.position;}

		}




	// With each fruit bite, the caterpillar grows longer by getting a new segment added matching the color of the fruit ingested. The new segment should be added at the tail of the caterpillar, and its position should be the most recent position previously occupied by the caterpillar. Make sure to update all relevant fields to represent this growth.
	//This method runs in O(1).
	public void eat(Fruit f) {
		this.tail.next = new Segment(this.positionsPreviouslyOccupied.peek(), f.getColor());
		this.tail = this.tail.next;
		this.length++;
		this.positionsPreviouslyOccupied.push(this.tail.position);

	}

	// the caterpillar moves one step backwards because of sourness
	public void eat(Pickle p) {
		/*
		 * TODO: ADD YOUR CODE HERE
		 */	
	}


	// all the caterpillar's colors shuffles around
	public void eat(Lollipop lolly) {
		/*
		 * TODO: ADD YOUR CODE HERE
		 */	
	}

	// brain freeze!!
	// It reverses and its (new) head turns blue
	public void eat(IceCream gelato) {
		/*
		 * TODO: ADD YOUR CODE HERE
		 */	
	}

	// the caterpillar embodies a slide of Swiss cheese loosing half of its segments. 
	public void eat(SwissCheese cheese) {
		/*
		 * TODO: ADD YOUR CODE HERE
		 */	
	}


	public void eat(Cake cake) {
		/*
		 * TODO: ADD YOUR CODE HERE
		 */	
	}


	// This nested class was declared public for testing purposes
	public class Segment {
		private Position position;
		private Color color;
		private Segment next;

		public Segment(Position p, Color c) {
			this.position = p;
			this.color = c;
		}

	}


	public String toString() {
		Segment s = this.head;
		String gus = "";
		while (s!=null) {
			String coloredPosition = GameColors.colorToANSIColor(s.color) + 
					s.position.toString() + GameColors.colorToANSIColor(Color.WHITE);
			gus = coloredPosition + " " + gus;
			s = s.next;
		}
		return gus;
	}

}