//"use strict"

var leftFrontPanner;
var rightPanner;
var backPanner;


window.onload = function () {
    var audio = {
	context: 0,
	hrtfContainer: 0
    };
    
    // initialize Audio Context
    try {
	audio.context = new (window.AudioContext || window.webkitAudioContext)();		
    }
    catch (e) {
	alert("Web Audio API is not supported in this browser");
    }

    // load hrir to the container
    audio.hrtfContainer = new HRTFContainer();
    audio.hrtfContainer.loadHrir("./res/kemar_L.bin");

    leftFrontPanner = createPanner("player-left-front", -1, 1, 0, audio);
    rightPanner = createPanner("player-right", 1, 0, 0, audio);
    backPanner = createPanner("player-back", 0, -1, 0, audio);

}


function createPanner(sourceId, x, y, z, audio) {
    // create audio source node from the <audio> element
    var sourceNode = audio.context.createMediaElementSource(document.getElementById(sourceId));
    var gain = audio.context.createGain();
    gain.gain.value = 0.3;
    sourceNode.connect(gain);

    // create new hrtf panner, source node gets connected automatically
    panner = new HRTFPanner(audio.context, sourceNode, audio.hrtfContainer);

    // connect the panner to the destination node
    panner.connect(audio.context.destination);

    // For some reason the panners cannot be updated as the page is being loaded
    // maybe they are not yet connected to the output? Who knows...
    // Thus: update them... later :).
    // This is a wonderful workaround.
    setTimeout(updatePanners, 1000);

    return panner;
}

function updatePanners() {
    // It'd be nice to be able to write:
    // leftFrontPanner.updateCart(-1, 1, 0);
    // Still a mistery why it hangs.

    var coords = cartesianToInteraural(-1, 1, 0);
    leftFrontPanner.update(coords.azm, coords.elv);

    coords = cartesianToInteraural(1, 0, 0);
    rightPanner.update(coords.azm, coords.elv);

    coords = cartesianToInteraural(0, -1, 0);
    backPanner.update(coords.azm, coords.elv);
}
