local ButtonGroupEvent = require("core.ui.ButtonGroupEvent")
local ButtonEvent = require("core.ui.ButtonEvent")
---@class CharacterSelectView: View
local M = class("CharacterSelectView", require("core.ui.mvp.View"))

---@param model CharacterSelectModel
function M:initialize(model)
    self._vars = require("ui.configuration.vars.CharacterSelectViewVars").attach(self.binder)
    self.backEvent = ButtonEvent:create(self._vars.ButtonViews.btnBack)
    self.startEvent = ButtonEvent:create(self._vars.ButtonViews.btnStart)

    self._vars.Texts.characterName.text = model:getName()
    self._vars.Texts.description.text = model:getDescription()
    self._vars.Texts.goldText.text = model:getGoldText()
    self._vars.Texts.hpText.text = model:getHpText()
    self._vars.Texts.relicsName.text = model:getRelicsName()
    self._vars.Texts.relicsDescription.text = model:getRelicsDescription()

    self.characterSelectEvent = ButtonGroupEvent:create(
            {
                self._vars.ButtonViews.buttonIron
            },
            {
                'iron'
            }
    )
end

function M:dispose()
    self.backEvent:dispose()
    self.startEvent:dispose()
    self.characterSelectEvent:dispose()
end

return M