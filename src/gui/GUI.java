package gui;

import cards.Cards;
import images.ImageOperations;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class GUI extends JFrame {
    private static final String APP_NAME = "Cards Generator";
    private final ParametersPanel parametersPanel;
    private final PreviewPanel previewPanel;
    private final BufferedImage[] charactersImages;
    private final BufferedImage[] symbolsImages;
    private final BufferedImage[] drawingsImages;
    private boolean readyForGeneration;

    public GUI() {
        super(APP_NAME);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1200, 600);
        setLayout(new GridLayout(1, 2));
        centerFrame();

        readyForGeneration = false;
        charactersImages = new BufferedImage[26];
        symbolsImages = new BufferedImage[4];
        drawingsImages = new BufferedImage[4];

        parametersPanel = new ParametersPanel();
        putParameterPanels();
        add(parametersPanel);
        autoAssign();

        JPanel rightSidePanel = new JPanel(new BorderLayout());

        previewPanel = new PreviewPanel();
        rightSidePanel.add(previewPanel, BorderLayout.CENTER);

        JPanel operationsPanel = new JPanel(new GridLayout(1, 0));

        JButton autoAssignButton = new JButton("Auto assign");
        autoAssignButton.addActionListener(event -> autoAssign());
        operationsPanel.add(autoAssignButton);

        JButton showPreviewButton = new JButton("Show preview");
        showPreviewButton.addActionListener(event -> showPreview());
        operationsPanel.add(showPreviewButton);

        JButton generateCardsButton = new JButton("Generate cards");
        generateCardsButton.addActionListener(event -> generateCards());
        operationsPanel.add(generateCardsButton);

        rightSidePanel.add(operationsPanel, BorderLayout.PAGE_END);
        add(rightSidePanel);

        setVisible(true);
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        requestFocus();
        readImages();
        showPreview();
    }

    private void putParameterPanels() {
        parametersPanel.add(new ParameterPanel("Card width") {
            @Override
            public void assignValue() {
                Cards.setCardWidth(this.getValue());
            }

            @Override
            public void autoAssign() {
                this.setValue(Cards.getCardWidth());
            }
        });
        parametersPanel.add(new ParameterPanel("Card height") {
            @Override
            public void assignValue() {
                Cards.setCardHeight(this.getValue());
            }

            @Override
            public void autoAssign() {
                this.setValue(Cards.getCardHeight());
            }
        });
        parametersPanel.add(new ParameterPanel("Card's outer horizontal padding") {
            @Override
            public void assignValue() {
                Cards.setCardOuterHorizontalPadding(this.getValue());
            }

            @Override
            public void autoAssign() {
                this.setValue(Cards.getCardOuterHorizontalPadding());
            }
        });
        parametersPanel.add(new ParameterPanel("Card's outer vertical padding") {
            @Override
            public void assignValue() {
                Cards.setCardOuterVerticalPadding(this.getValue());
            }

            @Override
            public void autoAssign() {
                this.setValue(Cards.getCardOuterVerticalPadding());
            }
        });
        parametersPanel.add(new ParameterPanel("Card's inner horizontal padding") {
            @Override
            public void assignValue() {
                Cards.setCardInnerHorizontalPadding(this.getValue());
            }

            @Override
            public void autoAssign() {
                this.setValue(Cards.getCardInnerHorizontalPadding());
            }
        });
        parametersPanel.add(new ParameterPanel("Card's inner vertical padding") {
            @Override
            public void assignValue() {
                Cards.setCardInnerVerticalPadding(this.getValue());
            }

            @Override
            public void autoAssign() {
                this.setValue(Cards.getCardInnerVerticalPadding());
            }
        });
        parametersPanel.add(new ParameterPanel("Symbol-character gap") {
            @Override
            public void assignValue() {
                Cards.setSymbolCharacterGap(this.getValue());
            }

            @Override
            public void autoAssign() {
                this.setValue(Cards.getSymbolCharacterGap());
            }
        });
        parametersPanel.add(new ParameterPanel("Drawing-character gap") {
            @Override
            public void assignValue() {
                Cards.setDrawingCharacterGap(this.getValue());
            }

            @Override
            public void autoAssign() {
                this.setValue(Cards.getDrawingCharacterGap());
            }
        });
        parametersPanel.add(new ParameterPanel("Symbols' horizontal gap percentage (%)") {
            @Override
            public void assignValue() {
                Cards.setSymbolsHorizontalGapPercentage(this.getValue() / 100D);
            }

            @Override
            public void autoAssign() {
                this.setValue((int) (100 * Cards.getSymbolsHorizontalGapPercentage()));
            }
        });
        parametersPanel.add(new ParameterPanel("Symbols' vertical gap percentage (%)") {
            @Override
            public void assignValue() {
                Cards.setSymbolsVerticalGapPercentage(this.getValue() / 100D);
            }

            @Override
            public void autoAssign() {
                this.setValue((int) (100 * Cards.getSymbolsVerticalGapPercentage()));
            }
        });
    }

    private void centerFrame() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dimension.width / 2 - getSize().width / 2, dimension.height / 2 - getSize().height / 2);
    }

    private void readImages() {
        try {
            File charactersDirectory = new File("Characters");
            File symbolsDirectory = new File("Symbols");
            File drawingsDirectory = new File("Drawings");
            if (!(assignImageFiles(charactersDirectory.listFiles(), charactersImages) &&
                    assignImageFiles(symbolsDirectory.listFiles(), symbolsImages) &&
                    assignImageFiles(drawingsDirectory.listFiles(), drawingsImages))) {
                return;
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        ArrayList<BufferedImage> tempArray = new ArrayList<>(Arrays.asList(charactersImages));
        tempArray.add(17, tempArray.remove(0));
        tempArray.add(17, tempArray.remove(0));
        tempArray.toArray(charactersImages);
        readyForGeneration = true;
    }

    private boolean assignImageFiles(File[] imageFiles, BufferedImage[] imageArray) throws IOException {
        if (imageFiles == null || imageFiles.length != imageArray.length) {
            return false;
        }
        for (int i = 0; i < imageFiles.length; i++) {
            imageArray[i] = ImageIO.read(imageFiles[i]);
            System.out.println(imageFiles[i].getName());
        }
        return true;
    }

    private void generateCards() {
        if (!readyForGeneration) {
            JOptionPane.showMessageDialog(this, "Files are not available or are incomplete",
                    "Cards generation is unavailable", JOptionPane.ERROR_MESSAGE);
            return;
        }
        File outputFolder = new File("Output");
        if (!outputFolder.exists()) {
            if (!outputFolder.mkdir()) {
                showIOErrorMessage();
                return;
            }
        }
        int cardNumber = 1;
        for (int i = 0; i < symbolsImages.length; i++) {
            int characterIndex;
            switch (i) {
                case 0:
                case 3:
                    characterIndex = 0;
                    break;
                case 1:
                case 2:
                    characterIndex = 1;
                    break;
                default:
                    return;
            }
            int drawingIndex = 0;
            for (int n = 0; n < 13; n++) {
                BufferedImage card;
                if (n < 9) {
                    card = Cards.generateCardWithSymbols(charactersImages[characterIndex], symbolsImages[i],
                            n + 2);
                } else {
                    card = Cards.generateCardWithDrawing(charactersImages[characterIndex], symbolsImages[i],
                            drawingsImages[drawingIndex]);
                    drawingIndex++;
                }
                try {
                    writeImage(card, String.valueOf(cardNumber++));
                } catch (IOException exception) {
                    showIOErrorMessage();
                    return;
                }
                characterIndex += 2;
            }
        }
        showOperationFinishedMessage();
    }

    private void writeImage(BufferedImage image, String name) throws IOException {
        ImageIO.write(image, "png", new File("Output\\" + name + ".png"));
    }

    private void showPreview() {
        parametersPanel.assignValues();

        BufferedImage character;
        BufferedImage symbol;
        BufferedImage card;

        character = new BufferedImage(130, 200, BufferedImage.TYPE_INT_ARGB);
        symbol = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        ImageOperations.fill(character, Color.BLUE);
        ImageOperations.fill(symbol, Color.RED);

        card = Cards.generateCardWithSymbols(character, symbol, 10);

        BufferedImage[] previewImages;
        if (readyForGeneration) {
            BufferedImage threeOfClubs = Cards.generateCardWithSymbols(charactersImages[2],
                    symbolsImages[0], 3);
            BufferedImage tenOfHearts = Cards.generateCardWithSymbols(charactersImages[17]
                    , symbolsImages[2], 10);
            BufferedImage kingOfSpades = Cards.generateCardWithDrawing(charactersImages[22]
                    , symbolsImages[3], drawingsImages[2]);
            previewImages = new BufferedImage[]{card, threeOfClubs, tenOfHearts, kingOfSpades};
        } else {
            previewImages = new BufferedImage[]{card, card, card, card};
        }

        previewPanel.setImages(previewImages);
        previewPanel.showPreview();
    }

    private void autoAssign() {
        Cards.autoAssign();
        parametersPanel.autoAssignAll();
    }

    private void showOperationFinishedMessage() {
        JOptionPane.showMessageDialog(this, "Operation finished", "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showIOErrorMessage() {
        JOptionPane.showMessageDialog(this, "Error occurred while trying to create folder" +
                        " or writing image to folder",
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}
