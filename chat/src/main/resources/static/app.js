let stompClient = null;
let connected = false;
let username = null;

//Esto es para obtener los elementos del html los cuales van a ser modificados
const messagesDiv = document.getElementById("messages");
const input = document.getElementById("messageInput");
const sendBtn = document.getElementById("sendBtn");
const userIdInput = document.getElementById("userIdInput");
const joinBtn = document.getElementById("joinBtn");
const inputArea = document.getElementById("inputArea");

// Evento para unirse al chat con un usuario
joinBtn.addEventListener("click", joinChat);
userIdInput.addEventListener("keypress", (e) => {
    if (e.key === "Enter") joinChat();
});

function joinChat() {
    const enteredUsername = userIdInput.value.trim();
    if (enteredUsername === "") {
        alert("Por favor ingresa un nombre de usuario");
        return;
    }
    username = enteredUsername;
    userIdInput.disabled = true;
    joinBtn.disabled = true;
    inputArea.style.display = "flex";
    addSystemMessage(`${username} se ha unido al chat`);
    input.focus();
    connect();
}

function connect() {
    const socket = new SockJS("http://localhost:8080/ws");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);
}

function onConnected() {
    connected = true;
    addSystemMessage("Conectado al servidor");
    sendBtn.disabled = false;
    stompClient.subscribe("/server/canal-de-la-mancha", onMessageReceived);
}

function onError(error) {
    console.error("Error en WebSocket:", error);
    addSystemMessage("Error de conexión");
    connected = false;
    sendBtn.disabled = true;
}

// esta funcion maneja los mensajes recibidos del servidor
function onMessageReceived(message) {
    const body = JSON.parse(message.body || "{}");
    const sender = body.user;
    const content = body.text;
    const timestamp = body.timeStamp;

    // Si el mensaje viene del mismo username local, no lo mostramos (evitar eco)
    if (sender && username && sender === username) {
        return;
    }

    const timeStr = timestamp ? new Date(timestamp).toLocaleTimeString('es-ES') : new Date().toLocaleTimeString('es-ES');
    addMessage(`[${timeStr}] ${sender || 'Anónimo'}: ${content || ''}`, false);
}

//eventos para enviar mensajes ya sea por click o enter
sendBtn.addEventListener("click", sendMessage);
input.addEventListener("keypress", (e) => {
    if (e.key === "Enter") sendMessage();
});

function sendMessage() {
    const text = input.value.trim();
    if (text === "") return;
    if (!connected) {
        addSystemMessage("No conectado al servidor");
        return;
    }

    const isoTime = new Date().toISOString();
    const message = {
        user: username,
        text: text,
        timeStamp: isoTime
    };

    stompClient.send("/app/chat1", {}, JSON.stringify(message));
    addMessage(`[${new Date().toLocaleTimeString('es-ES')}] Tú: ${text}`, true);
    input.value = "";
}

function addMessage(text, isOwn = false) {
    const messageDiv = document.createElement("div");
    messageDiv.classList.add("message");
    if (isOwn) messageDiv.classList.add("own-message");
    messageDiv.textContent = text;
    messagesDiv.appendChild(messageDiv);
    messagesDiv.scrollTop = messagesDiv.scrollHeight;
}

function addSystemMessage(text) {
    const messageDiv = document.createElement("div");
    messageDiv.classList.add("message");
    messageDiv.style.background = "#333";
    messageDiv.style.color = "#999";
    messageDiv.textContent = "[Sistema] " + text;
    messagesDiv.appendChild(messageDiv);
    messagesDiv.scrollTop = messagesDiv.scrollHeight;
}

window.addEventListener("load", () => {
    console.log("Página cargada. Esperando que el usuario ingrese su nombre...");
});
