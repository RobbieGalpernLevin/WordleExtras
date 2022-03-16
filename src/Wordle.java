import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @author Robbie Galpern-Levin
 */

public class Wordle
{

    private final String BLACK = "\u001B[30m";
    public static final String GREEN_BACKGROUND = "\033[42m";  // GREEN
    public static final String YELLOW_BACKGROUND = "\033[43m"; // YELLOW
    public static final String WHITE_BACKGROUND_BRIGHT = "\033[0;107m";   // WHITE


    private ArrayList<String> possibleAnswers;
    private ArrayList<String> allWords;

    private Scanner scan;
    private Player player;
    String[][] guesses;


    /**
     * Constructor with no parameter
     * imports possibleAnswers and allWords
     * initializes scan to a Scanner object
     */
    public Wordle()
    {
        importPossibleAnswers();
        importAllWords();
        scan = new Scanner(System.in);
        guesses = new String[6][5];
        for(int row = 0; row < guesses.length; row++)
        {
            for(int col = 0; col < guesses[0].length; col++)
            {
                guesses[row][col] = " ";
            }
        }
    }

    public ArrayList<String> getPossibleAnswers()
    {
        return possibleAnswers;
    }

    public ArrayList<String> getAllWords()
    {
        return allWords;
    }

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
                else
                {
                    if (spelledCorrectly)
                    {
                        actualWord = true;
                        player.setGuessNumber(player.getGuessNumber() + 1);
                        guess = guessAttempt;
                    }
                    else
                    {
                        System.out.println(BLACK + "That is not a word");
                    }
                }
            }
            String newStr = "";
            if(!hasTripleLetter(guess) && !hasDoubleLetter(guess))
            {
                for(int i = 0; i < 5; i++)
                {
                    if(guess.indexOf(guess.substring(i, i + 1)) == word.indexOf(guess.substring(i, i + 1)))
                    {
                        newStr += GREEN_BACKGROUND + BLACK + guess.substring(i, i + 1) + WHITE_BACKGROUND_BRIGHT;
                    }
                    else if(word.indexOf(guess.substring(i, i + 1)) >= 0)
                    {
                        newStr += YELLOW_BACKGROUND + BLACK + guess.substring(i, i + 1) + WHITE_BACKGROUND_BRIGHT;
                    }
                    else
                    {
                        newStr += WHITE_BACKGROUND_BRIGHT + BLACK + guess.substring(i, i + 1) + WHITE_BACKGROUND_BRIGHT;
                    }
                }
            }
            else if(hasTripleLetter(guess))
            {
                for(int i = 0; i < 5; i++)
                {
                    String tripledLetter = returnTripledLetter(guess);
                    if(guess.indexOf(guess.substring(i, i + 1)) == word.indexOf(guess.substring(i, i + 1)))
                    {
                        newStr += GREEN_BACKGROUND + BLACK + guess.substring(i, i + 1) + WHITE_BACKGROUND_BRIGHT;
                    }
                    else if(word.indexOf(guess.substring(i, i + 1)) >= 0)
                    {
                        newStr += YELLOW_BACKGROUND + BLACK + guess.substring(i, i + 1) + WHITE_BACKGROUND_BRIGHT;
                    }
                    else
                    {
                        newStr += WHITE_BACKGROUND_BRIGHT + BLACK + guess.substring(i, i + 1) + WHITE_BACKGROUND_BRIGHT;
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
                            newStr += GREEN_BACKGROUND + BLACK + guess.substring(i, i + 1) + WHITE_BACKGROUND_BRIGHT;
                            doubledLetterColored = true;
                        }
                        else
                        {
                            changeGuess = guess.substring(0, guess.indexOf(doubledLetter)) + "#" + guess.substring(guess.indexOf(doubledLetter) + 1);
                            if(changeGuess.indexOf(doubledLetter) == word.indexOf(doubledLetter))
                            {
                                if(firstOrSecondLetter == 1)
                                {
                                    newStr += WHITE_BACKGROUND_BRIGHT + BLACK + guess.substring(i, i + 1) + WHITE_BACKGROUND_BRIGHT;
                                    firstOrSecondLetter++;
                                }
                                else
                                {
                                    newStr += GREEN_BACKGROUND + BLACK + guess.substring(i, i + 1) + WHITE_BACKGROUND_BRIGHT;
                                }

                            }
                            else if(word.indexOf(guess.substring(i, i + 1)) >= 0)
                            {
                                newStr += YELLOW_BACKGROUND + BLACK + guess.substring(i, i + 1) + WHITE_BACKGROUND_BRIGHT;
                                doubledLetterColored = true;
                            }
                            else
                            {
                                newStr += WHITE_BACKGROUND_BRIGHT + BLACK + guess.substring(i, i + 1) + WHITE_BACKGROUND_BRIGHT;
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
                                newStr += GREEN_BACKGROUND + BLACK + guess.substring(i, i + 1) + WHITE_BACKGROUND_BRIGHT;
                            }
                            else if (word.indexOf(guess.substring(i, i + 1)) >= 0)
                            {
                                newStr += YELLOW_BACKGROUND + BLACK + guess.substring(i, i + 1) + WHITE_BACKGROUND_BRIGHT;
                            }
                            else
                            {
                                newStr += WHITE_BACKGROUND_BRIGHT + BLACK + guess.substring(i, i + 1) + WHITE_BACKGROUND_BRIGHT;
                            }
                        }
                        else
                        {
                            newStr += WHITE_BACKGROUND_BRIGHT + BLACK + guess.substring(i, i + 1) + WHITE_BACKGROUND_BRIGHT;
                        }

                    }
                }
            }
            while(newStr.indexOf("\033[42m") > 0)
            {
            newStr = newStr.substring(0, newStr.indexOf("033[42m") - 1) + newStr.substring(newStr.indexOf("033[42m") + 7);
            System.out.println("hi");
            }
            while(newStr.indexOf("\033[43m") > 0)
            {
                newStr = newStr.substring(0, newStr.indexOf("033[43m") - 1) + newStr.substring(newStr.indexOf("033[43m") + 7);
                System.out.println("hi");
            }
            while(newStr.indexOf("\033[0;107m") > 0)
            {
                newStr = newStr.substring(0, newStr.indexOf("033[0;107m") - 1) + newStr.substring(newStr.indexOf("033[0;107m") + 10);
                System.out.println("hi");
            }
            while(newStr.indexOf("\u001B[30m") > 0)
            {
                newStr = newStr.substring(0, newStr.indexOf("u001B[30m") - 1) + newStr.substring(newStr.indexOf("u001B[30m") + 9);
                System.out.println("hi");
            }


            if(player.getGuessNumber() == 1)
            {
                for(int i = 0; i < guesses[0].length; i++)
                {
                    guesses[0][i] = newStr.substring(i, i + 1);
                }
            }
            else if(player.getGuessNumber() == 2)
            {
                for(int i = 0; i < guesses[1].length; i++)
                {
                    guesses[1][i] = newStr.substring(i, i + 1);
                }
            }
            else if(player.getGuessNumber() == 3)
            {
                for(int i = 0; i < guesses[2].length; i++)
                {
                    guesses[2][i] = newStr.substring(i, i + 1);
                }
            }
            else if(player.getGuessNumber() == 4)
            {
                for(int i = 0; i < guesses[3].length; i++)
                {
                    guesses[3][i] = newStr.substring(i, i + 1);
                }
            }
            else if(player.getGuessNumber() == 5)
            {
                for(int i = 0; i < guesses[4].length; i++)
                {
                    guesses[4][i] = newStr.substring(i, i + 1);
                }
            }
            else
            {
                for(int i = 0; i < guesses[5].length; i++)
                {
                    guesses[5][i] = newStr.substring(i, i + 1);
                }
            }
            clearConsole();

            for(int row = 0; row < guesses.length; row++)
            {
                System.out.println();
                System.out.print(row + 1 + ". ");
                System.out.print("|");
                for(int col = 0; col < guesses[0].length; col++)
                {
                    System.out.print(guesses[row][col]);
                    if(col != guesses[0].length)
                    {
                        System.out.print("|");
                    }
                }
            }
            System.out.println();

            if(guess.equals(word))
            {
                wordGuessed = true;
                System.out.println();
                if(player.getGuessNumber() == 1)
                {
                    System.out.println("Genius");
                }
                else if(player.getGuessNumber() == 2)
                {
                    System.out.println("Magnificent");
                }
                else if(player.getGuessNumber() == 3)
                {
                    System.out.println("Impressive");
                }
                else if(player.getGuessNumber() == 4)
                {
                    System.out.println("Splendid");
                }
                else if(player.getGuessNumber() == 5)
                {
                    System.out.println("Great");
                }
                else
                {
                    System.out.println("Phew");
                }
            }
        }
        if(!wordGuessed)
        {
            System.out.println();
            System.out.println("BETTER LUCK NEXT TIME ");
            System.out.println("THE WORD WAS " + word);
        }
    }

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

    private void removeWordsWithDoubleLettersFromPossibleAnswers()
    {
        for(int i = 1; i < possibleAnswers.size(); i++)
        {
            String letter1 = possibleAnswers.get(i).substring(0, 1);
            String letter2 = possibleAnswers.get(i).substring(1, 2);
            String letter3 = possibleAnswers.get(i).substring(2, 3);
            String letter4 = possibleAnswers.get(i).substring(3, 4);
            String letter5 = possibleAnswers.get(i).substring(4);
            if(letter1.equals(letter2) || letter1.equals(letter3) || letter1.equals(letter4) || letter1.equals(letter5) || letter2.equals(letter3) || letter2.equals(letter4) || letter2.equals(letter5) || letter3.equals(letter4) || letter3.equals(letter5) || letter4.equals(letter5))
            {
                possibleAnswers.remove(i);
            }
        }
    }

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
            }
        }
    }

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
            return "this will never be returned";
        }
    }

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

    private static String returnTripledLetter(String word)
    {
        String letter1 = word.substring(0, 1);
        String letter2 = word.substring(1, 2);
        String letter3 = word.substring(2, 3);
        String letter4 = word.substring(3, 4);
        String letter5 = word.substring(4);
        if((letter1.equals(letter2) && letter1.equals(letter3)) || (letter1.equals(letter2) && letter1.equals(letter4)) || (letter1.equals(letter2) && letter1.equals(letter5)) || (letter1.equals(letter3) && letter1.equals(letter4)) || (letter1.equals(letter3) && letter1.equals(letter5)) || (letter1.equals(letter4) && letter1.equals(letter5)))
        {
            return letter1;
        }
        else if((letter2.equals(letter3) && letter2.equals(letter4)) || (letter2.equals(letter3) && letter2.equals(letter5)) || (letter2.equals(letter4) && letter2.equals(letter5)))
        {
            return letter2;
        }
        else if((letter3.equals(letter4) && letter3.equals(letter5)))
        {
            return letter3;
        }
        else
        {
            return "this will never be returned";
        }
    }

    // private helper method, called in the constructor, which loads the words
    // from the allWords.txt text file into the "dictionary" instance variable!
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

    private static void insertionSortWordList(ArrayList<String> words)
    {
        for (int j = 1; j < words.size(); j++)
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

    public static void removeWordsWithTripleLettersFromList(ArrayList<String> word)
    {
        for(int i = 0; i < word.size(); i++)
        {
            String letter1 = word.get(i).substring(0, 1);
            String letter2 = word.get(i).substring(1, 2);
            String letter3 = word.get(i).substring(2, 3);
            String letter4 = word.get(i).substring(3, 4);
            String letter5 = word.get(i).substring(4);
            if((letter1.equals(letter2) && letter1.equals(letter3)) || (letter1.equals(letter2) && letter1.equals(letter4)) || (letter1.equals(letter2) && letter1.equals(letter5)) || (letter1.equals(letter3) && letter1.equals(letter4)) || (letter1.equals(letter3) && letter1.equals(letter5)) || (letter1.equals(letter4) && letter1.equals(letter5)) || (letter2.equals(letter3) && letter2.equals(letter4)) || (letter2.equals(letter3) && letter2.equals(letter5)) || (letter2.equals(letter4) && letter2.equals(letter5)) || (letter3.equals(letter4) && letter3.equals(letter5)))
            {
                word.remove(i);
            }
        }
    }

    public static void clearConsole()
    {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}