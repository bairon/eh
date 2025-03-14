// Dragging Logic
let isDragging = false;
let startX, startY, scrollLeft, scrollTop;

const $gameFieldContainer = $('#game-field-container');
const $draggableLayer = $('.draggable-layer');

$draggableLayer.on('mousedown', (e) => {
    // Only start dragging if the left mouse button is pressed
    if (e.button === 0) { // 0 = left button
        isDragging = true;
        startX = e.pageX - $draggableLayer.offset().left;
        startY = e.pageY - $draggableLayer.offset().top;
        scrollLeft = $gameFieldContainer.scrollLeft();
        scrollTop = $gameFieldContainer.scrollTop();

        // Prevent default behavior (e.g., text selection, drag-and-drop)
        e.preventDefault();
    }
});

$draggableLayer.on('mouseup', () => {
    isDragging = false;
});

$draggableLayer.on('mouseleave', () => {
    isDragging = false;
});

$draggableLayer.on('mousemove', (e) => {
    if (!isDragging) return;

    // Prevent default behavior (e.g., text selection, drag-and-drop)
    e.preventDefault();

    const x = e.pageX - $draggableLayer.offset().left;
    const y = e.pageY - $draggableLayer.offset().top;
    const walkX = (x - startX) * 2; // Adjust sensitivity
    const walkY = (y - startY) * 2; // Adjust sensitivity
    $gameFieldContainer.scrollLeft(scrollLeft - walkX);
    $gameFieldContainer.scrollTop(scrollTop - walkY);
});

// Prevent text selection while dragging
$(document).on('selectstart', (e) => {
    if (isDragging) {
        e.preventDefault();
    }
});