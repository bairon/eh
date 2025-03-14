// Black Frame Overlay
const doomProgress = document.getElementById('doom-progress');

// Example: Position the black frame over the doom bar
function positionDoomProgress(value, maxValue) {
    const doomBar = document.querySelector('.doom-bar');
    const rect = doomBar.getBoundingClientRect();
    doomProgress.style.left = `${(value / maxValue) * rect.width + 5}px`;
    doomProgress.style.height = rect.height;
    doomProgress.style.width = doomProgress.style.height;
}
