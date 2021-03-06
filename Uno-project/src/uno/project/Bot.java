/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uno.project;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author Utilisateur
 */
public class Bot extends Player
{

    public Bot(String name, ArrayList<BufferedImage[]> cardImages)
    {
        super(name, cardImages);
    }

    public char getPriorityColor()
    {
        int[] colors = {0,0,0,0};
        int priorityColor = 0;

        for (int i = 0; i < cards.size(); i++)
            switch (cards.get(i).getCard().getColor())
            {
                case 'r':
                    colors[0]++;
                    break;
                case 'g':
                    colors[1]++;
                    break;
                case 'b':
                    colors[2]++;
                    break;
                case 'y':
                    colors[3]++;
                    break;
                default:
                    break;
            }

        for (int i = 0; i < colors.length; i++)
            for (int j = 0; j < colors.length; j++)
                if (colors[i] >= colors[priorityColor])
                    priorityColor = i;

        switch (priorityColor)
        {
            case 0:
                return 'r';
            case 1:
                return 'g';
            case 2:
                return 'b';
            default:
                return 'y';
        }
    }

    public void turn(Game g)
    {
        boolean playedACard = false;
        char priorityColor = getPriorityColor();

        try
        {
            Thread.sleep(1200);
        } catch (InterruptedException e)
        {
        }

        outerloop:
        for (int step = 0; step < 4; step++)
            for (int i = 0; i < cards.size(); ++i)
                if (cards.get(i).getCard().canPlayOn(g.getRevealedTop()))
                    switch (step)
                    {
                        case 0:
                            if (cards.get(i).getCard() instanceof DrawCard || cards.get(i).getCard() instanceof SkipCard)
                            {
                                cards.get(i).getCard().play(g);
                                cards.remove(cards.get(i));
                                playedACard = true;
                                break outerloop;
                            }
                            break;
                        case 1:
                            if (cards.get(i).getCard().getColor() != 'd')
                                if (cards.get(i).getCard().getColor() == priorityColor)
                                {
                                    cards.get(i).getCard().play(g);
                                    cards.remove(cards.get(i));
                                    playedACard = true;
                                    break outerloop;
                                }
                            break;
                        case 2:
                            if (cards.get(i).getCard().getColor() != 'd')
                            {
                                cards.get(i).getCard().play(g);
                                cards.remove(cards.get(i));
                                playedACard = true;
                                break outerloop;
                            }
                            break;
                        case 3:
                            if (cards.get(i).getCard().getColor() == 'd')
                            {
                                if (cards.get(i).getCard() instanceof WildDrawCard)
                                    ((WildDrawCard) cards.get(i).getCard()).botPlay(g, priorityColor);
                                else if (cards.get(i).getCard() instanceof WildCard)
                                    ((WildCard) cards.get(i).getCard()).botPlay(g, priorityColor);
                                playedACard = true;

                                cards.remove(cards.get(i));
                                break outerloop;
                            }
                            break;
                        default:
                            break;
                    }

        if (!playedACard)
            draw(g);

        if (cards.isEmpty())
        {
            g.removePlayer();
            g.removeBot();
        }
    }

    public void draw(Game g)
    {
        Card c = g.getHiddenTop();
        char priorityColor = getPriorityColor();

        if (c.canPlayOn(g.getRevealedTop()))
            if (c instanceof WildCard)
                ((WildCard) c).botPlay(g, priorityColor);
            else if (c instanceof WildDrawCard)
                ((WildDrawCard) c).botPlay(g, priorityColor);
            else
                c.play(g);
        else
            addCard(c, g);

    }
}
