---@class CharacterSelectView: View
local M = class("CharacterSelectView", require("app.ui.base.mvp.View"))

---@param model CharacterSelectModel
function M:initialize(model)
    self._vars = require("app.ui.configuration.vars.CharacterSelectViewVars").attach(self.binder)
    self.disposable:append(self._vars)

    self.backEvent = self._vars.ButtonViews.btnBack.clickEvent
    self.startEvent = self._vars.ButtonViews.btnStart.clickEvent

    local ironEvent = self._vars.ButtonViews.buttonIron.clickEvent:selectTo('Ironclad')
    local silentEvent = self._vars.ButtonViews.buttonSilent.clickEvent:selectTo('Silent')
    self.characterSelectEvent = ironEvent:merge(silentEvent)
    self:selectCharacter('Ironclad')
end

function M:selectCharacter(name)
    self:setCharacterVisible(true)

    local character = App.battle.CharacterManager:getCharacter(name)
    self._vars.Texts.characterName.text = character:getLocalizedName()
    self._vars.Texts.title.text = character:getLocalizedTitle()
    self._vars.Texts.goldText.text = character:getCurrentGoldText()
    self._vars.Texts.hpText.text = character:getCurrentHpText()

    local relic = character:getDefaultRelic()
    self._vars.Texts.relicsName.text = relic:getSetId()
    self._vars.Texts.relicsDescription.text = relic:getUpdatedDescription()

    -- todo change sprites
end

function M:setCharacterVisible(visible)
    self._vars.GameObjects.characterInfo:SetActive(visible == true)
end

return M