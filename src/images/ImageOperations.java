package images;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

// A class that contains all basic image operations

public final class ImageOperations {
    private ImageOperations() {
    }

    public static BufferedImage resize(BufferedImage image, int scaledWidth, int scaledHeight) {
        BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight,
                image.getType() == BufferedImage.TYPE_CUSTOM ? BufferedImage.TYPE_INT_ARGB : image.getType());

        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(image, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        return outputImage;
    }

    public static BufferedImage resize(BufferedImage image, double percentage) {
        int scaledWidth = (int) (image.getWidth() * percentage);
        int scaledHeight = (int) (image.getHeight() * percentage);
        return resize(image, scaledWidth, scaledHeight);
    }

    public static BufferedImage scaleImageToFit(BufferedImage drawing, double maxWidth, double maxHeight) {
        double percentage;
        int width = drawing.getWidth();
        int height = drawing.getHeight();
        if (height > width) {
            percentage = maxHeight / height;
        } else {
            percentage = maxWidth / width;
        }
        drawing = ImageOperations.resize(drawing, percentage);
        return drawing;
    }

    public static void fill(BufferedImage image, Color color) {
        Graphics2D graphics2D = image.createGraphics();
        graphics2D.setPaint(color);
        graphics2D.fillRect(0, 0, image.getWidth(), image.getHeight());
    }

    public static BufferedImage copy(BufferedImage image) {
        ColorModel colorModel = image.getColorModel();
        boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
        WritableRaster raster = image.copyData(null);
        return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
    }
}
