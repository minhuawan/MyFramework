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


-- modules
local App = {
    asset = require("app.asset.init"),
    ui = require("app.ui.init"),
    network = require("app.network.init"),
    device = require("core.device"),
    storage = require("core.storage"),
    resources = require("app.asset.resources"),
}
local readonly = require("core.utils.readonly")
App = readonly(App)

return App