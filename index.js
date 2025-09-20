import WebSocket from "ws";
import { exec } from "child_process";

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

  for (let i = 0; i < 100; i++) {
    ws.send(JSON.stringify({
      type: "click",
      word: `${i} - ${Buffer.from((Math.random() * 100000).toFixed(0)).toString('base64')}`,
      pressEnter: true
    }));
  }
});
