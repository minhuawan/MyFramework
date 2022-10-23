return function()
    local CharacterManager = require("app.battle.character.CharacterManager")
    return {
        CharacterManager = CharacterManager()
    }
end