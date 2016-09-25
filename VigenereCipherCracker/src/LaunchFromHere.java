/* AUTHOR: John Russell, j362r647@ku.edu
 * DATE: 24 September 2016
 * FILE NAME: LaunchFromHere.java
 * PURPOSE: Exists purely as a "main.cpp" style file.
 */
public class LaunchFromHere 
{

	/* @PRE: none
	 * @POST: starts the whole cipher testing process
	 * @RETURN: none
	 */
	public static void main(String[] args) 
	{
		VigenereCipherTesterDriver doit = new VigenereCipherTesterDriver();
		doit.getUserInput();
	}

}
