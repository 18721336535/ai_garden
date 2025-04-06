const canvas = document.getElementById('videoCanvas');
const ctx = canvas.getContext('2d');
let ws;
const img = new Image(); // Create a single Image object

function connect() {
    const button = document.getElementById('connectButton');
    button.classList.toggle('active');
    if (button.classList.contains('active')) {
        button.textContent = '监视开关开';
    } else {
        button.textContent = '监视开关关';
    }
    // 获取label元素
    const labelElement = document.querySelector('label');
    // 获取label的文本内容
    const name = labelElement.textContent;

    if (!name) {
        alert('Please enter your name.');
        return;
    }

    const messageObject = {
        name: name,
        message: 'hello',
    };
  ws = new WebSocket('ws://192.168.1.6:9090/video-stream');
//    ws = new WebSocket('ws://123.60.128.58:8080/video-stream');

    ws.onopen = function() {
        console.log('WebSocket connection opened');
        ws.send(JSON.stringify(messageObject));
    };

    ws.onmessage = function(event) {
        const arrayBuffer = event.data;
        const blob = new Blob([arrayBuffer], { type: 'image/jpeg' });
        const url = URL.createObjectURL(blob);
        img.onload = function() {
            ctx.drawImage(img, 0, 0, canvas.width, canvas.height);
            URL.revokeObjectURL(url);
        };
        img.src = url;
    };

    ws.onclose = function() {
        console.log('WebSocket connection closed');
    };

    ws.onerror = function(error) {
        console.error('WebSocket error:', error);
    };
}

// Define the onload handler outside the onmessage function
img.onload = function() {
    ctx.drawImage(img, 0, 0, canvas.width, canvas.height);
    URL.revokeObjectURL(img.src);
};
