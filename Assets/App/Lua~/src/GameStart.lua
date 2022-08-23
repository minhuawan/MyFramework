-- todo all csharp-call-lua function should pcall with that
function __G__TRACEBACK__(msg)
    local _msg = debug.traceback(msg, 3)
    log.error(_msg)
    return _msg
end

OnLuaEnvDisposeBefore = function()
    CS.MyFramework.Runtime.Utils.UnityTickWrapper.ForceCleanAll()
    require("core.ui.UIManager"):dispose()
    require("core.task.TaskFactory"):disposeFactory()
    os.time()
end

xpcall(function()
    require("core.preclude")
    local UIManager = require("core.ui.UIManager")
    local main = require("ui.configuration.context.main.main")
    UIManager:initialize()
    UIManager:switchTo(main)
end, __G__TRACEBACK__)
