local AssetProvider = require("app.asset.AssetProvider")
---@class CharacterSelectView: View
local M = class("CharacterSelectView", require("app.ui.base.mvp.View"))

---@param model CharacterSelectModel
function M:initialize(model)
    self._vars = require("app.ui.configuration.vars.CharacterSelectViewVars").attach(self.binder)
    self.disposable:append(self._vars)

    self.highlightGos = collections.map()
    self.backEvent = self._vars.ButtonViews.btnBack.clickEvent
    self.startEvent = self._vars.ButtonViews.btnStart.clickEvent

    local ironEvent = self._vars.ButtonViews.buttonIron.clickEvent:selectTo('Ironclad')
    local silentEvent = self._vars.ButtonViews.buttonSilent.clickEvent:selectTo('Silent')
    self.characterSelectEvent = ironEvent:merge(silentEvent)
end

function M:appearAsync(next)
    self.provider = AssetProvider()
    self.disposable:append(self.provider)

    local chars = App.battle.CharacterManager:getAllCharacters()
    for name, ch in pairs(chars) do
        local relicPath = ch:getDefaultRelic():getAssetPath()
        local selectPath = ch:getSelectAssetPath()
        self.provider:require(relicPath, "sprite")
        self.provider:require(selectPath, "sprite")
    end

    local transforms = self._vars.Transforms
    self.highlightGos:set("Silent", transforms.buttonSilent:Find("highlight").gameObject)
    self.highlightGos:set("Ironclad", transforms.buttonIron:Find("highlight").gameObject)

    self.provider:loadAsync(function()
        self:selectCharacter('Ironclad')
        self.super.appearAsync(self, next)
    end)
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

    self._vars.Images.relicsIcon.sprite = self.provider:getAsset(relic:getAssetPath())
    self._vars.Images.characterPoster.sprite = self.provider:getAsset(character:getSelectAssetPath())

    for n, go in self.highlightGos:iter() do
        go:SetActive(n == name)
    end
end

function M:setCharacterVisible(visible)
    self._vars.GameObjects.characterInfo:SetActive(visible == true)
end

return M