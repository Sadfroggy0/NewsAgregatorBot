package timofey.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Map;

public class TopicsKeyboard {
    private ReplyKeyboardMarkup topicKeyboard;
    public TopicsKeyboard(Map<String,String> resourceMap){

        int rows = resourceMap.size() / 3;
        topicKeyboard = new ReplyKeyboardMarkup();
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();


        topicKeyboard.setSelective(true);
        topicKeyboard.setResizeKeyboard(true);
        topicKeyboard.setOneTimeKeyboard(false);

        int i =0;
        KeyboardRow row = new KeyboardRow();
        for (String key: resourceMap.keySet()) {
            if(i % 3 == 0){
                keyboard.add(row);
                row = new KeyboardRow();
            }
            row.add(key.split("\\.")[1]);
            i++;
        }
        keyboard.add(row);

        topicKeyboard.setKeyboard(keyboard);

    }

    public ReplyKeyboardMarkup getTopicKeyboard() {
        return topicKeyboard;
    }
}
