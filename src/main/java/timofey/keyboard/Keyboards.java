package timofey.keyboard;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;


@Configuration
public class Keyboards{
    @Bean
    @Scope("prototype")
    @Qualifier("defaultMenuKeyboard")
    public InlineKeyboardMarkup defaultMenuKeyboard(){

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Reuters");
        inlineKeyboardButton1.setCallbackData("Button \"Тык\" has been pressed");

        inlineKeyboardButton2.setText("CNBC");
        inlineKeyboardButton2.setCallbackData("Button \"Тык2\" has been pressed");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();

        keyboardButtonsRow1.add(inlineKeyboardButton1);
        keyboardButtonsRow2.add(inlineKeyboardButton2);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>(
                List.of(
                        keyboardButtonsRow1, keyboardButtonsRow2
                )
        );
//            rowList.add(keyboardButtonsRow1);
//            rowList.add(keyboardButtonsRow2);
        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;

    }


}