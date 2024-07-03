const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/gs-guide-websocket'
});

function getCookie(name) {
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${name}=`);
  if (parts.length === 2) {
    return parts.pop().split(';').shift();
  }
  return '';
}

var uninitialized = true;
var connected = false;
var session = getCookie('auth-session');

function init() {
    connect();
}

stompClient.onConnect = (frame) => {
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/components', (message) => {
        reloadComponent(JSON.parse(message.body));
    });
    connected = true;
    if(uninitialized)
    {
        triggerLoad('app'); // Trigger to reload (initial load) the app component from the server.
        uninitialized = false;
    }
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function connect() {
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

/**
* Send a HTMLComponentMessage object that contains the id parameter to trigger a reload of the specific html-node by subscription callback.
* @param id The html-element-id that shall be reloaded
* @param props Properties for the request {'key1':'value1', 'key2':'value2'} example {'page':'2', 'items-per-page':'20'}
*/
function triggerLoad(id, props = {}) {
    stompClient.publish({
        destination: "/app/component",
        body: JSON.stringify({
            session: session,
            id: id,
            request: true,
            response: false,
            html: '',
            version: '1.0.0',
            props: props
        })
    });
}

/**
* ### The main reload callback - if something changes on the server this get an updated component from the server ###
* @param component HTMLComponentMessage (see body in triggerLoad above).
*/
function reloadComponent(component) {
    //console.log(component);
    const redirect = component.props['redirect'];
    const e = document.getElementById(component.id);
    e.innerHTML = component.html;
    if(redirect)
    {
        location = redirect;
    }
}

// ######################## Initialize / first load the whole SPA. ########################
init();
