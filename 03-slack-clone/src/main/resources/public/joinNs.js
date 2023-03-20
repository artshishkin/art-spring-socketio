function joinNs(nsEndpoint) {

    if(nsSocket){
        // check to see if nsSocket is actually a socket
        nsSocket.close();
    }

    nsSocket = io(BASE_URL + nsEndpoint);
    nsSocket.on('nsRoomLoad', (nsRooms) => {
        // console.log(nsRooms);
        const roomListUl = $('.room-list');
        roomListUl.empty();
        nsRooms.forEach(nsRoom => {
            const glyph = nsRoom.privateRoom ? 'lock' : 'globe';
            roomListUl.append(`<li class="room"><span class="glyphicon glyphicon-${glyph}"></span>${nsRoom.roomTitle}</li>`);
        });
        roomListUl.children().click((e) => {
            const roomName = $(e.target).text();
            joinRoom(roomName);
        });

        //add room automatically... first time here
        const topRoom = roomListUl.find("li:nth-child(1)");
        const topRoomName = topRoom.text();
        joinRoom(topRoomName);

    });

    nsSocket.on('messageToClients', (msg) => {
        const messagesUl = $('#messages');
        messagesUl.append(buildMessageHTML(msg));
        // animate the scrollTop property to the height of the messagesUl
        messagesUl.animate({scrollTop: messagesUl.prop('scrollHeight')}, 1000);
    });

    nsSocket.on('updateMembersCount', (clientsCount) => {
        displayClientsCount(clientsCount);
    });


}

function displayClientsCount(newNumberOfMembers) {
    $('.curr-room-num-users').html(`${newNumberOfMembers}<span class="glyphicon glyphicon-user"></span>`);
}
