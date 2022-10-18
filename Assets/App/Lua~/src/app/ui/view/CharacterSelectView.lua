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

    local character = App.localization.LocalizationManager:getTextModule('characters')
    local selectCharacter = character[name]
    local properties = charMeta.properties
    local ui = App.localization.LocalizationManager:getTextModule('ui')
    local texts = ui.dataMap.CharacterOption.TEXT


    local char = App.battle.CharacterManager:getCharacter(name)

    self._vars.Texts.characterName.text = char.name
    self._vars.Texts.title.text = char.title
    local goldText = formatter.string("{}{}", texts[5], properties.gold)
    local hpText = formatter.string("{}{}", texts[6], properties.hp)
    self._vars.Texts.goldText.text = info.gold
    self._vars.Texts.hpText.text = info.initialHp
    self._vars.Texts.relicsName.text = relicsMeta.name
    self._vars.Texts.relicsDescription.text = relicsMeta.description
end

function M:setCharacterVisible(visible)
    self._vars.GameObjects.characterInfo:SetActive(visible == true)
end

return M