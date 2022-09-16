local AssetTypes = {
    ['sprite'] = '',
    ['text'] = '',
    ['prefab'] = '',
}

---@class AssetLoader
local M = class("AssetLoader")

function M:ctor()
    self._required = false
end

function M:require(path, at, events)
    log.assert(type(path) == 'string' and path ~= "", 'invalid path: {}', path)
    log.assert(type and AssetTypes(at), 'invalid asset type: {}', at)

    self._path = path
    self._at = at
    self._events = events
end

return M