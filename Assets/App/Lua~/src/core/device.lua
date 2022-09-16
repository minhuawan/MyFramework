local __internal__ = {}
---@class device
device = {
    ---@privateN
    __internal__ = __internal__
}
local FLAG = true
setmetatable(device, {
    __index = function(t, k)
        local v = __internal__[k]
        assert(v ~= nil, 'get `device` failed, key: ' .. tostring(k))
        return v
    end,
    __newindex = function(t, k, v)
        if FLAG then
            __internal__[k] = v
        else
            assert(false, "can't write the table `device`")
        end
    end
})

local app = CS.UnityEngine.Application
local RuntimePlatform = UnityEngine.RuntimePlatform
local platform = app.platform
device.editor = app.isEditor == true
device.ios = false
device.windows = false
device.android = false
device.language = ''

if RuntimePlatform.IPhonePlayer == platform then
    device.ios = true
elseif RuntimePlatform.WindowsPlayer == platform then
    device.windows = true
elseif RuntimePlatform.Android == platform then
    device.android = true
end

FLAG = false