// Fetch Ancient Ones from the Server
async function fetchAncientOnes() {
    try {
        const response = await fetch('/api/ancient-one/list');
        if (!response.ok) {
            throw new Error('Failed to fetch Ancient Ones');
        }
        const ancientOnes = await response.json();
        return ancientOnes;
    } catch (error) {
        console.error('Error fetching Ancient Ones:', error);
        return [];
    }
}

// Display Ancient One Details
function displayAncientOneDetails(ancientOne) {
    document.getElementById('ancient-one-image1').src = ancientOne.image1;
    document.getElementById('ancient-one-image2').src = ancientOne.image2;
    document.getElementById('ancient-one-description').textContent = ancientOne.description;
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
    const popup = document.getElementById('ancient-one-popup');
    const ancientOneList = document.getElementById('ancient-one-list');

    // Clear the list
    ancientOneList.innerHTML = '';

    // Add each Ancient One to the list
    ancientOnes.forEach((ancientOne) => {
        const div = document.createElement('div');
        div.textContent = ancientOne.name;
        div.addEventListener('mouseover', () => displayAncientOneDetails(ancientOne));
        div.addEventListener('click', () => selectAncientOne(ancientOne));
        ancientOneList.appendChild(div);
    });

    // Show the popup
    popup.style.display = 'block';
}

// Handle Ancient One Selection
function selectAncientOne(ancientOne) {
    const sessionId = localStorage.getItem('sessionId');
    if (!sessionId) {
        console.error('No game session found.');
        return;
    }

    // Submit the selected Ancient One to the server
    fetch('/api/ancient-one/select', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ sessionId, name: ancientOne.name }),
    })
        .then((response) => {
            if (!response.ok) {
                throw new Error('Failed to select Ancient One');
            }
            // Close the popup
            document.getElementById('ancient-one-popup').style.display = 'none';
            log(`Selected Ancient One: ${ancientOne.name}`);
        })
        .catch((error) => {
            console.error('Error selecting Ancient One:', error);
        });
}

// Log Messages to the Game Output
function log(message) {
    const gameOutput = document.getElementById('game-output');
    const p = document.createElement('p');
    p.textContent = message;
    gameOutput.appendChild(p);
    gameOutput.scrollTop = gameOutput.scrollHeight;
}

// Make the functions globally accessible
window.populateAncientOnes = populateAncientOnes;
window.selectAncientOne = selectAncientOne;