---@class TextLocalizeModule
local M = class("LocalizeModule")

function M:ctor(dataMap)
    log.assert(type(dataMap) == 'table', 'invalid data')
    self.dataMap = dataMap
end



return M