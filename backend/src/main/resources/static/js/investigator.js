// Fetch Investigators from the Server
async function fetchInvestigators() {
    try {
        const response = await $.ajax({
            url: '/api/investigator/list',
            method: 'GET',
        });
        return response;
    } catch (error) {
        alert(error.responseText);
        return [];
    }
}

// Display Investigator Details
function displayInvestigatorDetails(investigator) {

    const frontImage = staticData[investigator.image];
    $('#investigator-image1').attr('src', frontImage);

    const backImage = staticData[investigator.imageback];
    $('#investigator-image2').attr('src', backImage);

    const description = staticData[investigator.description];
    $('#investigator-description').text(description);

}

// Show the Popup and Populate Investigators
async function populateInvestigators() {
    const investigators = await fetchInvestigators();
    if (investigators.length > 0) {
        showInvestigatorsPopup(investigators);
    } else {
        alert('No investigators found.');
    }
}

// Show the Popup
function showInvestigatorsPopup(investigators) {
    showPopup('#investigator-popup');
    const $investigatorList = $('#investigator-list');

    // Clear the list
    $investigatorList.empty();

    // Add each investigator to the list
    investigators.forEach((investigator) => {
        const $div = $('<div>').text(staticData[investigator.id + '.name'])
            .on('mouseover', () => displayInvestigatorDetails(investigator))
            .on('click', () => selectInvestigator(investigator));
        $investigatorList.append($div);
    });
}

// Handle Investigator Selection
async function selectInvestigator(investigator) {
    try {
        await $.ajax({
            url: '/api/investigator/select',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ id: investigator.id }),
        });
    } catch (error) {
        alert(error.responseText);
    } finally {
        hideAllPopups();
    }
}