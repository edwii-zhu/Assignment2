package assignment2;

import java.awt.Color;
import java.util.Arrays;
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

	public Color getSegmentColor(Position p) {
		for (Segment check = this.head; check != null; check = check.next) {
			if (check.position.equals(p)) {
				return check.color;
			}
		}
		return null;
	}

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

	}


	public void eat(Fruit f) {
		this.tail.next = new Segment(this.positionsPreviouslyOccupied.pop(), f.getColor());
		this.tail = this.tail.next;
		this.length++;
	}

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

	public void eat(IceCream gelato) {
		Segment prev = null;
		Segment current = this.head;
		Segment next;
		while (current != null) {
			next = current.next;
			current.next = prev;
			prev = current;
			current = next;
		}
		this.head = prev;
		Segment check;
        check = this.head;
        while (check.next != null) {
            check = check.next;
        }
        this.tail = check;
		this.head.color = GameColors.BLUE;
		this.positionsPreviouslyOccupied.clear();
	}
	public void eat(SwissCheese cheese) {
		if (length == 2) {
			positionsPreviouslyOccupied.push(this.tail.position);
			this.tail = this.head;
			length = 1;
		}
		int nlen = this.getLength() / 2 + this.getLength() % 2;
		Position[] positions = new Position[length];
		Color[] colors = new Color[length];
		int i = 0;
		for(Segment check = this.head; i<length; check = check.next, i++) {
			positions[i] = check.position;
			colors[i] = check.color;
		}
		int j = 0;
		int k = 0;
		Segment check;
		for (check = this.head; j < nlen - 1; check = check.next, j++,k+=2) {
			check.position = positions[j];
			check.color = colors[k];
		}

		this.tail = check;
		this.tail.color = colors[length - 1];
		tail.next = null;
		for (int l = length - 1; l > nlen - 1; l--) {
			positionsPreviouslyOccupied.push(positions[l]);
		}
		this.length = nlen;
	}

	public void eat(Cake cake) {

		int energy = cake.getEnergyProvided();
		this.stage = EvolutionStage.GROWING_STAGE;
		for (int i = 0; i<energy; i++){
			if(this.positionsPreviouslyOccupied.isEmpty() || checkCollision(this.head)){
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

	public boolean checkCollision(Segment head) {
		for (Segment check = head; check != null; check = check.next) {
			if (check.position.equals(this.positionsPreviouslyOccupied.peek())) {
				return true;
			}
		}
        return false;
    }


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

