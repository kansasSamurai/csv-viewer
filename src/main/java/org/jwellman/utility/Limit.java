package org.jwellman.utility;

/**
 * A fluent API for limiting integers when incrementing/decrementing.
 * TODO Add ability to multipliedBy() and dividedBy()
 * TODO Possibly add generics to do other data types
 * 
 * @author rwellman
 *
 */
public class Limit {

	private int subject;
	
	private boolean initialized;
	
	private boolean isIncrementing;
	
	private Limit(int i) {
		subject = i;
	}
	
	public static Limit decrementOf(int i) {
		final Limit limit = new Limit(i-1);
		limit.isIncrementing = false;
		limit.initialized = true;
		return limit;
	}
	
	public static Limit incrementOf(int i) {
		final Limit limit = new Limit(i+1);
		limit.isIncrementing = true;
		limit.initialized = true;
		return limit;
	}
	
	public static Limit rangeOf(int i) {
		final Limit limit = new Limit(i);
		limit.isIncrementing = false;
		limit.initialized = true;
		return limit;
	}
	
	/**
	 * Only necessary if adjusting by more than 1
	 * 
	 * @param delta
	 * @return
	 */
	public Limit by(int delta) {
		if (!initialized) throw new RuntimeException("Limit NOT INITIALIZED");
		
		int adjust = this.isIncrementing ? -1 : 1;
		this.subject += (adjust + (-adjust*delta));
		return this;
	}

	public int to(int alimit) {
		if (!initialized) throw new RuntimeException("Limit NOT INITIALIZED");
		
		if (isIncrementing) 
			return Math.min(alimit, this.subject);
		else 
			return Math.max(alimit, this.subject);
	}

	public int toRange(int lower, int upper) {
		if (this.subject < lower)
			return lower;
		else if (this.subject > upper)
			return upper;
		else
			return this.subject;
	}
}
