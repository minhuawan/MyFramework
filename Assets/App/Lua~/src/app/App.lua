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

---@field device device
---@field storage storage
---@field resources resources
local App = readonly {
    device = require("core.device"),
    storage = require("core.storage"),
    resources = require("core.resources"),
    assets = readonly {
        ---@type AssetLoaderManager
        AssetLoaderManager = require("core.asset.manager.AssetLoaderManager")(),
    },
    ui = readonly {
        UIManager = require("app.ui.base.UIManager")(),
    },
    protocol = readonly {
        ProtocolManager = require("app.network.protocol.ProtocolManager")()
    }
}



return App
