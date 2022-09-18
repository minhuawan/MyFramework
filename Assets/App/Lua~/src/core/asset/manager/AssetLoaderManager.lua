---@class AssetLoaderManager
local M = class("AssetLoaderManager")

function M:ctor()
    self._manifest = json.decode(resources.load("assets/manifest.json"))
    for k, v in pairs(self._manifest) do
        log.debug("K: {}", k)
        if k == "assets" then
            for kk, vv in pairs(v) do
                if kk == "assets/appdata/sts/beyondscene/scene.jpg" then
                    for kkk, vvv in pairs(vv) do
                        log.debug("kkk: {}, vvv: {}", kkk, vvv)
                    end
                end
            end
        end
    end
end

return M