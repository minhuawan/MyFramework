---@class BurningBlood : AbstractRelic
local M = class("BurningBlood", require("app.battle.relics.AbstractRelic"))

function M:initialize()
end

function M:getImageName()
    return "burningBlood.png"
end

function M:getTier()
end

function M:getSfx()
end

function M:getUpdatedDescription()
    formatter.localize_fmt("{}{}{}", self.descriptions[1], 6, self.descriptions[2])
end

return M