---@class TextLocalizeModule
local M = class("LocalizeModule")

function M:ctor(texts)
    log.assert(type(texts) == 'table', 'invalid data')
    ---@protected
    self.texts = texts
end

return M