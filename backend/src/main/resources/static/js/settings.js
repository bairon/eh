function showPreferencesPopup() {
    document.getElementById('preferences-popup').style.display = 'block';
    document.getElementById('overlay').style.display = 'block';
    document.getElementById('nickname-input').value = userData.nickname; // Prepopulate nickname
    document.getElementById('login-input-disabled').value = userData.login; // Prepopulate login
    const languageSelect = document.getElementById('language-select');
    if (userData.language) {
        languageSelect.value = userData.language; // Set the selected value
    } else {
        languageSelect.value = 'en'; // Default to English if no language is set
    }
}

function hidePreferencesPopup() {
    document.getElementById('preferences-popup').style.display = 'none';
    document.getElementById('overlay').style.display = 'none';
}

async function savePreferences() {
    const nickname = document.getElementById('nickname-input').value;
    const language = document.getElementById('language-select').value;

    try {
        userData = await $.ajax({
            url: '/api/user/preferences',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                nickname: nickname,
                language: language
            })
        });
        updateUserAvatar();
    } catch (error) {
        console.error('Error:', error);
        alert('An error occurred while saving preferences.');
    } finally {
        hidePreferencesPopup();
    }
}

function updateSettingsButton(authenticated) {
    if (authenticated) {
        $('#settings-btn').show();
    } else {
        $('#settings-btn').hide();
    }
}