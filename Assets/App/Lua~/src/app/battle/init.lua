return function()
    local CharacterManager = require("app.battle.characters.CharacterManager")
    local RelicsManager = require("app.battle.relics.RelicsManager")
    return {
        CharacterManager = CharacterManager(),
        RelicsManager = RelicsManager(),
    }
end