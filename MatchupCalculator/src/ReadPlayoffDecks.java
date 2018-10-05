
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import static java.lang.System.out;
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
public class ReadPlayoffDecks
{
    public static void main(String[] args) throws IOException
    {
        /*
        BufferedReader f = new BufferedReader(new InputStreamReader(System.in));
        
        out.println("Welcome to Conquest Helper v1.0.0");
        out.println("Type help for more information");
        
        //Load all decks into app
        CommandHandler.loadDecks();
        
        while(true)
        {
            String input = f.readLine();
            
            String output = CommandHandler.handleCommand(input);
            
            if(output.equals("quit"))
            {
                break;
            }
        }
        */
        String fName = "PlayoffDecks";
        File file;
        file = new File("PlayoffDecks.txt");
        PrintWriter writer = new PrintWriter(new FileWriter(file));
        
        File myFile = new File(fName + ".xlsx");//CHANGE NAME TO CURRENT COMPETITION
        //File myFile = new File("SR_South_Bluford.xlsx");//Hardcode for testing
        InputStream inp = new FileInputStream(myFile);
        XSSFWorkbook wb = new XSSFWorkbook(inp);

        //Access the match result sheet
        Sheet sheet = wb.getSheetAt(0);
        
        for(int i = 1; i < 78; i++)
        {
            Row row = sheet.getRow(i);
            Cell playerNameCell = row.getCell(0);
            writer.write(playerNameCell.toString() + "\n");
        }
        
        for(int i = 1; i < 78; i++)
        {
            Row row = sheet.getRow(i);
            for(int j = 3; j < 7; j++)
            {
                Cell deckCell = row.getCell(j);
                String deckInputString = deckCell.toString();
                String deckOutputString = "";
                
                if(deckInputString.contains("Token"))
                {
                    deckOutputString += "Tok";
                }
                else if(deckInputString.contains("Shudderwock"))
                {
                    deckOutputString += "Shu";
                }
                else if(deckInputString.contains("Malygos"))
                {
                    deckOutputString += "Mal";
                }
                else
                {
                    deckOutputString += deckInputString.substring(0,3);
                }
                
                if(deckInputString.contains("Druid"))
                {
                    deckOutputString += "Dru";
                }
                else if(deckInputString.contains("Hunter"))
                {
                    deckOutputString += "Hun";
                }
                else if(deckInputString.contains("Mage"))
                {
                    deckOutputString += "Mag";
                }
                else if(deckInputString.contains("Paladin"))
                {
                    deckOutputString += "Pal";
                }
                else if(deckInputString.contains("Rogue"))
                {
                    deckOutputString += "Rog";
                }
                else if(deckInputString.contains("Shaman"))
                {
                    deckOutputString += "Sha";
                }
                else if(deckInputString.contains("Warlock"))
                {
                    deckOutputString += "Wlk";
                }
                else if(deckInputString.contains("Warrior"))
                {
                    deckOutputString += "Wrr";
                }
                else if(deckInputString.contains("Priest"))
                {
                    deckOutputString += "Pri";
                }
                
                writer.write(deckOutputString + "\n");
            }
        }
        writer.close();
    }
}
