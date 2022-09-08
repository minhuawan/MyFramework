formatter = {}

function formatter.string(fmt, ...)
    local message = tostring(fmt)
    local args = { ... }
    --if #args == 0 then
    --    return message
    --end
    local i = 0
    message = string.gsub(message, "%{%}", function(...)
        i = i + 1
        return tostring(args[i])
    end)
    return message
end

function formatter.number(num)
    -- todo
end

function formatter.date(timestamp)
    -- todo
end

function formatter.time(timestamp)
    -- todo
end

function formatter.datetime(timestamp)
    -- todo
end
