return function()
    local TaskFactory = require("app.task.TaskFactory")
    return {
        TaskFactory = TaskFactory() -- create instance
    }
end