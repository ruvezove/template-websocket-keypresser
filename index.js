import WebSocket from "ws";
import { exec } from "child_process";

const RUNS = 1;
const CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 `~!@#$%^&*()-_=+[]{}\\|;:'\",.<>/?"; 

console.log("Запускаем Java-сервер: npm run javas...");

exec("npm run javas", (err, stdout, stderr) => {
  if (err) console.error("Ошибка при запуске Java:", err);
  if (stdout) console.log(stdout);
  if (stderr) console.error(stderr);
});

await new Promise(r => setTimeout(r, 5000));

const ws = new WebSocket("ws://localhost:3031");

ws.on("open", async () => {
  console.log("WebSocket подключен. Начинаем тест...");

  for (let run = 1; run <= RUNS; run++) {
    console.log(`=== Прогон ${run} ===`);
    for (let i = 0; i < CHARS.length; i++) {
      ws.send(JSON.stringify({
        type: "click",
        word: CHARS[i],
        pressEnter: false
      }));
    }
  }
});
