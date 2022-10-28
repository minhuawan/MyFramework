local stopwatch = require("core.utils.stopwatch")
---@class AssetLoaderManager
local M = class("AssetLoaderManager")

function M:initialize()
    local str = App.resources.load("assets/manifest.json")
    self._manifest = json.decode(str)
end



return M