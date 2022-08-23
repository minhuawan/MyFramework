local offset = 0
local time = os.time
local date = os.date
os.time = function(...)
    log.verbose('try to use with "calendar" for time/date manipulate')
    return time(...)
end
os.date = function(...)
    log.verbose('try to use with "calendar" for time/date manipulate')
    return date(...)
end

local function check_timestamp(timestamp)
    assert(type(timestamp) == 'number' and timestamp > 0, 'invalid timestamp ' .. tostring(timestamp))
end

local presets = {
    std = {
        format = function(timestamp)
            check_timestamp(timestamp)
            return date("%Y-%m-%d %H:%M:%S", timestamp)
        end,
        reverse = function(formatted)
            local year, month, day, hour, min, sec = string.match(formatted, '(%d+)-(%d+)-(%d+) (%d+):(%d+):(%d+)')
            assert(year, 'unable to reverse formatted: ' .. formatted)
            return time {
                year = tonumber(year),
                month = tonumber(month),
                day = tonumber(day),
                hour = tonumber(hour),
                min = tonumber(min),
                sec = tonumber(sec),
            }
        end
    }
}


calendar = {}


function calendar.synctime(now)
    offset = now - os.time()
end

function calendar.timestamp()
    return time() + offset
end

function calendar.format(timestamp, style)
    local p = presets[style]
    assert(p, 'format preset not found, style: ' .. tostring(style))
    return p.format(timestamp)
end

function calendar.reverse(formatted, style)
    local p = presets[style]
    assert(p, 'format preset not found, style: ' .. tostring(style))
    return p.reverse(formatted)
end

-- test cases

-- print('now', os.time())
-- local offset = os.time() - 60 * 60 * 24
-- calendar.synctime(offset)
-- print('synctime', offset)
-- local now = calendar.timestamp()
-- local formatted = calendar.format(now, 'std')
-- print('formatted', formatted)
-- local reverse = calendar.reverse(formatted, 'std')
-- print('reverse', reverse)
