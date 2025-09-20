import java.awt.*;
import java.awt.event.KeyEvent;
import java.net.InetSocketAddress;
import java.util.*;
import org.java_websocket.server.WebSocketServer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import com.google.gson.*;

public class WsKeyPresser extends WebSocketServer {
    private Robot robot;
    private Map<String, Integer> keyMap;
    private Gson gson = new Gson();

    public WsKeyPresser(int port) throws Exception {
        super(new InetSocketAddress(port));
        robot = new Robot();
        robot.setAutoDelay(2);
        keyMap = new HashMap<>();
        keyMap.put("shift", KeyEvent.VK_SHIFT);
        keyMap.put("ctrl", KeyEvent.VK_CONTROL);
        keyMap.put("alt", KeyEvent.VK_ALT);
        keyMap.put("enter", KeyEvent.VK_ENTER);
        keyMap.put("escape", KeyEvent.VK_ESCAPE);
        keyMap.put("backspace", KeyEvent.VK_BACK_SPACE);
        keyMap.put("tab", KeyEvent.VK_TAB);
        keyMap.put("capslock", KeyEvent.VK_CAPS_LOCK);
        keyMap.put("numlock", KeyEvent.VK_NUM_LOCK);
        keyMap.put("insert", KeyEvent.VK_INSERT);
        keyMap.put("delete", KeyEvent.VK_DELETE);
        keyMap.put("up", KeyEvent.VK_UP);
        keyMap.put("down", KeyEvent.VK_DOWN);
        keyMap.put("left", KeyEvent.VK_LEFT);
        keyMap.put("right", KeyEvent.VK_RIGHT);
        for (int i = 1; i <= 12; i++) keyMap.put("f" + i, KeyEvent.VK_F1 + (i - 1));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        conn.send("ok");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        try {
            JsonObject obj = gson.fromJson(message, JsonObject.class);
            String type = obj.get("type").getAsString();
            if (type.equals("click")) {
    String word = obj.get("word").getAsString();
    boolean pressEnter = obj.has("pressEnter") && obj.get("pressEnter").getAsBoolean();

    Map<Character, Integer> simpleKeys = Map.ofEntries(
        Map.entry('a', KeyEvent.VK_A), Map.entry('b', KeyEvent.VK_B), Map.entry('c', KeyEvent.VK_C),
        Map.entry('d', KeyEvent.VK_D), Map.entry('e', KeyEvent.VK_E), Map.entry('f', KeyEvent.VK_F),
        Map.entry('g', KeyEvent.VK_G), Map.entry('h', KeyEvent.VK_H), Map.entry('i', KeyEvent.VK_I),
        Map.entry('j', KeyEvent.VK_J), Map.entry('k', KeyEvent.VK_K), Map.entry('l', KeyEvent.VK_L),
        Map.entry('m', KeyEvent.VK_M), Map.entry('n', KeyEvent.VK_N), Map.entry('o', KeyEvent.VK_O),
        Map.entry('p', KeyEvent.VK_P), Map.entry('q', KeyEvent.VK_Q), Map.entry('r', KeyEvent.VK_R),
        Map.entry('s', KeyEvent.VK_S), Map.entry('t', KeyEvent.VK_T), Map.entry('u', KeyEvent.VK_U),
        Map.entry('v', KeyEvent.VK_V), Map.entry('w', KeyEvent.VK_W), Map.entry('x', KeyEvent.VK_X),
        Map.entry('y', KeyEvent.VK_Y), Map.entry('z', KeyEvent.VK_Z),
        Map.entry('0', KeyEvent.VK_0), Map.entry('1', KeyEvent.VK_1), Map.entry('2', KeyEvent.VK_2),
        Map.entry('3', KeyEvent.VK_3), Map.entry('4', KeyEvent.VK_4), Map.entry('5', KeyEvent.VK_5),
        Map.entry('6', KeyEvent.VK_6), Map.entry('7', KeyEvent.VK_7), Map.entry('8', KeyEvent.VK_8),
        Map.entry('9', KeyEvent.VK_9),
        Map.entry(' ', KeyEvent.VK_SPACE),
        Map.entry('-', KeyEvent.VK_MINUS), Map.entry('=', KeyEvent.VK_EQUALS),
        Map.entry('[', KeyEvent.VK_OPEN_BRACKET), Map.entry(']', KeyEvent.VK_CLOSE_BRACKET),
        Map.entry('\\', KeyEvent.VK_BACK_SLASH), Map.entry(';', KeyEvent.VK_SEMICOLON),
        Map.entry('\'', KeyEvent.VK_QUOTE), Map.entry(',', KeyEvent.VK_COMMA),
        Map.entry('.', KeyEvent.VK_PERIOD), Map.entry('/', KeyEvent.VK_SLASH),
        Map.entry('`', KeyEvent.VK_BACK_QUOTE)
    );

    Map<Character, Integer> shiftKeys = Map.ofEntries(
        Map.entry('A', KeyEvent.VK_A), Map.entry('B', KeyEvent.VK_B), Map.entry('C', KeyEvent.VK_C),
        Map.entry('D', KeyEvent.VK_D), Map.entry('E', KeyEvent.VK_E), Map.entry('F', KeyEvent.VK_F),
        Map.entry('G', KeyEvent.VK_G), Map.entry('H', KeyEvent.VK_H), Map.entry('I', KeyEvent.VK_I),
        Map.entry('J', KeyEvent.VK_J), Map.entry('K', KeyEvent.VK_K), Map.entry('L', KeyEvent.VK_L),
        Map.entry('M', KeyEvent.VK_M), Map.entry('N', KeyEvent.VK_N), Map.entry('O', KeyEvent.VK_O),
        Map.entry('P', KeyEvent.VK_P), Map.entry('Q', KeyEvent.VK_Q), Map.entry('R', KeyEvent.VK_R),
        Map.entry('S', KeyEvent.VK_S), Map.entry('T', KeyEvent.VK_T), Map.entry('U', KeyEvent.VK_U),
        Map.entry('V', KeyEvent.VK_V), Map.entry('W', KeyEvent.VK_W), Map.entry('X', KeyEvent.VK_X),
        Map.entry('Y', KeyEvent.VK_Y), Map.entry('Z', KeyEvent.VK_Z),
        Map.entry('!', KeyEvent.VK_1), Map.entry('@', KeyEvent.VK_2), Map.entry('#', KeyEvent.VK_3),
        Map.entry('$', KeyEvent.VK_4), Map.entry('%', KeyEvent.VK_5), Map.entry('^', KeyEvent.VK_6),
        Map.entry('&', KeyEvent.VK_7), Map.entry('*', KeyEvent.VK_8), Map.entry('(', KeyEvent.VK_9),
        Map.entry(')', KeyEvent.VK_0), Map.entry('_', KeyEvent.VK_MINUS), Map.entry('+', KeyEvent.VK_EQUALS),
        Map.entry('{', KeyEvent.VK_OPEN_BRACKET), Map.entry('}', KeyEvent.VK_CLOSE_BRACKET),
        Map.entry('|', KeyEvent.VK_BACK_SLASH), Map.entry(':', KeyEvent.VK_SEMICOLON),
        Map.entry('"', KeyEvent.VK_QUOTE), Map.entry('<', KeyEvent.VK_COMMA),
        Map.entry('>', KeyEvent.VK_PERIOD), Map.entry('?', KeyEvent.VK_SLASH),
        Map.entry('~', KeyEvent.VK_BACK_QUOTE)
    );

    for (char c : word.toCharArray()) {
        boolean shift = shiftKeys.containsKey(c);
        int code = shift ? shiftKeys.get(c) : simpleKeys.getOrDefault(c, KeyEvent.VK_UNDEFINED);
        if (code == KeyEvent.VK_UNDEFINED) continue;

        if (shift) robot.keyPress(KeyEvent.VK_SHIFT);
        robot.keyPress(code);
        robot.keyRelease(code);
        if (shift) robot.keyRelease(KeyEvent.VK_SHIFT);
    }

    if (pressEnter) {
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }
}
 else if (type.equals("key")) {
                String name = obj.get("name").getAsString().toLowerCase();
                boolean lock = obj.get("lock").getAsBoolean();
                if (keyMap.containsKey(name)) {
                    int code = keyMap.get(name);
                    if (lock) robot.keyPress(code); else robot.keyRelease(code);
                }
            }
            conn.send("ok");
        } catch (Exception e) {
            conn.send("error:" + e.getMessage());
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {}
    @Override
    public void onError(WebSocket conn, Exception ex) {}
    @Override
    public void onStart() {}

    public static void main(String[] args) throws Exception {
        new WsKeyPresser(3031).start();
    }
}
