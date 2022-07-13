if app then
    return
end

local internal = {}
app = setmetatable({}, { __index = internal })
internal.cs = CS

print(app.cs)