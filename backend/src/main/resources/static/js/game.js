async function drawGame(lobbyInfo) {
    if (!lobbyInfo) return;

    if (lobbyInfo.gameStatus === 'ONGOING') {
        $('#game-state-container').css('display', 'block');
        // Set Ancient One card
        if (lobbyInfo.ancientId) {
            const ancientImage = staticData[lobbyInfo.ancientId + '.image1'];
            $('#ancient-one-card').attr('src', ancientImage).css('display', 'block');
        }

        // Clear and repopulate other investigators
        const $otherInvestigators = $('#other-investigators').empty();

        lobbyInfo.agents.forEach(agent => {
            if (agent.id !== userData.id && agent.investigatorId) {
                const investigatorImage = staticData[agent.investigatorId + '.image1'];
                $otherInvestigators.append(
                    $('<img>').addClass('other-investigator')
                        .attr('src', investigatorImage)
                        .attr('title', agent.name)
                );
            }
        });

        // Set current player's investigator
        const currentAgent = lobbyInfo.agents.find(ag => ag.id === userData.id);
        if (currentAgent?.investigatorId) {
            const investigatorImage = staticData[currentAgent.investigatorId + '.image1'];
            $('#player-investigator').attr('src', investigatorImage).css('display', 'block');
        }
    }
}