let socket;

const connectButton = document.getElementById('connect');
const sendButton = document.getElementById('send');
const disconnectButton = document.getElementById('disconnect');
const logsDiv = document.getElementById('logs');

function logMessage(message) {
    const log = document.createElement('div');
    log.textContent = message;
    logsDiv.appendChild(log);
    logsDiv.scrollTop = logsDiv.scrollHeight;
}

connectButton.onclick = () => {
    socket = new WebSocket("ws://localhost:8080/raw-socket")

    socket.onopen = () => {
        logMessage("Connected to server");
        connectButton.disabled = true;
        sendButton.disabled = false;
        disconnectButton.disabled = false;
    };

    socket.onmessage = event => logMessage(`Server: ${event.data}`);

    socket.onclose = () => {
        logMessage("Disconnected from server");
        connectButton.disabled = false;
        sendButton.disabled = true;
        disconnectButton.disabled = true;
    };

    socket.onerror = error => logMessage(`Error: ${error.message}`);
}

sendButton.onclick = () => {
    const message = 'Hello Server!';
    socket.send(message);
    logMessage(`You: ${message}`);
};

disconnectButton.onclick = () => {
    logMessage("Disconnected")
    socket.clone();
}