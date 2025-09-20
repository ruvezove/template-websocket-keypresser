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

                for (char c : word.toCharArray()) {
                    int code = KeyEvent.getExtendedKeyCodeForChar(c);
                    if (Character.isUpperCase(c)) {
                        robot.keyPress(KeyEvent.VK_SHIFT);
                        robot.keyPress(code);
                        robot.keyRelease(code);
                        robot.keyRelease(KeyEvent.VK_SHIFT);
                    } else {
                        robot.keyPress(code);
                        robot.keyRelease(code);
                    }
                }

                if (pressEnter) {
                    robot.keyPress(KeyEvent.VK_ENTER);
                    robot.keyRelease(KeyEvent.VK_ENTER);
                }
            } else if (type.equals("key")) {
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
