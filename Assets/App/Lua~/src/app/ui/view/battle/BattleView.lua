---@class BattleView : View
local M = class("BattleView", require("app.ui.base.mvp.View"))


function M:initialize(model)
    self.vars = require("app.ui.generated.battle.BattleViewVars").attach(self.binder)
    self.disposable:append(self.vars)


    self.topPartView = require("app.ui.view.battle.parts.BattleTopPartView")(self)
end


function M:appearAsync(next)
    self.super.appearAsync(self, next)
end

return M