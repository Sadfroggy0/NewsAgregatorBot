package timofey.keyboard;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import timofey.utils.Resources;

import java.util.ArrayList;
import java.util.List;


@Configuration
public class Keyboards{
    @Bean
    @Scope("singleton")
    @Qualifier("defaultMenuKeyboard")
    public InlineKeyboardMarkup defaultMenuKeyboard(){

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();

        inlineKeyboardButton1.setText("Reuters");
        inlineKeyboardButton1.setCallbackData(Resources.Reuters.name());

        inlineKeyboardButton2.setText("CNBC");
        inlineKeyboardButton2.setCallbackData(Resources.CNBC.name());

        inlineKeyboardButton3.setText("RBK");
        inlineKeyboardButton3.setCallbackData(Resources.RBK.name());

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();

        keyboardButtonsRow1.add(inlineKeyboardButton1);
        keyboardButtonsRow2.add(inlineKeyboardButton2);
        keyboardButtonsRow2.add(inlineKeyboardButton3);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>(
                List.of(
                        keyboardButtonsRow1, keyboardButtonsRow2
                )
        );
        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;

    }
    @Bean
    @Scope("singleton")
    @Qualifier("sourceMenuKeyboard")
    public ReplyKeyboardMarkup sourceMenuKeyboard(){

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardFirstRow.add("Business Latest News");
        keyboardFirstRow.add("Earnings Latest News");
        keyboardSecondRow.add("Finance Latest News");
        keyboardSecondRow.add("Auto-Mailing");
        keyboard.add(keyboardFirstRow);
        keyboard.add((keyboardSecondRow));

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;

    }

}
