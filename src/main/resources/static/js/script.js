function copySessionId() {
    const sessionId = document.querySelector('.session-id').textContent;

    const input = document.createElement('input');
    input.value = sessionId;
    document.body.appendChild(input);
    input.select();
    document.execCommand('copy');
    document.body.removeChild(input);

    const copyMessage = document.getElementById('copyMessage');
    copyMessage.style.display = 'block';
    setTimeout(function() {
        copyMessage.style.display = 'none';
    }, 3000);
}
