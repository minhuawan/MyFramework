local AbstractCharacter = require("app.battle.characters.AbstractCharacter")
---@class Ironclad : AbstractCharacter
local M = class("Ironclad", AbstractCharacter)

function M:initialize()
    self.title = self.name
    self.initialHp = 80
    self.maxHp =  80
    self.gold = 100
    self.currentHp = self.maxHp
end

function M:getDefaultRelic()
    return App.battle.RelicsManager:createRelicInst('Burning Blood')
end

return M