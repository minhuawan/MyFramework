local __internal__ = {}
---@class device
local device = {
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
local RuntimePlatform = CS.UnityEngine.RuntimePlatform
local SystemLanguage = CS.UnityEngine.SystemLanguage
local platform = app.platform
device.editor = app.isEditor == true
device.ios = false
device.windows = false
device.android = false
device.language = 'unknown'
device.platform = platform:ToString()

if RuntimePlatform.IPhonePlayer == platform then
    device.ios = true
    device.platform = 'ios'
elseif RuntimePlatform.WindowsPlayer == platform then
    device.windows = true
    device.platform = 'windows'
elseif RuntimePlatform.Android == platform then
    device.android = true
    device.platform = 'android'
end

-- unity 对这个语言及地区支持不是很好。。 后续再做吧
--if app.systemLanguage == SystemLanguage.ChineseSimplified then
--elseif app.systemLanguage == SystemLanguage.ChineseTraditional then
--end

FLAG = false

return device