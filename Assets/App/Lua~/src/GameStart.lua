-- todo all csharp-call-lua function should pcall with that
function __G__TRACEBACK__(msg)
    local _msg = debug.traceback(msg, 3)
    log.error(_msg)
    return _msg
end

xpcall(function()
    require("core.preclude")
    local UIManager = require("core.ui.UIManager")
    local main = require("ui.configuration.context.main.main")
    UIManager.getInstance():single(main)
end, __G__TRACEBACK__)