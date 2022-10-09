---@class CharacterSelectView: View
local M = class("CharacterSelectView", require("app.ui.base.mvp.View"))

---@param model CharacterSelectModel
function M:initialize(model)
    self._vars = require("app.ui.configuration.vars.CharacterSelectViewVars").attach(self.binder)
    self.disposable:append(self._vars)

    self.backEvent = self._vars.ButtonViews.btnBack.clickEvent
    self.startEvent = self._vars.ButtonViews.btnStart.clickEvent

    local ironEvent = self._vars.ButtonViews.buttonIron.clickEvent:selectTo('iron')
    local silentEvent = self._vars.ButtonViews.buttonSilent.clickEvent:selectTo('silent')
    self.characterSelectEvent = ironEvent:merge(silentEvent)
    self:setCharacterVisible(false)
end

function M:selectCharacter(name)
    self:setCharacterVisible(true)
    local charMeta = App.metadata.load('character', name)
    local id = formatter.int(charMeta.properties.relicsId, "%03d")
    local relicsMeta = App.metadata.load('relics', 'relics_' .. id)
    self:setCharacterInfo(charMeta, relicsMeta)
end

function M:setCharacterInfo(charMeta, relicsMeta)
    local manager = App.localization.LocalizationManager
    local description = charMeta.description
    local properties = charMeta.properties
    self._vars.Texts.characterName.text = description.name
    self._vars.Texts.title.text = description.title
    self._vars.Texts.goldText.text = manager:getText('ui', 'CharacterOption', 'TEXT', 5, properties.gold)
    self._vars.Texts.hpText.text =
    self._vars.Texts.relicsName.text = relicsMeta.name
    self._vars.Texts.relicsDescription.text = relicsMeta.description
end

function M:setCharacterVisible(visible)
    self._vars.GameObjects.characterInfo:SetActive(visible == true)
end

return M