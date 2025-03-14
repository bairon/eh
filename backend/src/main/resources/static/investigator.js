// Fetch Investigators from the Server
async function fetchInvestigators() {
    const response = await fetch('/api/investigator/list');
    if (!response.ok) {
        prompt(await response.text());
    }
    return await response.json();
}

// Display Investigator Details
function displayInvestigatorDetails(investigator) {
    document.getElementById('investigator-image1').src = investigator.image1;
    document.getElementById('investigator-image2').src = investigator.image2;
    document.getElementById('investigator-description').textContent = investigator.description;
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
    const popup = document.getElementById('investigator-popup');
    const investigatorList = document.getElementById('investigator-list');

    // Clear the list
    investigatorList.innerHTML = '';

    // Add each investigator to the list
    investigators.forEach((investigator) => {
        const div = document.createElement('div');
        div.textContent = investigator.name;
        div.addEventListener('mouseover', () => displayInvestigatorDetails(investigator));
        div.addEventListener('click', () => selectInvestigator(investigator));
        investigatorList.appendChild(div);
    });

    // Show the popup
    popup.style.display = 'block';
}

// Handle Investigator Selection
async function selectInvestigator(investigator) {
    // Submit the selected investigator to the server
    let response = await fetch('/api/investigator/select', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({investigatorName: investigator.name }),
    });
    if (!response.ok) {
        prompt(await response.text());
    }
    hidePopups();
}
