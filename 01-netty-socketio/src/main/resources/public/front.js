const BASE_URL = 'http://192.168.1.154:8085';
const socket = io(BASE_URL); //the / namespace/endpoint

socket.on('connect', () => {
    console.log(`I'm connected to the socketio server with id ${socket.id}`)
})

$('.msg-form')
    .submit((event) => {
        event.preventDefault();
        const $msgInput = $('#msg-input');
        const msg = $msgInput.val();
        $msgInput.val('');
        socket.emit('send_message', {
            message: msg,
            type: 'CLIENT',
            room: '/'
        });
    })

socket.on('get_message', (data) => {
    console.log(data)
})
