package timofey.keyboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import timofey.config.SourceInit;
import timofey.utils.enums.Commands;
import timofey.utils.enums.Resources;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Configuration
public class Keyboards{

    @Bean
    @Qualifier("baseKeyboard")
    @Scope("prototype")
    public InlineKeyboardMarkup getInlineKeyBoardMarkup(){
        return new InlineKeyboardMarkup();
    }
    @Bean
    public ReplyKeyboardMarkup replyKeyboardMarkup(){
        return new ReplyKeyboardMarkup();
    }
    @Autowired
    SourceInit sources;

    @Bean
    @Qualifier("defaultMenuKeyboard")
    public InlineKeyboardMarkup defaultMenuKeyboard(@Qualifier("baseKeyboard")InlineKeyboardMarkup inlineKeyboardMarkup){
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton4 = new InlineKeyboardButton();

        inlineKeyboardButton1.setText("Reuters");
        inlineKeyboardButton1.setCallbackData(Resources.Reuters.name());

        inlineKeyboardButton2.setText("CNBC");
        inlineKeyboardButton2.setCallbackData(Resources.CNBC.name());

        inlineKeyboardButton3.setText("RBK");
        inlineKeyboardButton3.setCallbackData(Resources.RBK.name());

        inlineKeyboardButton4.setText("Подписка на новости");
        inlineKeyboardButton4.setCallbackData(Commands.subscription.name());

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();

        keyboardButtonsRow1.add(inlineKeyboardButton1);
        keyboardButtonsRow2.add(inlineKeyboardButton2);
        keyboardButtonsRow2.add(inlineKeyboardButton3);
        keyboardButtonsRow3.add(inlineKeyboardButton4);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>(
                List.of(
                        keyboardButtonsRow1, keyboardButtonsRow2, keyboardButtonsRow3
                )
        );
        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;

    }
    @Bean
    @Qualifier("sourceMenuKeyboard")
    public ReplyKeyboardMarkup sourceMenuKeyboard(ReplyKeyboardMarkup replyKeyboardMarkup){

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
    @Bean
    @Qualifier("subscriptionMenu")
    public InlineKeyboardMarkup subscriptionMenu (@Qualifier("baseKeyboard")InlineKeyboardMarkup inlineKeyboardMarkup){

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Подписка на все последние новости");
        button1.setCallbackData(Commands.subscribe.name());

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("Выбрать подписку на конкретный источник");
        button2.setCallbackData(Commands.certainSourceSub.name());

        List<InlineKeyboardButton> row1 = new ArrayList<>(
                Arrays.asList(button1, button2)
        );
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(row1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
    @Bean
    @Qualifier("sourceMultipleChoice")
    public InlineKeyboardMarkup sourceMultipleChoice (@Qualifier("baseKeyboard") InlineKeyboardMarkup inlineKeyboardMarkup){

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Подписка на CNBC");
        button1.setCallbackData(Commands.cnbcSub.name());

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("Подписка на Reuters");
        button2.setCallbackData(Commands.reutersSub.name());

        List<InlineKeyboardButton> row1 = new ArrayList<>(
                Arrays.asList(button1, button2)
        );

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(row1);
        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    @Bean
    @Qualifier("optionCnbcChoice")
    public InlineKeyboardMarkup optionCnbcChoice(@Qualifier("baseKeyboard")InlineKeyboardMarkup inlineKeyboardMarkup){
        Map<String, String> options = sources.getResourceMap();

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>(new ArrayList<>());
        ArrayList<InlineKeyboardButton> row = new ArrayList<>();

        int i = 0;
        for (String key: options.keySet()) {
            if(i % 3 == 0){
                rowList.add(row);
                row = new ArrayList<>();
            }
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(key.split("\\.")[1]);
            button.setCallbackData(key.split("\\.")[1]);
            row.add(button);
            i++;
        }
        rowList.add(row);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
    @Bean
    @Qualifier("optionReutersChoice")
    public InlineKeyboardMarkup optionReutersChoice(@Qualifier("baseKeyboard")InlineKeyboardMarkup inlineKeyboardMarkup){
        return inlineKeyboardMarkup;
    }

}
