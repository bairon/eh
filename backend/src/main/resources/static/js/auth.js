let staticData;
let userData;
let allUsers;
async function getAllUsers() {
    allUsers = await $.ajax({
        url: '/api/auth/users',
        method: 'GET',
    });
}
function initiateGoogleAuth() {
    window.location.href = '/oauth2/authorization/google';
}

function isAuthenticated() {
    return userData !== undefined && userData !== null && userData !== '';
}
function updateUserAvatar() {
    const $avatar = $('#user-avatar');
    const $avatarInitials = $('#avatar-initials');
    const $nicknameDisplay = $('#nickname-display');
    const $authLinkLogin = $('#auth-link-login');
    const $authLinkLogout = $('#auth-link-logout');

    if (userData) {
        // Remove quotes and special characters from the nickname
        const sanitizedName = userData.nickname ? userData.nickname.replace(/["']/g, '') : userData.name;

        // Extract initials from the sanitized name
        const initials = sanitizedName.split(' ').map(part => part.charAt(0).toUpperCase()).join('');

        // Update the avatar
        $avatarInitials.text(initials);
        $avatar.css('background-color', getRandomColor()); // Optional: Add a random color
        $nicknameDisplay.text(sanitizedName);
        $authLinkLogin.hide();
        $authLinkLogout.show();
    } else {
        $avatarInitials.text('?');
        $avatar.css('background-color', '#ccc');
        $nicknameDisplay.text('');
        $authLinkLogin.show();
        $authLinkLogout.hide();
    }
    updateSettingsButton(isAuthenticated());
}

// Optional: Generate a random color for the avatar background
function getRandomColor() {
    const letters = '0123456789ABCDEF';
    let color = '#';
    for (let i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}


$(document).ready(async function () {
    await checkUser();
    updateControlPanel(); // Check authentication and update UI on page load
});

// auth.js

function showRegisterForm() {
    document.getElementById('login-form').style.display = 'none';
    document.getElementById('register-form').style.display = 'block';
}

function showLoginForm() {
    document.getElementById('register-form').style.display = 'none';
    document.getElementById('login-form').style.display = 'block';
}

async function checkUser() {
    try {

        userData = await $.ajax({
            url: '/api/user',
            method: 'GET',
        });

        staticData = await getStaticData();
        console.log(staticData);
        updateUserAvatar();
        updateStaticUI();
        if (userData) {
            await checkGame(); // Check if the user is already in a game session
        }
    } catch (error) {
        console.log('CheckUser:', error);
    }
}
async function getStaticData() {
    return $.ajax({
        url: '/api/static/data',
        method: 'GET',
    });
}

async function handleRegister() {
    const login = $('#register-login').val();
    const password = $('#register-password').val();
    const confirmPassword = $('#register-confirm-password').val();

    if (password !== confirmPassword) {
        alert('Passwords do not match');
        return;
    }

    try {
        const registerResponse = await $.ajax({
            url: '/api/auth/register',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                login: login,
                password: password
            })
        });

        if (registerResponse.message) {
            alert('Registration failed: ' + registerResponse.message);
        } else {
            userData = registerResponse;
            updateUserAvatar();
            await checkGame(); // Check if the user is already in a game session

         }
    } catch (error) {
        console.error('Error:', error);
        alert('An error occurred during registration.');
    } finally {
        hideAuthPopup();
    }
}

async function handleLogin() {
    const login = document.getElementById('login-input').value;
    const password = document.getElementById('password-input').value;
    try {
        const loginResponse = await $.ajax({
            url: '/api/auth/login',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                login: login,
                password: password
            })
        });


        if (loginResponse.message) {
            alert('Login failed: ' + loginResponse.message);
        } else {
            userData = loginResponse;
            updateUserAvatar();
            await checkGame(); // Check if the user is already in a game session
        }
    } catch (error) {
        console.error('Error:', error);
        alert('An error occurred during registration.');
    } finally {
        hideAuthPopup();
    }
}

async function handleLogout() {
    // Simulate a logout request
    const logoutResponse = await $.ajax({
        url: '/api/auth/logout',
        method: 'POST',
    });
    await checkUser();
}

async function handleAuthLinkClick() {
    if (userData) {
        await handleLogout();
    } else {
        showAuthPopup();
    }
}

function hideAuthPopup() {
    document.getElementById('auth-popup').style.display = 'none';
    document.getElementById('overlay').style.display = 'none';
}

function showAuthPopup() {
    document.getElementById('auth-popup').style.display = 'block';
    document.getElementById('overlay').style.display = 'block';
}
