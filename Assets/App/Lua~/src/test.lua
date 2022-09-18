function test(str)
    print(str)
end

function try (fn, errorHandle)
    pcall(fn)
end

test(123)

test "123"

try(test, errorHandle)