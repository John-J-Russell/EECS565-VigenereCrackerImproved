/* AUTHOR: John Russell, j362r647@ku.edu
 * DATE: 24 September 2016
 * FILE NAME: VigenereCipherTester.java
 * PURPOSE: Class has multiple member variables and methods for the purpose
 * 			of testing a given key and ciphertext to determine whether 
 * 			or not they could be part of a valid translation.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

public class VigenereCipherTester 
{
	//methods:
	
	/* Constructor:
	 * @PRE: Is passed valid inputs
	 * @POST: instantiates a VigenereCipherTester object
	 * @RETURN: nothing
	 */
	VigenereCipherTester(char[] sanatizedCipherText, int firstWordLengthInput, String outputFileName, int keyLength)
	{
		//pass values to member variables, set up dictionary, file set up in driver
		m_cipherText = sanatizedCipherText;
		m_firstWordLength = firstWordLengthInput;
		m_outputFileName = outputFileName;
		m_cipherTextNumbers = new int[m_cipherText.length];
		m_firstWordNumbersCT = new int[m_firstWordLength];
		m_keyHolder = new int[keyLength];
		m_keyLength = keyLength;
		
		m_dictionary =  new Hashtable<Integer, String>(100000);
		m_firstThreeLetters = new Hashtable<Integer, String>(100000);
		m_firstFiveLetters = new Hashtable<Integer, String>(150000);
		m_firstSevenLetters = new Hashtable<Integer, String>(200000);
		m_firstNineLetters = new Hashtable<Integer, String>(250000);
		
		BufferedReader buffRead = null;
		try
		{
			int x=1;
			int y=1;
			int z=1;
			int counter7 = 1;
			int counter9 = 1;
			String nextWord;
			buffRead = new BufferedReader(new FileReader("words.txt"));
			while((nextWord = buffRead.readLine())!= null)
			{
				if(nextWord.length() == m_firstWordLength)
				{
					m_dictionary.put(x, nextWord.toUpperCase());
					x++;
					//Hashtable for valid 3 letter starts (only trips if first word is equal to or longer than 3)
					if(nextWord.length() >2 && m_firstWordLength >2)
					{
						if(m_firstThreeLetters.contains(nextWord.substring(0,3).toUpperCase()))
						{
							//do nothing
						}
						else
						{
							m_firstThreeLetters.put(y, nextWord.substring(0,3).toUpperCase());
							y++;
						}
					}
					//Hashtable for valid 5 letter starts (only trips if first word is equal to or longer than 5)
					if(nextWord.length() > 4 && m_firstWordLength >4)
					{
						if(m_firstFiveLetters.contains(nextWord.substring(0,5).toUpperCase()))
						{
							
						}
						else
						{
							m_firstFiveLetters.put(z, nextWord.substring(0,5).toUpperCase());
							z++;
						}
					
					}
					//Hashtable for valid 7 letter starts (only trips if first word is equal to or longer than 7)
					if(nextWord.length() > 6 && m_firstWordLength > 6 )
					{
						if(m_firstSevenLetters.contains(nextWord.substring(0,7).toUpperCase()))
						{
							
						}
						else
						{
							m_firstSevenLetters.put(counter7, nextWord.substring(0,7).toUpperCase());
							counter7++;
						}
					}
					//Hashtable for valid 9 letter starts (only trips if first word is equal to or longer than 9)
					if(nextWord.length() > 8 && m_firstWordLength > 8 )
					{
						if(m_firstNineLetters.contains(nextWord.substring(0,9).toUpperCase()))
						{
							
						}
						else
						{
							m_firstNineLetters.put(counter9, nextWord.substring(0,9).toUpperCase());
							counter9++;
						}
					}
				}
			}
			System.out.println(x + " " + y + " " +z);
			
			buffRead.close();
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
	
		//cipher text numbers and first word ct numbers:
		for(int x=0; x<m_cipherText.length; x++)
		{
			m_cipherTextNumbers[x]= convertToNumber(m_cipherText[x]);
		}
		for(int x=0; x<m_firstWordLength; x++)
		{
			m_firstWordNumbersCT[x] = m_cipherTextNumbers[x];
		}
		
	}
	
	/* @PRE: VigenereCipherTester has been correctly instantiated, valid int[] has been passed.
	 * @POST: Key passed to function will be tested against the first word of ciphertext. If deciphered text 
	 * 		  forms an actual word, then decipherWholeText will be called.
	 * @RETURN: true if everything went normally, false if the key has been iterated in-function
	 * 			(meaning that the generate key function does not need to call)
	 * @THROWS: IterationException if the in-method incrementation would cause the key to "roll over" to 0,0,0,etc.
	 */
	public boolean testKey() throws IterationException
	{
		//System.out.println("Testing a key: "+keyHolder[0] + "," + keyHolder[1]);
		//tests first word of CT
		int keyLength = m_keyLength;
		int tempNumber;
		char[] firstWordDeciphered = new char[m_firstWordLength];
		
		for(int x=0; x<m_firstWordLength; x++)
		{
			tempNumber = m_firstWordNumbersCT[x] - m_keyHolder[x%keyLength];
			if(tempNumber <0)
			{
				//this means it's negative, and has to roll around to undo the modulus
				tempNumber = 26 + tempNumber;
			}
			firstWordDeciphered[x] = convertToLetter(tempNumber);
			
			//Checks if the first 3 letters are valid for any words of length firstWordLength
			//Note that these checks below only trip if word length is equal to or greater than
			//the number being checked +1. Will not prompt them if length is insufficient.
			if(x==2)
			{
				String first3Letters = new String(firstWordDeciphered,0,3);
				if(!(m_firstThreeLetters.contains(first3Letters)))
				{
					if(m_keyLength>3)
					{
						m_keyHolder[x]=m_keyHolder[x]+1;
						if(m_keyHolder[x] == 26)
						{
							if(m_keyHolder[0] == 25 && m_keyHolder[1] == 25 && m_keyHolder[2] == 26)
							{
								throw new IterationException("Out of bounds");
							}
							//Call recursive function with the final position being the jossed key location, let it sort it out
							incrementKeyRecursive(x,0);
						}
					}
					//System.out.println("Dumped");
					return(false);
				}
			}
			//Checks if the first 5 letters are valid for any words of length firstWordLength
			if(x == 4)
			{
				String first5Letters = new String(firstWordDeciphered,0,5);
				if(!(m_firstFiveLetters.contains(first5Letters)))
				{
					if(m_keyLength>5)
					{
						m_keyHolder[x]=m_keyHolder[x]+1;
						if(m_keyHolder[x] == 26)
						{
							if(m_keyHolder[0] == 25 && m_keyHolder[1] == 25 && m_keyHolder[2] == 25 && m_keyHolder[3] == 25 && m_keyHolder[4] == 26)
							{
								throw new IterationException("Out of bounds");
							}
							//Call recursive function with the final position being the jossed key location, let it sort it out
							incrementKeyRecursive(x,0);
						}
					}
					return(false);
				}
			}
			//Checks if the first 7 letters are valid for any words of length firstWordLength
			if(x == 6)
			{
				String first7Letters = new String(firstWordDeciphered,0,7);
				if(!(m_firstSevenLetters.contains(first7Letters)))
				{
					if(m_keyLength>7)
					{
						m_keyHolder[x]=m_keyHolder[x]+1;
						if(m_keyHolder[x] == 26)
						{
							if(m_keyHolder[0] == 25 && m_keyHolder[1] == 25 && m_keyHolder[2] == 25 && m_keyHolder[3] == 25 && m_keyHolder[4] == 25
									&& m_keyHolder[5] == 25 && m_keyHolder[6] == 26)
							{
								throw new IterationException("Out of bounds");
							}
							//Call recursive function with the final position being the jossed key location, let it sort it out
							incrementKeyRecursive(x,0);
						}
					}
					return(false);
				}
			}
			//check if valid first 9 letters
			if(x == 8)
			{
				String first9Letters = new String(firstWordDeciphered,0,9);
				if(!(m_firstNineLetters.contains(first9Letters)))
				{
					if(m_keyLength>9)
					{
						//I doubt this one will ever prompt, would be a very long key.
						m_keyHolder[x]=m_keyHolder[x]+1;
						if(m_keyHolder[x] == 26)
						{
							if(m_keyHolder[0] == 25 && m_keyHolder[1] == 25 && m_keyHolder[2] == 25 && m_keyHolder[3] == 25 && m_keyHolder[4] == 25
									&& m_keyHolder[5] == 25 && m_keyHolder[6] == 25 && m_keyHolder[7] == 25 && m_keyHolder[8] == 26)
							{
								throw new IterationException("Out of bounds");
							}
							//Call recursive function with the final position being the jossed key location, let it sort it out
							incrementKeyRecursive(x,0);
						}
					}
					return(false);
				}
			}
		}
		
		//first word deciphered, now being checked:
		String testThisWord = new String(firstWordDeciphered);
		if(m_dictionary.contains(testThisWord))
		{
			System.out.println("Found a word: " + testThisWord);
			decipherWholeText(m_keyHolder);
		}
		
		return(true);
	}
	
	/* @PRE: VigenereCipherTester has been correctly instantiated, valid int[] has been passed,
	 * 		 testKey has successfully translated a first word, and output file exists
	 * @POST: Deciphered ciphertext and key are passed to writeToFile(String plaintext, String validKey)
	 * @RETURN: none
	 */
	private void decipherWholeText(int[] keyHolder)
	{
		int keyLength = keyHolder.length;
		int tempNumber;
		char[] decipheredText = new char[m_cipherText.length];
		
		for(int x=0; x<m_cipherText.length; x++)
		{
			tempNumber = m_cipherTextNumbers[x] - keyHolder[x%keyLength];
			if(tempNumber <0)
			{
				//this means it's negative, and has to roll around to undo the modulus
				tempNumber = 26 + tempNumber;
			}
			decipheredText[x] = convertToLetter(tempNumber);
		}
		
		char[] alphabeticKey = new char[keyHolder.length];
		for(int x=0; x<keyHolder.length; x++)
		{
			alphabeticKey[x] = convertToLetter(keyHolder[x]);
		}
		String plaintext = new String(decipheredText);
		String key = new String(alphabeticKey);
		System.out.println(plaintext + " " + key);
		writeToFile(plaintext, key);
	}
	
	/* @PRE: decipherWholeText has successfully run
	 * @POST: deciphered text and the corresponding key are written to a previously specified output file
	 * @RETURN: none
	 */
	private void writeToFile(String plaintext, String validKey)
	{
		try
		{
			//This sets the append flag to "true", so that it doesn't erase the file every time
			BufferedWriter output = new BufferedWriter(new FileWriter(m_outputFileName, true));
			output.write("Plaintext is: \"" + plaintext + "\", with key: \"" + validKey +"\"");
			output.newLine();
			output.close();
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
	}
	
	/* @PRE:	m_keyHolder exists and is instantiated with a value
	 * @POST:	After having called and run through incrementKeyRecursive, the key will be incremented by one
	 * @RETURN:	none
	 */
	public void incrementKey()
	{
		int errorCheck = 0;
		errorCheck = incrementKeyRecursive((m_keyLength -1), 0);
		if(errorCheck == 1)
		{
			System.out.println("The key is looping");
		}
	}
	
	/* @PRE:	Function is passed the proper numbers, where final position is the key array size -1, and the current position starts at zero
	 * @POST:	The key is incremented much like a clock, where upon reaching 26 the value rolls over and the next highest "rung" of the key
	 * 			increases by one each time the rollover occurs.
	 * @RETURN:	An integer value of either zero or one. One means that the next value up must increment, while zero means it doesn't.
	 * 			If at the highest level it returns one, then incrementKey alerts via the console of this error.
	 */
	private int incrementKeyRecursive(int finalPosition, int currentPosition)
	{
		//System.out.println(m_keyHolder[0] + "," + m_keyHolder[1]);
		//finalPosition starts out as the length - 1, and current position starts at 0
		if(currentPosition == finalPosition)
		{
			m_keyHolder[currentPosition] = m_keyHolder[currentPosition]+1;
			if(m_keyHolder[currentPosition] > 25)
			{
				m_keyHolder[currentPosition] = 0;
				return(1);
			}
			
			return(0);
		}
		m_keyHolder[currentPosition] = m_keyHolder[currentPosition] + incrementKeyRecursive(finalPosition, currentPosition+1);
		
		if(m_keyHolder[currentPosition]>25)
		{
			m_keyHolder[currentPosition]=0;
			return(1);
		}
		return(0);
	}
	
	/* @PRE: is passed an uppercase character
	 * @POST: nothing changes
	 * @RETURN: the integer that maps from A-Z -> 0-25
	 */
	public int convertToNumber(char input)
	{
		switch (input) 
		{
			case 'A': return(0);					  
			case 'B': return(1);					  
			case 'C': return(2);					  
			case 'D': return(3);					  
			case 'E': return(4);					  
			case 'F': return(5);					  
			case 'G': return(6);					  
			case 'H': return(7);					  
			case 'I': return(8);					  
			case 'J': return(9);					  
			case 'K': return(10);					  
			case 'L': return(11);					  
			case 'M': return(12);					  
			case 'N': return(13);					  
			case 'O': return(14);					  
			case 'P': return(15);					  
			case 'Q': return(16);					  
			case 'R': return(17);					  
			case 'S': return(18);					  
			case 'T': return(19);					  
			case 'U': return(20);					  
			case 'V': return(21);					  
			case 'W': return(22);					  
			case 'X': return(23);					  
			case 'Y': return(24);					  
			case 'Z': return(25);					  
			default: return(0);					 
		}
	}
	
	/* @PRE: is passed an integer between 0 and 25
	 * @POST: nothing changes
	 * @RETURN: the letter that maps from 0-25 -> A-Z
	 */
	public char convertToLetter(int input)
	{
		switch (input) 
		{
			case 0: return('A');					  
			case 1: return('B');					  
			case 2: return('C');					  
			case 3: return('D');					  
			case 4: return('E');					  
			case 5: return('F');					  
			case 6: return('G');					  
			case 7: return('H');					  
			case 8: return('I');					  
			case 9: return('J');					  
			case 10: return('K');					  
			case 11: return('L');					  
			case 12: return('M');					  
			case 13: return('N');					  
			case 14: return('O');					  
			case 15: return('P');					  
			case 16: return('Q');					  
			case 17: return('R');					  
			case 18: return('S');					  
			case 19: return('T');					  
			case 20: return('U');					  
			case 21: return('V');					  
			case 22: return('W');					  
			case 23: return('X');					  
			case 24: return('Y');					  
			case 25: return('Z');					  
			default: return('A');					 
		}
	}
	
	//member variables:
	
	//contains the sanatized ciphertext
	char[] m_cipherText;
	
	//contains the numerical equivalents to the ciphertext
	int[] m_cipherTextNumbers;
	
	//the first word of the numerical ciphertext
	int[] m_firstWordNumbersCT;
	
	//array holding the key that is used for deciphering
	int[] m_keyHolder;
	
	//the file name where the results are printed
	String m_outputFileName;
	
	//A hashtable consisting of around 355,000 words
	Hashtable<Integer, String> m_dictionary;
	
	//A hashtable consisting of all valid 3 letter word starts
	Hashtable<Integer, String> m_firstThreeLetters;
	//valid 5 letters
	Hashtable<Integer,String> m_firstFiveLetters;
	//valid 7 letters
	Hashtable<Integer,String> m_firstSevenLetters;
	//valid 9 letters
	Hashtable<Integer,String> m_firstNineLetters;
	
	//the length of the first word in the ciphertext
	int m_firstWordLength;
	
	//length of key
	int m_keyLength;
}
