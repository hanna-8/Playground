var panner;

function update(x, y, z) {
    var coords = cartesianToInteraural(x, y, z);
    panner.update(coords.azm, coords.elv);
}

window.onload = function () {
    // initialize Audio Context
    try {
	var audioContext = new (window.AudioContext || window.webkitAudioContext)();		
    }
    catch (e) {
	alert("Web Audio API is not supported in this browser");
    }

    // load hrir to the container
    var hrtfContainer = new HRTFContainer();
    hrtfContainer.loadHrir("./res/kemar_L.bin");

    // create audio source node from the <audio> element
    var sourceNode = audioContext.createMediaElementSource(document.getElementById("player"));
    var gain = audioContext.createGain();
    gain.gain.value = 0.3;
    sourceNode.connect(gain);

    // create new hrtf panner, source node gets connected automatically
    panner = new HRTFPanner(audioContext, gain, hrtfContainer);

    // connect the panner to the destination node
    panner.connect(audioContext.destination);

//    update(0, 1, 0);
}
