local ButtonGroupEvent = require("app.ui.base.ButtonGroupEvent")
local ButtonEvent = require("app.ui.base.ButtonEvent")
---@class CharacterSelectView: View
local M = class("CharacterSelectView", require("app.ui.base.mvp.View"))

---@param model CharacterSelectModel
function M:initialize(model)

    self._vars = require("app.ui.configuration.vars.CharacterSelectViewVars").attach(self.binder)
    self.backEvent = ButtonEvent(self._vars.ButtonViews.btnBack):addTo(self.disposable)
    self.startEvent = ButtonEvent(self._vars.ButtonViews.btnStart):addTo(self.disposable)

    self.characterSelectEvent = ButtonGroupEvent(
            {
                self._vars.ButtonViews.buttonIron,
                self._vars.ButtonViews.buttonSilent
            },
            {
                'iron', 'silent'
            }
    ):addTo(self.disposable)
    self:setCharacterVisible(false)
end

function M:selectCharacter(name)
    self:setCharacterVisible(true)
    local charMeta = require("app.metadata.character." .. name)
    local id = formatter.int(charMeta.properties.relicsId, "%03d")
    local relicsMeta = require("app.metadata.relics.relics_" .. id)
    self:setCharacterInfo(charMeta, relicsMeta)
end

function M:setCharacterInfo(charMeta, relicsMeta)
    local description = charMeta.description
    local properties = charMeta.properties
    self._vars.Texts.characterName.text = description.name
    self._vars.Texts.title.text = description.title
    self._vars.Texts.goldText.text = self:localizeText('goldText', properties.gold)
    self._vars.Texts.hpText.text = self:localizeText('hpText', properties.hp)
    self._vars.Texts.relicsName.text = relicsMeta.name
    self._vars.Texts.relicsDescription.text = relicsMeta.description
end

function M:setCharacterVisible(visible)
    self._vars.GameObjects.characterInfo:SetActive(visible == true)
end

return M