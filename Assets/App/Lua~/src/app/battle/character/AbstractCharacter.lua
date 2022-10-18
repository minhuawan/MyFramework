---@class AbstractCharacter
local M = class("AbstractCharacter")

function M:ctor(name)
    self.name = name
    self.title = ""
    self.gold = 0
    self.initialHp = 0
    self.maxHp = 0
    self:initialize()
end

function M:initialize()
end

function M:getTitle()
end

return M