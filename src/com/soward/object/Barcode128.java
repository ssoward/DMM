package com.soward.object;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.Calendar;

public class Barcode128 extends Barcode {

    /** The bars to generate the code.

     */

    static byte BARS[][] = { { 2, 1, 2, 2, 2, 2 }, { 
    2, 2, 2, 1, 2, 2 }, { 2, 2, 2, 2, 2, 1 }, { 1, 2, 1, 2, 2, 3 }, { 1, 2, 1, 3, 2, 2 }, { 1, 3, 1, 2, 2, 2 }, { 
    1, 2, 2, 2, 1, 3 }, { 1, 2, 2, 3, 1, 2 }, { 1, 3, 2, 2, 1, 2 }, { 2, 2, 1, 2, 1, 3 }, { 2, 2, 1, 3, 1, 2 }, { 
    2, 3, 1, 2, 1, 2 }, { 1, 1, 2, 2, 3, 2 }, { 1, 2, 2, 1, 3, 2 }, { 1, 2, 2, 2, 3, 1 }, { 1, 1, 3, 2, 2, 2 }, { 
    1, 2, 3, 1, 2, 2 }, { 1, 2, 3, 2, 2, 1 }, { 2, 2, 3, 2, 1, 1 }, { 2, 2, 1, 1, 3, 2 }, { 2, 2, 1, 2, 3, 1 }, { 
    2, 1, 3, 2, 1, 2 }, { 2, 2, 3, 1, 1, 2 }, { 3, 1, 2, 1, 3, 1 }, { 3, 1, 1, 2, 2, 2 }, { 3, 2, 1, 1, 2, 2 }, { 
    3, 2, 1, 2, 2, 1 }, { 3, 1, 2, 2, 1, 2 }, { 3, 2, 2, 1, 1, 2 }, { 3, 2, 2, 2, 1, 1 }, { 2, 1, 2, 1, 2, 3 }, { 
    2, 1, 2, 3, 2, 1 }, { 2, 3, 2, 1, 2, 1 }, { 1, 1, 1, 3, 2, 3 }, { 1, 3, 1, 1, 2, 3 }, { 1, 3, 1, 3, 2, 1 }, { 
    1, 1, 2, 3, 1, 3 }, { 1, 3, 2, 1, 1, 3 }, { 1, 3, 2, 3, 1, 1 }, { 2, 1, 1, 3, 1, 3 }, { 2, 3, 1, 1, 1, 3 }, { 
    2, 3, 1, 3, 1, 1 }, { 1, 1, 2, 1, 3, 3 }, { 1, 1, 2, 3, 3, 1 }, { 1, 3, 2, 1, 3, 1 }, { 1, 1, 3, 1, 2, 3 }, { 
    1, 1, 3, 3, 2, 1 }, { 1, 3, 3, 1, 2, 1 }, { 3, 1, 3, 1, 2, 1 }, { 2, 1, 1, 3, 3, 1 }, { 2, 3, 1, 1, 3, 1 }, { 
    2, 1, 3, 1, 1, 3 }, { 2, 1, 3, 3, 1, 1 }, { 2, 1, 3, 1, 3, 1 }, { 3, 1, 1, 1, 2, 3 }, { 3, 1, 1, 3, 2, 1 }, { 
    3, 3, 1, 1, 2, 1 }, { 3, 1, 2, 1, 1, 3 }, { 3, 1, 2, 3, 1, 1 }, { 3, 3, 2, 1, 1, 1 }, { 3, 1, 4, 1, 1, 1 }, { 
    2, 2, 1, 4, 1, 1 }, { 4, 3, 1, 1, 1, 1 }, { 1, 1, 1, 2, 2, 4 }, { 1, 1, 1, 4, 2, 2 }, { 1, 2, 1, 1, 2, 4 }, { 
    1, 2, 1, 4, 2, 1 }, { 1, 4, 1, 1, 2, 2 }, { 1, 4, 1, 2, 2, 1 }, { 1, 1, 2, 2, 1, 4 }, { 1, 1, 2, 4, 1, 2 }, { 
    1, 2, 2, 1, 1, 4 }, { 1, 2, 2, 4, 1, 1 }, { 1, 4, 2, 1, 1, 2 }, { 1, 4, 2, 2, 1, 1 }, { 2, 4, 1, 2, 1, 1 }, { 
    2, 2, 1, 1, 1, 4 }, { 4, 1, 3, 1, 1, 1 }, { 2, 4, 1, 1, 1, 2 }, { 1, 3, 4, 1, 1, 1 }, { 1, 1, 1, 2, 4, 2 }, { 
    1, 2, 1, 1, 4, 2 }, { 1, 2, 1, 2, 4, 1 }, { 1, 1, 4, 2, 1, 2 }, { 1, 2, 4, 1, 1, 2 }, { 1, 2, 4, 2, 1, 1 }, { 
    4, 1, 1, 2, 1, 2 }, { 4, 2, 1, 1, 1, 2 }, { 4, 2, 1, 2, 1, 1 }, { 2, 1, 2, 1, 4, 1 }, { 2, 1, 4, 1, 2, 1 }, { 
    4, 1, 2, 1, 2, 1 }, { 1, 1, 1, 1, 4, 3 }, { 1, 1, 1, 3, 4, 1 }, { 1, 3, 1, 1, 4, 1 }, { 1, 1, 4, 1, 1, 3 }, { 
    1, 1, 4, 3, 1, 1 }, { 4, 1, 1, 1, 1, 3 }, { 4, 1, 1, 3, 1, 1 }, { 1, 1, 3, 1, 4, 1 }, { 1, 1, 4, 1, 3, 1 }, { 
    3, 1, 1, 1, 4, 1 }, { 4, 1, 1, 1, 3, 1 }, { 2, 1, 1, 4, 1, 2 }, { 2, 1, 1, 2, 1, 4 }, { 2, 1, 1, 2, 3, 2 } };

    /** The stop bars.
     */

    static byte BARS_STOP[] = { 2, 3, 3, 1, 1, 1, 2 };

    /** The charset code change.
     */

    public static final char CODE_AB_TO_C = 99;

    /** The charset code change.
     */

    public static final char CODE_AC_TO_B = 100;

    /** The charset code change.
     */

    public static final char CODE_BC_TO_A = 101;

    /** The code for UCC/EAN-128.
     */

    public static final char FNC1 = 102;

    /** The start code.
     */

    public static final char START_A = 103;

    /** The start code.

     */

    public static final char START_B = 104;

    /** The start code.
     */

    public static final char START_C = 105;

    /** Creates new Barcode128 */

    public Barcode128() {
        x = 1;
        setFont( new Font( "Helvetica", Font.PLAIN, 20 ) );
        textAlignment = Barcode.ALIGN_CENTER;
        codeType = CODE128;
        quietZone = true;
        quietZoneX = 10;
    }

    /** Gets the maximum width that the barcode will occupy.
     *  The lower left corner is always (0, 0).
     * @return the size the barcode occupies.
     */

    public float getBarcodeWidth() {
        String fullCode;
        if ( codeType == CODE128_RAW ) {
            int idx = code.indexOf( '\uffff' );
            if ( idx >= 0 )
                fullCode = code.substring( 0, idx );
            else
                fullCode = code;
        } else {
            fullCode = getRawText( code, codeType == CODE128_UCC );
        }
        int len = fullCode.length();
        float fullWidth = ( len + 2 ) * 11 * x + 2 * x;
        int quietZone = 0;
        if ( isQuietZone() ) {
            quietZone = Math.round( quietZoneX * x );
            fullWidth = fullWidth + ( quietZoneX * 2 * x );
        }
        return fullWidth;
    }

    /** Generates the bars. The input has the actual barcodes, not
     * the human readable text.
     * @param text the barcode
     * @return the bars
     */
    public static byte[] getBarsCode128Raw( String text ) {
        int idx = text.indexOf( '\uffff' );
        if ( idx >= 0 )
            text = text.substring( 0, idx );
        int chk = text.charAt( 0 );
        for ( int k = 1; k < text.length(); ++k )
            chk += k * text.charAt( k );
        chk = chk % 103;
        text += (char) chk;
        byte bars[] = new byte[( text.length() + 1 ) * 6 + 7];
        int k;
        for ( k = 0; k < text.length(); ++k )
            System.arraycopy( BARS[text.charAt( k )], 0, bars, k * 6, 6 );
        System.arraycopy( BARS_STOP, 0, bars, k * 6, 7 );
        return bars;
    }

    /** Packs the digits for charset C. It assumes that all the parameters are valid.
     * @param text the text to pack
     * @param textIndex where to pack from
     * @param numDigits the number of digits to pack. It is always an even number
     * @return the packed digits, two digits per character
     */
    static String getPackedRawDigits(
    String text,
    int textIndex,
    int numDigits ) {
        String out = "";
        while ( numDigits > 0 ) {
            numDigits -= 2;
            int c1 = text.charAt( textIndex++ ) - '0';
            int c2 = text.charAt( textIndex++ ) - '0';
            out += (char) ( c1 * 10 + c2 );
        }
        return out;
    }

    /** Converts the human readable text to the characters needed to
     * create a barcode. Some optimization is done to get the shortest code.
     * @param text the text to convert
     * @param ucc <CODE>true</CODE> if it is an UCC/EAN-128. In this case
     * the character FNC1 is added
     * @return the code ready to be fed to getBarsCode128Raw()
     */
    public static String getRawText( String text, boolean ucc ) {
        String out = "";
        int tLen = text.length();
        if ( tLen == 0 ) {
            out += START_B;
            if ( ucc )
                out += FNC1;
            return out;
        }
        int c = 0;
        for ( int k = 0; k < tLen; ++k ) {
            c = text.charAt( k );
            if ( c > 127 )
                throw new RuntimeException( "There are illegal characters for barcode 128 in '" + text + "'." );
        }
        c = text.charAt( 0 );
        char currentCode = START_B;
        int index = 0;
        if ( isNextDigits( text, index, 2 ) ) {
            currentCode = START_C;
            out += currentCode;
            if ( ucc )
                out += FNC1;
            out += getPackedRawDigits( text, index, 2 );
            index += 2;
        } else if ( c < ' ' ) {
            currentCode = START_A;
            out += currentCode;
            if ( ucc )
                out += FNC1;
            out += (char) ( c + 64 );
            ++index;
        } else {
            out += currentCode;
            if ( ucc )
                out += FNC1;
            out += (char) ( c - ' ' );
            ++index;
        }
        while ( index < tLen ) {
            switch ( currentCode ) {
                case START_A:
                {
                    if ( isNextDigits( text, index, 4 ) ) {
                        currentCode = START_C;
                        out += CODE_AB_TO_C;
                        out += getPackedRawDigits( text, index, 4 );
                        index += 4;
                    } else {
                        c = text.charAt( index++ );
                        if ( c > '_' ) {
                            currentCode = START_B;
                            out += CODE_AC_TO_B;
                            out += (char) ( c - ' ' );
                        } else if ( c < ' ' )
                            out += (char) ( c + 64 );
                        else
                            out += (char) ( c - ' ' );
                    }
                }
                    break;
                case START_B:
                {
                    if ( isNextDigits( text, index, 4 ) ) {
                        currentCode = START_C;
                        out += CODE_AB_TO_C;
                        out += getPackedRawDigits( text, index, 4 );
                        index += 4;
                    } else {
                        c = text.charAt( index++ );
                        if ( c < ' ' ) {
                            currentCode = START_A;
                            out += CODE_BC_TO_A;
                            out += (char) ( c + 64 );
                        } else {
                            out += (char) ( c - ' ' );
                        }
                    }
                }
                    break;
                case START_C:
                {
                    if ( isNextDigits( text, index, 2 ) ) {
                        out += getPackedRawDigits( text, index, 2 );
                        index += 2;
                    } else {
                        c = text.charAt( index++ );
                        if ( c < ' ' ) {
                            currentCode = START_A;
                            out += CODE_BC_TO_A;
                            out += (char) ( c + 64 );
                        } else {
                            currentCode = START_B;
                            out += CODE_AC_TO_B;
                            out += (char) ( c - ' ' );
                        }
                    }
                }
                    break;
            }
        }
        return out;
    }

    /** Returns <CODE>true</CODE> if the next <CODE>numDigits</CODE>
     * starting from index <CODE>textIndex</CODE> are numeric.
     * @param text the text to check
     * @param textIndex where to check from
     * @param numDigits the number of digits to check
     * @return the check result
     */
    static boolean isNextDigits( String text, int textIndex, int numDigits ) {
        if ( textIndex + numDigits > text.length() )
            return false;
        while ( numDigits-- > 0 ) {
            char c = text.charAt( textIndex++ );
            if ( c < '0' || c > '9' )
                return false;
        }
        return true;
    }
    public void placeBarcode(java.awt.image.BufferedImage i,Color barColor,Color textColor, Product prod ) {
        String fullCode = code;
        Graphics2D g = (Graphics2D) i.getGraphics();
        int imageX = i.getWidth()/2;
        int imageY = i.getHeight()/2;
        int fontHeight = 0;
        int descent = 0;
        if ( codeType == CODE128_RAW ) {
            int idx = code.indexOf( '\uffff' );
            if ( idx < 0 )
                fullCode = "";
            else
                fullCode = code.substring( idx + 1 );
        }
        float fontX = 0;
        float fontY = 0;
        if ( isShowText() && getFont() != null ) {
            g.setFont( getFont() );
            FontMetrics metrics = g.getFontMetrics();
            fontX = metrics.stringWidth( fullCode );
            fontY = imageY - metrics.getMaxDescent();
            fontHeight =
            metrics.getMaxAscent()
            + metrics.getMaxDescent()
            + metrics.getLeading();
            descent = metrics.getMaxDescent();
        }
        barHeight = imageY - fontHeight-5;
        String bCode;
        if ( codeType == CODE128_RAW ) {
            int idx = code.indexOf( '\uffff' );
            if ( idx >= 0 )
                bCode = code.substring( 0, idx );
            else
                bCode = code;
        } else {
            bCode = getRawText( code, codeType == CODE128_UCC );
        }
        int len = bCode.length();
        //bcode width
        len+=-3;
        float fullWidth = ( len + 2 ) * 11 * x + 2 * x;
        float barStartX = 0;
        float textStartX = 0;
        switch ( textAlignment ) {
            case Barcode.ALIGN_LEFT:
                break;
            case Barcode.ALIGN_RIGHT:
                textStartX = imageX - fontX;
                break;
            default:
                textStartX = ( imageX - fontX ) / 2;
                break;
        }
        //barCode Y coordinate
        float barStartY = 20;
        byte bars[] = getBarsCode128Raw( bCode );
        boolean print = true;
        if ( barColor != null )
            g.setPaint( barColor );
        int quietZone = 0;
        if ( isQuietZone() ) {
            fullWidth = fullWidth + ( quietZoneX * 2 * x );
            quietZone = scale( imageX, fullWidth, quietZoneX * x );
        }
        for ( int k = 0; k < bars.length; ++k ) {
            float w = ( bars[k] * x );
            if ( print )
                g.fill(
                new java.awt.Rectangle(
                quietZone + scale( imageX, fullWidth, barStartX ),
                scale( imageY, barHeight, barStartY ),
                scale( imageX, fullWidth, w ),
                Math.round( barHeight ) ) );
            print = !print;
            barStartX += w;
        }
        float textStartY = barStartY+barHeight +25 ;
        float wwwStartX = 5;
        Calendar now = Calendar.getInstance();
        float wwwStartY = textStartY+12;
        if ( isShowText() && getFont() != null ) {
            if ( textColor != null )
                g.setPaint( textColor );
            g.drawString( fullCode, textStartX, textStartY );
            Font font = null;
            
            g.drawString( prod.getProductName(), wwwStartX, 10 );
            g.drawString( prod.getProductCatalogNum()+ "  "+now.get( Calendar.YEAR ), wwwStartX, 20 );
            g.drawString( "$"+prod.getProductCost1(), i.getWidth()-60, barStartY+10 );
            g.drawString( "Day", i.getWidth()-60, barStartY+25 );
            g.drawString( "Murray", i.getWidth()-60, barStartY+35 );
            g.drawString( "Music", i.getWidth()-60, barStartY+45 );
            font = new Font( getFont().getName(), getFont().getStyle(), 11 );
            g.setFont( font );
            g.drawString( "www.daymurraymusic.com", wwwStartX, wwwStartY );
        }
        return;
    }
}
