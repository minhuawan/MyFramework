require("core.class")
require("core.formatter")
require("core.json")
require("core.log")
require("core.collections.setup")
require("core.calendar")
--require("core.storage")
--require("core.event")
--require("core.resources")

-- require modules before global
require("core.globals")

local readonly = require("core.utils.readonly")
local app = {
    device = require("core.device"),
    storage = require("core.storage"),
    resources = require("core.resources"),
    asset = {
        AssetLoaderManager = require("core.asset.manager.AssetLoaderManager")(),
    },
    ui = {
        UIManager = require("app.ui.base.UIManager")(),
    },
    protocol = {
        ProtocolManager = require("app.network.protocol.ProtocolManager")()
    }
}

local internal = {}
for name, module in pairs(app) do
    internal[name] = readonly(module)
end

app = internal

return app
