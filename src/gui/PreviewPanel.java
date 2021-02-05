package gui;

import images.ImageOperations;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PreviewPanel extends JPanel {
    private static final int NUMBER_OF_IMAGES = 4;
    private final GridLayout gridLayout;
    private BufferedImage[] images;

    public PreviewPanel() {
        setLayout(gridLayout = new GridLayout(2, 2, 5, 5));
        images = new BufferedImage[NUMBER_OF_IMAGES];
    }

    public void showPreview() {
        removeAll();
        int maxWidth = (getWidth() - gridLayout.getHgap()) / 2;
        int maxHeight = (getHeight() - gridLayout.getVgap()) / 2;
        for (BufferedImage image : images) {
            if (image == null) {
                continue;
            }
            image = ImageOperations.scaleImageToFit(image, maxWidth, maxHeight);
            add(new ImagePanel(image));
        }
        revalidate();
    }

    public void setImages(BufferedImage[] images) {
        if (images.length != 4) {
            return;
        }
        this.images = images;
    }
}
