package cards;

import images.ImageOperations;

import java.awt.*;
import java.awt.image.BufferedImage;

public final class Cards {
    private static final int[][] DISTRIBUTION = new int[][]{
            new int[]{12},
            new int[]{1, -8},
            new int[]{1, -8, 12},
            new int[]{0, 2, -7, -9},
            new int[]{0, 2, -7, -9, 12},
            new int[]{0, 2, -7, -9, 11, 13},
            new int[]{0, 2, -7, -9, 10, 11, 13},
            new int[]{0, 2, -7, -9, 10, 11, 13, -14},
            new int[]{0, 2, 3, 4, -5, -6, -7, -9, 12},
            new int[]{0, 2, 3, 4, -5, -6, -7, -9, 10, -14},
    };
    private static boolean assigned = false;
    private static int cardWidth;
    private static int cardHeight;
    private static int cardOuterVerticalPadding;
    private static int cardOuterHorizontalPadding;
    private static int cardInnerVerticalPadding;
    private static int cardInnerHorizontalPadding;
    private static int symbolCharacterGap;
    private static int drawingCharacterGap;
    private static double symbolsHorizontalGapPercentage;
    private static double symbolsVerticalGapPercentage;
    private static BufferedImage template;

    private Cards() {

    }

    public static BufferedImage generateCardWithSymbols(BufferedImage characterImage, BufferedImage symbolImage, int number) {
        BufferedImage symbolImageCopy = ImageOperations.copy(symbolImage);
        generateTemplate(characterImage, symbolImageCopy);

        int fullWidth = cardWidth - (2 * (cardInnerHorizontalPadding + drawingCharacterGap));
        int horizontalGapWidth = (int) Math.floor(fullWidth * (symbolsHorizontalGapPercentage / 2));
        int scaledWidth = (fullWidth - (2 * horizontalGapWidth)) / 3;

        double percentage = (double) scaledWidth / symbolImage.getWidth();

        int fullHeight = cardHeight - (2 * cardInnerVerticalPadding);
        int verticalGapHeight = (int) Math.floor(fullHeight * (symbolsVerticalGapPercentage / 3));
        int scaledHeight = (int) Math.min((double) (fullHeight - (3 * verticalGapHeight)) / 4,
                symbolImage.getHeight() * percentage);

        symbolImage = ImageOperations.resize(symbolImage, scaledWidth, scaledHeight);

        for (int position : DISTRIBUTION[number - 1]) {
            drawSymbolAt(symbolImage, position);
        }

        return template;
    }

    public static BufferedImage generateCardWithDrawing(BufferedImage characterImage, BufferedImage symbolImage,
                                                        BufferedImage drawing) {
        generateTemplate(characterImage, symbolImage);

        int maxWidth = cardWidth - (2 * (cardInnerHorizontalPadding + drawingCharacterGap));
        int maxHeight = cardHeight - (2 * cardInnerVerticalPadding);
        drawing = ImageOperations.scaleImageToFit(drawing, maxWidth, maxHeight);

        int x = cardInnerHorizontalPadding + drawingCharacterGap + ((maxWidth - drawing.getWidth()) / 2);
        int y = cardInnerVerticalPadding + ((maxHeight - drawing.getHeight()) / 2);
        drawImageAt(drawing, x, y);

        return template;
    }

    private static void generateTemplate(BufferedImage characterImage, BufferedImage symbolImage) {
        if (!assigned) {
            autoAssign();
        }

        double percentage;
        percentage = (double) (cardInnerHorizontalPadding - cardOuterHorizontalPadding) / characterImage.getWidth();
        characterImage = ImageOperations.resize(characterImage, percentage);
        percentage = (double) (cardInnerHorizontalPadding - cardOuterHorizontalPadding) / symbolImage.getWidth();
        symbolImage = ImageOperations.resize(symbolImage, percentage);

        createEmptyCard();

        int x;
        int y;

        x = cardOuterHorizontalPadding;
        y = cardOuterVerticalPadding;
        drawImageAt(characterImage, x, y);
        x = cardWidth - cardOuterHorizontalPadding - characterImage.getWidth();
        y = cardHeight - cardOuterVerticalPadding - characterImage.getHeight();
        drawImageAt(flip(characterImage), x, y);

        x = cardOuterHorizontalPadding;
        y = cardOuterVerticalPadding + characterImage.getHeight() + symbolCharacterGap;
        drawImageAt(symbolImage, x, y);
        x = cardWidth - cardOuterHorizontalPadding - characterImage.getWidth();
        y = cardHeight - cardOuterVerticalPadding - characterImage.getHeight() - symbolCharacterGap - symbolImage.getHeight();
        drawImageAt(flip(symbolImage), x, y);
    }

    private static void drawSymbolAt(BufferedImage symbolImage, int position) {
        if (position < 0) {
            symbolImage = flip(symbolImage);
            position *= -1;
        }
        int fullWidth = cardWidth - (2 * (cardInnerHorizontalPadding + drawingCharacterGap));
        int fullHeight = cardHeight - (2 * cardInnerVerticalPadding);

        int x;
        int y;
        switch (position) {
            case 0:
                x = cardInnerHorizontalPadding + drawingCharacterGap;
                y = cardInnerVerticalPadding;
                break;
            case 1:
                x = cardInnerHorizontalPadding + drawingCharacterGap + (fullWidth / 2) - (symbolImage.getWidth() / 2);
                y = cardInnerVerticalPadding;
                break;
            case 2:
                x = cardWidth - cardInnerHorizontalPadding - drawingCharacterGap - symbolImage.getWidth();
                y = cardInnerVerticalPadding;
                break;
            case 3:
                x = cardInnerHorizontalPadding + drawingCharacterGap;
                y = cardInnerVerticalPadding + (fullHeight / 4) + (fullHeight / 8) - (symbolImage.getHeight() / 2);
                break;
            case 4:
                x = cardWidth - cardInnerHorizontalPadding - drawingCharacterGap - symbolImage.getWidth();
                y = cardInnerVerticalPadding + (fullHeight / 4) + (fullHeight / 8) - (symbolImage.getHeight() / 2);
                break;
            case 5:
                x = cardInnerHorizontalPadding + drawingCharacterGap;
                y = cardInnerVerticalPadding + (2 * fullHeight / 4) + (fullHeight / 8) - (symbolImage.getHeight() / 2);
                break;
            case 6:
                x = cardWidth - cardInnerHorizontalPadding - drawingCharacterGap - symbolImage.getWidth();
                y = cardInnerVerticalPadding + (2 * fullHeight / 4) + (fullHeight / 8) - (symbolImage.getHeight() / 2);
                break;
            case 7:
                x = cardInnerHorizontalPadding + drawingCharacterGap;
                y = cardHeight - (cardInnerVerticalPadding + symbolImage.getHeight());
                break;
            case 8:
                x = cardInnerHorizontalPadding + drawingCharacterGap + (fullWidth / 2) - (symbolImage.getWidth() / 2);
                y = cardHeight - (cardInnerVerticalPadding + symbolImage.getHeight());
                break;
            case 9:
                x = cardWidth - cardInnerHorizontalPadding - drawingCharacterGap - symbolImage.getWidth();
                y = cardHeight - (cardInnerVerticalPadding + symbolImage.getHeight());
                break;
            case 10:
                x = cardInnerHorizontalPadding + drawingCharacterGap + (fullWidth / 2) - (symbolImage.getWidth() / 2);
                y = cardInnerVerticalPadding + (fullHeight / 4) - (symbolImage.getHeight() / 2);
                break;
            case 11:
                x = cardInnerHorizontalPadding + drawingCharacterGap;
                y = cardInnerVerticalPadding + (fullHeight / 2) - (symbolImage.getHeight() / 2);
                break;
            case 12:
                x = cardInnerHorizontalPadding + drawingCharacterGap + (fullWidth / 2) - (symbolImage.getWidth() / 2);
                y = cardInnerVerticalPadding + (fullHeight / 2) - (symbolImage.getHeight() / 2);
                break;
            case 13:
                x = cardWidth - cardInnerHorizontalPadding - drawingCharacterGap - symbolImage.getWidth();
                y = cardInnerVerticalPadding + (fullHeight / 2) - (symbolImage.getHeight() / 2);
                break;
            case 14:
                x = cardInnerHorizontalPadding + drawingCharacterGap + (fullWidth / 2) - (symbolImage.getWidth() / 2);
                y = cardInnerVerticalPadding + (3 * fullHeight / 4) - (symbolImage.getHeight() / 2);
                break;
            default:
                return;
        }
        drawImageAt(symbolImage, x, y);
    }

    public static void autoAssign() {
        cardWidth = 822;
        cardHeight = 1122;
        cardOuterVerticalPadding = 50;
        cardOuterHorizontalPadding = 40;
        cardInnerVerticalPadding = 100;
        cardInnerHorizontalPadding = 80;
        symbolCharacterGap = 5;
        drawingCharacterGap = 10;
        symbolsHorizontalGapPercentage = 0.13;
        symbolsVerticalGapPercentage = 0.13;
        assigned = true;
    }

    private static void createEmptyCard() {
        template = new BufferedImage(cardWidth, cardHeight, BufferedImage.TYPE_INT_RGB);
        ImageOperations.fill(template, Color.WHITE);
    }

    private static void drawImageAt(BufferedImage imageToPaint, int x, int y) {
        template.createGraphics().drawImage(imageToPaint, x, y, null);
    }

    public static BufferedImage flip(BufferedImage image) {
        BufferedImage flippedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        flippedImage.createGraphics()
                .drawImage(image, image.getWidth(), image.getHeight(),
                        -image.getWidth(), -image.getHeight(), null);
        return flippedImage;
    }

    public static int getCardWidth() {
        return cardWidth;
    }

    public static void setCardWidth(int cardWidth) {
        Cards.cardWidth = cardWidth;
    }

    public static int getCardHeight() {
        return cardHeight;
    }

    public static void setCardHeight(int cardHeight) {
        Cards.cardHeight = cardHeight;
    }

    public static int getCardOuterVerticalPadding() {
        return cardOuterVerticalPadding;
    }

    public static void setCardOuterVerticalPadding(int cardOuterVerticalPadding) {
        Cards.cardOuterVerticalPadding = cardOuterVerticalPadding;
    }

    public static int getCardOuterHorizontalPadding() {
        return cardOuterHorizontalPadding;
    }

    public static void setCardOuterHorizontalPadding(int cardOuterHorizontalPadding) {
        Cards.cardOuterHorizontalPadding = cardOuterHorizontalPadding;
    }

    public static int getCardInnerVerticalPadding() {
        return cardInnerVerticalPadding;
    }

    public static void setCardInnerVerticalPadding(int cardInnerVerticalPadding) {
        Cards.cardInnerVerticalPadding = cardInnerVerticalPadding;
    }

    public static int getCardInnerHorizontalPadding() {
        return cardInnerHorizontalPadding;
    }

    public static void setCardInnerHorizontalPadding(int cardInnerHorizontalPadding) {
        Cards.cardInnerHorizontalPadding = cardInnerHorizontalPadding;
    }

    public static int getSymbolCharacterGap() {
        return symbolCharacterGap;
    }

    public static void setSymbolCharacterGap(int symbolCharacterGap) {
        Cards.symbolCharacterGap = symbolCharacterGap;
    }

    public static int getDrawingCharacterGap() {
        return drawingCharacterGap;
    }

    public static void setDrawingCharacterGap(int drawingCharacterGap) {
        Cards.drawingCharacterGap = drawingCharacterGap;
    }

    public static double getSymbolsHorizontalGapPercentage() {
        return symbolsHorizontalGapPercentage;
    }

    public static void setSymbolsHorizontalGapPercentage(double symbolsHorizontalGapPercentage) {
        Cards.symbolsHorizontalGapPercentage = symbolsHorizontalGapPercentage;
    }

    public static double getSymbolsVerticalGapPercentage() {
        return symbolsVerticalGapPercentage;
    }

    public static void setSymbolsVerticalGapPercentage(double symbolsVerticalGapPercentage) {
        Cards.symbolsVerticalGapPercentage = symbolsVerticalGapPercentage;
    }
}
