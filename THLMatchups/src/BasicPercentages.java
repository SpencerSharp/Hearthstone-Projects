
import static java.lang.System.out;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author spencersharp
 */
public class BasicPercentages
{
    public static class SelectionStrategy
    {
        int deckToPick;
        double winrate;
        SelectionStrategy[] winningSelectionStrategies;
        SelectionStrategy[] losingSelectionStrategies;
        int[] yourDecks;
        int[] opponentDecks;
        double[][] matchups;
    }
    
    public static void main(String[] args)
    {
        double[][] matchups = {
            {50.0, 63.7, 50.5, 62.5},
            {42.3, 47.1, 55.2, 47.9},
            {53.3, 53.8, 49.5, 36.6},
            {46.4, 58.9, 38.1, 56.9}
        };
        
        String[] yourDecks = {
            "Big Druid",
            "Secret Hunter",
            "Big Spell Mage",
            "Even Warlock"
        };
        
        String[] opponentDecks = {
            "Big Druid",
            "Odd Paladin",
            "Odd Rogue",
            "Shudderwock Shaman"
        };
        
        Scanner liner = new Scanner(System.in);
        
        out.println("Your Ban\n"
                + "0 for " + opponentDecks[0] + ", "
                + "1 for " + opponentDecks[1] + ", "
                + "2 for " + opponentDecks[2] + ", "
                + "3 for " + opponentDecks[3] + ": ");
        
        int myBan = Integer.parseInt(liner.next());
        
        out.println("Opponent's Ban\n"
                + "0 for " + yourDecks[0] + ", "
                + "1 for " + yourDecks[1] + ", "
                + "2 for " + yourDecks[2] + ", "
                + "3 for " + yourDecks[3] + ": ");
        
        int opponentBan = Integer.parseInt(liner.next());
        
        double[][] matchupsCopy = new double[3][3];
        
        int curRow = 0;
        for(int i = 0; i < matchups.length; i++)
        {
            int curCol = 0;
            if(i == opponentBan)
            {
                curRow--;
            }
            else
            {
                for(int j = 0; j < matchups[i].length; j++)
                {
                    if(j == myBan)
                    {
                        curCol--;
                    }
                    else
                    {
                        matchupsCopy[curRow][curCol] = matchups[i][j];
                    }
                    curCol++;
                }
            }
            curRow++;
        }
        
        matchups = matchupsCopy;
        
        String[] yourDecksCopy = new String[3];
        int index = 0;
        for(int i = 0; i < yourDecks.length; i++)
        {
            if(i == opponentBan)
            {
                index--;
            }
            else
            {
                yourDecksCopy[index] = yourDecks[i];
            }
            index++;
        }
        yourDecks = yourDecksCopy;
        
        String[] opponentDecksCopy = new String[3];
        index = 0;
        for(int i = 0; i < opponentDecks.length; i++)
        {
            if(i == myBan)
            {
                index--;
            }
            else
            {
                opponentDecksCopy[index] = opponentDecks[i];
            }
            index++;
        }
        opponentDecks = opponentDecksCopy;
        
        display(matchups);
        
        int[] myDeckInts = {0,1,2};
        int[] opponentDeckInts = {0,1,2};
        
        SelectionStrategy fullStrategy = winSeriesProb(matchups, 0, false, myDeckInts, opponentDeckInts);
        
        SelectionStrategy currentStrategy = fullStrategy;
        
        while(currentStrategy != null)
        {
            out.println("------------------");
            out.println("Estimated win probability: " + currentStrategy.winrate + "\n");
            out.println("AVAILABLE DECK INCIDES");
            display(currentStrategy.yourDecks);
            out.println("OPPONENT DECK INCIDES");
            display(currentStrategy.opponentDecks);
            display(currentStrategy.matchups);
        
            out.println("You should pick " + yourDecks[currentStrategy.yourDecks[currentStrategy.deckToPick]]);
            out.println("What deck did your opponent pick?");
            for(int i = 0; i < currentStrategy.opponentDecks.length; i++)
            {
                out.print(i + " for " + opponentDecks[currentStrategy.opponentDecks[i]] + ", ");
            }
            out.println();
            int opponentDeckPicked = Integer.parseInt(liner.next());
            out.println("Did you win? W for win, L for loss");
            String didWin = liner.next().toLowerCase();
            
            if(didWin.equals("w"))
            {
                  currentStrategy = currentStrategy.winningSelectionStrategies[opponentDeckPicked];
            }
            else
            {
                currentStrategy = currentStrategy.losingSelectionStrategies[opponentDeckPicked];
            }
        }
    }
    
    public static SelectionStrategy winSeriesProb(double[][] matchups, int level, boolean isWin, int[] yourDecks, int[] opponentDecks)
    {
        SelectionStrategy strat = new SelectionStrategy();
        strat.yourDecks = yourDecks;
        strat.opponentDecks = opponentDecks;
        strat.matchups = matchups;
        //1 row
        if(matchups.length == 1)
        {
            double result = 1.0;
            for(int i = 0; i < matchups[0].length; i++)
            {
                double prob = (100-matchups[0][i])/100;
                result *= prob;
            }
            strat.winrate = 1-result;
            return strat;
        }
        //1 col
        if(matchups[0].length == 1)
        {
            double result = 1.0;
            for(int i = 0; i < matchups.length; i++)
            {
                double prob = matchups[i][0]/100;
                result *= prob;
            }
            strat.winrate = result;
            return strat;
        }
        
        double maxProb = 0.0;
        double totalProb = 0.0;
        double[] rowProbs = new double[matchups.length];
        SelectionStrategy[][] winningSelectionStrategies = new SelectionStrategy[matchups.length][matchups[0].length];
        SelectionStrategy[][] losingSelectionStrategies = new SelectionStrategy[matchups.length][matchups[0].length];
        for(int i = 0; i < matchups.length; i++)
        {
            double rowProb = 0.0;
            for(int j = 0; j < matchups[i].length; j++)
            {
                double probToWinGame = matchups[i][j]/100;
                SelectionStrategy winningSelectionStrategy = winSeriesProb(newMatchupsIfWin(matchups,i), level+1, true, removeIndex(yourDecks, i), opponentDecks);
                double probToWinSeriesIfWinGame = winningSelectionStrategy.winrate;
                double probToLoseGame = 1 - probToWinGame;
                SelectionStrategy losingSelectionStrategy = winSeriesProb(newMatchupsIfLose(matchups,j), level+1, false, yourDecks, removeIndex(opponentDecks, j));
                double probToWinSeriesIfLoseGame = losingSelectionStrategy.winrate;
                double probToWinSeries = probToWinGame * probToWinSeriesIfWinGame + probToLoseGame * probToWinSeriesIfLoseGame;
                totalProb += probToWinSeries/(matchups.length * matchups[i].length);
                rowProb += probToWinSeries/(matchups[i].length);
                
                winningSelectionStrategies[i][j] = winningSelectionStrategy;
                losingSelectionStrategies[i][j] = losingSelectionStrategy;
            }
            rowProbs[i] = rowProb;
        }
        
        if(level == 0)
        {
            out.println("ROW PROBS");
            display(rowProbs);
        }
        
        int maxRow = 0;
        for(int i = 0; i < rowProbs.length; i++)
        {
            if(rowProbs[i] >= maxProb)
            {
                maxProb = rowProbs[i];
                maxRow = i;
            }
        }
        String display = "";
        for(int i = 0; i < level; i++)
        {
            display += "   ";
        }
        display += isWin + " ";
        display += String.format("%.2f",rowProbs[maxRow]) + " ";
        display += maxRow;
        out.println(display);
        
        strat.winrate = maxProb;
        strat.winningSelectionStrategies = winningSelectionStrategies[maxRow];
        strat.losingSelectionStrategies = losingSelectionStrategies[maxRow];
        strat.deckToPick = maxRow;
        
        return strat;
    }
    
    public static double[][] newMatchupsIfWin(double[][] matchups, int row)
    {
        //WE ARE REMOVING AN ENTIRE ROW
        int curRow = 0;
        double[][] newMatchups = new double[matchups.length-1][matchups[0].length];
        for(int i = 0; i < matchups.length; i++)
        {
            if(i == row)
            {
                curRow--;
            }
            else
            {
                for(int j = 0; j < matchups[i].length; j++)
                {
                    newMatchups[curRow][j] = matchups[i][j];
                }
            }
            curRow++;
        }
        return newMatchups;
    }
    
    public static double[][] newMatchupsIfLose(double[][] matchups, int col)
    {
        //WE ARE REMOVING AN ENTIRE COLUMN
        
        int curCol = 0;
        double[][] newMatchups = new double[matchups.length][matchups[0].length-1];
        for(int i = 0; i < matchups.length; i++)
        {
            curCol = 0;
            for(int j = 0; j < matchups[i].length; j++)
            {
                if(j == col)
                {
                    curCol--;
                }
                else
                {
                    newMatchups[i][curCol] = matchups[i][j];
                }
                curCol++;
            }
        }
        //display(newMatchups);
        return newMatchups;
    }
    
    public static void display(double[][] ray)
    {
        String display = "--------------\n";
        for(int i = 0; i < ray.length; i++)
        {
            for(int j = 0; j < ray[i].length; j++)
            {
                display += ray[i][j] + " ";
            }
            display += "\n";
        }
        display += "--------------\n";
        out.print(display);
    }
    
    public static void display(int[] ray)
    {
        String display = "--------------\n";
        for(int i = 0; i < ray.length; i++)
        {
            display += ray[i] + " ";
        }
        display += "\n--------------\n";
        out.print(display);
    }
    
    public static void display(double[] ray)
    {
        String display = "--------------\n";
        for(int i = 0; i < ray.length; i++)
        {
            display += ray[i] + " ";
        }
        display += "\n--------------\n";
        out.print(display);
    }
    
    public static int[] removeIndex(int[] array, int index)
    {
        int[] copy = new int[array.length-1];
        int cur = 0;
        for(int i = 0; i < array.length; i++)
        {
            if(index == i)
            {
                cur--;
            }
            else
            {
                copy[cur] = array[i];
            }
            cur++;
        }
        return copy;
    }
}