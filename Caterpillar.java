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
		for (Segment check = this.head; check != null; check = check.next) {
			if (check.position.equals(p)) {
				return check.color;
			}
		}
		return null;
	}


	//  if possible the caterpillar moves its head (and all its body) to the input position. If the input position is out of reach, i.e. if when seen as a point on a Cartesian plane it is not orthogonally connected to the head’s position, then an IllegalArgumentException should be raised. If the position is within reach, but it is already occupied by a segment of the caterpillar’s body, then moving will generate a collision leading to the caterpillar being in an ENTANGLED stage (unfortunately, caterpillars do not recover from this stage and the game will end). If on the other hand, the position is reachable and moving to it would not lead to a collision, then the body of the caterpillar should be updated accordingly: all the positions in the segments should be updated to represent the caterpillar moving forward to the input position, while the colors remain the same.
	public void move(Position p) {
		if (Position.getDistance(head.position, p) > 1) {
			throw new IllegalArgumentException();
		}
		positionsPreviouslyOccupied.push(this.tail.position);
		Position prev = p;
		for (Segment check = this.head; check != null; check = check.next) {
			Position temp = check.position;
			check.position = prev;
			prev = temp;
			if (prev.equals(p) && !prev.equals(positionsPreviouslyOccupied.peek())) {
				this.stage = EvolutionStage.ENTANGLED;
				return;
			}
		}
		//Finally, if the caterpillar is still digesting a cake it had previously eaten, a segment with a random color should be added at the tail of its body ensuring its ever-growing journey continues. This in turn should decrease the number of turns left for its digestion, and might update its evolution stage to BUTTERFLY (if the goal was reached). If the caterpillar is in a growing stage, but its digestion is fully completed, then the FEEDING STAGE should be resumed since caterpillar is now ready to venture out for more treats.
		if (this.stage == EvolutionStage.GROWING_STAGE && this.turnsNeededToDigest > 0) {
			this.tail.next = new Segment(this.positionsPreviouslyOccupied.pop(), GameColors.SEGMENT_COLORS[randNumGenerator.nextInt(GameColors.SEGMENT_COLORS.length)]);
			this.tail = this.tail.next;
			this.length++;
			this.turnsNeededToDigest--;
			if (this.length >= this.goal) {
				this.stage = EvolutionStage.BUTTERFLY;
				return;
			}
			if (this.turnsNeededToDigest == 0) {
				this.stage = EvolutionStage.FEEDING_STAGE;
			}
		}
		// positionsPreviouslyOccupied.push(this.tail.position);
		//this.tail.position = prev;
	}


	// With each fruit bite, the caterpillar grows longer by getting a new segment added matching the color of the fruit ingested. The new segment should be added at the tail of the caterpillar, and its position should be the most recent position previously occupied by the caterpillar. Make sure to update all relevant fields to represent this growth.
	//This method runs in O(1).
	public void eat(Fruit f) {
		this.tail.next = new Segment(this.positionsPreviouslyOccupied.pop(), f.getColor());
		this.tail = this.tail.next;
		this.length++;
	}

	// the caterpillar moves one step backwards because of sourness
	// the sourness of the pickle makes the caterpillar retrace its steps. This
	// method updates its segments accordingly.
	// For instance, suppose we have the following caterpillar: (2,2) (2,1) (3,1)
	// And suppose that the most recent position previously occupied by this caterpillar is (2,3). Then, after eating a pickle the caterpillar will have the following segments:
	// (2,3) (2,2) (2,1)
	public void eat(Pickle p) {

		Position[] positions = new Position[this.getLength()];
		int i = 0;
		for (Segment check = this.head.next; check != null; check = check.next, i++) {
			positions[i] = check.position;
		}
		positions[i] = this.positionsPreviouslyOccupied.pop();
		int j = 0;
		for (Segment check = this.head; check != null; check = check.next, j++) {
			check.position = positions[j];
		}
	}


	// all the caterpillar's colors shuffles around
	//
	//Shuffle all the caterpillar’s colors!
	//There are different ways of doing this, but for this assignment you will need to implement the method using the Fisher–Yates shuffle algorithm. The algorithm runs in O(n) using O(n) space, where n is the number of segments. To perform a shuffle of the colors follow the steps:
	//– Copy all the colors inside an array
	//– Shuffle the array using the following algorithm:
	//               for i from n-1 to 1 do
	//                   j <-- random integer such that 0 <= j <= i
	//                   swap a[j] and a[i]
	//To generate a random integer use the Random object stored in the class field called randNumGenerator.
	//– Use the array to update the colors in all the segments.

	public void eat(Lollipop lolly) {
		Color[] colors = new Color[this.getLength()];
		int i = 0;
		for (Segment check = this.head; check != null; check = check.next, i++) {
			colors[i] = check.color;
		}
		for (i = this.getLength() - 1; i > 0; i--) {
			int j = randNumGenerator.nextInt(i + 1);
			Color temp = colors[i];
			colors[i] = colors[j];
			colors[j] = temp;
		}
		int j = 0;
		for (Segment check = this.head; check != null; check = check.next, j++) {
			check.color = colors[j];
		}
	}

	// brain freeze!!
	// It reverses and its (new) head turns blue
	// Its body does a hilarious flip, reversing on itself and its (new) head turns blue like an icicle. At this point it lost track of where it has been before, and the stack of previously occupied positions is now empty.
	// This method runs in O(n), where n is the number of segments.
	public void eat(IceCream gelato) {
		Segment prev = null;
		Segment current = this.head;
		Segment next = null;
		while (current != null) {
			next = current.next;
			current.next = prev;
			prev = current;
			current = next;
		}
		this.head = prev;
		this.head.color = GameColors.BLUE;
		this.positionsPreviouslyOccupied.clear();
	}
//

	// the caterpillar embodies a slide of Swiss cheese loosing half of its segments.
	//This method shrinks the caterpillar who loses every other segment of its body. This means that the segments left will have the colors of every other segment of the original body. But be careful, we do not want a caterpillar in pieces! The segments should still appear in positions
	// that are adjacent to one another, specifically the head will remain in the original position and only the first half (rounding up) of the segments’ positions will be maintained
	public void eat(SwissCheese cheese) {
		int nlen = this.length / 2 + this.length % 2;
		Position[] positions = new Position[length];
		Color[] colors = new Color[length];
		int i = 0;
		for(Segment check = this.head; check != null; check = check.next, i++) {
			positions[i] = check.position;
			colors[i] = check.color;
		}
		int j = 0;
		int k = 0;
		Segment check;
		for (check = this.head; j < nlen; check = check.next, j++, k+=2) {
			check.position = positions[j];
			check.color = colors[k];
		}
		check.next = null;
		for (int l = length - 1; l > nlen - 1; l--) {
			positionsPreviouslyOccupied.push(positions[l]);
		}
		length = nlen;
	}
	

	//the holy grail of all treats! Here’s something that will make the caterpillar truly grow. When eating a cake, the caterpillar enters its GROWING STAGE. Its body will grow by as many segments as the energy provided by the cake. These segments will have a random color and will be added at the tail of the caterpillar’s body. Be careful though, this growth might not take place entirely in this method! In fact, the caterpillar will grow by a number of segments that is equivalent to the minimum between the energy provided and the number of previously occupied positions that are still available to the caterpillar. For example, consider the following caterpillar:
	// eat(Cake cake) : the holy grail of all treats! Here’s something that will make the caterpillar truly grow. When eating a cake, the caterpillar enters its GROWING STAGE. Its body will grow by as many segments as the energy provided by the cake. These segments will have a random color and will be added at the tail of the caterpillar’s body. Be careful though, this growth might not take place entirely in this method! In fact, the caterpillar will grow by a number of segments that is equivalent to the minimum between the energy provided and the number of previously occupied positions that are still available to the caterpillar. For example, consider the following caterpillar:
	//(0,1) (0,0) (1,0)
	//and assume the cake eaten provides an energy equal to 3 while the stack of previously occupied positions contains [(2,0), (1,0), (1,1)] (the right most element being at the top of the stack). From eating the cake, the caterpillar should grow by 3 segments, but it is not possible to do so in this method. Only (1,1) can be used for growth by the caterpillar, since (1,0) has already been occupied by another caterpillar’s segment.
	// 8
	// If the cake’s energy cannot be all consumed by the caterpillar in this method, the “left over” is then stored in the field turnsNeededToDigest, as the caterpillar will need that many turns in order to fully digest the cake.
	//There are two more things you should keep in mind: it is possible for the caterpillar to reach its butterfly stage while growing in this method. If this is the case, its stage should be updated and the method should terminate right away. It is also possible for the caterpillar to fully consume the energy provided by the cake right here in this method. If this is the case, and the butterfly stage has not been reached, then the feeding stage should be resumed.
	//Note: to generate a random color generate a random index that you can then use to select a color from the array GameColors.SEGMENT COLORS.
	//This method runs in O(n ∗ m), where n is the number of segments and m the energy provided by the cake.
	public void eat(Cake cake) {
		/*
		 * TODO: ADD YOUR CODE HERE
		 */
		int energy = cake.getEnergyProvided();
		this.stage = EvolutionStage.GROWING_STAGE;
		for (int i = 0; i<energy; i++){
			if(this.positionsPreviouslyOccupied.isEmpty() || checkCollision(this.head, this.positionsPreviouslyOccupied.peek())){
				this.turnsNeededToDigest = energy - i;
				break;
			}
			this.tail.next = new Segment(this.positionsPreviouslyOccupied.pop(), GameColors.SEGMENT_COLORS[randNumGenerator.nextInt(GameColors.SEGMENT_COLORS.length)]);
			this.tail = this.tail.next;
			this.length++;
			if (this.length >= this.goal){
				this.stage = EvolutionStage.BUTTERFLY;
				return;
			}
		}
		if (this.turnsNeededToDigest == 0){
			this.stage = EvolutionStage.FEEDING_STAGE;
			}
		}

	public boolean checkCollision(Segment head, Position p) {
		for (Segment check = head; check != null; check = check.next) {
			if (check.position.equals(this.positionsPreviouslyOccupied.peek())) {
				return true;
			}
		}
        return false;
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

	public static void main(String[] args) {
	Caterpillar gus = new Caterpillar(new Position(1,1), GameColors.GREEN, 5);
	Fruit f = new Fruit(GameColors.ORANGE);
	Fruit f2 = new Fruit(GameColors.RED);
	Fruit f3 = new Fruit(GameColors.BLUE);
	Lollipop l = new Lollipop();
	gus.move(new Position(1,2));
	gus.move(new Position(1,3));
	gus.move(new Position(2,3));

	gus.eat(f);
	gus.eat(f2);
	gus.eat(f3);
	System.out.println(gus);
	gus.eat(l);
	System.out.println(gus);
	}
}

