
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.System.out;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author spencersharp
 */
public class CommandHandler
{
    static Deck[] allDecks;
    static Deck[] myDecks = new Deck[4];
    static Deck[] oppDecks = new Deck[4];
    static Class myBan = null;
    static Class oppBan = null;
    
    static final String[] commands = {
        "help",
        "info",
        "setdeck",
        "setoppdeck",
        "setban",
        "setoppban",
        "winprob",
        "showdecks",
        "showoppdecks",
        "showban",
        "showoppban",
        "show",
        "matchups",
        "playwinprob",
        "play",
        "quit"
    };
    
    public static void loadDecks() throws IOException
    {
        File myFile = new File("../Matchups.xlsx");//CHANGE NAME TO CURRENT COMPETITION
        InputStream inp = new FileInputStream(myFile);
        XSSFWorkbook wb = new XSSFWorkbook(inp);

        //Access the match result sheet
        Sheet sheet = wb.getSheetAt(0);
        Row row;
        int index = 0;
        ArrayList<Deck> decks = new ArrayList<Deck>();
        while((row = sheet.getRow(index++)) != null)
        {
            //System.out.println(index);
            if(index == 1)
            {
                continue;
            }
            Deck deck = new Deck();
            deck.id = index-1;
            deck.names = new String[1];
            Cell cell = row.getCell(0);
            //System.out.println(cell);
            deck.names[0] = cell.toString();
            deck.hearthstoneClass = Class.getClassOfDeckName(cell.toString());
            decks.add(deck);
        }
        
        index = 0;
        allDecks = new Deck[decks.size()];
        for(Deck deck : decks)
        {
            allDecks[index] = decks.get(index);
            index++;
        }
        
        //Thru row
        for(int i = 1; i <= allDecks.length; i++)
        {
            row = sheet.getRow(i);
            //Thru col
            if(row.getCell(1) == null)
            {
                continue;
            }
            for(int j = 1; j <= allDecks.length; j++)
            {
                Cell cell = row.getCell(j);
                
                double winrate = cell.getNumericCellValue();
                
                allDecks[i-1].matchups.put(j, winrate);
            }
        }
    }
    
    private static Deck getDeckFromId(int id)
    {
        for(Deck deck : allDecks)
        {
            if(deck.id == id)
            {
                return deck;
            }
        }
        return null;
    }
    
    public static String handleCommand(String input) throws FileNotFoundException, IOException
    {
        StringTokenizer st = new StringTokenizer(input);
        if(!st.hasMoreTokens())
        {
            return "";
        }
        String first = st.nextToken();
        input = input.toLowerCase();
        
        switch(first.toLowerCase())
        {
            case "help":            return help();
            case "info":            return info(input);
            case "setdeck":         return setDeck(input);
            case "setoppdeck":      return setOppDeck(input);
            case "setban":          return setBan(input);
            case "setoppban":       return setOppBan(input);
            case "winprob":         return winProb();
            case "showdecks":       return showDecks();
            case "showoppdecks":    return showOppDecks();
            case "showban":         return showBan();
            case "showOppBan":      return showOppBan();
            case "show":            return show();
            case "playwinprob":     return playWinProb(input);
            case "play":            return play(input);
            case "load":            return load(input);
            case "save":            return save(input);
            case "minmax":          return minmax();
            case "worstcase":       return worstcase();
            case "maxvs":           return maxvs(input);
            case "matchups":        return showMatchups();
            case "allmatchups":     return allMatchups();
            case "banmatchups":     return banMatchups();
            case "maxban":          return maxBan();
            case "maxoppban":       return maxOppBan();
            case "quit":            return "quit";
        }
        return "That is not a supported command.\n";
    }
    
    private static String help()
    {
        String output = ""
                + "This is a list of commands that you can run. For more "
                + "information on a command type info + the command name.\n";
        for(String command : commands)
        {
            output += command + "\n";
        }
        return output;
    }
    
    private static String info(String input)
    {
        HashMap<String,String> commandInfo = new HashMap<String,String>();
        
        commandInfo.put("help", "Shows a list of all commands.");
        commandInfo.put("info", "When followed by any command it will give more "
                + "detailed information about that command.");
        commandInfo.put("setdeck", "When followed by an index and then a valid deck name "
                + "(for example, \"setoppdeck 0 Zoo Warlock\") it will put that deck "
                + "at that index in the list of \"YOUR\" stored decks. Valid indices "
                + "currently range from 0 to 3.");
        commandInfo.put("setdeck", "When followed by an index and then a valid deck name "
                + "(for example, \"setoppdeck 0 Zoo Warlock\") it will put that deck "
                + "at that index in the list of \"YOUR OPPONENT'S\" stored decks. Valid indices "
                + "currently range from 0 to 3.");
        commandInfo.put("setban", "When followed by valid deck name "
                + "(for example, \"setban Zoo Warlock\") it will make that "
                + "deck your stored ban. If the deck is not in your opponent's "
                + "lineup when a command that requires a ban is run, an error "
                + "will occur.");
        commandInfo.put("setoppban", "When followed by valid deck name "
                + "(for example, \"setban Zoo Warlock\") it will make that "
                + "deck your opponent's stored ban. If the deck is not in your "
                + "lineup when a command that requires a ban is run, an error "
                + "will occur.");
        commandInfo.put("winprob", "Will print the win probability of your lineup "
                + "against your opponent's with the current bans. Requires "
                + "4 decks for you and your opponent, as well as a ban for both"
                + "you and your opponent. It uses a naive one sided deck selection "
                + "optimizer. This means you always queue the deck that gives you "
                + "the best chance to win assuming your opponent queues a random deck, "
                + "and your opponent always queues a random deck.");
        commandInfo.put("showdecks", "Prints all of the decks that you currently "
                + "have stored as yours, along with their indices in the list.");
        commandInfo.put("showoppdecks", "Prints all of the decks that you currently "
                + "have stored as your opponent's, along with their indices in the list");
        commandInfo.put("showban", "Prints out the ban you currently have stored.");
        commandInfo.put("showoppban", "Prints out the ban you currently have stored for your opponent");
        commandInfo.put("show", "Prints all information currently stored, including "
                + "your decks, your opponent's decks, your ban and your opponent's ban.");
        commandInfo.put("play", "Play is the command to run for playing a match");
        commandInfo.put("load", "Loads a file in the format of: Each of your decks on subsequent lines,"
                + "then the class you're banning, then your opponent's decks on subsequent lines, then"
                + "the class your opponent is banning.");
        commandInfo.put("quit", "Quits the application.");
        
        String commandAskedAbout;
        
        StringTokenizer st = new StringTokenizer(input);
        st.nextToken();
        commandAskedAbout = st.nextToken();
        
        if(!commandInfo.containsKey(commandAskedAbout))
        {
            return "There is no command with the name " + commandAskedAbout + ".\n";
        }
        
        return commandInfo.get(commandAskedAbout) + "\n";
    }
    
    private static String setDeck(String input)
    {
        input = input.replace("setdeck ", "");
        StringTokenizer st = new StringTokenizer(input);
        String indexAsString = st.nextToken();
        String deckName = input.replace(indexAsString + " ", "");
        Deck deck = null;
        
        for(Deck tempDeck : allDecks)
        {
            for(String name : tempDeck.names)
            {
                if(name.equalsIgnoreCase(deckName))
                {
                    deck = tempDeck;
                }
            }
        }
        
        int index = Integer.parseInt(indexAsString);
        
        myDecks[index] = deck;
        
        return deckName + " has been set as your deck number " + index + ".\n";
    }
    
    private static String setOppDeck(String input)
    {
        input = input.replace("setoppdeck ", "");
        StringTokenizer st = new StringTokenizer(input);
        String indexAsString = st.nextToken();
        String deckName = input.replace(indexAsString + " ", "");
        Deck deck = null;
        
        for(Deck tempDeck : allDecks)
        {
            for(String name : tempDeck.names)
            {
                if(name.equalsIgnoreCase(deckName))
                {
                    deck = tempDeck;
                }
            }
        }
        
        int index = Integer.parseInt(indexAsString);
        
        oppDecks[index] = deck;
        
        return deckName + " has been set as your opponent's deck number " + index + ".\n";
    }
    
    private static String setBan(String input)
    {
        input = input.replace("setban ", "");
        Deck deck = null;
        String deckName = input;
        
        myBan = Class.getClassFromClassName(input);
        
        return myBan + " has been set as your ban.\n";
    }
    
    private static String setOppBan(String input)
    {
        input = input.replace("setoppban ", "");
        Deck deck = null;
        String deckName = input;
        
        oppBan = Class.getClassFromClassName(input);
        
        return oppBan + " has been set as your opponent's ban.\n";
    }
    
    private static String winProb()
    {
        String winProbResult = String.format("%.2f",SimpleWinProbCalculator.calculateWinProb(myDecks, oppDecks, myBan, oppBan));
        String output = "Your current lineup's estimated winrate against your "
                + "opponent's is " + winProbResult + ".\n";
        return output;
    }
    
    private static String showDecks()
    {
        String output = "My Decks\n";
        for(int i = 0; i < myDecks.length; i++)
        {
            Deck deck = myDecks[i];
            if(deck == null)
            {
                output += "Index " + i + ": None";
            }
            else
            {
                output += "Index " + i + ": " + myDecks[i].names[0] + " " + myDecks[i].hearthstoneClass + "\n";
            }
            
        }
        return output;
    }
    
    private static String showOppDecks()
    {
        String output = "Opponent Decks\n";
        for(int i = 0; i < oppDecks.length; i++)
        {
            output += "Index " + i + ": " + oppDecks[i].names[0] + "\n";
        }
        return output;
    }
    
    private static String showBan()
    {
        String output;
        if(myBan == null)
        {
            output = "You currently have no class stored as a ban.\n";
        }
        else
        {
            output = "Your ban is currently stored as " + myBan + ".\n";
        }
        
        return output;
    }
    
    private static String showOppBan()
    {
        String output;
        if(oppBan == null)
        {
            output = "You currently have no deck stored as your opponent's ban.\n";
        }
        else
        {
            output = "Your opponent's ban is currently stored as " + oppBan + ".\n";
        }
        return output;
    }
    
    public static String load(String input) throws FileNotFoundException
    {
        File file = new File("../" + input.replace("load ", ""));
        
        Scanner sc = new Scanner(file);
        
        for(int i = 0; i < 4; i++)
        {
            String line = sc.nextLine();
            //out.println(line);
            setDeck(i + " " + line);
        }
        setBan(sc.nextLine());
        for(int i = 0; i < 4; i++)
        {
            setOppDeck(i + " " + sc.nextLine());
        }
        setOppBan(sc.nextLine());
        
        return "File " + input.replace("load ","") + " has been loaded.\n";
    }
    
    public static String save(String input) throws FileNotFoundException, IOException
    {
        File file = new File("../" + input.replace("save ", ""));
        
        BufferedWriter f = new BufferedWriter(new FileWriter(file));
        
        f.write(myDecks[0]+"\n");
        f.write(myDecks[1]+"\n");
        f.write(myDecks[2]+"\n");
        f.write(myDecks[3]+"\n");
        f.write(myBan+"\n");
        f.write(oppDecks[0]+"\n");
        f.write(oppDecks[1]+"\n");
        f.write(oppDecks[2]+"\n");
        f.write(oppDecks[3]+"\n");
        f.write(oppBan+"\n");
        
        f.close();
        
        return "File " + input.replace("save ","") + " has been saved to.\n";
    }

    private static String show()
    {
        String result = showDecks() + "\n" + showOppDecks() + "\n" + showBan() + "\n" + showOppBan() + "\n";
        return result;
    }
    
    private static String minmax()
    {
        Object[] results = SimpleWinProbCalculator.minmax(myDecks, oppDecks, allDecks);
        String result = "Optimal lineup:\n";
        Deck myDeck1 = (Deck) results[0];
        Deck myDeck2 = (Deck) results[1];
        Deck myDeck3 = (Deck) results[2];
        Deck myDeck4 = (Deck) results[3];
        Class classBan = (Class) results[4];
        double winprob = (Double) results[5];
        
        result += "Deck 1: " + myDeck1.names[0];
        result += "\nDeck 2: " + myDeck2.names[0];
        result += "\nDeck 3: " + myDeck3.names[0];
        result += "\nDeck 4: " + myDeck4.names[0];
        result += "\nBan: " + classBan;
        result += "\nWorst case win probability: " + winprob + "\n";
        return result;
    }
    
    private static String worstcase()
    {
        Object[] results = SimpleWinProbCalculator.worstcase(myDecks, oppDecks, allDecks, myBan);
        String result = "Scary lineup:\n";
        Deck myDeck1 = (Deck) results[0];
        Deck myDeck2 = (Deck) results[1];
        Deck myDeck3 = (Deck) results[2];
        Deck myDeck4 = (Deck) results[3];
        Class classBan = (Class) results[4];
        double winprob = (Double) results[5];
        
        result += "Deck 1: " + myDeck1.names[0];
        result += "\nDeck 2: " + myDeck2.names[0];
        result += "\nDeck 3: " + myDeck3.names[0];
        result += "\nDeck 4: " + myDeck4.names[0];
        result += "\nBan: " + classBan;
        result += "\nWorst case win probability: " + winprob + "\n";
        return result;
    }
    
    private static String maxvs(String input)
    {
        boolean allClasses = input.contains("-a");
        boolean ignoreOppBan = input.contains("-i");
        
        Object[] results;
        if(allClasses && ignoreOppBan)
        {
            results = SimpleWinProbCalculator.maxVs(oppDecks, allDecks);
        }
        else if(allClasses)
        {
            results = SimpleWinProbCalculator.maxVs(oppDecks, allDecks, oppBan);
        }
        else if(ignoreOppBan)
        {
            results = SimpleWinProbCalculator.maxVs(myDecks, oppDecks, allDecks);
        }
        else
        {
            results = SimpleWinProbCalculator.maxVs(myDecks, oppDecks, allDecks, oppBan);
        }
        
        String result = "Best Lineup To Beat Opponent's:\n";
        Deck myDeck1 = (Deck) results[0];
        Deck myDeck2 = (Deck) results[1];
        Deck myDeck3 = (Deck) results[2];
        Deck myDeck4 = (Deck) results[3];
        Class classBan = (Class) results[4];
        double winprob = (Double) results[5];
        
        result += "Deck 1: " + myDeck1.names[0];
        result += "\nDeck 2: " + myDeck2.names[0];
        result += "\nDeck 3: " + myDeck3.names[0];
        result += "\nDeck 4: " + myDeck4.names[0];
        result += "\nBan: " + classBan;
        result += "\nWin probability: " + winprob + "\n";
        return result;
    }
    
    private static String maxvsallclassesignoreoppban()
    {
        return "ERROR";
    }
    
    private static String maxvsallclasses()
    {
        return "ERROR";
    }
    
    private static String maxvsignoreoppban()
    {
        return "ERROR";
    }
    
    private static String banMatchups()
    {
        String result = "";
        int squaresize = 20;
        for(int i = 0; i < squaresize; i++)
        {
            result += " ";
        }
        result += "|";
        for(Deck deck : oppDecks)
        {
            String name = deck.hearthstoneClass.name;
            int spacing = squaresize - name.length();
            for(int i = 0; i < (int)Math.ceil(spacing / 2.0); i++)
            {
                result += " ";
            }
            result += name;
            for(int i = 0; i < (int)Math.floor(spacing/2.0); i++)
            {
                result += " ";
            }
            result += "|";
        }
        result += "\n";
        
        
        for(Deck deck : myDecks)
        {
            String name = deck.hearthstoneClass.name;
            int spacing = squaresize - name.length();
            for(int i = 0; i < (int)Math.ceil(spacing / 2.0); i++)
            {
                result += " ";
            }
            result += name;
            for(int i = 0; i < (int)Math.floor(spacing/2.0); i++)
            {
                result += " ";
            }
            result += "|";
            
            for(Deck oppDeck : oppDecks)
            {
                spacing = squaresize - 4;
                for(int i = 0; i < (int)Math.ceil(spacing / 2.0); i++)
                {
                    result += " ";
                }
                result += String.format("%.2f", SimpleWinProbCalculator.calculateWinProb(myDecks, oppDecks, oppDeck.hearthstoneClass, deck.hearthstoneClass));
                for(int i = 0; i < (int)Math.floor(spacing / 2.0); i++)
                {
                    result += " ";
                }
                result += "|";
            }
            result += "\n";
        }
        return result;
    }
    
    private static String allMatchups()
    {
        String result = "";
        int squaresize = 20;
        for(int i = 0; i < squaresize; i++)
        {
            result += " ";
        }
        result += "|";
        for(Deck deck : oppDecks)
        {
            String name = deck.names[0];
            int spacing = squaresize - name.length();
            for(int i = 0; i < (int)Math.ceil(spacing / 2.0); i++)
            {
                result += " ";
            }
            result += name;
            for(int i = 0; i < (int)Math.floor(spacing/2.0); i++)
            {
                result += " ";
            }
            result += "|";
        }
        result += "\n";
        
        
        for(Deck deck : myDecks)
        {
            String name = deck.names[0];
            int spacing = squaresize - name.length();
            for(int i = 0; i < (int)Math.ceil(spacing / 2.0); i++)
            {
                result += " ";
            }
            result += name;
            for(int i = 0; i < (int)Math.floor(spacing/2.0); i++)
            {
                result += " ";
            }
            result += "|";
            
            for(Deck oppDeck : oppDecks)
            {
                spacing = squaresize - 4;
                for(int i = 0; i < (int)Math.ceil(spacing / 2.0); i++)
                {
                    result += " ";
                }
                result += deck.matchups.get(oppDeck.id);
                for(int i = 0; i < (int)Math.floor(spacing / 2.0); i++)
                {
                    result += " ";
                }
                result += "|";
            }
            result += "\n";
        }
        return result;
    }
    
    private static String showMatchups()
    {
        String result = "";
        int squaresize = 20;
        for(int i = 0; i < squaresize; i++)
        {
            result += " ";
        }
        result += "|";
        for(Deck deck : oppDecks)
        {
            if(deck.hearthstoneClass.equals(myBan))
                continue;
            String name = deck.names[0];
            int spacing = squaresize - name.length();
            for(int i = 0; i < (int)Math.ceil(spacing / 2.0); i++)
            {
                result += " ";
            }
            result += name;
            for(int i = 0; i < (int)Math.floor(spacing/2.0); i++)
            {
                result += " ";
            }
            result += "|";
        }
        result += "\n";
        
        
        for(Deck deck : myDecks)
        {
            if(deck.hearthstoneClass.equals(oppBan))
            {
                continue;
            }
            
            String name = deck.names[0];
            int spacing = squaresize - name.length();
            for(int i = 0; i < (int)Math.ceil(spacing / 2.0); i++)
            {
                result += " ";
            }
            result += name;
            for(int i = 0; i < (int)Math.floor(spacing/2.0); i++)
            {
                result += " ";
            }
            result += "|";
            
            for(Deck oppDeck : oppDecks)
            {
                if(oppDeck.hearthstoneClass.equals(myBan))
                    continue;
                
                spacing = squaresize - 4;
                for(int i = 0; i < (int)Math.ceil(spacing / 2.0); i++)
                {
                    result += " ";
                }
                result += deck.matchups.get(oppDeck.id);
                for(int i = 0; i < (int)Math.floor(spacing / 2.0); i++)
                {
                    result += " ";
                }
                result += "|";
            }
            result += "\n";
        }
        return result;
    }
    
    private static String maxBan()
    {
        Object[] results = SimpleWinProbCalculator.maxBan(myDecks, oppDecks, allDecks, oppBan);
        myBan = (Class) results[4];
        return "Your ban is now set to: " + myBan + "\nYour lineup winrate is now: " + results[5] + "\n";
    }
    
    private static String maxOppBan()
    {
        Object[] results = SimpleWinProbCalculator.maxOppBan(myDecks, oppDecks, allDecks, myBan);
        oppBan = (Class) results[4];
        return "Opponent's ban is now set to: " + oppBan + "\nYour lineup winrate is now: " + results[5] + "\n";
    }
    
    private static String playWinProb(String input)
    {
        return "ERROR";
    }
    
    private static String play(String input)
    {
        return "ERROR";
    }
}