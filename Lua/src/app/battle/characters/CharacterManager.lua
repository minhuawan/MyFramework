local CHARACTERS = {
    'Ironclad',
    'Silent'
}

---@class CharacterManager
local M = class("CharacterManager")

function M:ctor()
    self.characters = {}
    for _, name in ipairs(CHARACTERS) do
        local char = require('app.battle.characters.' .. name)(name)
        self.characters[name] = char
    end
end

---@return AbstractCharacter
function M:getCharacter(name)
    local char = self.characters[name]
    log.assert(char, 'invalid character {}', name)
    return char
end

---@return AbstractCharacter[]
function M:getAllCharacters()
    return self.characters
end

return M