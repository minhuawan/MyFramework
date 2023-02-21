return function()
    local AssetLoaderManager = require("app.asset.manager.AssetLoaderManager")
    return {
        AssetLoaderManager = AssetLoaderManager() -- create instance
    }
end