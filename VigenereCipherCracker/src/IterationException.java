/* AUTHOR: John Russell, j362r647@ku.edu
 * DATE: 25 September 2016
 * FILE NAME: IterationException.java
 * PURPOSE: To be thrown whenever the in-testKey() iteration exceeds the valid key values
 * CREDIT: Structure for class taken from StackOverflow user Fortega, http://stackoverflow.com/a/6942715
 */
public class IterationException extends Exception
{
	public IterationException(String message)
	{
		super(message);
	}
}
