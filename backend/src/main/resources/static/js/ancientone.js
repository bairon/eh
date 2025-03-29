// Fetch Ancient Ones from the Server
async function fetchAncientOnes() {
    try {
        const response = await $.ajax({
            url: '/api/ancient-one/list',
            method: 'GET',
        });
        return response;
    } catch (error) {
        console.error('Error fetching Ancient Ones:', error);
        return [];
    }
}

// Display Ancient One Details
function displayAncientOneDetails(ancientOne) {
    $('#ancient-one-image1').attr('src', staticData[ancientOne.image]);
    $('#ancient-one-image2').attr('src', staticData[ancientOne.imageback]);
    $('#ancient-one-description').text(staticData[ancientOne.description]);
}

// Show the Popup and Populate Ancient Ones
async function populateAncientOnes() {
    const ancientOnes = await fetchAncientOnes();
    if (ancientOnes.length > 0) {
        showAncientOnePopup(ancientOnes);
    } else {
        alert('No Ancient Ones found.');
    }
}

// Update ancientone.js
function showAncientOnePopup(ancientOnes) {
    showPopup('#ancient-one-popup');
    const $popup = $('#ancient-one-popup');
    const $ancientOneList = $('#ancient-one-list');

    // Clear the list
    $ancientOneList.empty();

    // Add each Ancient One to the list
    ancientOnes.forEach((ancientOne, index) => {
        const $div = $('<div>').text(staticData[ancientOne.id + '.name'])
            .on('mouseover', () => displayAncientOneDetails(ancientOne))
            .on('click', () => selectAncientOne(ancientOne));

        // Highlight the first item by default
        if (index === 0) {
            displayAncientOneDetails(ancientOne); // Show details for first item
        }

        $ancientOneList.append($div);
    });

    // Show the popup
    $popup.show();
}

// Handle Ancient One Selection
async function selectAncientOne(ancientOne) {
    // Submit the selected Ancient One to the server
    await $.ajax({
        url: '/api/ancient-one/select',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({id: ancientOne.id }),
    });
    hideAllPopups();
}

// Log Messages to the Game Output
function log(message) {
    const $gameOutput = $('#game-output');
    $gameOutput.append($('<p>').text(message));
    $gameOutput.scrollTop($gameOutput[0].scrollHeight);
}

// Make the functions globally accessible
window.populateAncientOnes = populateAncientOnes;
window.selectAncientOne = selectAncientOne;