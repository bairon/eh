// Black Frame Overlay
const $doomProgress = $('#doom-progress');

// Example: Position the black frame over the doom bar
function positionDoomProgress(value, maxValue) {
    const $doomBar = $('.doom-bar');
    const rect = $doomBar[0].getBoundingClientRect();
    $doomProgress.css({
        left: `${(value / maxValue) * rect.width + 5}px`,
        height: rect.height,
        width: rect.height,
    });
}