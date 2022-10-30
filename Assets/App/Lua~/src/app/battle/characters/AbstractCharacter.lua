---@class AbstractCharacter
local M = class("AbstractCharacter")

function M:ctor(name)
    self.name = name
    self.title = ""
    self.gold = 0
    self.initialHp = 0
    self.maxHp = 0
    self.currentHp = 0

    self:initialize()
    self:localize()
end

function M:initialize()
end

function M:localize()
    local data = App.localization.LocalizationManager:requireModule("characters").dataMap[self.name]
    self.localizedName = data.NAMES[1]
    self.localizedTitle = data.TEXT[1]
end

function M:getCurrentGoldText()
    local ui = App.localization.LocalizationManager:getTextModule('ui')
    local texts = ui.dataMap.CharacterOption.TEXT
    local goldText = formatter.localize_fmt("{}{}", texts[5], self.gold)
    return goldText
end

function M:getCurrentHpText()
    local ui = App.localization.LocalizationManager:getTextModule('ui')
    local texts = ui.dataMap.CharacterOption.TEXT
    local hpText = formatter.localize_fmt("{}{}/{}", texts[6], self.currentHp, self.maxHp)
    return hpText
end

function M:getLocalizedName()
    return self.localizedName
end

function M:getLocalizedTitle()
    return self.localizedTitle
end

---@return AbstractRelic
function M:getDefaultRelic()
    log.unimplemented()
end

function M:getSelectImageName()
    log.unimplemented()
end

function M:getSelectAssetPath()
    local name = self:getSelectImageName()
    local path = formatter.string("{}/{}", "Assets/AppData/STS/images/ui/charSelect", name)
    return path
end

return M