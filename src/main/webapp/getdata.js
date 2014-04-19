var wsCon = new WebSocket('ws://' + window.location.host + window.location.pathname + '/socket');
wsCon.binaryType = "arraybuffer";

var anArgot = argot(window.location.pathname + '/dictionaries');
var graph = makeGraph($('#content'));


wsCon.onopen = function () {
    console.log('Connected to server!');
};


wsCon.onmessage = function (e) {
    var messageData = new Uint8Array(e.data);
    if (messageData[0] === 65) { // magic number, hints start of Argot Message
        anArgot.readMessage(messageData).then(function (libAndData) {
            var lib = libAndData[0];
            registerForData(lib);
        });
    }
};

function registerForData(lib) {
    wsCon.onmessage = function (e) {
        var messageData = new Uint8Array(e.data);
        var data = anArgot.read(lib, messageData);
        graph.addValue(data['short']);
    }
}

