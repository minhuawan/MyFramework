local LOAD = CS.MyFramework.Runtime.Utils.LuaResourceUtils.Load
---@class resources
local resources = {}

function resources.load(path)
    local content = LOAD(path)
    return content
end

return resources