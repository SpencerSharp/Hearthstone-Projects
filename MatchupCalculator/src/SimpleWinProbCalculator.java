
import static java.lang.System.out;
import java.util.HashMap;
import java.util.Map;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author spencersharp
 */
public class SimpleWinProbCalculator
{
    private static Deck[] deepCopy(Deck[] array)
    {
        Deck[] copy = new Deck[array.length];
        int i = 0;
        for(Deck deck : array)
        {
            copy[i++] = deck;
        }
        return copy;
    }
    
    public static Object[] maxBan(Deck[] myDecks, Deck[] oppDecks, Deck[] allDecks, Class oppBan)
    {
        Deck[] results = null;
        Deck ban = null;
        double resultWinrate = 0.0;
        
        for(Deck myBanDeck : oppDecks)
        {
            double winprob = calculateWinProb(myDecks, oppDecks, myBanDeck.hearthstoneClass, oppBan);
            if(winprob > resultWinrate)
            {
                resultWinrate = winprob;
                results = deepCopy(myDecks);
                ban = myBanDeck;
            }
        }
        
        Object[] finalResults = new Object[6];
        int i = 0;
        for(Deck deck : results)
        {
            myDecks[i] = deck;
            finalResults[i++] = deck;
        }
        finalResults[4] = ban.hearthstoneClass;
        finalResults[5] = resultWinrate;
        return finalResults;
    }
    
    public static Object[] maxOppBan(Deck[] myDecks, Deck[] oppDecks, Deck[] allDecks, Class myBan)
    {
        Deck[] results = null;
        Deck ban = null;
        double resultWinrate = 100.0;
        
        for(Deck oppBanDeck : myDecks)
        {
            double winprob = calculateWinProb(myDecks, oppDecks, myBan, oppBanDeck.hearthstoneClass);
            if(winprob < resultWinrate)
            {
                resultWinrate = winprob;
                results = deepCopy(myDecks);
                ban = oppBanDeck;
            }
        }
        
        Object[] finalResults = new Object[6];
        int i = 0;
        for(Deck deck : results)
        {
            myDecks[i] = deck;
            finalResults[i++] = deck;
        }
        finalResults[4] = ban.hearthstoneClass;
        finalResults[5] = resultWinrate;
        return finalResults;
    }
    
    public static Object[] maxVs(Deck[] myDecks, Deck[] oppDecks, Deck[] allDecks, Class oppBan)
    {
        HashMap<Class, HashMap<Integer,Deck>> setupMyDecks = new HashMap<Class,HashMap<Integer,Deck>>();
        for(Deck deck : allDecks)
        {
            HashMap<Integer, Deck> temp = new HashMap<Integer,Deck>();
            if(setupMyDecks.containsKey(deck.hearthstoneClass))
            {
                temp = setupMyDecks.get(deck.hearthstoneClass);
            }
            temp.put(deck.id, deck);
            setupMyDecks.put(deck.hearthstoneClass, temp);
        }
        
        Class myHearthstoneClass1 = myDecks[0].hearthstoneClass;
        Class myHearthstoneClass2 = myDecks[1].hearthstoneClass;
        Class myHearthstoneClass3 = myDecks[2].hearthstoneClass;
        Class myHearthstoneClass4 = myDecks[3].hearthstoneClass;
        
        Deck[] results = null;
        Deck ban = null;
        double resultWinrate = 0.0;
        
        //Loop thru your deck 1
        for(Deck myDeck1 : setupMyDecks.get(myHearthstoneClass1).values())
        {
            myDecks[0] = myDeck1;
            if(myDeck1.matchups == null || myDeck1.matchups.size() == 0)
                continue;
            //Loop thru opp deck 2
            for(Deck myDeck2 : setupMyDecks.get(myHearthstoneClass2).values())
            {
                myDecks[1] = myDeck2;
                if(myDeck2.matchups == null || myDeck2.matchups.size() == 0)
                    continue;
                //Loop thru opp deck 3
                for(Deck myDeck3 : setupMyDecks.get(myHearthstoneClass3).values())
                {
                    myDecks[2] = myDeck3;
                    if(myDeck3.matchups == null || myDeck3.matchups.size() == 0)
                        continue;
                    //Loop thru opp deck 4
                    for(Deck myDeck4 : setupMyDecks.get(myHearthstoneClass4).values())
                    {
                        myDecks[3] = myDeck4;
                        if(myDeck4.matchups == null || myDeck4.matchups.size() == 0)
                            continue;
                        //Loop thru my ban
                        for(Deck myBanDeck : oppDecks)
                        {
                            double winprob = calculateWinProb(myDecks, oppDecks, myBanDeck.hearthstoneClass, oppBan);
                            if(winprob > resultWinrate)
                            {
                                resultWinrate = winprob;
                                results = deepCopy(myDecks);
                                ban = myBanDeck;
                            }
                        }
                    }
                }
            }
        }
        Object[] finalResults = new Object[6];
        int i = 0;
        for(Deck deck : results)
        {
            myDecks[i] = deck;
            finalResults[i++] = deck;
        }
        finalResults[4] = ban.hearthstoneClass;
        finalResults[5] = resultWinrate;
        return finalResults;
    }
    
    //Go through all classes
    public static Object[] maxVs(Deck[] oppDecks, Deck[] allDecks, Class oppBan)
    {
        HashMap<Class, HashMap<Integer,Deck>> setupMyDecks = new HashMap<Class,HashMap<Integer,Deck>>();
        for(Deck deck : allDecks)
        {
            HashMap<Integer, Deck> temp = new HashMap<Integer,Deck>();
            if(setupMyDecks.containsKey(deck.hearthstoneClass))
            {
                temp = setupMyDecks.get(deck.hearthstoneClass);
            }
            temp.put(deck.id, deck);
            setupMyDecks.put(deck.hearthstoneClass, temp);
        }
        
        Deck[] myDecks = new Deck[4];
        myDecks[0] = new Deck();
        myDecks[1] = new Deck();
        myDecks[2] = new Deck();
        myDecks[3] = new Deck();
        
        Object[] finalResults = new Object[6];
        double maxWinrate = 0.0;
        int total = 0;
        int expectedLoops = (3*7*6);
        
        for(int a = 1; a <= 1; a++)
        {
            myDecks[0].hearthstoneClass = Class.getClassFromId(a);
            for(int b = 1; b <= 9; b++)
            {
                if(b <= a)
                    continue;
                myDecks[1].hearthstoneClass = Class.getClassFromId(b);
                for(int c = 1; c <= 9; c++)
                {
                    System.out.println(String.format("%.2f",((100*(double)total)/expectedLoops)) + "%");
                    if(c <= b)
                        continue;
                    myDecks[2].hearthstoneClass = Class.getClassFromId(c);
                    for(int d = 1; d <= 9; d++)
                    {
                        if(d <= c)
                            continue;
                        myDecks[3].hearthstoneClass = Class.getClassFromId(d);
                        double minWinrate = 100.0;
                        Object[] tempFinalResults = new Object[6];
                        for(Deck deck : myDecks)
                        {
                            total++;
                            Object[] results = maxVs(myDecks, oppDecks, allDecks, deck.hearthstoneClass);
                            double winrate = (double)results[5];
                            if(winrate < minWinrate)
                            {
                                minWinrate = winrate;
                                for(int i = 0; i < results.length; i++)
                                {
                                    tempFinalResults[i] = results[i];
                                }
                            }
                        }
                        if(minWinrate > maxWinrate)
                        {
                            for(int i = 0; i < tempFinalResults.length; i++)
                            {
                                finalResults[i] = tempFinalResults[i];
                            }
                        }
                    }
                }
            }
        }
        return finalResults;
    }
    
    //Ignore opp ban
    public static Object[] maxVs(Deck[] myDecks, Deck[] oppDecks, Deck[] allDecks)
    {
        return null;
    }
    
    //Go through all classes and ignore opponent ban
    public static Object[] maxVs(Deck[] oppDecks, Deck[] allDecks)
    {
        return null;
    }

    public static Object[] worstcase(Deck[] myDecks, Deck[] oppDecks, Deck[] allDecks, Class myBan)
    {
        HashMap<Class, HashMap<Integer,Deck>> setupMyDecks = new HashMap<Class,HashMap<Integer,Deck>>();
        for(Deck deck : allDecks)
        {
            HashMap<Integer, Deck> temp = new HashMap<Integer,Deck>();
            if(setupMyDecks.containsKey(deck.hearthstoneClass))
            {
                temp = setupMyDecks.get(deck.hearthstoneClass);
            }
            temp.put(deck.id, deck);
            setupMyDecks.put(deck.hearthstoneClass, temp);
        }
        
        Class oppHearthstoneClass1 = oppDecks[0].hearthstoneClass;
        Class oppHearthstoneClass2 = oppDecks[1].hearthstoneClass;
        Class oppHearthstoneClass3 = oppDecks[2].hearthstoneClass;
        Class oppHearthstoneClass4 = oppDecks[3].hearthstoneClass;
        
        Deck[] results = null;
        Deck ban = null;
        double resultWinrate = 100.0;
        int total = 0;
        
        //Loop thru your deck 1
        for(Deck oppDeck1 : setupMyDecks.get(oppHearthstoneClass1).values())
        {
            oppDecks[0] = oppDeck1;
            //Loop thru opp deck 2
            for(Deck oppDeck2 : setupMyDecks.get(oppHearthstoneClass2).values())
            {
                oppDecks[1] = oppDeck2;
                //Loop thru opp deck 3
                for(Deck oppDeck3 : setupMyDecks.get(oppHearthstoneClass3).values())
                {
                    oppDecks[2] = oppDeck3;
                    //Loop thru opp deck 4
                    for(Deck oppDeck4 : setupMyDecks.get(oppHearthstoneClass4).values())
                    {
                        oppDecks[3] = oppDeck4;
                        //Loop thru opp ban
                        for(Deck oppBanDeck : myDecks)
                        {
                            double winprob = calculateWinProb(myDecks, oppDecks, myBan, oppBanDeck.hearthstoneClass);
                            if(winprob < resultWinrate)
                            {
                                resultWinrate = winprob;
                                results = deepCopy(oppDecks);
                                ban = oppBanDeck;
                            }
                        }
                    }
                }
            }
        }
        Object[] finalResults = new Object[6];
        int i = 0;
        for(Deck deck : results)
        {
            oppDecks[i] = deck;
            finalResults[i++] = deck;
        }
        finalResults[4] = ban.hearthstoneClass;
        finalResults[5] = resultWinrate;
        return finalResults;
    }
    
    public static Object[] minmax(Deck[] myDecks, Deck[] oppDecks, Deck[] allDecks)
    {
        HashMap<Class, HashMap<Integer,Deck>> setupMyDecks = new HashMap<Class,HashMap<Integer,Deck>>();
        for(Deck deck : allDecks)
        {
            HashMap<Integer, Deck> temp = new HashMap<Integer,Deck>();
            if(setupMyDecks.containsKey(deck.hearthstoneClass))
            {
                temp = setupMyDecks.get(deck.hearthstoneClass);
            }
            temp.put(deck.id, deck);
            setupMyDecks.put(deck.hearthstoneClass, temp);
        }
        
        Class myHearthstoneClass1 = myDecks[0].hearthstoneClass;
        Class myHearthstoneClass2 = myDecks[1].hearthstoneClass;
        Class myHearthstoneClass3 = myDecks[2].hearthstoneClass;
        Class myHearthstoneClass4 = myDecks[3].hearthstoneClass;
        Class oppHearthstoneClass1 = oppDecks[0].hearthstoneClass;
        Class oppHearthstoneClass2 = oppDecks[1].hearthstoneClass;
        Class oppHearthstoneClass3 = oppDecks[2].hearthstoneClass;
        Class oppHearthstoneClass4 = oppDecks[3].hearthstoneClass;
        
        Deck[] results = null;
        Deck ban = null;
        double resultWinrate = -1.0;
        int total = 0;
        
        //Loop thru your deck 1
        for(Deck myDeck1 : setupMyDecks.get(myHearthstoneClass1).values())
        {
            myDecks[0] = myDeck1;
            //Loop thru your deck 2
            for(Deck myDeck2 : setupMyDecks.get(myHearthstoneClass2).values())
            {
                myDecks[1] = myDeck2;
                //Loop thru your deck 3
                
                for(Deck myDeck3 : setupMyDecks.get(myHearthstoneClass3).values())
                {
                    myDecks[2] = myDeck3;
                    out.println(++total);
                    //Loop thru your deck 4
                    for(Deck myDeck4 : setupMyDecks.get(myHearthstoneClass4).values())
                    {
                        myDecks[3] = myDeck4;
                        //Minmax begins
                        double min = 100.0;
                        Deck[] posResults = null;
                        Deck posBan = null;
                        //Loop thru opp deck 1
                        for(Deck oppDeck1 : setupMyDecks.get(oppHearthstoneClass1).values())
                        {
                            oppDecks[0] = oppDeck1;
                            //Loop thru opp deck 2
                            for(Deck oppDeck2 : setupMyDecks.get(oppHearthstoneClass2).values())
                            {
                                oppDecks[1] = oppDeck2;
                                //Loop thru opp deck 3
                                for(Deck oppDeck3 : setupMyDecks.get(oppHearthstoneClass3).values())
                                {
                                    oppDecks[2] = oppDeck3;
                                    //Loop thru opp deck 4
                                    for(Deck oppDeck4 : setupMyDecks.get(oppHearthstoneClass4).values())
                                    {
                                        oppDecks[3] = oppDeck4;
                                        //Loop thru your bans
                                        for(Deck myBanDeck : oppDecks)
                                        {
                                            //Loop thru opp ban
                                            for(Deck oppBanDeck : myDecks)
                                            {
                                                double winprob = calculateWinProb(myDecks, oppDecks, myBanDeck.hearthstoneClass, oppBanDeck.hearthstoneClass);
                                                if(winprob < min)
                                                {
                                                    min = winprob;
                                                    posResults = deepCopy(myDecks);
                                                    posBan = myBanDeck;
                                                }
                                            }
                                        }
                                            
                                    }
                                }
                            }
                        }
                        if(min > resultWinrate)
                        {
                            resultWinrate = min;
                            results = deepCopy(posResults);
                            ban = posBan;
                        }
                    }
                }
            }
        }
        Object[] finalResults = new Object[6];
        int i = 0;
        for(Deck deck : results)
        {
            finalResults[i++] = deck;
        }
        finalResults[4] = ban.hearthstoneClass;
        finalResults[5] = resultWinrate;
        return finalResults;
    }
    
    public static double calculateWinProb(Deck[] myDecks, Deck[] oppDecks, Class myBan, Class oppBan)
    {
        for(Deck deck : myDecks)
        {
            if(deck == null)
            {
                return -1.0;
            }
        }
        for(Deck deck : oppDecks)
        {
            if(deck == null)
            {
                return -1.0;
            }
        }
        
        double[][] matchups = getMatchups(myDecks, oppDecks, myBan, oppBan);
        
        int[] myDeckInts = {0,1,2};
        int[] opponentDeckInts = {0,1,2};
        
        //display(matchups);
        
        SelectionStrategy fullStrategy = winSeriesProb(matchups, 0, false, myDeckInts, opponentDeckInts);
        
        return fullStrategy.winrate;
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
        
        /*
        if(level == 0)
        {
            out.println("ROW PROBS");
            display(rowProbs);
        }
        */
        
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
        //out.println(display);
        
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
    
    private static double[][] getMatchups(Deck[] myDecks, Deck[] oppDecks, Class myBan, Class oppBan)
    {
        double[][] result = new double[3][3];
        int row = 0;
        int col = 0;
        for(int i = 0; i < 4; i++)
        {
            if(myDecks[i].hearthstoneClass.equals(oppBan))
            {
                continue;
            }
            col = 0;
            for(int j = 0; j < 4; j++)
            {
                if(oppDecks[j].hearthstoneClass.equals(myBan))
                {
                    continue;
                }
                result[row][col] = myDecks[i].matchups.get(oppDecks[j].id);
                col++;
            }
            row++;
        }
        return result;
    }
}
