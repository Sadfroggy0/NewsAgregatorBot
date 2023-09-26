package timofey.meta.usersMeta;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

@Component
public class KeyboardMeta {
    private Map<Long, Stack<InlineKeyboardMarkup>> userKeyboardMeta;
    public KeyboardMeta(){
        this.userKeyboardMeta = new HashMap<>();
    }
    public void updateMeta(Long userId, Stack<InlineKeyboardMarkup> updatedKeyboard){
        this.userKeyboardMeta.put(userId, updatedKeyboard);
    }
    public Stack<InlineKeyboardMarkup> getKeyboardByUserId(Long id){
        Stack<InlineKeyboardMarkup> userKeyboardHistory =  userKeyboardMeta.get(id);

        if(userKeyboardHistory == null){
            userKeyboardHistory = new Stack<>();
            return userKeyboardHistory;
        }

        return userKeyboardHistory;
    }
}
