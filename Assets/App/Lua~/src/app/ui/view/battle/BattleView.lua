---@class BattleView : View
local M = class("BattleView", require("app.ui.base.mvp.View"))


function M:initialize(model)
    require("app.ui.configuration.vars.battle.BattleTopVars")
end


function M:appearAsync(next)
end

return M