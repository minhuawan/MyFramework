local AbstractCharacter = require("app.battle.character.AbstractCharacter")
---@class Silent : AbstractCharacter
local M = class("Silent", AbstractCharacter)

function M:initialize()
    self.title = self.name
    self.initialHp = 60
    self.maxHp = 60
    self.gold = 100
end

function M:getTitle()
    local player = App.localization.LocalizationManager:requireModule("ui").dataMap['AbstractPlayer']
    return player[2]
end


return M