const express = require('express');
const socketio = require('socket.io')
const namespaces = require('./data/namespaces')

// console.log(namespaces)
// console.log(namespaces[0])

const app = express();

// app.use(express.static(path.join(__dirname, '/public')));
app.use(express.static(__dirname + '/public'));

const expressServer = app.listen(8000);

const io = socketio(expressServer);

io.on('connection', (socket) => {

    //build an array to send back with the img and endpoint for each namespace
    const nsData = namespaces.map(ns => {
        return {
            img: ns.img,
            endpoint: ns.endpoint
        }
    });
    //send the ns data back to the client
    socket.emit('nsList', namespaces);

})

//loop through all namespaces and listen for a connection
namespaces.forEach(namespace => {
    io.of(namespace.endpoint).on('connect', (nsSocket) => {
        console.log(`Server received a connection to namespace ${namespace.endpoint} from socket ${nsSocket.id}`)

        const username = nsSocket.handshake.query.username;

        nsSocket.emit('nsRoomLoad', namespace.rooms)
        nsSocket.on('joinRoom', (roomToJoin, joiningRoomCallback) => {
            //deal with history... once we have it
            const rooms = Array.from(nsSocket.rooms);
            const roomToLeave = rooms[1];
            if (roomToLeave) {
                //leave previous room
                // console.log('leaving room ' + rooms[1])
                nsSocket.leave(roomToLeave); //[0] his own room, [1] last room client was joined to
                updateRoomMembers(namespace, roomToLeave)
            }
            // console.log('----------------')
            // console.log('Rooms of client: ' + nsSocket.id, rooms)
            // console.log('----------------')

            nsSocket.join(roomToJoin);

            const nsRoom = namespace.rooms.find(r => r.roomTitle === roomToJoin);

            updateRoomMembers(namespace, roomToJoin);

            io.of(namespace.endpoint).in(roomToJoin).fetchSockets().then((clients) => {
                // console.log('Joined clients:', clients.map(soc => soc.id));
                joiningRoomCallback(clients.length, `You joined the room ${roomToJoin}`, nsRoom.history);
            });

            //switch off previous listener of `messageToServer` event
            if (nsSocket.listeners('messageToServer'))
                nsSocket.removeAllListeners('messageToServer');
            nsSocket.on('messageToServer', (msg) => {
                const fullMsg = {
                    text: msg.text,
                    time: Date.now(),
                    username,
                    avatar: `https://robohash.org/${username}?set=set1&size=30x30`
                };
                // console.log(`received message ${msg.text} in room '${roomToJoin}' of '${namespace.endpoint}' namespace`)
                nsRoom.addMessage(fullMsg);
                io.of(namespace.endpoint).in(roomToJoin).emit('messageToClients', fullMsg);
            })
        })

        //leaving all the rooms on disconnect
        nsSocket.on('disconnect', (reason) => {
            namespace.rooms.forEach(room => {
                const roomToLeave = room.roomTitle;
                nsSocket.leave(roomToLeave);
                updateRoomMembers(namespace, roomToLeave);
            })
        });

    })
})

function updateRoomMembers(namespace, roomName) {
    io.of(namespace.endpoint).in(roomName).fetchSockets().then((clients) => {
        //inform other clients about new clients count
        // console.log(`Clients count in room '${roomName}' of namespace '${namespace.endpoint}' is ${clients.length}`)

        io.of(namespace.endpoint).in(roomName).emit('updateMembersCount', clients.length);
    });
}
