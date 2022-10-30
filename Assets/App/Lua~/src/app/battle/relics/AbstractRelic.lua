---@class AbstractRelic
local M = class("AbstractRelic")

function M:ctor(setId)
    self.setId = setId

    self:localize()
    self:initialize()
end

function M:initialize()
end

function M:localize()
    local data = App.localization.LocalizationManager:requireModule("relics").dataMap[self.setId]
    log.assert(data, "get relic localize date error, setId `{}`", self.setId)
    self.setId = data.NAME
    self.flavor = data.FLAVOR
    self.descriptions = data.DESCRIPTIONS
end

function M:getSetId()
    return self.setId
end

function M:getFlavor()
    return self.flavor
end

function M:getImageName()
end

function M:getTier()
end

function M:getSfx()
end

function M:getUpdatedDescription()
    return ""
end

function M:onVictory()

end

function M:getAssetPath()
    local name = self:getImageName()
    log.assert(name, 'invalid name at {}', self)
    local path = formatter.string("{}/{}", "Assets/AppData/STS/images/relics", name)
    return path
end

return M