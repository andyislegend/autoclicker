package engine.entity;

import engine.enums.KeyEnum;
import lombok.Getter;

@Getter
public class Click {

    private KeyEnum buttonOne;
    private KeyEnum buttonTwo;
    private KeyEnum buttonThree;

    public Click(KeyEnum buttonOne) {
        this.buttonOne = buttonOne;
    }

    public Click(KeyEnum buttonOne, KeyEnum buttonTwo) {
        this.buttonOne = buttonOne;
        this.buttonTwo = buttonTwo;
    }

    public Click(KeyEnum buttonOne, KeyEnum buttonTwo, KeyEnum buttonThree) {
        this.buttonOne = buttonOne;
        this.buttonTwo = buttonTwo;
        this.buttonThree = buttonThree;
    }

}
