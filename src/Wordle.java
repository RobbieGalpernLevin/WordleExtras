import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * The Wordle class allows a user to play the popular game Wordle
 * @author Robbie Galpern-Levin
 */
public class Wordle
{
    /**
     * Used to record what the user inputs
     */
    private Scanner scan;

    /**
     * player represents the user playing the wordle game
     */
    private Player player;

    /**
     * These instance variables are used to color the users guesses either yellow, green or white
     */
    private final String BLACK = "\u001B[30m";
    public static final String GREEN_BACKGROUND = "\033[42m";  // GREEN
    public static final String YELLOW_BACKGROUND = "\033[43m"; // YELLOW
    public static final String WHITE_BACKGROUND_BRIGHT = "\033[0;107m";   // WHITE

    /**
     * represents all the words that could be the solution
     */
    private ArrayList<String> possibleAnswers;

    /**
     * represents all the words that could be the word the user is able to guess
     */
    private ArrayList<String> allWords;

    /**
     * Stores the users guesses before the letters are given colors
     */
    String[][] guesses;

    /**
     * Used to print the users colored guesses
     */
    private String guess1;
    private String guess2;
    private String guess3;
    private String guess4;
    private String guess5;
    private String guess6;


    /**
     * Constructor that creates a Wordle object
     * imports possibleAnswers and allWords
     * initializes scan to a Scanner object
     * initializes guesses to a 6 by 5 2D Array
     * initializes all 6 "guess" instance variables to empty strings
     */
    public Wordle()
    {
        importPossibleAnswers();
        importAllWords();
        scan = new Scanner(System.in);
        guesses = new String[6][5];
        guess1 = "";
        guess2 = "";
        guess3 = "";
        guess4 = "";
        guess5 = "";
        guess6 = "";
    }

    /**
     * The main body of code for the Wordle class
     * When run the user is able to play a full game of wordle.
     */
    public void play()
    {
        String word = "";
        int idx = (int) (Math.random() * possibleAnswers.size()) + 1;
        word = possibleAnswers.get(idx);
        boolean wordGuessed = false;
        System.out.println(WHITE_BACKGROUND_BRIGHT + BLACK + "What is your name?");
        String playerName = scan.nextLine();
        player = new Player(playerName);
        System.out.println(WHITE_BACKGROUND_BRIGHT + BLACK + "Welcome to Wordle " + player.getName() + WHITE_BACKGROUND_BRIGHT + BLACK + "!");
        System.out.println(WHITE_BACKGROUND_BRIGHT + BLACK + "If a letter shows up Black it means it is not in the word.");
        System.out.println(WHITE_BACKGROUND_BRIGHT + BLACK + "If a letter shows up yellow it is in the word but in the wrong spot");
        System.out.println(WHITE_BACKGROUND_BRIGHT + BLACK + "If a letter shows up green, it is the right letter in the right spot");
        System.out.println(WHITE_BACKGROUND_BRIGHT + BLACK + "Good Luck");
        while ((player.getGuessNumber() < 6) && (!wordGuessed))
        {
            boolean doubledLetterColored = false;
            String guess = "";
            boolean actualWord = false;
            while (!actualWord)
            {
                String guessAttempt = scan.nextLine();

                boolean spelledCorrectly = binarySpellCheck(guessAttempt);
                if(guessAttempt.length() != 5)
                {
                    System.out.println(BLACK + "Please enter a word that is 5 letters long");
                }
                else if(hasTripleLetter(guessAttempt))
                {
                    System.out.println(BLACK + "Please enter a word that does not have the same letter 3 times");
                }
                else
                {
                    if (spelledCorrectly)
                    {
                        actualWord = true;
                        player.setGuessNumber(player.getGuessNumber() + 1);
                        guess = guessAttempt;
                        for(int i = 0; i < guesses[0].length; i++)
                        {
                            guesses[player.getGuessNumber() - 1][i] = guess.substring(i, i + 1);
                        }
                    }
                    else
                    {
                        System.out.println(BLACK + "That is not a word");
                    }
                }
            }
            String code = "";
            if(!hasDoubleLetter(guess))
            {
                for(int i = 0; i < 5; i++)
                {
                    if(guess.indexOf(guess.substring(i, i + 1)) == word.indexOf(guess.substring(i, i + 1)))
                    {
                        code += "G";
                    }
                    else if(word.indexOf(guess.substring(i, i + 1)) >= 0)
                    {
                        code += "Y";
                    }
                    else
                    {
                        code += "W";
                    }
                }
            }
            else
            {
                int firstOrSecondLetter = 1;
                for(int i = 0; i < 5; i++)
                {
                    String doubledLetter = returnDoubledLetter(guess);
                    if(guess.substring(i, i + 1 ).equals(doubledLetter) && !doubledLetterColored)
                    {
                        String changeGuess = guess;
                        if(guess.indexOf(guess.substring(i, i + 1)) == word.indexOf(guess.substring(i, i + 1)))
                        {
                            code += "G";
                            doubledLetterColored = true;
                        }
                        else
                        {
                            changeGuess = guess.substring(0, guess.indexOf(doubledLetter)) + "#" + guess.substring(guess.indexOf(doubledLetter) + 1);
                            if(changeGuess.indexOf(doubledLetter) == word.indexOf(doubledLetter))
                            {
                                if(firstOrSecondLetter == 1)
                                {
                                    code += "W";
                                    firstOrSecondLetter++;
                                }
                                else
                                {
                                    code += "G";
                                }

                            }
                            else if(word.indexOf(guess.substring(i, i + 1)) >= 0)
                            {
                                code += "Y";
                                doubledLetterColored = true;
                            }
                            else
                            {
                                code += "W";
                                doubledLetterColored = true;
                            }
                        }
                    }
                    else
                    {
                        if(!(guess.substring(i, i + 1 ).equals(doubledLetter)))
                        {
                            if (guess.indexOf(guess.substring(i, i + 1)) == word.indexOf(guess.substring(i, i + 1)))
                            {
                                code += "G";
                            }
                            else if (word.indexOf(guess.substring(i, i + 1)) >= 0)
                            {
                                code += "Y";
                            }
                            else
                            {
                                code += "W";
                            }
                        }
                        else
                        {
                            code += "W";
                        }

                    }
                }
            }


            if(player.getGuessNumber() == 1)
            {
                for(int i = 0; i < guesses[0].length; i++)
                {
                    if(code.substring(i, i + 1).equals("G"))
                    {
                        guess1 += GREEN_BACKGROUND + BLACK + guesses[player.getGuessNumber() - 1][i] + WHITE_BACKGROUND_BRIGHT;
                    }
                    else if(code.substring(i, i + 1).equals("Y"))
                    {
                        guess1 += YELLOW_BACKGROUND + BLACK + guesses[player.getGuessNumber() - 1][i] + WHITE_BACKGROUND_BRIGHT;
                    }
                    else
                    {
                        guess1 += WHITE_BACKGROUND_BRIGHT + BLACK + guesses[player.getGuessNumber() - 1][i] + WHITE_BACKGROUND_BRIGHT;
                    }
                }
                System.out.println(guess1);
            }
            else if(player.getGuessNumber() == 2)
            {
                for(int i = 0; i < guesses[0].length; i++)
                {
                    if(code.substring(i, i + 1).equals("G"))
                    {
                        guess2 += GREEN_BACKGROUND + BLACK + guesses[player.getGuessNumber() - 1][i] + WHITE_BACKGROUND_BRIGHT;
                    }
                    else if(code.substring(i, i + 1).equals("Y"))
                    {
                        guess2 += YELLOW_BACKGROUND + BLACK + guesses[player.getGuessNumber() - 1][i] + WHITE_BACKGROUND_BRIGHT;
                    }
                    else
                    {
                        guess2 += WHITE_BACKGROUND_BRIGHT + BLACK + guesses[player.getGuessNumber() - 1][i] + WHITE_BACKGROUND_BRIGHT;
                    }
                }
                System.out.println(guess1);
                System.out.println(guess2);
            }
            else if(player.getGuessNumber() == 3)
            {
                for(int i = 0; i < guesses[0].length; i++)
                {
                    if(code.substring(i, i + 1).equals("G"))
                    {
                        guess3 += GREEN_BACKGROUND + BLACK + guesses[player.getGuessNumber() - 1][i] + WHITE_BACKGROUND_BRIGHT;
                    }
                    else if(code.substring(i, i + 1).equals("Y"))
                    {
                        guess3 += YELLOW_BACKGROUND + BLACK + guesses[player.getGuessNumber() - 1][i] + WHITE_BACKGROUND_BRIGHT;
                    }
                    else
                    {
                        guess3 += WHITE_BACKGROUND_BRIGHT + BLACK + guesses[player.getGuessNumber() - 1][i] + WHITE_BACKGROUND_BRIGHT;
                    }
                }
                System.out.println(guess1);
                System.out.println(guess2);
                System.out.println(guess3);
            }
            else if(player.getGuessNumber() == 4)
            {
                for(int i = 0; i < guesses[0].length; i++)
                {
                    if(code.substring(i, i + 1).equals("G"))
                    {
                        guess4 += GREEN_BACKGROUND + BLACK + guesses[player.getGuessNumber() - 1][i] + WHITE_BACKGROUND_BRIGHT;
                    }
                    else if(code.substring(i, i + 1).equals("Y"))
                    {
                        guess4 += YELLOW_BACKGROUND + BLACK + guesses[player.getGuessNumber() - 1][i] + WHITE_BACKGROUND_BRIGHT;
                    }
                    else
                    {
                        guess4 += WHITE_BACKGROUND_BRIGHT + BLACK + guesses[player.getGuessNumber() - 1][i] + WHITE_BACKGROUND_BRIGHT;
                    }
                }
                System.out.println(guess1);
                System.out.println(guess2);
                System.out.println(guess3);
                System.out.println(guess4);
            }
            else if(player.getGuessNumber() == 5)
            {
                for(int i = 0; i < guesses[0].length; i++)
                {
                    if(code.substring(i, i + 1).equals("G"))
                    {
                        guess5 += GREEN_BACKGROUND + BLACK + guesses[player.getGuessNumber() - 1][i] + WHITE_BACKGROUND_BRIGHT;
                    }
                    else if(code.substring(i, i + 1).equals("Y"))
                    {
                        guess5 += YELLOW_BACKGROUND + BLACK + guesses[player.getGuessNumber() - 1][i] + WHITE_BACKGROUND_BRIGHT;
                    }
                    else
                    {
                        guess5 += WHITE_BACKGROUND_BRIGHT + BLACK + guesses[player.getGuessNumber() - 1][i] + WHITE_BACKGROUND_BRIGHT;
                    }
                }
                System.out.println(guess1);
                System.out.println(guess2);
                System.out.println(guess3);
                System.out.println(guess4);
                System.out.println(guess5);
            }
            else
            {
                for(int i = 0; i < guesses[0].length; i++)
                {
                    if(code.substring(i, i + 1).equals("G"))
                    {
                        guess6 += GREEN_BACKGROUND + BLACK + guesses[player.getGuessNumber() - 1][i] + WHITE_BACKGROUND_BRIGHT;
                    }
                    else if(code.substring(i, i + 1).equals("Y"))
                    {
                        guess6 += YELLOW_BACKGROUND + BLACK + guesses[player.getGuessNumber() - 1][i] + WHITE_BACKGROUND_BRIGHT;
                    }
                    else
                    {
                        guess6 += WHITE_BACKGROUND_BRIGHT + BLACK + guesses[player.getGuessNumber() - 1][i] + WHITE_BACKGROUND_BRIGHT;
                    }
                }
                System.out.println(guess1);
                System.out.println(guess2);
                System.out.println(guess3);
                System.out.println(guess4);
                System.out.println(guess5);
                System.out.println(guess6);
            }

            if(guess.equals(word))
            {
                wordGuessed = true;
                System.out.println();
                if(player.getGuessNumber() == 1)
                {
                    System.out.println(BLACK + "Genius");
                }
                else if(player.getGuessNumber() == 2)
                {
                    System.out.println(BLACK + "Magnificent");
                }
                else if(player.getGuessNumber() == 3)
                {
                    System.out.println(BLACK + "Impressive");
                }
                else if(player.getGuessNumber() == 4)
                {
                    System.out.println(BLACK + "Splendid");
                }
                else if(player.getGuessNumber() == 5)
                {
                    System.out.println(BLACK + "Great");
                }
                else
                {
                    System.out.println(BLACK + "Phew");
                }
            }
        }
        if(!wordGuessed)
        {
            System.out.println();
            System.out.println(BLACK + "BETTER LUCK NEXT TIME ");
            System.out.println(BLACK + "THE WORD WAS " + word);
        }
    }

    /**
     * Used to check if word is spelled correctly by seeing if it is in the allWords ArrayList
     * @param word the word that will be spell checked
     * @return true if the word is a word spelled correctly and false otherwise
     */
    private boolean binarySpellCheck(String word)
    {
        int numChecks = 0;

        int left = 0;
        int right = allWords.size() - 1;

        while (left <= right)
        {
            numChecks++;

            int middle = (left + right) / 2;

            if (allWords.get(middle).compareTo(word) < 0)
            {
                left = middle + 1;
            }
            else if (allWords.get(middle).compareTo(word) > 0)
            {
                right = middle - 1;
            }
            else
            {
                return true;
            }
        }
        return false;
    }

    /**
     * removes all words that contain 2 of the same letter from the ArrayList possibleAnswers
     * PRECONDITION: each word in possibleAnswers is 5 letters long
     */
    private void removeWordsWithDoubleLettersFromPossibleAnswers()
    {
        for(int i = 0; i < possibleAnswers.size(); i++)
        {
            String letter1 = possibleAnswers.get(i).substring(0, 1);
            String letter2 = possibleAnswers.get(i).substring(1, 2);
            String letter3 = possibleAnswers.get(i).substring(2, 3);
            String letter4 = possibleAnswers.get(i).substring(3, 4);
            String letter5 = possibleAnswers.get(i).substring(4);
            if(letter1.equals(letter2) || letter1.equals(letter3) || letter1.equals(letter4) || letter1.equals(letter5) || letter2.equals(letter3) || letter2.equals(letter4) || letter2.equals(letter5) || letter3.equals(letter4) || letter3.equals(letter5) || letter4.equals(letter5))
            {
                possibleAnswers.remove(i);
                i--;
            }
        }
    }

    /**
     * removes all words that contain 3 of the same letter from the ArrayList possibleAnswers
     * PRECONDITION: each word in possibleAnswers is 5 letters long
     */
    private void removeWordsWithTripleLettersFromPossibleAnswers()
    {
        for(int i = 0; i < possibleAnswers.size(); i++)
        {
            String letter1 = possibleAnswers.get(i).substring(0, 1);
            String letter2 = possibleAnswers.get(i).substring(1, 2);
            String letter3 = possibleAnswers.get(i).substring(2, 3);
            String letter4 = possibleAnswers.get(i).substring(3, 4);
            String letter5 = possibleAnswers.get(i).substring(4);
            if((letter1.equals(letter2) && letter1.equals(letter3)) || (letter1.equals(letter2) && letter1.equals(letter4)) || (letter1.equals(letter2) && letter1.equals(letter5)) || (letter1.equals(letter3) && letter1.equals(letter4)) || (letter1.equals(letter3) && letter1.equals(letter5)) || (letter1.equals(letter4) && letter1.equals(letter5)) || (letter2.equals(letter3) && letter2.equals(letter4)) || (letter2.equals(letter3) && letter2.equals(letter5)) || (letter2.equals(letter4) && letter2.equals(letter5)) || (letter3.equals(letter4) && letter3.equals(letter5)))
            {
                possibleAnswers.remove(i);
                i--;
            }
        }
    }

    /**
     * Static method that checks word to see if it contains the same letter twice
     * @param word the word that will be checked to see if it contains the same letter twice
     * @return true if the word contains the same letter twice and false otherwise
     * PRECONDITION: word is 5 letters long
     */
    private static boolean hasDoubleLetter(String word)
    {
        String letter1 = word.substring(0, 1);
        String letter2 = word.substring(1, 2);
        String letter3 = word.substring(2, 3);
        String letter4 = word.substring(3, 4);
        String letter5 = word.substring(4);
        if(letter1.equals(letter2) || letter1.equals(letter3) || letter1.equals(letter4) || letter1.equals(letter5) || letter2.equals(letter3) || letter2.equals(letter4) || letter2.equals(letter5) || letter3.equals(letter4) || letter3.equals(letter5) || letter4.equals(letter5))
        {
            return true;
        }
        return false;
    }

    /**
     * Static method that checks word to see if it contains the same letter three times
     * @param word the word that will be checked to see if it contains the same letter three times
     * @return true if the word contains the same letter three times and false otherwise
     * PRECONDITION: word is 5 letters long
     */
    private static boolean hasTripleLetter(String word)
    {
        String letter1 = word.substring(0, 1);
        String letter2 = word.substring(1, 2);
        String letter3 = word.substring(2, 3);
        String letter4 = word.substring(3, 4);
        String letter5 = word.substring(4);
        if((letter1.equals(letter2) && letter1.equals(letter3)) || (letter1.equals(letter2) && letter1.equals(letter4)) || (letter1.equals(letter2) && letter1.equals(letter5)) || (letter1.equals(letter3) && letter1.equals(letter4)) || (letter1.equals(letter3) && letter1.equals(letter5)) || (letter1.equals(letter4) && letter1.equals(letter5)) || (letter2.equals(letter3) && letter2.equals(letter4)) || (letter2.equals(letter3) && letter2.equals(letter5)) || (letter2.equals(letter4) && letter2.equals(letter5)) || (letter3.equals(letter4) && letter3.equals(letter5)))
        {
            return true;
        }
        return false;
    }

    /**
     * Static method that finds and returns the letter that appears twice in word
     * @param word the word that will be searched for the double letter
     * @return the letter that appears twice in the word or a statement if no letter appears twice in the word
     * PRECONDITION: word is 5 letters long
     */
    private static String returnDoubledLetter(String word)
    {
        String letter1 = word.substring(0, 1);
        String letter2 = word.substring(1, 2);
        String letter3 = word.substring(2, 3);
        String letter4 = word.substring(3, 4);
        String letter5 = word.substring(4);
        if(letter1.equals(letter2) || letter1.equals(letter3) || letter1.equals(letter4) || letter1.equals(letter5))
        {
            return letter1;
        }
        else if(letter2.equals(letter3) || letter2.equals(letter4) || letter2.equals(letter5))
        {
            return letter2;
        }
        else if(letter3.equals(letter4) || letter3.equals(letter5))
        {
            return letter3;
        }
        else if(letter4.equals(letter5))
        {
            return letter4;
        }
        else
        {
            return "this word does not contain any letter twice";
        }
    }

    /**
     * Static method that sorts an ArrayList alphabetically
     * @param words the ArrayList being sorted
     * PRECONDITION: ArrayList words is not empty
     */
    private static void insertionSortWordList(ArrayList<String> words)
    {
        for (int j = 0; j < words.size(); j++)
        {
            String word = words.get(j);

            int possibleIndex = j;
            while ((possibleIndex > 0) && (word.compareTo(words.get(possibleIndex - 1)) < 0))
            {
                words.set(possibleIndex, words.get(possibleIndex - 1));
                possibleIndex--;
            }
            words.set(possibleIndex, word);
        }
    }

    /**
     * Imports possibleAnswers.txt
     * Initializes the instance variable possibleAnswers to an ArrayList containing the words in possibleAnswers.txt
     * removes all words with double and triple letters from possibleAnswers
     */
    private void importPossibleAnswers()
    {
        String[] tmp = null;
        try
        {
            FileReader fileReader = new FileReader("src\\possibleAnswers.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            ArrayList<String> lines = new ArrayList<String>();
            String line = null;

            while ((line = bufferedReader.readLine()) != null)
            {
                lines.add(line);
            }

            bufferedReader.close();
            tmp = lines.toArray(new String[lines.size()]);
        }
        catch (IOException e)
        {
            System.out.println("Error importing file; unable to access "+ e.getMessage());
        }

        possibleAnswers = new ArrayList<String>(Arrays.asList(tmp));
        removeWordsWithDoubleLettersFromPossibleAnswers();
        removeWordsWithTripleLettersFromPossibleAnswers();
    }

    /**
     * Imports allWords.txt
     * Initializes the instance variable allWords to an ArrayList containing the words in allWords.txt
     * Sorts the ArrayList allWords alphabetically
     */
    private void importAllWords()
    {
        String[] tmp = null;
        try
        {
            FileReader fileReader = new FileReader("src\\allWords.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            ArrayList<String> lines = new ArrayList<String>();
            String line = null;

            while ((line = bufferedReader.readLine()) != null)
            {
                lines.add(line);
            }

            bufferedReader.close();
            tmp = lines.toArray(new String[lines.size()]);
        }
        catch (IOException e)
        {
            System.out.println("Error importing file; unable to access "+ e.getMessage());
        }
        allWords = new ArrayList<String>(Arrays.asList(tmp));
        insertionSortWordList(allWords);
    }
}