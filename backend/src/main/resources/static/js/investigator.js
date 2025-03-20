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
    $('#investigator-image1').attr('src', investigator.image1);
    $('#investigator-image2').attr('src', investigator.image2);
    $('#investigator-description').text(investigator.description);
}

// Show the Popup and Populate Investigators
async function populateInvestigators() {
    const investigators = await fetchInvestigators();
    if (investigators.length > 0) {
        showPopup(investigators);
    } else {
        alert('No investigators found.');
    }
}

// Show the Popup
function showPopup(investigators) {
    const $popup = $('#investigator-popup');
    const $investigatorList = $('#investigator-list');

    // Clear the list
    $investigatorList.empty();

    // Add each investigator to the list
    investigators.forEach((investigator) => {
        const $div = $('<div>').text(investigator.name)
            .on('mouseover', () => displayInvestigatorDetails(investigator))
            .on('click', () => selectInvestigator(investigator));
        $investigatorList.append($div);
    });

    // Show the popup
    $popup.show();
}

// Handle Investigator Selection
async function selectInvestigator(investigator) {
    try {
        await $.ajax({
            url: '/api/investigator/select',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ investigatorName: investigator.name }),
        });
        $('#investigator-popup').hide();
    } catch (error) {
        alert(error.responseText);
    }
}