package sparkle.core;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public enum Key {
    NUM_0(KeyEvent.VK_0),
    NUM_1(KeyEvent.VK_1),
    NUM_2(KeyEvent.VK_2),
    NUM_3(KeyEvent.VK_3),
    NUM_4(KeyEvent.VK_4),
    NUM_5(KeyEvent.VK_5),
    NUM_6(KeyEvent.VK_6),
    NUM_7(KeyEvent.VK_7),
    NUM_8(KeyEvent.VK_8),
    NUM_9(KeyEvent.VK_9),
    A(KeyEvent.VK_A),
    ACCEPT(KeyEvent.VK_ACCEPT),
    ADD(KeyEvent.VK_ADD),
    AGAIN(KeyEvent.VK_AGAIN),
    ALL_CANDIDATES(KeyEvent.VK_ALL_CANDIDATES),
    ALPHANUMERIC(KeyEvent.VK_ALPHANUMERIC),
    ALT(KeyEvent.VK_ALT),
    ALT_GRAPH(KeyEvent.VK_ALT_GRAPH),
    AMPERSAND(KeyEvent.VK_AMPERSAND),
    ASTERISK(KeyEvent.VK_ASTERISK),
    AT(KeyEvent.VK_AT),
    B(KeyEvent.VK_B),
    BACK_QUOTE(KeyEvent.VK_BACK_QUOTE),
    BACK_SLASH(KeyEvent.VK_BACK_SLASH),
    BACK_SPACE(KeyEvent.VK_BACK_SPACE),
    BEGIN(KeyEvent.VK_BEGIN),
    BRACE_LEFT(KeyEvent.VK_BRACELEFT),
    BRACE_RIGHT(KeyEvent.VK_BRACERIGHT),
    C(KeyEvent.VK_C),
    CANCEL(KeyEvent.VK_CANCEL),
    CAPS_LOCK(KeyEvent.VK_CAPS_LOCK),
    CIRCUMFLEX(KeyEvent.VK_CIRCUMFLEX),
    CLEAR(KeyEvent.VK_CLEAR),
    CLOSE_BRACKET(KeyEvent.VK_CLOSE_BRACKET),
    CODE_INPUT(KeyEvent.VK_CODE_INPUT),
    COLON(KeyEvent.VK_COLON),
    COMMA(KeyEvent.VK_COMMA),
    COMPOSE(KeyEvent.VK_COMPOSE),
    CONTEXT_MENU(KeyEvent.VK_CONTEXT_MENU),
    CONTROL(KeyEvent.VK_CONTROL),
    CONVERT(KeyEvent.VK_CONVERT),
    COPY(KeyEvent.VK_COPY),
    CUT(KeyEvent.VK_CUT),
    D(KeyEvent.VK_D),
    DEAD_ABOVEDOT(KeyEvent.VK_DEAD_ABOVEDOT),
    DEAD_ABOVERING(KeyEvent.VK_DEAD_ABOVERING),
    DEAD_ACUTE(KeyEvent.VK_DEAD_ACUTE),
    DEAD_BREVE(KeyEvent.VK_DEAD_BREVE),
    DEAD_CARON(KeyEvent.VK_DEAD_CARON),
    DEAD_CEDILLA(KeyEvent.VK_DEAD_CEDILLA),
    DEAD_CIRCUMFLEX(KeyEvent.VK_DEAD_CIRCUMFLEX),
    DEAD_DIAERESIS(KeyEvent.VK_DEAD_DIAERESIS),
    DEAD_DOUBLEACUTE(KeyEvent.VK_DEAD_DOUBLEACUTE),
    DEAD_GRAVE(KeyEvent.VK_DEAD_GRAVE),
    DEAD_IOTA(KeyEvent.VK_DEAD_IOTA),
    DEAD_MACRON(KeyEvent.VK_DEAD_MACRON),
    DEAD_OGONEK(KeyEvent.VK_DEAD_OGONEK),
    DEAD_SEMIVOICED_SOUND(KeyEvent.VK_DEAD_SEMIVOICED_SOUND),
    DEAD_TILDE(KeyEvent.VK_DEAD_TILDE),
    DEAD_VOICED_SOUND(KeyEvent.VK_DEAD_VOICED_SOUND),
    DECIMAL(KeyEvent.VK_DECIMAL),
    DELETE(KeyEvent.VK_DELETE),
    DIVIDE(KeyEvent.VK_DIVIDE),
    DOLLAR(KeyEvent.VK_DOLLAR),
    DOWN(KeyEvent.VK_DOWN),
    E(KeyEvent.VK_E),
    END(KeyEvent.VK_END),
    ENTER(KeyEvent.VK_ENTER),
    EQUALS(KeyEvent.VK_EQUALS),
    ESCAPE(KeyEvent.VK_ESCAPE),
    EURO_SIGN(KeyEvent.VK_EURO_SIGN),
    EXCLAMATION_MARK(KeyEvent.VK_EXCLAMATION_MARK),
    F(KeyEvent.VK_F),
    F1(KeyEvent.VK_F1),
    F10(KeyEvent.VK_F10),
    F11(KeyEvent.VK_F11),
    F12(KeyEvent.VK_F12),
    F13(KeyEvent.VK_F13),
    F14(KeyEvent.VK_F14),
    F15(KeyEvent.VK_F15),
    F16(KeyEvent.VK_F16),
    F17(KeyEvent.VK_F17),
    F18(KeyEvent.VK_F18),
    F19(KeyEvent.VK_F19),
    F2(KeyEvent.VK_F2),
    F20(KeyEvent.VK_F20),
    F21(KeyEvent.VK_F21),
    F22(KeyEvent.VK_F22),
    F23(KeyEvent.VK_F23),
    F24(KeyEvent.VK_F24),
    F3(KeyEvent.VK_F3),
    F4(KeyEvent.VK_F4),
    F5(KeyEvent.VK_F5),
    F6(KeyEvent.VK_F6),
    F7(KeyEvent.VK_F7),
    F8(KeyEvent.VK_F8),
    F9(KeyEvent.VK_F9),
    FINAL(KeyEvent.VK_FINAL),
    FIND(KeyEvent.VK_FIND),
    FULL_WIDTH(KeyEvent.VK_FULL_WIDTH),
    G(KeyEvent.VK_G),
    GREATER(KeyEvent.VK_GREATER),
    H(KeyEvent.VK_H),
    HALF_WIDTH(KeyEvent.VK_HALF_WIDTH),
    HELP(KeyEvent.VK_HELP),
    HIRAGANA(KeyEvent.VK_HIRAGANA),
    HOME(KeyEvent.VK_HOME),
    I(KeyEvent.VK_I),
    INPUT_METHOD_ON_OFF(KeyEvent.VK_INPUT_METHOD_ON_OFF),
    INSERT(KeyEvent.VK_INSERT),
    INVERTED_EXCLAMATION_MARK(KeyEvent.VK_INVERTED_EXCLAMATION_MARK),
    J(KeyEvent.VK_J),
    JAPANESE_HIRAGANA(KeyEvent.VK_JAPANESE_HIRAGANA),
    JAPANESE_KATAKANA(KeyEvent.VK_JAPANESE_KATAKANA),
    JAPANESE_ROMAN(KeyEvent.VK_JAPANESE_ROMAN),
    K(KeyEvent.VK_K),
    KANA(KeyEvent.VK_KANA),
    KANA_LOCK(KeyEvent.VK_KANA_LOCK),
    KANJI(KeyEvent.VK_KANJI),
    KATAKANA(KeyEvent.VK_KATAKANA),
    KP_DOWN(KeyEvent.VK_KP_DOWN),
    KP_LEFT(KeyEvent.VK_KP_LEFT),
    KP_RIGHT(KeyEvent.VK_KP_RIGHT),
    KP_UP(KeyEvent.VK_KP_UP),
    L(KeyEvent.VK_L),
    LEFT(KeyEvent.VK_LEFT),
    LEFT_PARENTHESIS(KeyEvent.VK_LEFT_PARENTHESIS),
    LESS(KeyEvent.VK_LESS),
    M(KeyEvent.VK_M),
    META(KeyEvent.VK_META),
    MINUS(KeyEvent.VK_MINUS),
    MODECHANGE(KeyEvent.VK_MODECHANGE),
    MULTIPLY(KeyEvent.VK_MULTIPLY),
    N(KeyEvent.VK_N),
    NONCONVERT(KeyEvent.VK_NONCONVERT),
    NUM_LOCK(KeyEvent.VK_NUM_LOCK),
    NUMBER_SIGN(KeyEvent.VK_NUMBER_SIGN),
    NUMPAD_0(KeyEvent.VK_NUMPAD0),
    NUMPAD_1(KeyEvent.VK_NUMPAD1),
    NUMPAD_2(KeyEvent.VK_NUMPAD2),
    NUMPAD_3(KeyEvent.VK_NUMPAD3),
    NUMPAD_4(KeyEvent.VK_NUMPAD4),
    NUMPAD_5(KeyEvent.VK_NUMPAD5),
    NUMPAD_6(KeyEvent.VK_NUMPAD6),
    NUMPAD_7(KeyEvent.VK_NUMPAD7),
    NUMPAD_8(KeyEvent.VK_NUMPAD8),
    NUMPAD_9(KeyEvent.VK_NUMPAD9),
    O(KeyEvent.VK_O),
    OPEN_BRACKET(KeyEvent.VK_OPEN_BRACKET),
    P(KeyEvent.VK_P),
    PAGE_DOWN(KeyEvent.VK_PAGE_DOWN),
    PAGE_UP(KeyEvent.VK_PAGE_UP),
    PASTE(KeyEvent.VK_PASTE),
    PAUSE(KeyEvent.VK_PAUSE),
    PERIOD(KeyEvent.VK_PERIOD),
    PLUS(KeyEvent.VK_PLUS),
    PREVIOUS_CANDIDATE(KeyEvent.VK_PREVIOUS_CANDIDATE),
    PRINT_SCREEN(KeyEvent.VK_PRINTSCREEN),
    PROPS(KeyEvent.VK_PROPS),
    Q(KeyEvent.VK_Q),
    QUOTE(KeyEvent.VK_QUOTE),
    QUOTEDBL(KeyEvent.VK_QUOTEDBL),
    R(KeyEvent.VK_R),
    RIGHT(KeyEvent.VK_RIGHT),
    RIGHT_PARENTHESIS(KeyEvent.VK_RIGHT_PARENTHESIS),
    ROMAN_CHARACTERS(KeyEvent.VK_ROMAN_CHARACTERS),
    S(KeyEvent.VK_S),
    SCROLL_LOCK(KeyEvent.VK_SCROLL_LOCK),
    SEMICOLON(KeyEvent.VK_SEMICOLON),
    SEPARATER(KeyEvent.VK_SEPARATER),
    SEPARATOR(KeyEvent.VK_SEPARATOR),
    SHIFT(KeyEvent.VK_SHIFT),
    SLASH(KeyEvent.VK_SLASH),
    SPACE(KeyEvent.VK_SPACE),
    STOP(KeyEvent.VK_STOP),
    SUBTRACT(KeyEvent.VK_SUBTRACT),
    T(KeyEvent.VK_T),
    TAB(KeyEvent.VK_TAB),
    U(KeyEvent.VK_U),
    UNDEFINED(KeyEvent.VK_UNDEFINED),
    UNDERSCORE(KeyEvent.VK_UNDERSCORE),
    UNDO(KeyEvent.VK_UNDO),
    UP(KeyEvent.VK_UP),
    V(KeyEvent.VK_V),
    W(KeyEvent.VK_W),
    WINDOWS(KeyEvent.VK_WINDOWS),
    X(KeyEvent.VK_X),
    Y(KeyEvent.VK_Y),
    Z(KeyEvent.VK_Z);

    private static final Map<Integer, Key> keys = new HashMap<>();

    static {
        for (var key : values()) {
            keys.put(key.code, key);
        }
    }

    private final int code;

    Key(int code) {
        this.code = code;
    }

    static Key getByEvent(KeyEvent event) {
        return keys.get(event.getKeyCode());
    }
}