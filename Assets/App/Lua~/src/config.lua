local readonly = require("core.utils.readonly")
local config = {
    ['initial-language'] = 'en-us',
    --['initial-language'] = 'zh-cn',
}

config = readonly(config)
return config