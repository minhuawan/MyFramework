---@class AssetLoaderManager
local M = class("AssetLoaderManager")

function M:ctor()
    self._asset = require("metadata.asset")
end


return M