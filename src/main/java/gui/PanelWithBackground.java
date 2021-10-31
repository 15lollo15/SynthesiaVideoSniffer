package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PanelWithBackground extends JPanel {

    private BufferedImage backgroundImage;

    public PanelWithBackground(BufferedImage backgroundImage) {
        super(null);
        this.backgroundImage = backgroundImage;
        this.setPreferredSize(new Dimension(backgroundImage.getWidth(), backgroundImage.getHeight()));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(backgroundImage, 0, 0, null);
        for (Component c : this.getComponents())
            c.repaint();
    }
}
