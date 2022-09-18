---- todo all csharp-call-lua function should pcall with that
function __G__TRACEBACK__(msg)
    local _msg = debug.traceback(msg, 3)
    if log and log.error then
        log.error(_msg)
    else
        CS.UnityEngine.Debug.LogError(_msg)
    end
    return _msg
end

xpcall(function()
    OnLuaEnvDisposeBefore = function()
        CS.MyFramework.Runtime.Utils.UnityTickWrapper.ForceCleanAll()
        require("app.ui.base.UIManager"):dispose()
        require("core.task.TaskFactory"):disposeFactory()
        --log.debug("=== START PRINT FUNC REF BY C-SHARP ===")
        --require("misc.xlua.util").print_func_ref_by_csharp()
        --log.debug("=== END   PRINT FUNC REF BY C-SHARP ===")
    end

    require("core.preclude")
    protoc.loadall(true)

    local UIManager = require("app.ui.base.UIManager")
    UIManager:initialize()
    UIManager:navigateTo(require("app.ui.configuration.context.MainContext"))

    local mgr = require("core.asset.manager.AssetLoaderManager")()

end, __G__TRACEBACK__)