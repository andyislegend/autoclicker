package engine.enums;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public enum KeyEnum {

    ALT(KeyEvent.VK_ALT),
    TAB(KeyEvent.VK_TAB),
    CTRL(KeyEvent.VK_CONTROL),
    DELETE(KeyEvent.VK_DELETE),
    PAGE_UP(KeyEvent.VK_PAGE_UP),
    PAGE_DOWN(KeyEvent.VK_PAGE_DOWN),
    SPACE(KeyEvent.VK_SPACE),
    SHIFT(KeyEvent.VK_SHIFT),
    C(KeyEvent.VK_C),
    X(KeyEvent.VK_X),
    Z(KeyEvent.VK_Z),
    A(KeyEvent.VK_A),
    F(KeyEvent.VK_F),
    D(KeyEvent.VK_D),
    Q(KeyEvent.VK_Q),
    R(KeyEvent.VK_R),
    T(KeyEvent.VK_T),
    V(KeyEvent.VK_V),
    LEFT(KeyEvent.VK_LEFT),
    RIGHT(KeyEvent.VK_RIGHT),
    UP(KeyEvent.VK_UP),
    DOWN(KeyEvent.VK_DOWN),
    ESC(KeyEvent.VK_ESCAPE),
    F1(KeyEvent.VK_F1),
    F2(KeyEvent.VK_F2),
    F3(KeyEvent.VK_F3),
    F4(KeyEvent.VK_F4),
    F5(KeyEvent.VK_F5),
    F6(KeyEvent.VK_F6),
    F7(KeyEvent.VK_F7),
    F8(KeyEvent.VK_F8),
    F9(KeyEvent.VK_F9),
    F10(KeyEvent.VK_F10),
    F11(KeyEvent.VK_F11),
    F12(KeyEvent.VK_F12),
    ENTER(KeyEvent.VK_ENTER),
    BACKSPACE(KeyEvent.VK_BACK_SPACE),
    MOUSE_RIGHTCLICK(InputEvent.BUTTON3_DOWN_MASK),
    MOUSE_LEFTCLICK(InputEvent.BUTTON1_DOWN_MASK),
    MOUSE_MIDCLICK(InputEvent.BUTTON2_DOWN_MASK),
    DEFAULT(Integer.MAX_VALUE);

    private int vk;

    public int getVk() {
        return vk;
    }

    KeyEnum(int key) {
        this.vk = key;
    }

    public boolean isMouseEvent() {

        return this.name().equals("MOUSE_LEFTCLICK") ||
                this.name().equals("MOUSE_RIGHTCLICK") || this.name().equals("MOUSE_MIDCLICK") ;

    }
}
