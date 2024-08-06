package com.now.tele.util;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class ButtonUtility {

	/**
	 * Single Button
	 * @return
	 */
    public static InlineKeyboardMarkup createInlineKeyboard(List<String> buttonLabels) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        
        for (String label : buttonLabels) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(label);
            button.setCallbackData(label);  // You can customize the callback data as needed
            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(button);
            rows.add(row);
        }
        
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }
    
    public static InlineKeyboardMarkup createMultiButtonRowInlineKeyboard(List<String> buttonLabels, int buttonsPerRow) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> currentRow = new ArrayList<>();

        for (String label : buttonLabels) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(label);
            button.setCallbackData(label);

            currentRow.add(button);
            if (currentRow.size() == buttonsPerRow) {
                rows.add(new ArrayList<>(currentRow));
                currentRow.clear();
            }
        }

        if (!currentRow.isEmpty()) {
            rows.add(currentRow);
        }

        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }
    
    /**
     * Which creates the customized buttons per row
     */
    public static InlineKeyboardMarkup createInlineKeyboard(List<String> buttonLabels, int initialButtonsPerRow, int subsequentButtonsPerRow) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> currentRow = new ArrayList<>();

        int count = 0;
        for (String label : buttonLabels) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(label);
            button.setCallbackData(label);

            currentRow.add(button);
            count++;

            int buttonsPerRow = count <= initialButtonsPerRow ? initialButtonsPerRow : subsequentButtonsPerRow;
            if (currentRow.size() == buttonsPerRow) {
                rows.add(new ArrayList<>(currentRow));
                currentRow.clear();
            }
        }

        if (!currentRow.isEmpty()) {
            rows.add(currentRow);
        }

        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

}

