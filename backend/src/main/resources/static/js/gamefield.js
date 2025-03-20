// Dragging Logic
let isDragging = false;
let startX, startY, scrollLeft, scrollTop;
function initField() {
    const gameFieldContainer = document.getElementById('game-field-container');
    const draggableLayer = document.querySelector('.draggable-layer');
    draggableLayer.addEventListener('mousedown', (e) => {
        // Only start dragging if the left mouse button is pressed
        if (e.button === 0) { // 0 = left button
            isDragging = true;
            startX = e.pageX - draggableLayer.offsetLeft;
            startY = e.pageY - draggableLayer.offsetTop;
            scrollLeft = gameFieldContainer.scrollLeft;
            scrollTop = gameFieldContainer.scrollTop;

            // Prevent default behavior (e.g., text selection, drag-and-drop)
            e.preventDefault();
        }
    });

    draggableLayer.addEventListener('mouseup', () => {
        isDragging = false;
    });

    draggableLayer.addEventListener('mouseleave', () => {
        isDragging = false;
    });

    draggableLayer.addEventListener('mousemove', (e) => {
        if (!isDragging) return;

        // Prevent default behavior (e.g., text selection, drag-and-drop)
        e.preventDefault();

        const x = e.pageX - draggableLayer.offsetLeft;
        const y = e.pageY - draggableLayer.offsetTop;
        const walkX = (x - startX) * 2; // Adjust sensitivity
        const walkY = (y - startY) * 2; // Adjust sensitivity
        gameFieldContainer.scrollLeft = scrollLeft - walkX;
        gameFieldContainer.scrollTop = scrollTop - walkY;
    });

// Prevent text selection while dragging
    document.addEventListener('selectstart', (e) => {
        if (isDragging) {
            e.preventDefault();
        }
    });
}
$(window).on('load', initField);
