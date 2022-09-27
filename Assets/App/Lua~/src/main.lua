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
    require("app.App")
    local configuration = require("app.ui.configuration.context.MainContext")
    App.ui.UIManager:navigateTo(configuration)
end, __G__TRACEBACK__)