package engine.util;

import engine.entity.Click;
import engine.enums.KeyEnum;

public class ClickerBuilderUtil {

    public static Click createButtonClick(String firstButton, String secondButton, String thirdButton) {

        if (secondButton.isEmpty()) {
            return new Click(KeyEnum.valueOf(firstButton));
        } else if (thirdButton.isEmpty()) {
            return new Click(KeyEnum.valueOf(firstButton), KeyEnum.valueOf(secondButton));
        } else {
            return new Click(KeyEnum.valueOf(firstButton), KeyEnum.valueOf(secondButton), KeyEnum.valueOf(thirdButton));
        }
    }
}
