
import java.util.HashMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author spencersharp
 */
public class Deck
{
    int id;
    String[] names;
    HashMap<Integer,Double> matchups = new HashMap<Integer,Double>();
    Class hearthstoneClass;
    
    public String toString()
    {
        return names[0];
    }
}
