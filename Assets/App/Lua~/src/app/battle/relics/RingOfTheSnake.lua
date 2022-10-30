---@class RingOfTheSnake : AbstractRelic
local M = class("RingOfTheSnake", require("app.battle.relics.AbstractRelic"))

function M:getUpdatedDescription()
    formatter.localize_fmt("{}{}{}", self.descriptions[1], 2, self.descriptions[2])
end

function M:getImageName()
    return "snake_ring.png"
end

return M