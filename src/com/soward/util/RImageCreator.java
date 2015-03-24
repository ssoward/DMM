package com.soward.util;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class RImageCreator
{

    public RImageCreator()
    {
    }

    public Image getImage(int w, int h)
    {
        int s = w;
        if(h > w)
            s = h;
        im = new BufferedImage(s, s, 13);
        g = ((BufferedImage)im).createGraphics();
        return im;
    }

    public Graphics getGraphics()
    {
        return g;
    }

    private Image im;
    public Graphics g;
}
