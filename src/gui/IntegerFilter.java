package gui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

class IntegerFilter extends DocumentFilter {
    @Override
    public void insertString(FilterBypass filterBypass, int offset, String string,
                             AttributeSet attributes) throws BadLocationException {
        Document document = filterBypass.getDocument();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(document.getText(0, document.getLength()));
        stringBuilder.insert(offset, string);

        if (isInteger(stringBuilder.toString())) {
            super.insertString(filterBypass, offset, string, attributes);
        }
    }

    private boolean isInteger(String text) {
        if (text.isEmpty()) {
            return true;
        }
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    @Override
    public void replace(FilterBypass filterBypass, int offset, int length, String text,
                        AttributeSet attributes) throws BadLocationException {
        Document document = filterBypass.getDocument();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(document.getText(0, document.getLength()));
        stringBuilder.replace(offset, offset + length, text);

        if (isInteger(stringBuilder.toString())) {
            super.replace(filterBypass, offset, length, text, attributes);
        }
    }

    @Override
    public void remove(FilterBypass filterBypass, int offset, int length)
            throws BadLocationException {
        Document document = filterBypass.getDocument();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(document.getText(0, document.getLength()));
        stringBuilder.delete(offset, offset + length);

        if (isInteger(stringBuilder.toString())) {
            super.remove(filterBypass, offset, length);
        }
    }
}