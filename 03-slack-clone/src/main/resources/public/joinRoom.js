function joinRoom(roomName) {

    //update current room display name
    $('.curr-room-text').text(roomName);

    //send this room name to the server
    nsSocket.emit('joinRoom', roomName, (newNumberOfMembers, msg, roomHistory) => {
        console.log(msg);
        // console.log(roomHistory);
        const messagesUl = $('#messages');
        messagesUl.empty();
        roomHistory.forEach(fullMsg => messagesUl.append(buildMessageHTML(fullMsg)));
        messagesUl.scrollTop(messagesUl.prop('scrollHeight'));
        //we want to update the room member total now we have joined
        displayClientsCount(newNumberOfMembers);
    });

    const messageForm = $('.message-form');
    //remove the event listener before it's added again
    // Remove the submit event handler from the form
    messageForm.off('submit');

    messageForm.on('submit', (event) => {
        event.preventDefault();
        const userMsgField = $('#user-message');
        const newMessage = userMsgField.val();
        userMsgField.val('');
        // console.log(`sending new message ${newMessage} to nsSocket ${nsSocket.id}`)
        nsSocket.emit('messageToServer', {text: newMessage});

        console.log(nsSocket.listeners("messageToClients"));
    })

    const searchBox = $('#search-box');
    searchBox.off('input');
    searchBox.on('input', (e) => {
        const searchText = $(e.target).val();
        // console.log(searchText)

        const highlightClass = "bg-info";

        $('.message-text')
            .each(function () {
                const text = $(this).text();
                const regex = new RegExp(searchText, 'gi');
                const highlightedText = text.replace(regex, '<span class="' + highlightClass + '">$&</span>');
                $(this).html(highlightedText);
            });
    });

}

function buildMessageHTML(fullMessage) {
    // const options = {hour12: true, hour: 'numeric', minute: 'numeric', second: 'numeric'};
    // const msgTime = new Date(fullMessage.time).toLocaleTimeString('en-US', options); // output: 10:30:45 AM
    const msgTime = new Date(fullMessage.time).toLocaleString();
    return `
        <li>
            <div class="user-image">
                <img src="${fullMessage.avatar}" />
            </div>
            <div class="user-message">
                <div class="user-name-time">${fullMessage.username} <span>${msgTime}</span></div>
                <div class="message-text">${fullMessage.text}</div>
            </div>
        </li>
    `;
}