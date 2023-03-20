const BASE_URL = 'http://192.168.1.154:8085';
const socket = io(BASE_URL); //the / namespace/endpoint

socket.on('connect', () => {
    console.log(`I'm connected to the socketio server with id ${socket.id}`)
})

socket.on('progress', (progress) => {

    const {currentTask, totalTasks} = progress;
    const percent = Math.floor(1000 * currentTask / totalTasks) / 10;

    $('#progress')
        .empty()
        .html(`Totally done ${currentTask} of ${totalTasks} tasks [${percent}%]`);
})
