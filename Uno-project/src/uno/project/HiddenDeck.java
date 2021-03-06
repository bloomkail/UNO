/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uno.project;

import java.util.Stack;
import java.util.Collections;

/**
 *
 * @author Utilisateur
 */
public final class HiddenDeck
{

    private final CardButton topCardButton;
    private final Stack<Card> deck;
    private boolean revealed;

    public HiddenDeck(CardButton topCardButton)
    {
        this.topCardButton = topCardButton;
        this.deck = new Stack<>();
        this.revealed = false;

        Stack<Card> newDeck = new Stack<>();

        int id = 1;

        for (int i = 0; i < 4; ++i)
            newDeck.push(new WildCard(id));

        ++id;

        for (int i = 0; i < 4; ++i)
            newDeck.push(new WildDrawCard(id));

        char[] colors =
        {
            'r', 'g', 'b', 'y'
        };

        for (char i : colors)
        {
            newDeck.push(new NumberCard(++id, '0', i)); //Ajoute 1 carte 0
            for (int j = 0; j < 2; j++) //Ajoute la même carte 2x
            {
                for (char k = '1'; k < '9' + 1; k++)
                    newDeck.push(new NumberCard(++id, k, i)); //Ajoute une carte de chaque chiffre (hors 0)

                newDeck.push(new DrawCard(++id, i));
                newDeck.push(new ReverseCard(++id, i));
                newDeck.push(new SkipCard(++id, 's', i));

                if (j == 0)
                    id -= 12;
            }
        }
        setDeckShuffle(newDeck);
    }

    public void shuffle(Stack<Card> deck)
    {
        Collections.shuffle(deck);
    }

    public void setDeckShuffle(Stack<Card> deck)
    {
        shuffle(deck);
        this.deck.addAll(deck);
        topCardButton.setCard(this.deck.pop());
        revealed = false;
        setVisible(true);
    }

    public boolean isEmpty()
    {
        return topCardButton.getCard() == null;
    }

    public Card getTopCard()
    {
        Card top = topCardButton.getCard();

        if (!deck.isEmpty())
            topCardButton.setCard(deck.pop());
        else
        {
            topCardButton.setCard(null);
            setVisible(false);
        }
        revealed = false;

        return top;
    }

    public void setVisible(boolean visible)
    {
        topCardButton.setVisible(visible);
    }

    public void firstCard(Game g)
    {
        Card c;
        
        do
        {
            c = getTopCard();
            if(!(c instanceof WildDrawCard))
            {
                g.getActivePlayers().get(0).setRevealed(true);
                c.play(g);
            }
            else
                g.playCard(c);
            
        } while (c instanceof WildDrawCard);
    }

    void reveal()
    {
        topCardButton.setRevealed(true);
        this.revealed = true;
    }

    Card peek()
    {
        return topCardButton.getCard();
    }

    public int size()
    {
        return deck.size();
    }

    public CardButton getTopCardButton()
    {
        return topCardButton;
    }

    public boolean isRevealed()
    {
        return revealed;
    }
    
    public void setEnabled(boolean enabled)
    {
        topCardButton.setEnabled(enabled);
    }
}
