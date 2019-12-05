var stompClient = null;
var subscribedChannels = [];

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    $( "#sub-1" ).prop("disabled", !connected);
    $( "#sub-2" ).prop("disabled", !connected);
    $( "#sub-3" ).prop("disabled", !connected);
    $( "#sub-4" ).prop("disabled", !connected);
    $( "#sub-5" ).prop("disabled", !connected);

    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        // used in tandem with @SendTo of the BasicController.java
        stompClient.subscribe('/topic/greetings', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
        // used in tandem with @SendToUser of the BasicController.java
        stompClient.subscribe('/user/topic/greetings', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    subscribedChannels = [];
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));

    scLen = subscribedChannels.length;

    for (i = 0; i < scLen; i++) {
        stompClient.send("/app/channel/" + subscribedChannels[i], {}, JSON.stringify({'name': $("#name").val()}));
    }
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

function sub(channelID) {
    var id = "#sub-" + channelID;
    $( id ).prop("disabled", true);
    subscribedChannels.push(channelID);
    var channelSubURL = '/topic/channel/' + channelID;
    stompClient.subscribe(channelSubURL, function (greeting) {
        showGreeting(JSON.parse(greeting.body).content);
    });
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });

    $( "#sub-1" ).click(function() { sub(1); });
    $( "#sub-2" ).click(function() { sub(2); });
    $( "#sub-3" ).click(function() { sub(3); });
    $( "#sub-4" ).click(function() { sub(4); });
    $( "#sub-5" ).click(function() { sub(5); });
});