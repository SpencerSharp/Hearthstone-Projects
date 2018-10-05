/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author spencersharp
 */
public class Class
{
    int id;
    String name;

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 17 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj == null)
        {
            return false;
        }
        if(getClass() != obj.getClass())
        {
            return false;
        }
        final Class other = (Class) obj;
        if(this.id != other.id)
        {
            return false;
        }
        return true;
    }
    
    public String toString()
    {
        return name;
    }
    
    public static Class getClassFromId(int id)
    {
        Class toReturn = new Class();
        toReturn.id = id;
        switch(id)
        {
            case 1:     toReturn.name = "Druid";
                        break;
            case 2:     toReturn.name = "Hunter";
                        break;
            case 3:     toReturn.name = "Mage";
                        break;
            case 4:     toReturn.name = "Paladin";
                        break;
            case 5:     toReturn.name = "Priest";
                        break;
            case 6:     toReturn.name = "Rogue";
                        break;
            case 7:     toReturn.name = "Shaman";
                        break;
            case 8:     toReturn.name = "Warlock";
                        break;
            case 9:     toReturn.name = "Warrior";
                        break;
        }
        return toReturn;
    }
    
    public static Class getClassFromClassName(String className)
    {
        Class toReturn = new Class();
        toReturn.name = className.substring(0,1).toUpperCase() + className.substring(1);
        className = className.toLowerCase();
        switch(className)
        {
            case "druid":   toReturn.id = 1;
                            break;
            case "hunter":  toReturn.id = 2;
                            break;
            case "mage":    toReturn.id = 3;
                            break;
            case "paladin": toReturn.id = 4;
                            break;
            case "priest":  toReturn.id = 5;
                            break;
            case "rogue":   toReturn.id = 6;
                            break;
            case "shaman":  toReturn.id = 7;
                            break;
            case "warlock": toReturn.id = 8;
                            break;
            case "warrior": toReturn.id = 9;
                            break;
        }
        if(toReturn.id == 0)
            return null;
        return toReturn;
    }
    
    public static Class getClassOfDeckName(String deckName)
    {
        Class hearthstoneClass = new Class();
        deckName = deckName.toLowerCase();
        if(deckName.contains("druid"))
        {
            hearthstoneClass.id = 1;
            hearthstoneClass.name = "Druid";
        }
        else if(deckName.contains("hunter"))
        {
            hearthstoneClass.id = 2;
            hearthstoneClass.name = "Hunter";
        }
        else if(deckName.contains("mage"))
        {
            hearthstoneClass.id = 3;
            hearthstoneClass.name = "Mage";
        }
        else if(deckName.contains("paladin"))
        {
            hearthstoneClass.id = 4;
            hearthstoneClass.name = "Paladin";
        }
        else if(deckName.contains("priest"))
        {
            hearthstoneClass.id = 5;
            hearthstoneClass.name = "Priest";
        }
        else if(deckName.contains("rogue"))
        {
            hearthstoneClass.id = 6;
            hearthstoneClass.name = "Rogue";
        }
        else if(deckName.contains("shaman"))
        {
            hearthstoneClass.id = 7;
            hearthstoneClass.name = "Shaman";
        }
        else if(deckName.contains("warlock"))
        {
            hearthstoneClass.id = 8;
            hearthstoneClass.name = "Warlock";
        }
        else if(deckName.contains("warrior"))
        {
            hearthstoneClass.id = 9;
            hearthstoneClass.name = "Warrior";
        }
        return hearthstoneClass;
    }
}
