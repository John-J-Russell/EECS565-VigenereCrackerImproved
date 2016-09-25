/* AUTHOR: John Russell, j362r647@ku.edu
 * DATE: 24 September 2016
 * FILE NAME: VigenereCipherTesterDriver.java
 * PURPOSE: File gathers input from user, and handles key generation for
 * 			VigenereCipherTester.java
 * 			Also sanatizes the user input
 */

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Arrays;


public class VigenereCipherTesterDriver 
{
	//the only two member variables this class has.
	//the VCT is for accessibility purposes, and the m_keyHolder is so a recursive
	//function will work on it.
	VigenereCipherTester m_cipherTester;
	//int[] m_keyHolder;
	
	
	//exists only as a gateway to the user input
	//rendered redundant by LaunchFromHere.java
	//done this way so as to not require making functions static
	//I really wanted to keep the progression of this linear, and not open the
	//ability for people to run methods without the proper order.
	/* @PRE: none
	 * @POST: will have called getUserInput()
	 * @RETURN: none
	 */
	public void main(String[] args) 
	{
		getUserInput();
	}
	
	/* @PRE: none
	 * @POST: gathers input, makes tester object, instantiates m_keyHolder, calls generateAllKeys()
	 * @RETURN: none
	 */
	public void getUserInput()
	{
		String rawCipherText;
		String refinedCipherText;
		String outputFileDestination;
		String outputFinalName;
		int firstWordLength;
		int keyLength;
		
		Scanner input = new Scanner(System.in);
		System.out.println("Please enter the ciphertext, without any punctuation.");
		rawCipherText = input.nextLine();
		
		//regex edit removes all spaces, credit to StackOverflow user
		//nitro2k01, http://stackoverflow.com/a/5455828
		refinedCipherText=rawCipherText.replaceAll("\\s","");
		refinedCipherText=refinedCipherText.toUpperCase();
		System.out.println(refinedCipherText + " Is your input");
		
		System.out.println("Please enter the first word length. Putting letters here will break stuff, so please don't. \n"
				+ " Use numerals, please:");
		firstWordLength = Integer.parseInt(input.nextLine());
		
		System.out.println("Please give the key length, same drill:");
		keyLength = Integer.parseInt(input.nextLine());
		
		
		//////////////////
		//This entire file section is a mess
		//////////////////
		
		//Getting a valid file destination: must not exist previously, and must be a valid name.
		System.out.println("Now enter the file name you wish to save this to.  \nGo ahead and omit the \".txt\" and don't leave any spaces. \n"
				+ "Output file will be saved in the project-level folder.");
		
		outputFileDestination = input.nextLine();
		outputFinalName = outputFileDestination+".txt";
				
		File filename = new File(outputFinalName);
		if(filename.exists())
		{
			System.out.println("Sorry, but that name is taken. Please try again.");
			while(filename.exists())
			{
				System.out.println("Now enter the file name you wish to save this to.  \nGo ahead and omit the \".txt\" and don't leave any spaces. \n"
						+ "Output file will be saved in the project-level folder.");
				
				outputFileDestination = input.nextLine();
				outputFinalName = outputFileDestination+".txt";
				
				filename = new File(outputFinalName);
			}
		}
		
		Path destination = Paths.get("./" + outputFinalName);
		
		try
		{
			Files.createFile(destination);
		}
		catch(IOException e)
		{
			System.out.println(e);
			System.out.println("That already exists apparently, so restart the program.");
		}
		
		m_cipherTester = new VigenereCipherTester(refinedCipherText.toCharArray(),firstWordLength,outputFinalName,keyLength);
		//m_keyHolder = new int[keyLength];
		
		input.close();
		
		generateAllKeys();
	}
	
	/* @PRE:	m_keyHolder has been instantiated with the correct length, and m_cipherTester has been constructed with valid values
	 * @POST:	Every possible key of combination keyLength will have been generated and tested in the cipherTester.testKey function
	 * @RETURN:	none
	 */
	private void generateAllKeys()
	{
		long startTime = System.nanoTime();
		int[] lastCombination = new int[m_cipherTester.m_keyLength];
		for(int x=0; x<m_cipherTester.m_keyLength; x++)
		{
			//m_keyHolder[x]=0;
			lastCombination[x]=25;
		}
		try
		{
			while(!(Arrays.equals(m_cipherTester.m_keyHolder,lastCombination)))
			{
				//If the key being tested wasn't tossed, then increment normally
				//if it was tossed, then the corresponding spot was increased manually
				if(m_cipherTester.testKey());
				{
					m_cipherTester.incrementKey();
				}
			}
			//m_cipherTester.testKey(lastCombination);
			m_cipherTester.m_keyHolder = lastCombination;
			boolean throwaway = m_cipherTester.testKey();
		}
		catch(IterationException e)
		{
			System.out.println("Don't want this to loop");
		}
		long endTime = System.nanoTime();
		
		long elapsedTime = endTime - startTime;
		System.out.println(elapsedTime);
	}
	
	

}
