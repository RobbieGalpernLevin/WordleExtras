public class Player
{
    String name;
    int guessNumber;

    public Player(String name)
    {
        this.name = name;
        guessNumber = 0;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String newName)
    {
        name = newName;
    }

    public int getGuessNumber()
    {
        return guessNumber;
    }

    public void setGuessNumber(int newGuessNumber)
    {
        guessNumber = newGuessNumber;
    }
}
