local CHARACTERS = {
    'Ironclad',
    'Silent'
}

---@class CharacterManager
local M = class("CharacterManager")

function M:ctor()
    self.characters = {}
    for _, name in ipairs(CHARACTERS) do
        local char = require('app.battle.character.' .. name)(name)
        self.characters[name] = char
    end
end

---@return AbstractCharacter
function M:getCharacter(name)
    local char = self.characters[name]
    log.assert(char, 'invalid character {}', name)
    return char
end

return M