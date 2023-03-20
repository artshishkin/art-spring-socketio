const BASE_URL = 'http://192.168.1.154:8085';
// const socket = io(BASE_URL); //the / namespace/endpoint
const username = prompt('What is your username?');
const socket = io(BASE_URL, {
    query: {
        username
    }
}); //the / namespace/endpoint with query parameters

let nsSocket = "";

socket.on('nsList', (nsData) => {

    const namespacesDiv = $('.namespaces');
    namespacesDiv.empty();
    nsData.forEach(ns => namespacesDiv.append(`
        <div class="namespace" ns="${ns.endpoint}">
            <img src="${ns.img}">
        </div>`
    ));
    //add a click listener for each NS
    $('.namespace').click((event) => {
        const nsEndpoint = $(event.target).parent().attr('ns');
        joinNs(nsEndpoint);
    });
    //By default, join top namespace
    const topNamespaceEndpoint = nsData[0].endpoint;
    joinNs(topNamespaceEndpoint);
});

