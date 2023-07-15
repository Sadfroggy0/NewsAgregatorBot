package timofey.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TopicsKeyboard {
    private InlineKeyboardMarkup topicKeyboard;

    public TopicsKeyboard(Map<String,String> resourceMap){

        topicKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>(new ArrayList<>());
        ArrayList<InlineKeyboardButton> row = new ArrayList<>();

        int i = 0;
        for (String key: resourceMap.keySet()) {
            if(i % 3 == 0){
                rowList.add(row);
                row = new ArrayList<>();
            }
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(key.split("\\.")[1]);
            button.setCallbackData(key);
            row.add(button);
            i++;
        }
        rowList.add(row);
        topicKeyboard.setKeyboard(rowList);

    }

    public InlineKeyboardMarkup getTopicKeyboard() {
        return topicKeyboard;
    }
}
