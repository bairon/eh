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
    $('#ancient-one-image1').attr('src', ancientOne.image1);
    $('#ancient-one-image2').attr('src', ancientOne.image2);
    $('#ancient-one-description').text(ancientOne.description);
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

// Show the Popup
function showAncientOnePopup(ancientOnes) {
    const $popup = $('#ancient-one-popup');
    const $ancientOneList = $('#ancient-one-list');

    // Clear the list
    $ancientOneList.empty();

    // Add each Ancient One to the list
    ancientOnes.forEach((ancientOne) => {
        const $div = $('<div>').text(ancientOne.name)
            .on('mouseover', () => displayAncientOneDetails(ancientOne))
            .on('click', () => selectAncientOne(ancientOne));
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
        data: JSON.stringify({name: ancientOne.name }),
    });
    $('#ancient-one-popup').hide();
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