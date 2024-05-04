/**
 * @file
 * @authors Martin Slezák (xsleza26)
 * @brief Token for the room configuration tokenization.
 */

package ija.robots.load;

/**
 * Token types enum used for loading room
 */
public enum Token {
    Ident,
    Number,
    OptStart,
    OptEnd,
    PosStart,
    PosEnd,
    X,
    Colon,
    Eof
}
