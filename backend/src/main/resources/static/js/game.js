async function drawGame(gameSession) {
    if (!gameSession) return;

    if (gameSession.gameStatus === 'ONGOING') {
        $('#game-state-container').css('display', 'block');
        // Set Ancient One card
        if (gameSession.gameState.ancientId) {
            const ancientImage = staticData[gameSession.gameState.ancientId + '.image1'];
            $('#ancient-one-card').attr('src', ancientImage).css('display', 'block');
        }

        // Clear and repopulate other investigators
        const $otherInvestigators = $('#other-investigators').empty();

        gameSession.players.forEach(player => {
            if (player.id !== userData.id && player.investigatorId) {
                const investigatorImage = staticData[player.investigatorId + '.image1'];
                $otherInvestigators.append(
                    $('<img>').addClass('other-investigator')
                        .attr('src', investigatorImage)
                        .attr('title', player.name)
                );
            }
        });

        // Set current player's investigator
        const currentPlayer = gameSession.players.find(p => p.playerId === userData.id);
        if (currentPlayer?.investigatorId) {
            const investigatorImage = staticData[currentPlayer.investigatorId + '.image1'];
            $('#player-investigator').attr('src', investigatorImage).css('display', 'block');
        }
    }
}