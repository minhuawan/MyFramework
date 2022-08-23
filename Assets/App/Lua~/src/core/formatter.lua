formatter = {}

function formatter.string(fmt, ...)
    local message = tostring(fmt)
    local args = { ... }
    if #args == 0 then
        return message
    end
    local i = 0
    message = string.gsub(message, "%{%}", function(...)
        i = i + 1
        return tostring(args[i])
    end)
    return message
end

function formatter.number(num)
end
