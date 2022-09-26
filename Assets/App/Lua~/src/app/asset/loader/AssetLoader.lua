local AssetTypes = {
    ['sprite'] = '',
    ['text'] = '',
    ['prefab'] = '',
}

---@class AssetLoader
local M = class("AssetLoader")

function M:ctor()
    self._state = nil
end

function M:require(path, typ, events)
    log.assert(type(path) == 'string' and path ~= "", 'invalid path: {}', path)
    log.assert(type and AssetTypes(typ), 'invalid asset type: {}', typ)

    self._path = path
    self._type = typ
    self._events = events
    self:requireInternal()
end

---@private
function M:requireInternal()
    if app.device.editor then
        self:requireInternal_Editor()
    elseif app.device.android or app.device.ios or app.device.windows then
        self:requireInternal_WithBundle()
    else
        log.assert(false, 'device platform unsupported {}', app.device.platform)
    end
end

---@private
function M:requireInternal_Editor()
    if self._state == 'required' then
        self:onFinished()
    end
    self._state = 'requiring'
end

---@private
function M:requireInternal_WithBundle()
end

return M