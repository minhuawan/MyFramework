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
    --log.debug("=== START PRINT FUNC REF BY C-SHARP ===")
    --require("xlua.util").print_func_ref_by_csharp()
    --log.debug("=== END   PRINT FUNC REF BY C-SHARP ===")
end

xpcall(function()
    require("core.preclude")
    local UIManager = require("core.ui.UIManager")
    UIManager:initialize()
    UIManager:navigateTo(require("ui.configuration.context.main"))
end, __G__TRACEBACK__)
