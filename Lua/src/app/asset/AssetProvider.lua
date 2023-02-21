local STATES = {
    Initialize = "Initialize",
    Loading = "Loading",
    Loaded = "Loaded",
    Disposed = "Disposed",
}
local LOADERS = {
    prefab = require("app.asset.loader.PrefabAssetLoader"),
    text = require("app.asset.loader.TextAssetLoader"),
    sprite = require("app.asset.loader.SpriteAssetLoader"),
}

local counter = require("core.utils.counter")

---@class AssetProvider
local M = class("AssetProvider")

---@param assetLoaderManager AssetLoaderManager
function M:ctor(assetLoaderManager)
    self.assetLoaderManager = assetLoaderManager
    self.loaderMap = collections.map()
    self.state = STATES.Initialize
    self.counter = counter(0)
end

function M:require(path, type)
    if self.loaderMap:has(path) then
        return
    end
    log.assert(self.state == STATES.Initialize, 'incorrect state on require, state: `{}`', self.state)
    local Loader = LOADERS[type]
    log.assert(Loader, 'unsupported type: `{}`', type)
    local loader = Loader(path)
    self.loaderMap:set(path, loader)
end

function M:loadAsync(callback)
    log.assert(self.state == STATES.Initialize, 'incorrect state on require, state: `{}`', self.state)

    if self.loaderMap:count() == 0 then
        callback()
        return
    end

    self.callback = callback
    for _, loader in self.loaderMap:iter() do
        loader:load(bind(self.onLoadFinish, self))
    end
end

function M:onLoadFinish(loader)
    self.counter:plus()
    if self.counter:value() >= self.loaderMap:count() then
        self.state = STATES.Loaded
        if self.callback then
            self.callback(self)
        end
    end
end

function M:getAsset(path)
    if self.state ~= STATES.Loaded then
        log.error("get asset failed, state: `{}`", self.state)
        return
    end
    if not self.loaderMap:has(path) then
        log.error("path not found in loaderMap, path: `{}`", path)
    end
    local loader = self.loaderMap:get(path)
    log.assert(loader.isDone, 'loader isDone != true, path: `{}`', path)
    return loader:getAsset()
end

function M:dispose()
    for _, loader in self.loaderMap:iter() do
        loader:dispose()
    end
    self.loaderMap:clear()
    self.callback = nil
    self.state = STATES.Disposed
    self.counter = nil
end

return M