/**
 * This class allows for a player object to be created with a name and records the number of guesses the player has done.
 * @author Robbie Galpern-Levin
 */
public class Player
{
    /**
     * Represents the player's name
     */
    String name;

    /**
     * Represents the number of times the player has guessed a word
     */
    int guessNumber;

    /**
     * Creates a player object with a name and a starting value of 0 for guessNumber
     * @param name the name the player will be given
     */
    public Player(String name)
    {
        this.name = name;
        guessNumber = 0;
    }

    /**
     * simple getter method for the instance variable name
     * @return returns the instance variable name
     */
    public String getName()
    {
        return name;
    }

    /**
     * simple getter method for the instance variable guessNumber
     * @return returns the instance variable guessNumber
     */
    public int getGuessNumber()
    {
        return guessNumber;
    }

    /**
     * Allows the instance variable guessNumber to be changed
     * @param newGuessNumber the new value that guessNumber will be set to
     */
    public void setGuessNumber(int newGuessNumber)
    {
        guessNumber = newGuessNumber;
    }
}
