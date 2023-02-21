local stopwatch = require("core.utils.stopwatch")
---@class AssetLoaderManager
local M = class("AssetLoaderManager")

function M:initialize()
    local str = App.resources.load("assets/manifest.json")
    self._manifest = json.decode(str)
    self._assets = self._manifest.assets
end

function M:hasAsset(path)
    return self._assets[path] ~= nil
end

function M:getAssetHash(path)
    local info = self._assets[path]
    log.assert(info, 'path not found in manifest: `{}`', path)
    return info.hash or ""
end

function M:getAssetDependencies(path)
    local info = self._assets[path]
    log.assert(info, 'path not found in manifest: `{}`', path)
    return info.dependencies or {}
end

function M:createAssetProvider()
    return require("app.asset.AssetProvider")(self)
end

return M