local raw_error = _G.raw_error or error
_G.raw_error = raw_error

local function log2CSharp(type, message)
    if type == "verbose" then
        CS.UnityEngine.Debug.Log(message)
    elseif type == "log" then
        CS.UnityEngine.Debug.Log(message)
    elseif type == "info" then
        CS.UnityEngine.Debug.Log(message)
    elseif type == "warn" then
        CS.UnityEngine.Debug.LogWarning(message)
    elseif type == "error" then
        CS.UnityEngine.Debug.LogError(message)
    end
end

local function format(tag, fmt, ...)
    fmt = tostring(tag) .. ' ' .. tostring(fmt)
    return formatter.string(fmt, ...)
end

log = {
    verbose = function(fmt, ...)
        local message = format("[VER]", fmt, ...)
        log2CSharp("verbose", debug.traceback(message))
    end,
    debug = function(fmt, ...)
        local message = format("[DEB]", fmt, ...)
        log2CSharp("log", message)
    end,
    info = function(fmt, ...)
        local message = format("[INF]", fmt, ...)
        log2CSharp("log", message)
    end,
    warn = function(fmt, ...)
        local message = format("[WAR]", fmt, ...)
        log2CSharp("warn", message)
    end,
    error = function(fmt, ...)
        local message = format("[ERR]", fmt, ...)
        log2CSharp("error", debug.traceback(message))
    end,
    exception = function(fmt, ...)
        local message = format("[EXP]", fmt, ...)
        error(message, 2) -- lua native exception
    end,
    assert = function(exp, fmt, ...)
        if exp then
        else
            local message = format("[AST]", fmt, ...)
            assert(false, message)
        end
    end,
    traceback = debug.traceback,
}
