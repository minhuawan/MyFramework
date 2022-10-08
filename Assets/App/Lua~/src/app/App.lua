require("core.class")
require("core.formatter")
require("core.json")
require("core.log")
require("core.collections.setup")
require("core.calendar")
--require("core.storage")
--require("core.event")
--require("core.resources")

OnLuaEnvDisposeBefore = function()
    CS.MyFramework.Runtime.Utils.UnityTickWrapper.ForceCleanAll()
    App.ui.UIManager:dispose()
    App.task.TaskFactory:disposeFactory()


    --if App and App.device and App.device.editor then
    --    log.debug("=== START PRINT FUNC REF BY C-SHARP ===")
    --    require("misc.xlua.util").print_func_ref_by_csharp()
    --    log.debug("=== END   PRINT FUNC REF BY C-SHARP ===")
    --end
end

sha = require("core.sha2")

App = {}
-- require modules before global
require("core.globals")


-- modules
App = {
    asset = require("app.asset.init"),
    ui = require("app.ui.init"),
    network = require("app.network.init"),
    device = require("core.device"),
    storage = require("core.storage"),
    resources = require("app.asset.resources"),
    task = require("app.task.init"),
    metadata = require("app.metadata.init"),
    localization = require("app.localization.init")
}
local readonly = require("core.utils.readonly")
App = readonly(App)