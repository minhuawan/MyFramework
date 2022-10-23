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
    storage = require("core.storage"),
    device = require("core.device"),
    resources = require("app.asset.resources"),
}

App.asset = require("app.asset.init")()
App.ui = require("app.ui.init")()
App.network = require("app.network.init")()
App.task = require("app.task.init")()
App.metadata = require("app.metadata.init")()
App.localization = require("app.localization.init")()
App.battle = require("app.battle.init")()

local readonly = require("core.utils.readonly")
App = readonly(App)