---@class Model
local M = class("Model")

function M:ctor()
    self._tbl = {}
end

function M:getValue(key, default)
    if key and self._tbl[key] then
        return self._tbl[key]
    end
    return default
end

function M:setValue(key, value)
    log.assert(value ~= nil and key ~= nil)
    self._tbl[key] = value
end

function M:clearValue(key)
    self._tbl[key] = nil
end

return M