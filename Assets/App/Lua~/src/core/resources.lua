local LOAD = CS.MyFramework.Runtime.Utils.LuaResourceUtils.Load
resources = {}

function resources.load(path)
    local content = LOAD(path)
    return content
end