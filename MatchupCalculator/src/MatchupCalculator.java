
import java.awt.event.KeyEvent;
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
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.Scanner;
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
public class MatchupCalculator
{
    public static void main(String[] args) throws IOException, InterruptedException
    {
        
        Scanner sc = new Scanner(System.in);
        
        //Check if int
        
        out.println("Welcome to Hearthstone Conquest Helper v1.1.0");
        out.println("Type help for more information");
        
        //Load all decks into app
        CommandHandler.loadDecks();
        ArrayList<String> pastCommands = new ArrayList<String>();
        
        while(true)
        {
            out.print("hch> "); 
            String input = sc.nextLine();
            pastCommands.add(input);
            
            String output = CommandHandler.handleCommand(input);
            
            if(output.equals("quit"))
            {
                break;
            }
            out.print(output);
        }
                
    }
}
